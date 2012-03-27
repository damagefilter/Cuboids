package net.playblack.cuboids.blocks;

public class CItem {
    protected int id=0;
    protected short data=0;
    protected int slot=0;
    
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
    
}
