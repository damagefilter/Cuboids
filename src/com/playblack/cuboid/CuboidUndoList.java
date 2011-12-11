package com.playblack.cuboid;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Object holds a list of the X last actions made by the user so they can be undone at any time
 * @author Chris
 * TODO: Abstract this one more step so a handler can hold multiple of these for every using user one.
 */
public class CuboidUndoList {

	//private ArrayList<String> undo = new ArrayList<String>(0);
	private HashMap<String,ArrayList<String>> undoBag = new HashMap<String,ArrayList<String>>();
	private int maxUndo = 0;
	
	public CuboidUndoList(int undos) {
		maxUndo = undos;
	}
	
	
	public ArrayList<String> getUndoList(String player) {
		if(undoBag.get(player) == null) {
			undoBag.put(player, new ArrayList<String>(0));
		}
		return undoBag.get(player);
	}
	/**
	 * Find out the max possible undos for this undo list
	 * @return
	 */
	public int getUndos() {
		return maxUndo;
	}
	
	/**
	 * Add a content matrix to store so it can later be used to revert changes
	 * @param c
	 */
	public void add(String c, String player) {
		if(undoBag.get(player) == null) {
			undoBag.put(player, new ArrayList<String>(0));
		}
		if(undoBag.get(player).size() < maxUndo) {
			undoBag.get(player).add(c);
		}
		else {
			undoBag.get(player).remove(0);
			undoBag.get(player).add(c);
		}
	}
	
	/**
	 * Undo an operation or jumpto to the x last done change to revert it
	 * @param amount
	 * @return CuboidContentMatrix
	 */
	public String undo(int amount, String player) {
		int index = (undoBag.get(player).size()-1) - amount;
		if(index < 0) {
			index = 0;
		}	
		
		return undoBag.get(player).remove(index);
	}
	
}
