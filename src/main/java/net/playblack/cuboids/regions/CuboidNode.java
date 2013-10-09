package net.playblack.cuboids.regions;

import java.util.ArrayList;

@Deprecated
public class CuboidNode {

    /**
     * Base element. The node is a wrapper for this
     */
    Region element;
    ArrayList<CuboidNode> childs = new ArrayList<CuboidNode>(0);

    /**
     * Update the first layer of childs with the new parent name
     */
    public void updateChilds() {
        for (CuboidNode child : childs) {
            child.getCuboid().setParent(element);
        }
    }

    /**
     * Default constructor, this creates a node with an empty, no-space cuboid.
     * Do not use this if you can!
     */
    public CuboidNode() {
        element = new Region();
    }

    /**
     * Construct a node element with the given CuboidE
     *
     * @param cube
     */
    public CuboidNode(Region cube) {
        element = cube;
    }

    /**
     * Get the CuboidE of this element.
     *
     * @return
     */
    public Region getCuboid() {
        return element;
    }

    /**
     * Override the contained CuboidE. If name changes, this will also update
     * the childs parent entries accordingly.
     *
     * @param cube
     */
    public void setCuboid(Region cube) {
        String oldName = element.getName();
        element = cube;
        if (!oldName.equalsIgnoreCase(cube.getName())) {
            updateChilds();
        }
    }

    /**
     * Add a child to this node
     *
     * @param newNode
     */
    public void addChild(CuboidNode newNode) {
        childs.add(newNode);
    }

    /**
     * Get the entire child list
     *
     * @return
     */
    public ArrayList<CuboidNode> getChilds() {
        return childs;
    }

    /**
     * Set new childs. This will override all childs that already exist
     *
     * @param newChilds
     */
    public void setChilds(ArrayList<CuboidNode> newChilds) {
        childs = newChilds;
    }

    /**
     * Check if this node is a root node (has no parent)
     *
     * @return
     */
    public boolean isRoot() {
        return element.getParent() == null;
    }

    /**
     * Get the world for this node
     *
     * @return
     */
    public String getWorld() {
        return element.getWorld();
    }

    /**
     * Get the dimension for this node
     *
     * @return
     */
    public int getDimension() {
        return element.getDimension();
    }

    /**
     * Get name of wrapped cuboid
     *
     * @return
     */
    public String getName() {
        return element.getName();
    }

    /**
     * get parent name of wrapped cuboid
     *
     * @return
     */
    public Region getParent() {
//        return element.getParent();
        return null;
    }

    /**
     * Traverse child nodes and return a list of all child nodes that are
     * attached in any way to this node
     *
     * @return
     */
    public ArrayList<CuboidNode> toList() {
        ArrayList<CuboidNode> list = new ArrayList<CuboidNode>(0);
        traverse(this, list);
        return list;
    }

    /**
     * pre-order traversal, yay :D
     *
     * @param element
     * @param list
     */
    private void traverse(CuboidNode element, ArrayList<CuboidNode> list) {
        list.add(element);
        for (CuboidNode data : element.getChilds()) {
            traverse(data, list);
        }
    }
}
