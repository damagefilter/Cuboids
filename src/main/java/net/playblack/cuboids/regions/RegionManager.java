package net.playblack.cuboids.regions;

import java.util.ArrayList;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.datasource.BaseData;
import net.playblack.cuboids.datasource.FlatfileDataLegacy;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.Location;

/**
 * This manages CuboidNodes and takes care of lookups etc
 *
 * @author Chris
 *
 */
public class RegionManager {
    private ArrayList<Region> rootNodes = new ArrayList<Region>(15);
    private BaseData dataSource;
    private Region global;

    private static RegionManager instance = null;

    private RegionManager(BaseData dataSource) {
        this.dataSource = dataSource;
        Region insert = new Region();
        insert.putAll(Config.get().getGlobalSettings().getAllProperties());
        global = insert;
    }

    public static RegionManager get() {
        if (instance == null) {
            instance = new RegionManager(Config.get().getDataSource());
        }
        return instance;
    }

    /**
     * This must be called after the global settings in Config have changed!!!
     *
     * @param props
     */
    public void updateGlobalSettings() {
        global = Config.get().getGlobalSettings();
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
        new FlatfileDataLegacy().loadAll();
        dataSource.loadAll();
    }

    /**
     * Load a single cuboid from file
     *
     * @param name
     * @param world
     */
    public void loadSingle(String name, String world, int dimension) {
        dataSource.loadRegion(name, world, dimension);
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
        dataSource.saveRegion(getRegionByName(name, world, dimension));
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
        dataSource.deleteRegion(node);
    }

    /*
     * *********************************************************************************
     * ADD / REMOVE / CHANGE Cuboids
     * ********************************************
     * *************************************
     */

    /**
     * Add a new managed region and sort it in.
     *
     * @param cube
     * @return
     */
    public boolean addRegion(Region cube) {
        if (cuboidExists(cube.getName(), cube.getWorld(), cube.getDimension())) {
            Debug.log("Region already exists! Not adding it");
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
     * @return
     */
    public void removeRegion(Region cube) {

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
    }

    /**
     * Take the root nodes, take them apart, re-sort them so it's all cool, put
     * them back together. Good as new!
     */
    public void autoSortRegions() {
        ArrayList<Region> workerList = new ArrayList<Region>();
        ArrayList<Region> rootList = new ArrayList<Region>();
        for (Region tree : rootNodes) {
            tree.getChildsDeep(workerList);
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
     * Get the active region at the given location
     * @param v the location - pass null to return the global settings (must also set ignoreGlobal)
     * @param ignoreGlobal pass true to ignore the global settings
     * @return
     */
    public Region getActiveRegion(Location v, boolean ignoreGlobal) {
        if (v == null && !ignoreGlobal) {
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
     * Add a player to this region and all child regions it is within
     * @param player
     */
    public void addPlayerToRegions(CPlayer player, Location loc) {
        for(Region tree : rootNodes) {
            if(tree.isWithin(loc)) {
                tree.addPlayerWithin(player, loc);
            }
        }
    }

    public void removePlayerFromRegion(CPlayer player, Location loc) {
        Region r = player.getCurrentRegion();
        if(r != null) {
            if(!r.isWithin(loc)) {
                player.setRegion(null);
            }
        }
    }

    /**
     * Create a list of Regions that contain the given Vector in the given
     * world
     *
     * @param v
     * @param world
     * @return
     */
    public ArrayList<Region> getCuboidsContaining(Location v, String world, int dimension) {
        ArrayList<Region> list = new ArrayList<Region>();
        if (v == null) {
            return list;
        }
        for (Region tree : rootNodes) {
            if (tree.equalsWorld(world, dimension)) {
                if (tree.isWithin(v)) {
                    for (Region node : tree.getChildsDeep(new ArrayList<Region>())) {
                        if (node.isWithin(v) && !list.contains(node)) {
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
                tree.getChildsDeep(list);
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
    public Region getRegionByName(String name, String world, int dimension) {
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
     * This will sort the given cuboid into the root nodes list,
     * if it has no parent or removes it from the list if it is still there,
     * but suddenly has a parent attached
     * @param cube
     */
    public void updateRegion(Region cube) {
        if(cube.getParent() == null) {
            if(!rootNodes.contains(cube)) {
                addRoot(cube);
            }
        }
        else {
            if(rootNodes.contains(cube)) {
                //We have a prent but are filed unter rootNodes. must change ...
                rootNodes.remove(cube);
                //Parent is already set and updated, no need for more
            }
        }

    }

}
