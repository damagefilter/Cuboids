package com.playblack.blocks;

/**
 * This abstracts an Item from CanaryMod, mainly used when doing stuff with Chest Copying etc.<br>
 * This only contains amount, id and data/damage value. Nothing extra
 * @author Chris
 *
 */
public class BaseItem {
	private int amount;
	private int itemId;
	private int itemData;
	private int slot;
	
	public BaseItem() {
		itemId = 0;
		itemData = 0;
		amount=0;
		slot=0;
	}
	
	public BaseItem(int id, int data) {
		itemId = id;
		itemData = data;
		amount=1;
		slot=0;
	}
	
	public BaseItem(int id) {
		itemId = id;
		itemData = 0;
		amount=1;
		slot=0;
	}
	
	public BaseItem(int id, int data, int amount) {
		itemId = id;
		itemData = data;
		this.amount=amount;
		slot=0;
	}
	
	public BaseItem(int id, int data, int amount, int slot) {
		itemId = id;
		itemData = data;
		this.amount=amount;
		this.slot=slot;
	}
	/**
	 * Get the amount of items with this
	 * @return
	 */
	public int getAmount() { return amount; }
	
	/**
	 * Set the amount of items
	 * @param amount
	 */
	public void setAmount(int amount) { this.amount = amount; }
	
	/**
	 * Get item ID
	 * @return
	 */
	public int getItemId() { return itemId; }

	/**
	 * Get the damage value of this item 
	 * @return
	 */
	public int getItemData() {
		return itemData;
	}

	/**
	 * Set the damage value of this item
	 * @param itemData
	 */
	public void setItemData(int itemData) {
		this.itemData = itemData;
	}
	
	public int getSlot() {
		return slot;
	}
	
}
