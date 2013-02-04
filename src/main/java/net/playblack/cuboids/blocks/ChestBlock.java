package net.playblack.cuboids.blocks;

import java.util.ArrayList;

public class ChestBlock extends CBlock {

    ArrayList<CItem> items = new ArrayList<CItem>();

    public ChestBlock() {
        data = (byte) 0;
        type = (short) 54;
    }

    public ChestBlock(ChestBlock tpl) {
        data = (byte) 0;
        type = (short) 54;
        items = tpl.getItemList();
    }

    /**
     * Put an item into the chest
     * 
     * @param item
     */
    public void putItem(CItem item) {
        items.add(item);
    }

    /**
     * Check if an item like this one is in the chest
     * 
     * @param item
     * @return true if yes, false otherwise
     */
    public boolean itemIsInChest(CItem item) {
        if (items.contains(item)) {
            return true;
        }
        return false;
    }

    /**
     * Remove an item from this chest and return it
     * 
     * @param index
     * @return
     */
    public CItem removeItem(int index) {
        return items.remove(index);
    }

    /**
     * Override the current item list with a new one
     * 
     * @param items
     */
    public void putItemList(ArrayList<CItem> items) {
        this.items = items;
    }

    public ArrayList<CItem> getItemList() {
        return items;
    }

}
