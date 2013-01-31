package net.playblack.cuboids.regions;

import java.util.ArrayList;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.datasource.BaseData;
import net.playblack.cuboids.datasource.FlatfileDataLegacy;
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.Location;

/**
 * This manages CuboidNodes and takes care of lookups etc
 * 
 * @author Chris
 * 
 */
public class RegionManager {
    private ArrayList<Region> rootNodes = new ArrayList<Region>(15);
    public EventLogger log;
    private BaseData dataSource;
    private Cuboid global;

    private static RegionManager instance = null;

    private RegionManager(EventLogger log, BaseData dataSource) {
        this.log = log;
        this.dataSource = dataSource;
        Cuboid insert = new Cuboid();
        insert.putAll(Config.getInstance().getGlobalSettings().getAllProperties());
        global = insert;
    }

    public static RegionManager get() {
        if (instance == null) {
            instance = new RegionManager(EventLogger.getInstance(), Config
                    .getInstance().getDataSource());
        }
        return instance;
    }

    /**
     * This must be called after the global settings in Config have changed!!!
     * 
     * @param props
     */
    public void updateGlobalSettings() {
        global = new Cuboid(Config.getInstance().getGlobalSettings());
    }

    /*
     * *********************************************************************************
     * LOAD / SAVE / RELOAD / STUFF
     * *********************************************
     * ************************************
     */

    /**
     * Load all cuboids from the data source
     */
    public void load() {
        // load for old files
        new FlatfileDataLegacy(log).loadAll(this);
        dataSource.loadAll(this);
    }

    /**
     * Load a single cuboid from file
     * 
     * @param name
     * @param world
     */
    public void loadSingle(String name, String world, int dimension) {
        dataSource.loadCuboid(this, name, world);
    }

    /**
     * Save all cuboid files to backend
     * 
     * @param silent
     * @param force
     */
    public void save(boolean silent, boolean force) {
        dataSource.saveAll(rootNodes, silent, force);
    }

    /**
     * Save a single cuboid to backend
     * 
     * @param name
     * @param world
     * @return
     */
    public boolean saveSingle(String name, String world, int dimension) {
        dataSource.saveRegion(getCuboidByName(name, world, dimension));
        return true;
    }

    /**
     * Save a single cuboid to backend
     * 
     * @param node
     */
    public void saveSingle(Region node) {
        if (node == null) {
            return;
        }
        dataSource.saveRegion(node);
    }

    private void removeNodeFile(Region node) {
        if (node == null) {
            return;
        }
        dataSource.removeNode(node);
    }

    /*
     * *********************************************************************************
     * ADD / REMOVE / CHANGE Cuboids
     * ********************************************
     * *************************************
     */

    /**
     * Add a new managed cuboid and sort it in.
     * 
     * @param cube
     * @return
     */
    public boolean addRegion(Region cube) {
        if (cuboidExists(cube.getName(), cube.getWorld(), cube.getDimension())) {
            log.logMessage("Region already exists! Not adding it", "INFO");
            return false;
        }
        
        Region parent = getPossibleParent(cube);
        if(parent != null) {
            cube.setParent(parent);
        }
        else {
            addRoot(cube);
//            reverseFindChildNodes(cube);
        }
        cube.hasChanged = true;
        saveSingle(cube);
        return true;
    }

    /**
     * Add a new root node
     * 
     * @param root
     */
    public void addRoot(Region root) {
        if(root.isRoot()) {
            rootNodes.add(root);
        }
    }

    /**
     * Remove a cuboid from the tree list
     * TODO: Make this a void and remove the force thing
     * @param cube
     * @param force
     * @return
     */
    public String removeRegion(Region cube, boolean force) {
        
        if(rootNodes.contains(cube)) {
            rootNodes.remove(cube);
        }
        //Detach the childs of the region and re-sort them in
        //If we don't do this, all childs will be thrown away
        //in the next GC cycle
        ArrayList<Region> cleanUpList = cube.detachAllChilds();
        
        //Put parent-less childs into the root list
        for(Region child : cleanUpList) {
            if(child.getParent() == null) {
                addRoot(child);
            }
        }
        //In this was not a root region, we need to detach it from its parent 
        //so it will not be taken into consideration anymore
        cube.detach();
        removeNodeFile(cube);
        return "REMOVED";
    }

    /**
     * Take the root nodes, take them apart, re-sort them so it's all cool, put
     * them back together. Good as new!
     */
    public void autoSortRegions() {
        ArrayList<Region> workerList = new ArrayList<Region>();
        ArrayList<Region> rootList = new ArrayList<Region>();
        for (Region tree : rootNodes) {
            workerList.addAll(tree.getChildsDeep());
        }
        for (Region tree : workerList) {
            Region parent = getPossibleParent(tree);
            // Parent must not be null and also must not have the same name as
            // cube (because then it would be cube)
            if (parent != null && !(parent.getName().equals(tree.getName()))) {
                tree.setParent(parent);
                if (parent.getPriority() < 0) {
                    parent.setPriority(0);
                }
                if (tree.getPriority() <= parent.getPriority()) {
                    tree.setPriority(parent.getPriority() + 1);
                }
                tree.hasChanged = true;
            }
            
            if(parent == null) {
                tree.setParent(null);
                rootList.add(tree);
            }
        }
        rootNodes = rootList;
    }

    /**
     * Clear up parent parent relations. That means removing parent relations
     * where childs are not 100% inside their parent. This is legacy support!
     */
    public void cleanParentRelations() {
        ArrayList<Region> detachedRegions = new ArrayList<Region>();
        for (Region tree : rootNodes) {
            detachedRegions.addAll(tree.fixChilds());
        }
        //Put all detached into root
        for(Region r : detachedRegions) {
            addRoot(r);
        }
        //Re-sort root nodes
        autoSortRegions();
    }

    /**
     * Check if a cuboid with the given name, in the give world exists
     * 
     * @param cube
     * @param world
     * @return
     */
    public boolean cuboidExists(String cube, String world, int dimension) {
        for (Region tree : rootNodes) {
            if (tree.equalsWorld(world, dimension)) {
                if(tree.queryChilds(cube) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * *********************************************************************************
     * LOOKUP STUFF
     * *************************************************************
     * ********************
     */

    /**
     * This will return the cuboid with the highest priority at the Vector given
     * or the global settings if there was no cuboid at the given vector
     * 
     * @param v pass null to get the global settings
     * @param world
     * @param dimension
     * @param ignoreGlobal
     *            set true if you don't want to have the global settings to be
     *            passed along
     * @return
     */
    public Region getActiveCuboidNode(Location v, boolean ignoreGlobal) {
        if (v == null) {
            return global;
        }
        for (Region tree : rootNodes) {
            if (tree.equalsWorld(v.getWorld(), v.getDimension())) {
                if (!tree.isWithin(v)) {
                    continue;
                }
                Region r = tree.queryChilds(v, 0);
                if(r != null) {
                    return r;
                }
            }
        }
       return ignoreGlobal ? null : global;
    }

    /**
     * Scan for nodes inside a node and parent them if they don't have a parent
     * yet
     * 
     * @param node
     * @return
     */
//    public void reverseFindChildNodes(Region node) {
//        ArrayList<CuboidNode> childs = new ArrayList<CuboidNode>();
//        // System.out.println("Checking for possible childs in "+c.getName());
//        for (Region tree : rootNodes) {
//            if (tree.equalsWorld(node)) {
//                
//                for (CuboidNode n : tree.toList()) {
//                    // if(n.getCuboid().isWithin(c.getFirstPoint()) &&
//                    // n.getCuboid().isWithin(c.getSecondPoint())) {
//                    if (n.getCuboid().cuboidIsWithin(c.getOrigin(),
//                            c.getOffset(), true)) {
//                        // System.out.println(n.getCuboid().getName()+" is within "+c.getName());
//                        if (n.getParent() == null) {
//                            // System.out.println("We have no parent yet!");
//                            if (n.getName().equals(c.getName())) {
//                                // System.out.println("Same name, stupid");
//                                continue;
//                            }
//                            n.getCuboid().setParent(c);
//                            n.getCuboid().hasChanged = true;
//                            childs.add(n);
//                        }
//                    }
//                }
//            }
//        }
//        for (CuboidNode n : childs) {
//            updateCuboidNode(n.getCuboid());
//        }
//        save(false, true);
//    }

    /**
     * Create a list of Regions that contain the given Vector in the given
     * world
     * 
     * @param v
     * @param world
     * @return
     */
    public ArrayList<Region> getCuboidsContaining(Location v,
            String world, int dimension) {
        ArrayList<Region> list = new ArrayList<Region>();
        if (v == null) {
            return list;
        }
        for (Region tree : rootNodes) {
            if (tree.equalsWorld(world, dimension)) {
                if (tree.isWithin(v)) {
                    for (Region node : tree.getChildsDeep()) {
                        if (node.isWithin(v)) {
                            list.add(node);
                        }
                    }
                }
            }
        }
        if (list.isEmpty()) {
            list.add(global);
        }
        return list;
    }
    
//    public boolean canCreateCuboid(CuboidSelection selection, String playerName, CWorld world) {
//        Vector v1 = selection.getOrigin();
//        Vector v2 = selection.getOffset();
//        CuboidNode activeOrigin = getActiveCuboid(new Location(v1.getBlockX(), v1.getBlockY(), v1.getBlockZ(), world.getDimension(), world.getName()), true);
//        return false;
//    }

    /**
     * Get a list of all cuboids in the given world
     * 
     * @param world
     * @param dimension
     * @return CuboidNode List or null if there were no cuboids
     */
    public ArrayList<Region> getAllInDimension(String world, int dimension) {
        ArrayList<Region> list = new ArrayList<Region>();
        for (Region tree : rootNodes) {
            if (tree.equalsWorld(world, dimension)) {
                for (Region node : tree.getChildsDeep()) {
                    list.add(node);
                }
            }
        }

        if (list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    /**
     * Return the Region with the given name or null if not existent.
     * This operation can be very performance sensitive, use with care!
     * 
     * @param name
     * @param world
     * @return CuboidNode or null
     */
    public Region getCuboidByName(String name, String world, int dimension) {
        for (Region tree : rootNodes) {
            
            if (tree.equalsWorld(world, dimension)) {
                Region tmp = tree.queryChilds(name);
                if(tmp != null) {
                    return tmp;
                }
            }
        }
        return null;
    }

    /**
     * This'll try to find the best parent for a given cuboid, if there can be one. 
     * Returns null if no suitable parent was found
     * 
     * @param cube
     * @return
     */
    public Region getPossibleParent(Region cube) {
        // log.logMessage("Going to find a suitable parent for "+cube.getName(),
        // "INFO");
        ArrayList<Region> matches = new ArrayList<Region>();
        for (Region tree : rootNodes) {
            if (tree.equalsWorld(cube)) {
                if(cube.cuboidIsWithin(tree, true)) {
                    Region tmp = tree.queryChilds(cube);
                    if(tmp != null) {
                        matches.add(tmp);
                    }
                }
            }
        }
        if (matches.size() > 0) {
            Region min = null;
            for (int e = 0; e < matches.size(); e++) {
                if (min == null) {
                    min = matches.get(e);
                }
                if (min.getPriority() > matches.get(e).getPriority()) {
                    if (!matches.get(e).getName().equals(cube.getName())) {
                        min = matches.get(e);
                    }
                }
            }
            return min;
        }
        return null;
    }

    /**
     * Return the cuboid tree data.
     * 
     * @return
     */
    public ArrayList<Region> getRootNodeList() {
        return rootNodes;
    }

    /**
     * Effectively remove player from within all areas
     * 
     * @param name
     */
    public void removeFromAllAreas(String name) {
        for (Region tree : rootNodes) {
            for(Region child : tree.getChildsDeep()) {
                child.removePlayerWithin(name);
            }
        }

    }

    /**
     * Remove the player given from all areas that DO NOT match world and
     * dimension specified in the given location
     * 
     * @param playerName
     * @param loc
     */
    public void removeFromAllAreas(String player, Location loc) {
        for (Region tree : rootNodes) {
            
            if (!tree.equalsWorld(loc.getWorld(), loc.getDimension())) {
                for (Region node : tree.getChildsDeep()) {
                    node.removePlayerWithin(player);
                }
            }
        }
    }

}
