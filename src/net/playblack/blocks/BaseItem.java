package net.playblack.blocks;

import net.playblack.exceptions.DeserializeException;

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
        
    public StringBuilder serialize() {
        return new StringBuilder().append("[")
                .append(Integer.valueOf(amount)).append(",")
                .append(Integer.valueOf(itemId)).append(",")
                .append(Integer.valueOf(itemData)).append(",")
                .append(Integer.valueOf(slot)).append("]");
    }
    
    public static BaseItem deserialize(String serialized) throws DeserializeException {
        serialized = serialized.replaceAll("/[|/]", "");
        BaseItem tr = null;
        String[] values = serialized.split(",");
        if(values.length != 4) {
            throw new DeserializeException("Could not deserialize BaseItem object. Invalid serialized data!", serialized);
        }
        int amount = Integer.parseInt(values[0]);
        int itemId = Integer.parseInt(values[1]);
        int itemData = Integer.parseInt(values[2]);
        int slot = Integer.parseInt(values[3]);
        tr = new BaseItem(itemId,itemData,amount,slot);
        return tr;
    }
	
}
