package com.playblack.blocks;

import java.util.ArrayList;

public class DoubleChestBlock extends BaseBlock {

	private byte data;
	private short type;
	ArrayList<BaseItem> items = new ArrayList<BaseItem>();
	public DoubleChestBlock() {
		data=(byte)0;
		type=(short)54;
	}
	
	public DoubleChestBlock(DoubleChestBlock tpl) {
		data=(byte)0;
		type=(short)54;
		items = tpl.getItemList();
	}
	
	@Override
	public void setType(Number type) {
		//Can't set type of ChestBlock
		return;
		
	}

	@Override
	public Short getType() {
		
		return type;
	}

	@Override
	public void setData(Number data) {
		this.data = (Byte)data;
		
	}

	@Override
	public Byte getData() {
		return data;
	}
	
	/**
	 * Put an item into the chest
	 * @param item
	 */
	public void putItem(BaseItem item) {
		items.add(item);
	}
	
	/**
	 * Check if an item like this one is in the chest
	 * @param item
	 * @return true if yes, false otherwise
	 */
	public boolean itemIsInChest(BaseItem item) {
		if(items.contains(item)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Remove an item from this chest
	 * @param index
	 * @return
	 */
	public BaseItem removeItem(int index) {
		return items.remove(index);
	}
	
	/**
	 * Override the current item list with a new one
	 * @param items
	 */
	public void putItemList(ArrayList<BaseItem> items) {
		this.items = items;
	}
	
	public ArrayList<BaseItem> getItemList() {
		return items;
	}

}
