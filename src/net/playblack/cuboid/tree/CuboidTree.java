package net.playblack.cuboid.tree;

import java.util.ArrayList;
import java.util.List;

import net.playblack.EventLogger;
import net.playblack.cuboid.CuboidE;

/**
 * Define a Tree. This contains the root node and methods to get it.
 * TODO: index childNodes by cuboidE or vector to improve performance?
 * @author Christoph Ksoll
 *
 */

public class CuboidTree {
	
	CuboidNode root;
	EventLogger log;
	String world; //The world for this tree
	String name; //This is effectively the name of the cuboid that is contained in the root node.
	
	/**
	 * Default constructor, makes new tree root with the given node.
	 * The contained CuboidE will also determine the world this tree is valid in.
	 * @param node
	 */
	public CuboidTree(CuboidNode node, EventLogger log) {
		root = node;
		world = node.getCuboid().getWorld();
		name = node.getCuboid().getName();
		this.log = log;
	}
	
	/**
	 * Get the root node of this tree
	 * @return
	 */
	public CuboidNode getRoot() {
		return root;
	}
	
	/**
	 * Override the old data with a new one (this can cuse serious trouble if old tree root has childs)
	 * @param node
	 */
	public void setRootCuboid(CuboidE node) {
		root.setCuboid(node);
		world = node.getWorld();
		name = node.getName();
	}
	/**
	 * Return the name of this tree. 
	 * The name is effectively the same as the CuboidEs name that is contained in the 
	 * CuboidNode root element
	 * @return String name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the world this tree is "planted" in.
	 * Only Cuboids and CuboidNodes that are in the same world can be added to this.
	 * @return
	 */
	public String getWorld() {
		return world;
	}
	
	/**
	 * Create a list of ALL nodes in this tree and I mean ALL FUCKING NODES
	 * by pre-order traversal beginning at the root.
	 * @return ArrayList list
	 */
	public List<CuboidNode> toList() {
		List<CuboidNode>  list = new ArrayList<CuboidNode>(0);
        traverse(root, list);
        return list;
	}
	
	/**
	 * Check if the root has childs
	 * by pre-order traversal beginning at the root.
	 * @return true if has childs, false otherwise
	 */
	public boolean hasChilds() {
		if(root.getChilds().size() > 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Used in toList() to generate a list of ALL FUCKING NODES in this tree.
	 * @param element
	 * @param list
	 */
	private void traverse(CuboidNode element, List<CuboidNode> list) {
		//log.logMessage("Traversing - Current: "+element.getCuboid().getName(), "INFO");
        list.add(element);
        for (CuboidNode data : element.getChilds()) {
            traverse(data, list);
        }
    }

}
