package net.playblack.cuboids.blocks;

import net.playblack.cuboids.exceptions.DeserializeException;

public class CItem {
    protected int id=0;
    protected short data=0;
    protected int slot=0;
    protected int amount=0;
    
    public CItem() {
        id = slot = 0;
        data=0;
    }
    
    public CItem(int id, short data) {
        this.id = id;
        this.data = data;
    }
    
    public CItem(int id, short data, int slot) {
        this.id = id;
        this.data = data;
        this.slot = slot;
    }
    
    public CItem(int id) {
        this.id = id;
    }
    public CItem(int id, int data, int amount, int slot) {
        this.id = id;
        this.data = (short) data;
        this.slot = slot;
        this.amount = amount;
    }

    /**
     * set item id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * get item id
     * @return
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * set data/damage value
     * @param data
     */
    public void setData(int data) {
        this.data = (short)data;
    }
    
    /**
     * get data/damage value
     * @return
     */
    public short getData() {
        return this.data;
    }
    
    /**
     * set item slot in inventory
     * @param slot
     */
    public void setSlot(int slot) {
        this.slot = slot;
    }
    
    /**
     * get item slot in inventory
     * @return
     */
    public int getSlot() {
        return this.slot;
    }
    
    /**
     * Set quantity of this item
     * @param a
     */
    public void setAmount(int a) {
        amount = a;
    }
    
    /**
     * Get quantity of this item
     * @return
     */
    public int getAmount() {
        return amount;
    }
    
    public StringBuilder serialize() {
        return new StringBuilder().append("[")
                .append(Integer.valueOf(amount)).append(",")
                .append(Integer.valueOf(id)).append(",")
                .append(Integer.valueOf(data)).append(",")
                .append(Integer.valueOf(slot)).append("]");
    }
    
    public static CItem deserialize(String serialized) throws DeserializeException {
        serialized = serialized.replace("[", "").replace("]", "");
        CItem tr = null;
        String[] values = serialized.split(",");
        if(values.length != 4) {
            throw new DeserializeException("Could not deserialize CItem object. Invalid serialized data!", serialized);
        }
        int amount = Integer.parseInt(values[0]);
        int itemId = Integer.parseInt(values[1]);
        int itemData = Integer.parseInt(values[2]);
        int slot = Integer.parseInt(values[3]);
        tr = new CItem(itemId,itemData,amount,slot);
        return tr;
    }
    
}
