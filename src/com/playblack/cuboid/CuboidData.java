package com.playblack.cuboid;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.playblack.vector.Vector;

/**
 * Store data about blocks and all in here. This'll replace CuboidContentMatrix 
 * @author Chris
 *
 */
@SuppressWarnings("serial")
public class CuboidData implements Serializable {
	
	/*
	 * for (Iterator it=map.keySet().iterator(); it.hasNext(); ) {
		Object key = it.next();
		Object value = map.get(key);
		System.out.println( �Key = �+ key.toString() + � Value = � + value.toString() );
	 */
	private LinkedHashMap<Vector,Blockc> blockBag;
	/*
	 * Those are for some compatibility reason because there isn't always a selection to the data
	 * TODO: refactor and remove them so CuboidData is never without a selection
	 */
	public Vector start;
	public Vector end;
	private Vector size;
	
	/**
	 * Default init constructor
	 */
	public CuboidData() {
		start= new Vector();
		end = new Vector();
		blockBag = new LinkedHashMap<Vector,Blockc>(0);
	}
	
	/**
	 * Copy Constructor
	 * @param tpl
	 */
	public CuboidData(CuboidData tpl) {
		this.blockBag = tpl.blockBag;
		start = tpl.start;
		end = tpl.end;
	}
	
	/**
	 * Return a reference to the blockBag of this object
	 * @return
	 */
	public LinkedHashMap<Vector,Blockc> getBlockBag() {
		return blockBag;
	}
	
	/**
	 * Overwrite the existing Block bag
	 * @param tpl
	 */
	public void setBlockBag(LinkedHashMap<Vector,Blockc> tpl) {
		this.blockBag = tpl;
	}
	
	/**
	 * Get the Block at the given vector (which is also the actual world coordinate)
	 * @param v
	 * @return Returns a new Blockc instance with values of block at index
	 */
	public Blockc getBlockAt(Vector v) {
		return new Blockc(blockBag.get(v));
	}
	
	/**
	 * Put a new Block into the block bag or replace an existing one if the position (vector v) 
	 * happens to be mapped already
	 * @param v
	 * @param block
	 */
	public void setBlockAt(Vector v, Blockc block) {
		blockBag.put(v, block);
	}
	
	/**
	 * Get the size of the current list This is the actual amount of Blocks
	 * @return
	 */
	public int getSize() {
		return blockBag.size();
	}

	/**
	 * Get the Size Vector of this data area. That is the length in X/Y/Z direction
	 * @return
	 */
	public Vector getSizeVector() {
		return size;
	}

	/**
	 * Set the size Vector for this object
	 * @param size
	 */
	public void setSizeVector(Vector size) {
		this.size = size;
	}
	
	/**
	 * Fill the current selection with the given Block b
	 * @param b
	 */
	public void fill(Blockc b) {
		for(Iterator<Vector> data = blockBag.keySet().iterator(); data.hasNext();) {
			Vector key = (Vector) data.next();
			blockBag.put(key, b);
		}
	}
	
	/**
	 * Replace the "target" Block with the "substitute" Block
	 * @param target
	 * @param substitute
	 */
	public void replace(Blockc target, Blockc substitute) {
		Blockc block;
		for(Iterator<Vector> data = blockBag.keySet().iterator(); data.hasNext();) {
			Vector key = (Vector) data.next();
			block = blockBag.get(key);
			if(block.equalsSlack(target)) {
				blockBag.put(key, substitute);
			}
			
		}
	}
	
	/**
	 * Create a String representation of this Data Area. This can be pretty long.
	 */
	public String toString() {
		String out = "";
		for(Iterator<Vector> data = getBlockBag().keySet().iterator(); data.hasNext();) {
			Vector key = (Vector) data.next();
			Blockc value = blockBag.get(key);
			out += "\nCoords: "+key.toString()+"\n";
			out += "Type: "+value.getType()+"\n";
		}
		return out;
	}
	
	public void clearBlockBag() {
		blockBag.clear();
	}
}
