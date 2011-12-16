package com.playblack.cuboid;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.playblack.vector.Vector;
import com.playblack.blocks.BaseBlock;


/**
 * Resembles a dynamic cuboid selection.<br>
 * This class does not modify the actual world.<br>
 * It merely holds the information that is required by WorldObserver to actually change the world
 * @author Chris
 *
 */
public class CuboidSelection {

	/**
	 * The starting point
	 */
	private Vector origin = null;
	/**
	 * The ending Point
	 */
	private Vector offset = null;
	
	private String world = "no_world";
	
	private LinkedHashMap<Vector,BaseBlock> blocks;
	
	private int sculptData = 0;
	private int sculptType = 0;
	private int sculptRadius = 3;
	
	
	/**
	 * Default Contructor<br>
	 * This will set origin and offset to null and the block list 
	 * will be default initiated with 0 elements
	 */
	public CuboidSelection() {
		blocks = new LinkedHashMap<Vector,BaseBlock>(0);
	}
	/**
	 * This will init origin and offset as null and init the block list with listSize elements
	 * @param listSize
	 */
	public CuboidSelection(int listSize) {
		blocks = new LinkedHashMap<Vector,BaseBlock>(listSize);
	}
	
	/**
	 * This will set origin and offset but init the block list with 0 elements
	 * @param origin
	 * @param offset
	 */
	public CuboidSelection(Vector origin, Vector offset) {
		this.origin = origin;
		this.offset = offset;
		blocks = new LinkedHashMap<Vector,BaseBlock>(0);
	}
	
	/**
	 * Init the CuboidSelection accordingly with all details set
	 * @param origin
	 * @param offset
	 * @param listSize
	 */
	public CuboidSelection(Vector origin, Vector offset, int listSize) {
		this.offset = offset;
		this.origin = origin;
		blocks = new LinkedHashMap<Vector,BaseBlock>(listSize);
	}
	
	/**
	 * Set a block at a given point.
	 * @param v
	 * @param block
	 */
	public void setBlockAt(Vector v, BaseBlock block) {
		blocks.put(v, block);
	}
	
	/**
	 * Get a block from this selection from a given point
	 * @param v
	 * @return
	 */
	public BaseBlock getBlockAt(Vector v) {
		return blocks.get(v);
	}
	
	/**
	 * Set the origin point of this selection
	 * @param v
	 */
	public void setOrigin(Vector v) {
		origin = v;
	}
	
	/**
	 * Get the origin of this selection
	 * @return
	 */
	public Vector getOrigin() {
		return origin;
	}
	
	/**
	 * Set the offset point of this selection
	 * @param v
	 */
	public void setOffset(Vector v) {
		offset = v;
	}
	
	/**
	 * Get the offset point of this selection
	 * @return
	 */
	public Vector getOffset() {
		return offset;
	}
	
	/**
	 * Get the data value for the current sculpt tool setting
	 * @return
	 */
	public int getSculptData() {
		return sculptData;
	}
	
	/**
	 * Set the data value for the current sculpt tool setting
	 * @param sculptData
	 */
	public void setSculptData(int sculptData) {
		this.sculptData = sculptData;
	}
	
	/**
	 * Get block type for current sculpt tool setting
	 * @return
	 */
	public int getSculptType() {
		return sculptType;
	}
	
	/**
	 * Set block type for current sculpt tool setting
	 */
	public void setSculptType(int sculptType) {
		this.sculptType = sculptType;
	}
	
	public int getSculptRadius() {
		return sculptRadius;
	}
	public void setSculptRadius(int sculptRadius) {
		this.sculptRadius = sculptRadius;
	}
	/**
	 * Return the current Block List
	 * @return
	 */
	public LinkedHashMap<Vector,BaseBlock> getBlockList() {
		return blocks;
	}
	
	/**
	 * Override the current block list with a new one
	 * @param list
	 */
	public void setBlockList(LinkedHashMap<Vector,BaseBlock> list) {
		blocks = list;
	}
	
	public String getWorld() {
		return world;
	}
	public void setWorld(String world) {
		this.world = world;
	}
	/**
	 * Reset all properties to their defaults and empty the block list
	 */
	public void clearAll() {
		blocks.clear();
		origin=null;
		offset=null;
		sculptType=0;
		sculptData=0;
	}
	
	/**
	 * Clearthe block list. This leaves points and sculpt information untouched
	 */
	public void clearBlocks() {
		blocks.clear();
	}
	
	/**
	 * Check if the selection has both points set
	 * @return
	 */
	public boolean isComplete() {
		if(origin != null && offset != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check if the origin point is set
	 * @return
	 */
	public boolean originSet() {
		if(origin != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check if the offset point is set
	 * @return
	 */
	public boolean offsetSet() {
		if(offset != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Set Ceiling height for the selection
	 * @param height
	 */
	public void setCeiling(int height) {
		origin.setY(height);
	}
	
	/**
	 * Set the floor height for the selection
	 * @param height
	 */
	public void setFloor(int height) {
		offset.setY(height);
	}
	
	/**
	 * Expand the selection vertically from top to bottom
	 */
	public void expandVert() {
		origin.setY(127);
		offset.setY(0);
	}
	
	/**
	 * Turn this selection info a cuboid
	 * @param playerlist
	 * @param name
	 * @param defaultSettings
	 * @param world
	 * @return
	 */
	public CuboidE toCuboid(String playerlist, String name, HashMap<String, Boolean> defaultSettings, String world){
		CuboidE cube = new CuboidE();
	    cube.setPoints(this.origin, this.offset);
	    String[] allowed = playerlist.split(" ");

	    for (int i = 0; i < allowed.length; i++) {
	      if (allowed[i].indexOf("o:") != -1) {
	        cube.addPlayer(allowed[i]);
	      }
	      else if (allowed[i].indexOf("g:") != -1) {
	        cube.addGroup(allowed[i]);
	      }
	      else {
	        cube.addPlayer(allowed[i]);
	      }
	    }
	    cube.setWorld(world);
	    cube.setName(name);
	    cube.overrideProperties(defaultSettings);
	    
	    return cube;
	}
	
	/**
	 * Sort the selection points so origin is the greater one and offset the smaller
	 */
	public void sortEdgesOriginFirst() {

		Vector or_temp = Vector.getMaximum(origin, offset);
		Vector off_temp = Vector.getMinimum(origin, offset);
		origin = or_temp;
		offset = off_temp;
	}
	
	/**
	 * Sort the selection edges so offset is the greater one and origin the smaller
	 */
	public void sortEdgesOffsetFirst() {
		Vector or_temp = Vector.getMaximum(origin, offset);
		Vector off_temp = Vector.getMinimum(origin, offset);
		origin = off_temp;
		offset = or_temp;
	}
	
}
