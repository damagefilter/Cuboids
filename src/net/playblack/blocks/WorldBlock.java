package net.playblack.blocks;

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
public class WorldBlock extends BaseBlock implements Serializable {

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
	public WorldBlock() {
		data=0;
		type=0;
	}
	
	/**
	 * Construct a block with its type only
	 * @param type
	 */
	public WorldBlock(short type) {
		this.type= type;
		data = 0;
	}
	
	/**
	 * Construct a block with its type and data/damage value
	 * @param data
	 * @param type
	 * @param position
	 */
	public WorldBlock(byte data, short type) {
		this.data = data;
		this.type = type;
	}
	
	/**
	 * Copy Constructor
	 */
	public WorldBlock(WorldBlock tpl) {
		this.data = tpl.data;
		this.type = tpl.type;
	}
	

	public void setType(Number type) {
		this.type = (Short)type;
	}
	

	public Short getType() {
		return type;
	}
	

	public void setData(Number data) {
		this.data = (Byte)data;
	}
	
	
	public Byte getData() {
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
	public boolean equals(WorldBlock b) {
		if(type == b.getType() && data == b.getData()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean equalsSlack(WorldBlock b) {
		if(type == b.getType()) {
			return true;
		}
		else {
			return false;
		}
	}
}
