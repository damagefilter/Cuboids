import net.playblack.cuboids.gameinterface.CInventory;


public class CanaryInventory extends CInventory {
    private Inventory inventory;
    Item[] itemList;
    
    public CanaryInventory(Inventory inv) {
        inventory = inv;
        itemList = inv.getContents();
    }
    
    public CanaryInventory() {
        inventory = null;
    }
    
    public void setHandle(Inventory inv) {
        inventory = inv;
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
