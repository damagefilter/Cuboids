package com.playblack.cuboid;

import java.util.HashMap;

import com.playblack.vector.Vector;
import com.playblack.cuboid.CuboidData;

/**
 * Resembles a dynamic cuboid selection.
 * Please note: This class does not touch the actual world data. 
 * It merely loads abstracted data from the world and processes it to
 * return it to an Interfacee, which will, in turn, probably edit the actual world data,
 * depending on what the interface does.
 * @author Chris
 *
 */
public class CuboidSelection {

	/**
	 * The starting point
	 */
	protected Vector origin = null;
	/**
	 * The ending Point
	 */
	protected Vector offset = null;
	
//	private HashMap<String, CuboidSelection> selections = new HashMap<String, CuboidSelection>();
	
	/**
	 * Data storage for our blocks
	 */
	private CuboidData content;
	
	/**
	 * The world this selection is set in
	 */
	private String world;

	


	/**
	 * Construct a Cuboid Selection
	 */
	public CuboidSelection() {
//		origin = null;
//		offset = null;
		content = null;
		setWorld("no_world");
	}
	
	/**
	 * Construct a new Cuboid Selection
	 * @param world
	 */
	public CuboidSelection(String world) {
		origin = null;
		offset = null;
		content = null;
		this.setWorld(world);
	}
	
	/**
	 * Construct a Cuboid Selection with origin etc already set.
	 * @param origin
	 * @param offset
	 */
	public CuboidSelection(Vector origin, Vector offset) {
		this.origin = origin;
		this.offset = offset;
		content.setSizeVector(Vector.getAreaVolume(origin, offset));
		world = "no_world";
	}
	
	/**
	 * Construct a Cuboid Selection with origin etc already set.
	 * @param origin
	 * @param offset
	 * @param world
	 */
	public CuboidSelection(Vector origin, Vector offset, String world) {
		this.origin = origin;
		this.offset = offset;
		content = new CuboidData();
		content.setSizeVector(Vector.getAreaVolume(origin, offset));
		this.setWorld(world);
	}
	
	/*
	 * ***************************************************************************
	 * IINITIALISATION SETTING AND GETTING OF OUR FIELDS
	 * ***************************************************************************
	 */
	/**
	 * Check if origin and offset are not null and complete the selection by sorting
	 * the Vectors, making the origin the one nearer to the 0,0,0.
	 */
	private void completeCuboid() {
		if(origin != null && offset != null ) {
			content = new CuboidData();
			content.setSizeVector(Vector.getAreaVolume(origin, offset));
		}
//		if(isComplete == true) {
//			Vector min = Vector.getMinor(origin, offset);
//			Vector max = Vector.getMajor(origin, offset);
//			origin = min;
//			offset = max;
//		}
	}
	
	/**
	 * Check if the Cuboid is completed yet
	 * @return True if complete, false otherwise
	 */
	public boolean isComplete() {
		if(origin != null && offset != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Check if origin is set.
	 * 
	 * @return true if yes, false otherwise
	 */
	public boolean originSet() {
		if(origin != null) {
			System.out.println("Origin not null");
			return true;
		}
		return false;
	}
	
	/**
	 * Check if offset is set.
	 * 
	 * @return true if yes, false otherwise
	 */
	public boolean offsetSet() {
		if(offset != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Retrieve the current origin of this selection
	 * @return Vector origin
	 */
	public Vector getOrigin() {
		return origin;
	}

	/**
	 * Set the current origin of this selection
	 * @param origin
	 */
	public void setOrigin(Vector origin) {
			this.origin = origin;
		//Check if Cuboid is completed and sort the vectors 
		completeCuboid();
		
	}

	/**
	 * Retrieve the current offset of this selection
	 * @return Vector offset
	 */
	public Vector getOffset() {
		return offset;
	}

	public void setOffset(Vector offset) {
			this.offset = offset;
		//Check if Cuboid is completed and sort the vectors 
		completeCuboid();
	}
	
	/**
	 * Retrieve the current world name
	 * @return
	 */
public String getWorld() {
		return world;
	}

	/**
	 * Set the current World name
	 * @param world
	 */
	public void setWorld(String world) {
		this.world = world;
	}

	/**
	 * Clear this Selection, resetting fields to the defaults
	 * After this, you have to set details again
	 */
	public void clear() {
		origin = null;
		offset = null;
		content = null;
		world = "no_world"; 
	}
	

	
	/*
	 * ***************************************************************************
	 * SELECTION SIZE MANIPULATION
	 * ***************************************************************************
	 */
	
	/**
	 * Span the selecion vertically from 0 to 128
	 */
	public boolean expandVert() {
		if(isComplete()) {
			origin = new Vector(origin.getX(), 0, origin.getZ());
			offset = new Vector(offset.getX(), 130, offset.getZ());
			return true;
		}
		else {
			return false;
		}
		
		
	}
	
	/**
	 * Set Cuboids ceiling height
	 * @param ceiling
	 * @return
	 */
	public boolean setCeiling(int ceiling) {
		if(isComplete()) {
			offset = new Vector(offset.getX(), (double)ceiling, offset.getZ());
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean setFloor(int floor) {
		if(isComplete()) {
			origin = new Vector(origin.getX(), (double)floor, origin.getZ());
			return true;
		}
		else {
			return false;
		}
	}
	
	/*
	 * ***************************************************************************
	 * SOME MORE INTERACTIONS YAY
	 * ***************************************************************************
	 */
	
	/**
	 * Turn this selection into a cuboid area and save it.
	 * @param playerlist A String with a list of players, separated by spaces, add o: to define owner or g: to define a group.
	 * @param world The world this cuboid is made in
	 * @param defaultSettings <br> 
	 * The defaultSettings need the following keys:
	 * <ul>
	 * 	<li>allowPvp</li>
	 * 	<li>creeperSecure</li>
	 * 	<li>healing</li>
	 * 	<li>protection</li>
	 * <li>blockFireSpread</li>
	 * 	<li>sanctuary</li>
	 * <li>freeBuild</li>
	 * 	<li>sanctuarySpawnAnimals</li>
	 * </ul>
	 * @return a new CuboidE
	 */
	public CuboidE toCuboid(String playerlist, String name, HashMap<String, Boolean>defaultSettings) {
		CuboidE cube = new CuboidE();
		cube.setPoints(origin, offset);
		String[] allowed = playerlist.split(" ");
		//Add players and groups and all
		for(int i = 0; i < allowed.length; i++) {
			if(allowed[i].indexOf("o:") != -1) {
				cube.addPlayer(allowed[i]);
			}
			else if(allowed[i].indexOf("g:") != -1) {
				cube.addGroup(allowed[i]);
			}
			else {
				cube.addPlayer(allowed[i]);
			}
		}
		cube.setWorld(world);
		cube.setName(name);
		cube.setAllowPvp(defaultSettings.get("allowPvp"));
		cube.setCreeperSecure(defaultSettings.get("creeperSecure"));
		cube.setHealing(defaultSettings.get("healing"));
		cube.setProtection(defaultSettings.get("protection"));
		cube.setSanctuary(defaultSettings.get("sanctuary"));
		cube.setSanctuarySpawnAnimals(defaultSettings.get("sanctuarySpawnAnimals"));
		cube.setFreeBuild(defaultSettings.get("freeBuild"));
		cube.setBlockFireSpread(defaultSettings.get("blockFireSpread"));

		//V 1.2.0
		cube.setLavaControl(defaultSettings.get("lavaControl"));
		cube.setWaterControl(defaultSettings.get("waterControl"));
		cube.setFarmland(defaultSettings.get("farmland"));
		cube.setTntSecure(defaultSettings.get("tntSecure"));
		return cube;
	}
	
	/**
	 * Get the current Content Matrix.
	 * @return
	 */
	public CuboidData getContent() {
		return new CuboidData(content);
	}
	
	/**
	 * Set the content matrix - this will override everything that currently is in the matrix!
	 * @param c
	 */
	public void setContent(CuboidData c) {
		//content = null;
		content = new CuboidData(c);
	}
	
	/**
	 * Empties the BlockBag so it doesn't grow uncontrollably
	 */
	public void clearContentData() {
		if(content != null) {
			content.clearBlockBag();
		}
		else {
			content = new CuboidData();
		}
	}
}
