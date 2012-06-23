package net.playblack.cuboids.regions;

import java.util.ArrayList;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.datasource.BaseData;
import net.playblack.cuboids.datasource.FlatfileDataLegacy;
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.WorldLocation;

/**
 * This manages CuboidNodes and takes care of lookups etc
 * @author Chris
 *
 */
public class RegionManager {
    private ArrayList<CuboidNode> rootNodes = new ArrayList<CuboidNode>(15);
    public EventLogger log;
    private BaseData dataSource;
    private ArrayList<CuboidNode> nodeList = new ArrayList<CuboidNode>();
    private CuboidNode global;
    
    private static RegionManager instance = null;
    
    private RegionManager(EventLogger log, BaseData dataSource) {
        this.log = log;
        this.dataSource = dataSource;
        global = new CuboidNode(Config.getInstance().getGlobalSettings());
    }
    
    public static RegionManager getInstance() {
        if(instance == null) {
            instance = new RegionManager(EventLogger.getInstance(), Config.getInstance().getDataSource());
        }
        return instance;
    }
    
    /**
     * This must be called after the global settings in Config have changed!!!
     * @param props
     */
    public void updateGlobalSettings() {
        global = new CuboidNode(Config.getInstance().getGlobalSettings());
    }
    /*
     * *********************************************************************************
     * LOAD / SAVE / RELOAD / STUFF
     * *********************************************************************************
     */
    
    /**
     * Load all cuboids from the data source
     */
    public void load() {
        //load for old files
        new FlatfileDataLegacy(log).loadAll(this);
        dataSource.loadAll(this);
    }
    
    /**
     * Load a single cuboid from file
     * @param name
     * @param world
     */
    public void loadSingle(String name, String world, int dimension) {
        dataSource.loadCuboid(this, name, world);
    }
    
    /**
     * Save all cuboid files to backend
     * @param silent
     * @param force
     */
    public void save(boolean silent, boolean force) {
        dataSource.saveAll(rootNodes, silent, force);
    }
    
    /**
     * Save a single cuboid to backend
     * @param name
     * @param world
     * @return
     */
    public boolean saveSingle(String name, String world, int dimension) {      
        dataSource.saveCuboid(getCuboidNodeByName(name, world, dimension));
        return true;
    }
    
    /**
     * Save a single cuboid to backend
     * @param node
     */
    public void saveSingle(CuboidNode node) {
        if(node == null) {
            return;
        }
        dataSource.saveCuboid(node);
    }
    
    private void removeNodeFile(CuboidNode node) {
        if(node == null) {
            return;
        }
        dataSource.removeNode(node);
    }
    
    /*
     * *********************************************************************************
     * ADD / REMOVE / CHANGE Cuboids
     * *********************************************************************************
     */
    
    /**
     * Extend the Treelist by adding a new cuboid
     * @param cube
     * @return
     */
    public boolean addCuboid(CuboidE cube) {
        if(cuboidExists(cube.getName(), cube.getWorld(), cube.getDimension())) {
            log.logMessage("Cuboids2: Cuboid already exists :O", "INFO");
            return false;
        }
        CuboidNode nodee = createNode(cube);
        if(cube.getParent() == null) {
            cube.hasChanged = true;
            addRoot(cube);
        }   
        else {
            for(CuboidNode root : rootNodes) {
                if(root.equalWorlds(cube)) {
                    for(CuboidNode node: root.toList()) {
                        if(node.getCuboid().getName().equals(cube.getParent())) {
                            cube.hasChanged = true;
                            node.addChild(nodee);
                        }
                    }
                }   
            }
        }
        reverseFindChildNodes(nodee);
        return true;
    }
    
    /**
     * Extend the tree by adding a new node
     * @param cube
     */
    public void addNode(CuboidNode cube) {
        if(cuboidExists(cube.getName(), cube.getWorld(), cube.getDimension())) {
            return;
        }
        if(cube.getCuboid().getParent() == null) {
            addRoot(cube);
            saveSingle(cube);
        }
        else {
            for(CuboidNode root : rootNodes) {
                
                if(root.equalWorlds(cube)) {
                    
                    for(CuboidNode node: root.toList()) {
                        
                        if(node.getName().equals(cube.getParent())) {
                            
                            node.addChild(cube);
                            cube.getCuboid().hasChanged = true;
                            node.getCuboid().hasChanged = true;
                            saveSingle(cube);
                            saveSingle(node);
                        }
                    }
                }   
            }
        }
    }
    
    /**
     * Add a new root node
     * @param root
     */
    public void addRoot(CuboidE root) {
        rootNodes.add(new CuboidNode(root));
    }
    
    /**
     * Add a new root node
     * @param root
     */
    public void addRoot(CuboidNode root) {
        rootNodes.add(root);
    }
    
    /**
     * Remove a cuboid from the tree list
     * @param cube
     * @param force
     * @return
     */
    public String removeCuboid(CuboidE cube, boolean force) {
        CuboidNode node = null;
        for(CuboidNode tree : rootNodes) {
            for(CuboidNode n : tree.toList()) {
                if(n.getName().equals(cube.getName())) {
                    node = n;
                    break;
                }
            }
        }
        if(node == null) {
            return "NOT_REMOVED_NOT_FOUND";
        }
        
        if(node.getChilds().size() > 0) {
            for(CuboidNode child : node.getChilds()) {
                child.getCuboid().setParent(null);
                updateCuboidNode(child.getCuboid());
            }
        }
        
        if(node.getParent() == null) {
            deleteTree(node.getName());
            autoSortCuboidAreas();
            removeNodeFile(node);
            return "REMOVED";
        }
        else {
            CuboidNode parent = getCuboidNodeByName(node.getParent(), node.getWorld(), node.getDimension());
            if(parent != null) {
                if(!parent.getName().equals(node.getName())) {
                    parent.getChilds().remove(node);
                }
                removeNodeFile(node);
                return "REMOVED";
            }
            else {
                return "NOT_REMOVED_CRAZY_ERROR";
            }
        }
    }
    
    /**
     * Delets the WHOLE tree at once, this will also remove ALL child nodes!
     * @param name
     */
    private void deleteTree(String name) {
        CuboidNode n = null;
        for(CuboidNode root : rootNodes) {
            if(root.getName().equals(name)) {
                n = root;
            }
        }
        removeNodeFile(n);
        rootNodes.remove(n);
    }
    
    /**
     * Take the root nodes, take them apart, re-sort them so it's all cool,
     * put them back together. Good as new!
     */
    public void autoSortCuboidAreas() {
        ArrayList<CuboidNode> workerList = new ArrayList<CuboidNode>(0);
        for(CuboidNode tree : rootNodes) {
            for(CuboidNode node : tree.toList()) {
                workerList.add(node);
            }
        }
        for(CuboidNode tree : workerList) {
            CuboidE cube = tree.getCuboid();
            CuboidNode parent = getPossibleParent(cube);
            //Parent must not be null and also must not have the same name as cube (because then it would be cube)
            if(parent != null && !(parent.getName().equals(cube.getName()))) {
                cube.setParent(parent.getName());
                if(parent.getCuboid().getPriority() < 0) {
                    //prevent negative priorities
                    //It doesn't actually have any implications if the priority IS negative
                    //but it is cleaner if not.
                    parent.getCuboid().setPriority(0);
                }
                if(cube.getPriority() <= parent.getCuboid().getPriority()) {
                    cube.setPriority(parent.getCuboid().getPriority()+1);
                }
                cube.hasChanged = true;
                updateCuboidNode(cube);
            }
        }
        rootNodes = workerList;
    }
    
    /**
     * Clear up parent parent relations.
     * That means removing parent relations where childs are not 100% inside their parent.
     * This is legacy support!
     */
    public void cleanParentRelations() {
        CuboidNode parent;
        for(CuboidNode tree : rootNodes) {
            for(CuboidNode node : tree.toList()) {
                parent = getCuboidNodeByName(node.getParent(), node.getWorld(), node.getDimension());
                if(parent != null) {
                    //Check if the child is truley completely inside its parent
                    if (!node.getCuboid().cuboidIsWithin(parent.getCuboid(), true)) {
                        //If not, remove the parent and set to null
                        node.getCuboid().setParent(ToolBox.stringToNull("null"));
                        node.getCuboid().hasChanged=true;
                        updateCuboidNode(node.getCuboid());
                        
                    }
                }
                else {
                    node.getCuboid().setParent(getPossibleParent(node.getCuboid()).getName());
                    node.getCuboid().hasChanged = true;
                    updateCuboidNode(node.getCuboid());
                }
            }
        }
    }
    
    /**
     * Create a new node form a CuboidE object
     * @param cube
     * @return
     */
    public CuboidNode createNode(CuboidE cube) {
        return new CuboidNode(cube);
    }
    
    /**
     * This re-sorts and moves a given cuboid around in a tree, making sure it can be found
     * within the defined system logic.
     * @param cube
     * @return
     */
    public boolean updateCuboidNode(CuboidE cube) {
        for (CuboidNode tree : rootNodes) {
            if (tree.equalWorlds(cube)) {
                for (CuboidNode node : tree.toList()) {
                    // log.logMessage("Unfolding Tree in updateCuboidNode - looking for "+cube.getName(),
                    // "INFO");
                    if (!node.getName().equals(cube.getName())) {
                        // no match, continue with next set
                        continue;
                    }
                    if ((cube.getParent() != null)
                            && (node.getParent().equals(cube.getParent()))) {
                        // Uncomment if somethig derps!
                        // if ((node.getParent() != null) && (node.isRoot()))
                        // {
                        // //log.logMessage("Old Cuboid is tree! Moving ...",
                        // "INFO");
                        // node.getCuboid().hasChanged = true;
                        // deleteTree(node.getName());
                        // addNode(node);
                        // return true;
                        // }
                        if (cuboidExists(cube.getParent(), cube.getWorld(),
                                cube.getDimension())) {
                            // log.logMessage("Parent Exists, setting data!",
                            // "INFO");
                            node.getCuboid().hasChanged = true;
                            node.setCuboid(cube);

                            return true;
                        }

                        return false;
                    }

                    if ((cube.getParent() != null)
                            && (!node.getParent().equals(cube.getParent()))) {
                        // comment this out if somethign derps!
                        if ((node.getParent() != null) && (node.isRoot())) {
                            // log.logMessage("Old Cuboid is tree! Moving ...",
                            // "INFO");
                            node.getCuboid().hasChanged = true;
                            deleteTree(node.getName());
                        }
                        // log.logMessage("Parent Node has changed!!", "INFO");
                        if (cuboidExists(cube.getParent(), cube.getWorld(),
                                cube.getDimension())) {
                            // log.logMessage("Parent Node has changed!!",
                            // "INFO");
                            removeCuboid(node.getCuboid(), true);
                            CuboidNode newNode = createNode(cube);
                            CuboidNode parent = getCuboidNodeByName(
                                    cube.getParent(), cube.getWorld(),
                                    cube.getDimension());
                            parent.addChild(newNode);
                            newNode.getCuboid().hasChanged = true;
                            addNode(newNode);
                            return true;
                        }
                        return false;
                    }

                    // If we're here the cuboid has no parent
                    if (cube.getParent() == null && node.getParent() == null) {
                        // log.logMessage("Parent is null AND node Parent is null (no structural changes)",
                        // "INFO");
                        // Nothing severe changed, only update
                        cube.hasChanged = true;
                        node.setCuboid(cube);
                        return true;
                    }
                    if (cube.getParent() == null && node.getParent() != null) {
                        // log.logMessage("Parent is null AND node Parent is NOT null (converting to tree)",
                        // "INFO");
                        CuboidNode newNode = createNode(cube);
                        newNode.setChilds(node.getChilds());
                        removeCuboid(node.getCuboid(), true);

                        newNode.getCuboid().hasChanged = true;
                        addRoot(newNode);
                        return true;
                    }
                }

            }

        }
        return false;
    }
    
    
    /**
     * Check if a cuboid with the given name,
     * in the give world exists
     * @param cube
     * @param world
     * @return
     */
    public boolean cuboidExists(String cube, String world, int dimension) {
        for(CuboidNode tree : rootNodes) {
            if(tree.equalWorlds(world, dimension)) {
                for(CuboidNode node : tree.toList()) {
                    if(node.getName().equals(cube)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /*
     * *********************************************************************************
     * LOOKUP STUFF
     * *********************************************************************************
     */
    
    
    /**
     * This will return the cuboid with the highest priority at the Vector given
     * or the global settings if there was no cuboid at the given vector
     * @param v
     * @param world
     * @param dimension
     * @param ignoreGlobal set true if you don't want to have the global settings to be passed along
     * @return
     */
    public CuboidNode getActiveCuboid(WorldLocation v, boolean ignoreGlobal) {
        nodeList.clear();
        if (v == null) {
            return global;
        }
        for (CuboidNode tree : rootNodes) {
            if (tree.equalWorlds(v.getWorld(), v.getDimension())) {
                if (!tree.getCuboid().isWithin(v)) {
                    continue;
                }
                for (CuboidNode node : tree.toList()) {
                    if (node.getCuboid().isWithin(v)) {
                        nodeList.add(node);
                    }
                }
            }
        }
        if(nodeList.isEmpty()) {
            if(!ignoreGlobal) {
                return global;
            }
            return null;
        }
        CuboidNode max = null;
        for (CuboidNode node : nodeList) {
            if (max == null) {
                max = node;
            } else if (max.getCuboid().getPriority() < node.getCuboid()
                    .getPriority()) {
                max = node;
            } else if (max.getCuboid().getPriority() == node.getCuboid()
                    .getPriority()) {
                if (max.getCuboid().getSize() > node.getCuboid().getSize()) {
                    max = node;
                }
            }
        }
        return max;
    }
    
    /**
     * Scan for nodes inside a node and parent them if they don't have a parent yet
     * @param node
     * @return
     */
    public void reverseFindChildNodes(CuboidNode node) {
        CuboidE c = node.getCuboid();
        ArrayList<CuboidNode> childs = new ArrayList<CuboidNode>();
        //System.out.println("Checking for possible childs in "+c.getName());
        for(CuboidNode tree : rootNodes) {
            if(tree.equalWorlds(node)) {
                for(CuboidNode n : tree.toList()) {
                    //if(n.getCuboid().isWithin(c.getFirstPoint()) && n.getCuboid().isWithin(c.getSecondPoint())) {
                    if(n.getCuboid().cuboidIsWithin(c.getMajorPoint(), c.getMinorPoint(), true)) {
                        //System.out.println(n.getCuboid().getName()+" is within "+c.getName());
                        if(n.getParent() == null) {
                        //  System.out.println("We have no parent yet!");
                            if(n.getName().equals(c.getName())) {
                            //  System.out.println("Same name, stupid");
                                continue;
                            }
                            n.getCuboid().setParent(c.getName());
                            n.getCuboid().hasChanged=true;
                            childs.add(n);
                        }
                    }
                }
            }
        }
        for(CuboidNode n : childs) {
            updateCuboidNode(n.getCuboid());
        }
        save(false, true);
    }
    
    /**
     * Create a list of CuboidNodes that contain the given Vector in the given world
     * @param v
     * @param world
     * @return
     */
    public ArrayList<CuboidE> getCuboidsContaining(WorldLocation v, String world, int dimension) {
        ArrayList<CuboidE> list = new ArrayList<CuboidE>();
        if(v == null) {
            return list;
        }
        for(CuboidNode tree : rootNodes) {
            if(tree.equalWorlds(world, dimension)) {
                if(tree.getCuboid().isWithin(v)) {
                    for(CuboidNode node : tree.toList()) {
                        if(node.getCuboid().isWithin(v)) {
                            list.add(node.getCuboid());
                        }
                    }
                }
            }
        }
        if(list.isEmpty()) {
            list.add(global.getCuboid());
        }
        return list;
    }
    
    /**
     * Get a list of all cuboids in the given world
     * @param world
     * @param dimension
     * @return CuboidNode List or null if there were no cuboids
     */
    public ArrayList<CuboidNode> getAllInDimension(String world, int dimension) {
        ArrayList<CuboidNode> list = new ArrayList<CuboidNode>(0);
        for(CuboidNode tree : rootNodes) {
            if(tree.equalWorlds(world, dimension)) {
                for(CuboidNode node : tree.toList()) {
                    list.add(node);
                }
            }
        }
        
        if(list.size() > 0) {
            return list;
        }
        else {
            return null;
        }
    }
    
    /**
     * Return the CuboidNode with the given name or null if not existent
     * @param name
     * @param world
     * @return CuboidNode or null
     */
    public CuboidNode getCuboidNodeByName(String name, String world, int dimension) {
        for(CuboidNode tree : rootNodes) {
            if(tree.equalWorlds(world, dimension)) {
                for(CuboidNode node : tree.toList()) {
                    if(node.getName().equals(name)) {
                        return node;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Return the CuboidE with the given name or null if not existent
     * @param name
     * @param world
     * @param dimension
     * @return
     */
    public CuboidE getCuboidByName(String name, String world, int dimension) {
        for(CuboidNode tree : rootNodes) {
            if(tree.equalWorlds(world, dimension)) {
                for(CuboidNode node : tree.toList()) {
                    if(node.getName().equals(name)) {
                        return node.getCuboid();
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * This'll try to find the best parent for a given cuboid, if there can be one
     * Returns null if no suitable parent was found
     * @param cube
     * @return
     */
    public CuboidNode getPossibleParent(CuboidE cube) {
        //log.logMessage("Going to find a suitable parent for "+cube.getName(), "INFO");
        ArrayList<CuboidNode> list = new ArrayList<CuboidNode>();
        for(CuboidNode tree : rootNodes) {
            if(tree.equalWorlds(cube)) {
                for(CuboidNode node : tree.toList()) {
                    if(cube.cuboidIsWithin(node.getCuboid(), true)) {
                        //log.logMessage("Adding possible parent to list!", "INFO");
                        list.add(node);
                    }
                }
            }
        }
        if(list.size() > 0) {
            //Try to find the cuboid with the lowest priority
            CuboidNode min=null;
            for(int e = 0; e<list.size();e++) {
                if(min == null) {
                    min = list.get(e);
                }
                if(min.getCuboid().getPriority() <= list.get(e).getCuboid().getPriority()) {
                    if(!list.get(e).getName().equals(cube.getName())) {
                        min = list.get(e);
                    }
                }
            }
            return min;
        }
        return null;
    }
    
    /**
     * Return the cuboid tree data.
     * @return
     */
    public ArrayList<CuboidNode> getRootNodeList() {
        return rootNodes;
    }

    /**
     * Effectively remove player from within all areas
     * @param name
     */
    public void removeFromAllAreas(String name) {
        for(CuboidNode tree : rootNodes) {
            for(CuboidNode node : tree.toList()) {
                if(node.getCuboid().playerIsWithin(name)) {
                    node.getCuboid().removePlayerWithin(name);
                }
            }
        }
        
    }
    
    /**
     * Remove the player given from all areas that DO NOT match world and dimension specified in the given location
     * @param playerName
     * @param loc
     */
    public void removeFromAllAreas(String playerName, WorldLocation loc) {
        for(CuboidNode tree : rootNodes) {
            if((!tree.getName().equals(loc.getWorld())) || (tree.getDimension() != loc.getDimension())) {
                for(CuboidNode node : tree.toList()) {
                    node.getCuboid().removePlayerWithin(playerName);
                }
            }
        }
    }
    
}
