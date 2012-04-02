package net.playblack.cuboids.regions;

import java.util.ArrayList;

public class CuboidNode {

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
    
    /**
     * Check if this node is a root node (has no parent)
     * @return
     */
    public boolean isRoot() {
        if(element.getParent() == null) {
            return true;
        }
        return false;
    }
    
    /**
     * Get the world for this node
     * @return
     */
    public String getWorld() {
        return element.getWorld();
    }
    
    /**
     * Get the dimension for this node
     * @return
     */
    public int getDimension() {
        return element.getDimension();
    }
    
    /**
     * Get name of wrapped cuboid
     * @return
     */
    public String getName() {
        return element.getName();
    }
    
    /**
     * get parent name of wrapped cuboid
     * @return
     */
    public String getParent() {
        return element.getParent();
    }
    /**
     * Traverse child nodes and return a list of all child nodes that are attached in any way to this node
     * @return
     */
    public ArrayList<CuboidNode> toList() {
        return null;
    }
    
    /**
     * Check if the given cuboidNode is in the same world and dimension as this one
     * @param test
     * @return true if dimension and world are the same, false otherwise
     */
    public boolean equalWorlds(CuboidNode test) {
        if((element.getDimension() == test.getDimension()) && (element.getWorld().equalsIgnoreCase(test.getWorld()))) {
            return true;
        }
        return false;
    }
    
    /**
     * Check if the given cuboidE is in the same world and dimension as this CuboidNodes attached CuboidE
     * @param test
     * @return
     */
    public boolean equalWorlds(CuboidE test) {
        return element.equalWorlds(test);
    }
    
    /**
     * Check this node matches the gven world and dimension requirements (it's inside that)
     * @param world
     * @param dimension
     * @return
     */
    public boolean equalWorlds(String world, int dimension) {
        return element.equalWorlds(world, dimension);
    }
}
