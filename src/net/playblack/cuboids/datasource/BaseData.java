package net.playblack.cuboids.datasource;

import java.util.ArrayList;

import net.playblack.cuboids.regions.CuboidNode;
import net.playblack.cuboids.regions.RegionManager;

/**
 * This is an abstract data layer which can be extended so we can have multiple
 * sorts of data sources. Look at the initialisation of this one in the enable
 * function to read details
 * 
 * @author Chris
 * 
 */
public interface BaseData {

    /**
     * Save a single cuboid to file using its node
     * 
     * @param node
     */
    abstract public void saveCuboid(CuboidNode node);

    /**
     * Save the whole treelist to files
     * 
     * @param treeList
     *            list of CuboidTrees
     * @param silent
     */
    public void saveAll(ArrayList<CuboidNode> treeList, boolean silent,
            boolean force);

    public void loadAll(RegionManager handler);

    public void loadCuboid(RegionManager handler, String name, String world);

    public void removeNode(CuboidNode node);
}
