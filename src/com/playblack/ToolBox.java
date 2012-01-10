package com.playblack;


import com.playblack.vector.Vector;

/**
 * This is a collection of useful stuff that we might or might not need
 * @author Chris
 *
 */
public class ToolBox {
	
	/**
	 * Adjust the calculated block positions.
	 * This is used to correct some positioning bugs
	 * @param pos
	 * @return
	 */
	public Vector adjustWorldPosition(Vector pos) {
//		if(pos.getX() < 0.0D) {
//			pos.setX(pos.getX()-1.0);
//		}
//		if(pos.getZ() < 0.0D) {
//			pos.setZ(pos.getZ()-1.0);
//		}
//		return new Vector(pos.getX(), pos.getY(), pos.getZ());
		//NOTE: This is a dirty quick fix to work around the fact that all of the sudden,
		//there is no issue with negative coordinates anymore
		//In case it somehow, magically, comes back again, I'm leaving this here to uncomment again
		return pos;
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
