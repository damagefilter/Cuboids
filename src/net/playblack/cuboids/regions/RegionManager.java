package net.playblack.cuboids.regions;

import java.util.ArrayList;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.datasource.BaseData;
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;

/**
 * This manages CuboidNodes and takes care of lookups etc
 * @author Chris
 *
 */
public class RegionManager {
    private ArrayList<CuboidNode> rootNodes;
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
        dataSource.loadAll(this);
    }
    
    /**
     * Load a single cuboid from file
     * @param name
     * @param world
     */
    public void loadSingle(String name, String world) {
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
    public boolean saveSingle(String name, String world) {      
        dataSource.saveCuboid(getCuboidByName(name, world));
        return true;
    }
    
    /**
     * Save a single cuboid to backend
     * @param node
     */
    public void saveSingle(CuboidNode node) {
        dataSource.saveCuboid(node);
    }
    
    private void removeNodeFile(CuboidNode node) {
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
        if(cuboidExists(cube.getName(), cube.getDimension())) {
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
                if(root.getWorld().equalsIgnoreCase(cube.getDimension())) {                 
                    for(CuboidNode node: root.toList()) {                   
                        if(node.getCuboid().getName().equalsIgnoreCase(cube.getParent())) {
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
    private void addNode(CuboidNode cube) {
        if(cube.getCuboid().getParent() == null) {
            addRoot(cube);
            saveSingle(cube);
        }
        else {
            for(CuboidNode root : rootNodes) {
                
                if(root.getWorld().equalsIgnoreCase(cube.getCuboid().getDimension())) {
                    
                    for(CuboidNode node: root.toList()) {
                        
                        if(node.getCuboid().getName().equalsIgnoreCase(cube.getCuboid().getParent())) {
                            
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
    public String removeCuboid(CuboidE cube, boolean force)
    {
      for (CuboidNode tree : rootNodes) {
        for (CuboidNode node : tree.toList()) {
          if (node.getName().equalsIgnoreCase(cube.getName())) {
              //Found proper cuboid
            if (node.getChilds().size() <= 0) {
                //has no childs
              if (node.getCuboid().getParent() == null)
              {
                  //cuboid is a tree
                deleteTree(node.getCuboid().getName());
                return "REMOVED";
              }

              CuboidNode parent = getCuboidByName(node.getParent(), node.getWorld());
              if (parent != null) {
                  //Cuboid as a parent so we take it and remove the cuboid from the parents child list
                parent.getChilds().remove(node);
                removeNodeFile(node);
                return "REMOVED";
              }

              return "NOT_REMOVED";
            }
            else {
                if (force) {
                    //We have childs
                  if (node.getParent() == null)
                  {
                      //we have no parent
                      //When forcing just remove the whole tree including child nodes
                      //This also removes the files form disk
                    deleteTree(node.getCuboid().getName());
                    return "REMOVED";
                  }

                  CuboidNode parent = getCuboidByName(node.getParent(), node.getWorld());
                  if (parent != null) {
                      //We have a parent
                      for(CuboidNode childNode : node.getChilds()) {
                          //de-parent child nodes
                          childNode.getCuboid().setParent(ToolBox.stringToNull(parent.getName()));
                          childNode.getCuboid().hasChanged=true;
                          addRoot(childNode);
                          saveSingle(childNode);
                      }
                    parent.getChilds().remove(node);

                    removeNodeFile(node);
                    return "REMOVED";
                  }

                  return "NOT_REMOVED";
                }
                if (node.getCuboid().getParent() == null)
                {
                    CuboidNode parentNode;
                  for (CuboidNode child : node.getChilds()) {
                    child.getCuboid().setParent(ToolBox.stringToNull("null"));
                    addRoot(child);
                    saveSingle(child);
                    parentNode = getCuboidByName(child.getParent(), 
                      child.getCuboid().getDimension());
                    
                    if (parentNode != null) {
                      parentNode.getChilds().remove(child);
                      node.getChilds().remove(child);
                      removeNodeFile(node);
                    }
                  }
                  deleteTree(node.getCuboid().getName());
                  return "REMOVED";
                }
                else {
                    CuboidNode parent = getCuboidByName(node.getParent(), node.getWorld());
                    for (CuboidNode child : node.getChilds()) {
                        //Make childs root nodes
                      //child.getCuboid().setParent(ToolBox.stringToNull("null"));
                      child.getCuboid().setParent(ToolBox.stringToNull(parent.getName()));
                      child.getCuboid().hasChanged = true;
                      addRoot(child);
                      saveSingle(child);
                    }
                    
                    if (parent != null) {
                        parent.getChilds().remove(node);
                        removeNodeFile(node);
                    }
                    else {
                        deleteTree(node.getName());
                    }
                    return "REMOVED";
                }  
            }
          }
        }
      }

      return "NOT_REMOVED_NOT_FOUND";
    }
    
    private void deleteTree(String name) {
        for(CuboidNode root : rootNodes) {
            if(root.getName().equalsIgnoreCase(name)) {
                for(CuboidNode node : root.toList()) {
                    removeNodeFile(node);
                }
                removeNodeFile(root);
                rootNodes.remove(root);
            }
        }
    }
    
    /**
     * Take the root nodes, take them apart, re-sort them so it's all cool,
     * put them back together. Good as new!
     */
    public void autoSortCuboidAreas() {
        ArrayList<String> visited = new ArrayList<String>();
        ArrayList<CuboidNode> workerList = new ArrayList<CuboidNode>(0);
        for(CuboidNode tree : rootNodes) {
            workerList.add(tree);
        }
        for(CuboidNode tree : workerList) {
            if(!visited.contains(tree.getName())) {
                CuboidE cube = tree.getCuboid();
                CuboidNode parent = getPossibleParent(cube);
                if(parent != null) {
                    if(parent.getCuboid().getName().equalsIgnoreCase(cube.getName())) {
                        continue;
                    }
                    cube.setParent(parent.getCuboid().getName());
                    if(cube.getPriority() <= parent.getCuboid().getPriority()) {
                        cube.setPriority(parent.getCuboid().getPriority()+1);
                    }
                    cube.hasChanged = true;
                    updateCuboidNode(cube);
                }
                visited.add(tree.getName());
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
                //parent = null;
                parent = getCuboidByName(node.getCuboid().getParent(), node.getCuboid().getDimension());
                if(parent != null) {
                    //Check if the child is truley completely inside its parent
                    if (!node.getCuboid().cuboidIsWithin(parent.getCuboid().getMinorPoint(), 
                                                         parent.getCuboid().getMajorPoint(), 
                                                         true, 
                                                         parent.getCuboid().getDimension())) {
                        //If not, remove the parent and set to null
                        node.getCuboid().setParent(ToolBox.stringToNull("null"));
                        node.getCuboid().hasChanged=true;
                        updateCuboidNode(node.getCuboid());
                        
                    }
                }
                else {
                    getPossibleParent(node.getCuboid());
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
    public boolean updateCuboidNode(CuboidE cube)
    {
      for (CuboidNode tree : rootNodes) {
        if (tree.getWorld().equalsIgnoreCase(cube.getDimension())) {
          for (CuboidNode node : tree.toList()) {
              //log.logMessage("Unfolding Tree in updateCuboidNode - looking for "+cube.getName(), "INFO");
            if (!node.getName().equalsIgnoreCase(cube.getName())) {
                //no match, continue with next set
              continue;
            }
            
            if ((cube.getParent() != null) && (node.getParent().equalsIgnoreCase(cube.getParent())))
            {
                  //log.logMessage("Parent NOT null AND node parent is same!", "INFO");
                  if ((node.getParent() != null) && (node.isRoot()))
                  {
                       //log.logMessage("Old Cuboid is tree! Moving ...", "INFO");
                       node.getCuboid().hasChanged = true;
                       deleteTree(node.getName());
                       addNode(node);
                       return true;
                  }
                  if (cuboidExists(cube.getParent(), cube.getDimension())) {
                      //log.logMessage("Parent Exists, setting data!", "INFO");
                      node.getCuboid().hasChanged = true;
                      node.setCuboid(cube);
                  
                    return true;
                  }
                  
                  return false;
            }

            if ((cube.getParent() != null) && (!node.getParent().equalsIgnoreCase(cube.getParent())))
            {
               // log.logMessage("Parent Node has changed!!", "INFO");
                  if (cuboidExists(cube.getParent(), cube.getDimension())) {
                      //log.logMessage("Parent Node has changed!!", "INFO");
                    CuboidNode newNode = createNode(cube);
                    newNode.setChilds(node.getChilds());
                  
                    removeCuboid(node.getCuboid(), true);
                    removeNodeFile(node);
                    newNode.getCuboid().hasChanged = true;
                    addNode(newNode);
                  
                    return true;
                  }
                  
                  return false;
            }

          //If we're here the cuboid has no parent
            if (cube.getParent() == null && node.getParent() == null)
            {
                  //log.logMessage("Parent is null AND node Parent is null (no structural changes)", "INFO");
                //Nothing severe changed, only update
              cube.hasChanged = true;
              node.setCuboid(cube);
              return true;
            }
            if (cube.getParent() == null && node.getParent() != null) {
               // log.logMessage("Parent is null AND node Parent is NOT null (converting to tree)", "INFO");
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
    public boolean cuboidExists(String cube, String world) {
        for(CuboidNode tree : rootNodes) {
            if(tree.getWorld().equalsIgnoreCase(world)) {
                    for(CuboidNode node : tree.toList()) {
                        if(node.getName().equalsIgnoreCase(cube)) {
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
     * 
     * @return CuboidE
     */
    public CuboidNode getActiveCuboid(Vector v, String world) {
        nodeList.clear();
        if (v == null) {
            return global;
        }
        for (CuboidNode tree : rootNodes) {
            if (!tree.getCuboid().isWithin(v)) {
                continue;
            }
            if (tree.getWorld().equalsIgnoreCase(world)) {
                for (CuboidNode node : tree.toList()) {
                    if (node.getCuboid().isWithin(v)) {
                        nodeList.add(node);
                    }
                }
            }
        }
        if(nodeList.isEmpty()) {
            return global;
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
            if(tree.getWorld().equalsIgnoreCase(node.getCuboid().getDimension())) {
                for(CuboidNode n : tree.toList()) {
                    //if(n.getCuboid().isWithin(c.getFirstPoint()) && n.getCuboid().isWithin(c.getSecondPoint())) {
                    if(n.getCuboid().cuboidIsWithin(c.getMajorPoint(), c.getMinorPoint(), true)) {
                        //System.out.println(n.getCuboid().getName()+" is within "+c.getName());
                        if(n.getParent() == null) {
                        //  System.out.println("We have no parent yet!");
                            if(n.getName().equalsIgnoreCase(c.getName())) {
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
    public ArrayList<CuboidE> getCuboidsContaining(Vector v, String world) {
        ArrayList<CuboidE> list = new ArrayList<CuboidE>(0);
        if(v == null) {
            return list;
        }
        for(CuboidNode tree : rootNodes) {
            if(tree.getCuboid().isWithin(v) && tree.getWorld().equalsIgnoreCase(world)) {
                for(CuboidNode node : tree.toList()) {
                    if(node.getCuboid().isWithin(v)) {
                        list.add(node.getCuboid());
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
     * @return CuboidNode List or null if there were no cuboids
     */
    public ArrayList<CuboidNode> getAllInWorld(String world) {
        ArrayList<CuboidNode> list = new ArrayList<CuboidNode>(0);
        for(CuboidNode tree : rootNodes) {
            if(tree.getWorld().equalsIgnoreCase(world)) {
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
    public CuboidNode getCuboidByName(String name, String world) {
        for(CuboidNode tree : rootNodes) {
            if(tree.getWorld().equalsIgnoreCase(world)) {
                for(CuboidNode node : tree.toList()) {
                    if(node.getName().equalsIgnoreCase(name)) {
                        return node;
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
        ArrayList<CuboidNode> list = new ArrayList<CuboidNode>(0);
        for(CuboidNode tree : rootNodes) {
            if(tree.getWorld().equalsIgnoreCase(cube.getDimension())) {
                for(CuboidNode node : tree.toList()) {
                    if(cube.cuboidIsWithin(node.getCuboid().getMajorPoint(), node.getCuboid().getMinorPoint(), true, node.getCuboid().getDimension())) {
                        //log.logMessage("Adding possible parent to list!", "INFO");
                        list.add(node);
                    }
                }
            }
        }
        if(list.size() > 0) {
            CuboidNode min=null;
            for(int e = 0; e<list.size();e++) {
                if(min == null) {
                    min = list.get(e);
                }
                if(min.getCuboid().getPriority() <= list.get(e).getCuboid().getPriority()) {
                    if(!list.get(e).getName().equalsIgnoreCase(cube.getName())) {
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
    
}
