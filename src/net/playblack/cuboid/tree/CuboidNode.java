package net.playblack.cuboid.tree;

import java.io.Serializable;
import java.util.ArrayList;

import net.playblack.cuboid.CuboidE;

/**
 * This represents a tree node for CuboidTree. 
 * It contains a list with child nodes and all.
 * @author Christoph Ksoll
 * TODO: Add equals method in case the remove check for nodes in Cuboidtreehandler doesn't work
 */
@SuppressWarnings("serial")
public class CuboidNode implements Serializable {
	/**
	 * Base element. The node is a wrapper for this
	 */
	CuboidE element;
	ArrayList<CuboidNode> childs = new ArrayList<CuboidNode>(0);
	
	/**
	 * Update the first layer of childs with the new parent name
	 */
	public void updateChilds() {
		for(CuboidNode child: childs) {
			child.getCuboid().setParent(element.getName());
		}
	}
	
	/**
	 * Default constructor, this creates a node with an empty, no-space cuboid.
	 * Do not use this if you can!
	 */
	public CuboidNode() {
		element = new CuboidE();
	}
	
	/**
	 * Construct a node element with the given CuboidE
	 * @param cube
	 */
	public CuboidNode(CuboidE cube) {
		element = cube;
	}
	
	/**
	 * Get the CuboidE of this element.
	 * @return
	 */
	public CuboidE getCuboid() {
		return element;
	}
	
	/**
	 * Override the contained CuboidE. If name changes, this will also
	 * update the childs parent entries accordingly.
	 * @param cube
	 */
	public void setCuboid(CuboidE cube) {
		//TODO: Create auto-priority!
		String oldName = element.getName();
		element = cube;
		if(!oldName.equalsIgnoreCase(cube.getName())) {
			updateChilds();
		}
	}
	
	/**
	 * Add a child to this node
	 * @param newNode
	 */
	public void addChild(CuboidNode newNode) {
		childs.add(newNode);
	}
	
	/**
	 * Get a child from this node at the given index
	 * @param index
	 * @return CuboidNode
	 */
	public CuboidNode getChildAt(int index) {
		return childs.get(index);
	}
	
	/**
	 * Remove a child at the given index.
	 * @param index
	 */
	public void removeChildAt(int index) {
		childs.remove(index);
	}
	/**
	 * Get the entire child list
	 * @return
	 */
	public ArrayList<CuboidNode> getChilds() {
		return childs;
	}
	
	/**
	 * Set new childs. This will override all childs that already exist
	 * @param newChilds
	 */
	public void setChilds(ArrayList<CuboidNode> newChilds) {
		childs = newChilds;
	}

}
