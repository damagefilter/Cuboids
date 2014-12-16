package net.playblack.cuboids.regions;

import net.canarymod.Canary;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.Config;
import net.playblack.cuboids.datasource.BaseData;
import net.playblack.mcutils.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This manages Regions and takes care of lookups etc
 *
 * @author Chris
 */
public class RegionManager {
    private static RegionManager instance = null;
    private HashMap<String, List<Region>> rootNodes = new HashMap<String, List<Region>>();
    private BaseData dataSource;
    private Region global;

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
     */
    public void updateGlobalSettings() {
        global = Config.get().getGlobalSettings();
    }

    public boolean setGlobalProperty(String name, Region.Status status) {
        return global.setProperty(name, status);
    }
    public boolean unsetGlobalProperty(String name) {
        return global.removeProperty(name);
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
    public int load() {
        return dataSource.loadAll();
    }

    /**
     * Load a single cuboid from file
     *  @param name
     * @param world
     */
    public void loadSingle(String name, String world) {
        dataSource.loadRegion(name, world);
    }

    /**
     * Save all cuboid files to backend
     *
     */
    public void save() {
        dataSource.saveAll(rootNodes);
    }

    /**
     * Save a single cuboid to backend
     *
     * @param name
     * @param world
     * @return
     */
    public boolean saveSingle(String name, String world) {
        dataSource.saveRegion(getRegionByName(name, world));
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
        if (cuboidExists(cube.getName(), cube.getWorld())) {
            Debug.log("Region already exists! Not adding " + cube.getName());
            return false;
        }

        Region parent = getPossibleParent(cube);
        if (parent != null) {
            cube.setParent(parent);
        }
        else {
            addRoot(cube);
        }
        saveSingle(cube);
        return true;
    }

    /**
     * Add a new root node
     *
     * @param root
     */
    public void addRoot(Region root) {
        if (root.isRoot()) {
            if (!rootNodes.containsKey(root.getWorld())) {
                rootNodes.put(root.getWorld(), new ArrayList<Region>());
            }
            rootNodes.get(root.getWorld()).add(root);
        }
    }

    /**
     * Remove a cuboid from the tree list
     * TODO: Make this a void and remove the force thing
     *
     * @param cube
     * @return
     */
    public void removeRegion(Region cube) {

        List<Region> roots = rootNodes.get(cube.getWorld());
        if (roots == null) {
            Debug.logError(cube.getName() + " is within an unregistered world and therefore not tracked. Cannot remove!");
            return;
        }
        if (roots.contains(cube)) {
            roots.remove(cube);
        }
        //Detach the childs of the region and re-sort them in
        //If we don't do this, all childs will be thrown away
        //in the next GC cycle
        ArrayList<Region> cleanUpList = cube.detachAllChilds();

        //Put parent-less childs into the root list
        for (Region child : cleanUpList) {
            if (child.getParent() == null) {
                addRoot(child);
            }
        }
        //If this was not a root region, we need to detach it from its parent
        //so it will not be taken into consideration anymore
        cube.detach();
        removeNodeFile(cube);
    }

    /**
     * Take the root nodes, take them apart, re-sort them so it's all cool, put
     * them back together. Good as new!
     */
    public void autoSortRegions(String world) {
        ArrayList<Region> workerList = new ArrayList<Region>();
        ArrayList<Region> rootList = new ArrayList<Region>();
        List<Region> roots = rootNodes.get(world);
        for (Region tree : roots) {
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
            }

            if (parent == null) {
                tree.setParent(null);
                rootList.add(tree);
            }
        }
        roots = rootList;
        rootNodes.get(world).clear();
        rootNodes.get(world).addAll(roots);
    }

    public void autoSortRegions() {
        for (String world : rootNodes.keySet()) {
            autoSortRegions(world);
        }
    }

    /**
     * Clear up parent parent relations. That means removing parent relations
     * where childs are not 100% inside their parent.
     */
    public void cleanParentRelations(String world) {
        ArrayList<Region> detachedRegions = new ArrayList<Region>();
        for (Region tree : rootNodes.get(world)) {
            detachedRegions.addAll(tree.fixChilds());
        }

        //Put all detached into root
        for (Region r : detachedRegions) {
            addRoot(r);
        }
        //Re-sort root nodes
        autoSortRegions();
    }

    public void cleanParentRelations() {
        for (String world : rootNodes.keySet()) {
            cleanParentRelations(world);
        }
    }

    /**
     * Check if a cuboid with the given name, in the give world exists
     *
     * @param cube
     * @param world
     * @return
     */
    public boolean cuboidExists(String cube, String world) {
        if (!rootNodes.containsKey(world)) {
            return false;
        }
        for (Region tree : rootNodes.get(world)) {
            if (tree.equalsWorld(world)) {
                if (tree.queryChilds(cube) != null) {
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
     *
     * @param v            the location - pass null to return the global settings (must also set ignoreGlobal)
     * @param ignoreGlobal pass true to ignore the global settings
     * @return
     */
    public Region getActiveRegion(Location v, boolean ignoreGlobal) {
        if (v == null) {
            return !ignoreGlobal ? global : null;
        }
        String world = v.getWorld().getFqName();
        if (!rootNodes.containsKey(world)) {
            return ignoreGlobal ? null : global;
        }
        for (Region tree : rootNodes.get(world)) {
            if (!tree.isWithin(v)) {
                continue;
            }
            Region r = tree.queryChilds(v);
            if (r != null) {
                return r;
            }
        }
        return ignoreGlobal ? null : global;
    }

    /**
     * Create a list of Regions that contain the given Vector in the given
     * world
     *
     * @param v
     * @return
     */
    public ArrayList<Region> getCuboidsContaining(Location v) {
        ArrayList<Region> list = new ArrayList<Region>();
        if (v == null) {
            return list;
        }
        String world = v.getWorld().getFqName();
        if (!rootNodes.containsKey(world)) {
            return list;
        }

        for (Region tree : rootNodes.get(world)) {
            if (tree.isWithin(v)) {
                for (Region node : tree.getChildsDeep(new ArrayList<Region>())) {
                    if (node.isWithin(v) && !list.contains(node)) {
                        list.add(node);
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
     * @return Region List or null if there were no cuboids
     */
    public ArrayList<Region> getAllInDimension(String world) {
        ArrayList<Region> list = new ArrayList<Region>();

        if (!rootNodes.containsKey(world)) {
            return list;
        }
        for (Region tree : rootNodes.get(world)) {
            if (tree.equalsWorld(world)) {
                tree.getChildsDeep(list);
            }
        }

        if (list.size() > 0) {
            return list;
        }
        else {
            return null;
        }
    }

    /**
     * Return the Region with the given name or null if not existent.
     * This operation can be very performance sensitive, use with care!
     *
     * @param name
     * @param world
     * @return Region or null
     */
    public Region getRegionByName(String name, String world) {
        if (!rootNodes.containsKey(world)) {
            return null;
        }
        for (Region tree : rootNodes.get(world)) {
            if (tree.equalsWorld(world)) {
                Region tmp = tree.queryChilds(name);
                if (tmp != null) {
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

        if (!rootNodes.containsKey(cube.getWorld())) {
            // This is either the first in the world or this happens during the load cycle of the plugin.
            // In the latter case this is ultimately a root region anyway as the data source as already managed parenting.
            return null;
        }
        ArrayList<Region> matches = new ArrayList<Region>();
        for (Region tree : rootNodes.get(cube.getWorld())) {
            if (tree.equalsWorld(cube)) {
                if (cube.cuboidIsWithin(tree, true)) {
                    Region tmp = tree.queryChilds(cube);
                    if (tmp != null && tmp != cube) {
                        matches.add(tmp);
                    }
                }
            }
        }
        if (matches.size() > 0) {
            Region min = null;
            for (Region matche : matches) {
                if (min == null) {
                    min = matche;
                }
                if (min.getPriority() > matche.getPriority()) {
                    if (!matche.getName().equals(cube.getName())) {
                        min = matche;
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
    public ArrayList<Region> getRegionsWithProperty(String property) {
        ArrayList<Region> regions = new ArrayList<Region>();

        for (List<Region> roots : rootNodes.values()) {
            for (Region r : roots) {
                if (r.getProperty(property) == Region.Status.ALLOW) {
                    regions.add(r);
                }
            }
        }
        return regions;
    }

    /**
     * This will sort the given cuboid into the root nodes list,
     * if it has no parent or removes it from the list if it is still there,
     * but suddenly has a parent attached
     *
     * @param cube
     */
    public void updateRegion(Region cube) {
        if (!rootNodes.containsKey(cube.getWorld())) {
            rootNodes.put(cube.getWorld(), new ArrayList<Region>());
        }
        List<Region> roots = rootNodes.get(cube.getWorld());

        if (cube.getParent() == null) {
            if (!roots.contains(cube)) {
                addRoot(cube);
            }
        }
        else {
            if (roots.contains(cube)) {
                //We have a parent but are filed under rootNodes. must change ...
                roots.remove(cube);
                //Parent is already set and updated, no need for more
            }
        }

    }

}
