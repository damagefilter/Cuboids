package com.playblack;


import com.playblack.vector.Vector;

/**
 * This is a collection of useful stuff that we might or might not need
 * @author Chris
 *
 */
public class ToolBox {
	
	/**
	 * Take the position of a MC block as Vector and adjust it to fix the negative values bug.
	 * THIS IS USED TO CORRECT VALUES THAT WERE TAKEN FROM A Block OBJECT!!!
	 * @param vector
	 * @return new Vector with corrected values
	 */
	public Vector adjustWorldBlock(Vector vector) {
		int px = vector.getBlockX();
		int py = vector.getBlockY();
		int pz = vector.getBlockZ();
		if (px < 0) {
		    px = px + 1;
		}
		if (pz < 0){
		    pz = pz + 1;
		}
		return new Vector(px, py, pz);
	}
	
	/**
	 * Take the position of a MC block as Vector and adjust it to fix the negative values bug.
	 * This is the one wth -2
	 * @param vector
	 * @return new Vector with corrected values
	 */
	public Vector adjustBlockPosition(Vector v) {
		if (v.getX() < 0) {
		    v.setX(v.getX()-2);
		}
		if (v.getZ() < 0) {
		    v.setZ(v.getZ()-2);
		}
		return new Vector(v.getX(), v.getY(), v.getZ());
	}
	
	/**
	 * Adjust the calculated block positions.
	 * This is used to correct some positioning bugs
	 * @param pos
	 * @return
	 */
	public Vector adjustWorldPosition(Vector pos) {
		if(pos.getBlockX() < 0) {
			pos.setX(pos.getBlockX()-1);
		}
		if(pos.getBlockZ() < 0) {
			pos.setZ(pos.getBlockZ()-1);
		}
		return new Vector(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
	}
	
	/**
	 * Turn a String into a boolean value. If the string doesn't equal the words true or false, this'll return false
	 * @param boo
	 * @return boolean
	 */
	public static boolean stringToBoolean(String boo) {
		if(boo.equalsIgnoreCase("true")) {
			return true;
		}
		else if(boo.equalsIgnoreCase("false")){
			return false;
		}
		else {
			return false;
		}
	}
	
	public static String stringToNull(String str) {
		String s = null;
		if(str == null) {
			return null;
		}
		if(str.equalsIgnoreCase("null")) {
			return s;
		}
		else {
			return str;
		}
	}
}
