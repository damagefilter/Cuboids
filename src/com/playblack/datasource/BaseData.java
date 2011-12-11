package com.playblack.datasource;

import java.util.ArrayList;

//import com.playblack.cuboid.CuboidE;
import com.playblack.cuboid.tree.CuboidNode;
import com.playblack.cuboid.tree.CuboidTree;
import com.playblack.cuboid.tree.CuboidTreeHandler;

/**
 * This is an abstract data layer which can be extended so we can have multiple sorts of data sources.
 * Look at the initialisation of this one in the enable function to read details 
 * @author Chris
 *
 */
abstract public class BaseData {

	/**
	 * Save a single cuboid to file using its node
	 * @param node
	 */
	abstract public void saveCuboid(CuboidNode node);
	/**
	 * Save the whole treelist to files
	 * @param treeList list of CuboidTrees
	 * @param silent
	 */
	abstract public void saveAll(ArrayList<CuboidTree> treeList, boolean silent, boolean force);
	abstract public void loadAll(CuboidTreeHandler handler);
	abstract public void loadCuboid(CuboidTreeHandler handler, String name, String world);
	abstract public void removeNode(CuboidNode node);
//	abstract public CuboidE toCuboid();
}
