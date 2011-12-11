package com.playblack.cuboid;

import java.io.Serializable;


/**
 * Abstraction Layer for Minecraft Blocks.
 * This is the de-obfuscation reobfuscated block (lol)
 * of minecraft. There you are.
 * TODO: Remove Position information once CuboidData is finished
 * @author Chris
 *
 */
@SuppressWarnings("serial")
public class Blockc implements Serializable {

	/**
	 * The Data or damage value
	 */
	private byte data = 0;
	
	/**
	 * The Block or Item ID
	 */
	private short type = 0;

	
	/**
	 * Construct empty block (is Air)
	 */
	public Blockc() {
		data=0;
		type=0;
	}
	
	/**
	 * Construct a block with its type only
	 * @param type
	 */
	public Blockc(short type) {
		this.type= type;
		data = 0;
	}
	
	/**
	 * Construct a block with its type and data/damage value
	 * @param data
	 * @param type
	 * @param position
	 */
	public Blockc(byte data, short type) {
		this.data = data;
		this.type = type;
	}
	
	/**
	 * Copy Constructor
	 */
	public Blockc(Blockc tpl) {
		this.data = tpl.data;
		this.type = tpl.type;
	}
	
	/**
	 * Set the Item/Block Type of this block.
	 * @param type
	 */
	public void setType(short type) {
		this.type = type;
	}
	
	/**
	 * Get the current Item/Block Type of this block.
	 * @return short The Block Type
	 */
	public short getType() {
		return type;
	}
	
	/**
	 * Set the Data or Damage value of this block.
	 * @param data
	 */
	public void setData(byte data) {
		this.data = data;
	}
	
	/**
	 * Get the current damage/data value of this block.
	 * @return byte The current damage/data value
	 */
	public byte getData() {
		return data;
	}
	
	/**
	 * Check if the Block is air.
	 * @return true if block is air, false otherwise.
	 */
	public boolean isAir() {
		if(type == 0) {
			return true;
		}
		else { return false; }
	}
	
	
	/**
	 * Check if types are equal
	 * @param b
	 * @return true if is equal, false otherwise
	 */
	public boolean equals(Blockc b) {
		if(type == b.getType() && data == b.getData()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean equalsSlack(Blockc b) {
		if(type == b.getType()) {
			return true;
		}
		else {
			return false;
		}
	}
}
