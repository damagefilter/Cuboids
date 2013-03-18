package net.playblack.cuboids.gameinterface;


public abstract class CInventory {
    
    
    /**
     * Check if this inventory has any items at all
     * @return
     */
    public abstract boolean hasItems();
    
    /**
     * get the size of this inventory
     * @return
     */
    public abstract int size();
}
