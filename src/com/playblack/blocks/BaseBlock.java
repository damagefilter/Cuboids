package com.playblack.blocks;

public class BaseBlock {

	/**
	 * Set the Item/Block Type of this block.
	 * @param type
	 */
	public void setType(Number type) {
		
	}
	
	/**
	 * Get the current Item/Block Type of this block.
	 * @return Number The Item/Block Type
	 */
	public Number getType() {
		return null;
	}
	
	/**
	 * Set the Data or Damage value of this block.
	 * @param data
	 */
	public void setData(Number data) {
		
	}
	
	/**
	 * Get the current damage/data value of this block.
	 * @return byte The current damage/data value
	 */
	public Number getData() {
		return null;
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
