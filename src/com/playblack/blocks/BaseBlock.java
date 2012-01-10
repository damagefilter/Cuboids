package com.playblack.blocks;

public class BaseBlock {

	private byte data;
	private short type;
	/**
	 * Set the Item/Block Type of this block.
	 * @param type
	 */
	public void setType(Number type) {
		this.type = type.shortValue();
	}
	
	/**
	 * Get the current Item/Block Type of this block.
	 * @return Number The Item/Block Type
	 */
	public Number getType() {
		return type;
	}
	
	/**
	 * Set the Data or Damage value of this block.
	 * @param data
	 */
	public void setData(Number data) {
		this.data = data.byteValue();
	}
	
	/**
	 * Get the current damage/data value of this block.
	 * @return byte The current damage/data value
	 */
	public Number getData() {
		return data;
	}
	
	public boolean equals(Object block) {
		if(!(block instanceof BaseBlock)) {
			return false;
		}
		else {
			BaseBlock b = (BaseBlock)block;
			if(b.getType() == this.getType() && b.getData() == this.getData()) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	public boolean equalsSlack(Object block) {
		if(!(block instanceof BaseBlock)) {
			return false;
		}
		else {
			BaseBlock b = (BaseBlock)block;
			if(b.getType() == this.getType()) {
				return true;
			}
			else {
				return false;
			}
		}
	}
}
