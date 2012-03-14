package net.playblack.blocks;



/**
 * Abstraction Layer for Minecraft Blocks.
 * This is the de-obfuscation reobfuscated block (lol)
 * of minecraft. There you are.
 * @author Chris
 *
 */

public class WorldBlock extends BaseBlock {
	
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
