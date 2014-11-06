package net.playblack.cuboids.datasource;

import net.playblack.cuboids.regions.Region;

import java.util.ArrayList;

/**
 * This is an abstract data layer which can be extended so we can have multiple
 * sorts of data sources. Look at the initialisation of this one in the enable
 * function to read details
 *
 * @author Chris
 */
public interface BaseData {

    /**
     * Save a single cuboid to file using its node
     *
     * @param node
     */
    abstract public void saveRegion(Region node);

    /**
     * Save the whole treelist to datasource
     *
     * @param treeList list of CuboidTrees
     * @param silent
     */
    public void saveAll(ArrayList<Region> treeList, boolean silent, boolean force);

    /**
     * Load all regions from datasource into memory.
     *
     * @return int the amount of loaded areas
     */
    public int loadAll();

    /**
     * Load a single region from datasource and put into the RegionManager,
     * removing the old reference, if there was any
     *
     * @param name
     * @param world
     */
    public void loadRegion(String name, String world, int dimension);

    /**
     * Deletes a region from the datasource
     *
     * @param node
     */
    public void deleteRegion(Region node);
}
