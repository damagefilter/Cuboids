package net.playblack.cuboids.impl.canarymod;

import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.playblack.cuboids.gameinterface.CInventory;

public class CanaryInventory extends CInventory {
    Inventory inventory;
    Item[] itemList;
    public CanaryInventory(Inventory inventory) {
        this.inventory = inventory;
        itemList = inventory.getContents();
    }

    public CanaryInventory() {
        inventory = null;
    }

    public void setThisContents() {
        try {
            inventory.setContents(itemList);
        }
        catch(ArrayIndexOutOfBoundsException e) {

        }
    }

    @Override
    public boolean hasItems() {
        if(inventory == null) {
            return false;
        }
        return itemList.length > 0;
    }

    @Override
    public int size() {
        return itemList.length;
    }

}
