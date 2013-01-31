package net.playblack.cuboids.gameinterface;

import net.playblack.cuboids.blocks.CItem;
import net.playblack.cuboids.regions.Cuboid;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.cuboids.regions.Cuboid.Status;
import net.playblack.mcutils.Location;
import net.playblack.mcutils.Vector;

public abstract class CPlayer implements IBaseEntity {

    /**
     * Send a message to the player
     * 
     * @param message
     */
    public abstract void sendMessage(String message);

    /**
     * Notify the player (send him a red message)
     * 
     * @param message
     */
    public abstract void notify(String message);

    /**
     * Execute a permissions check on the player
     * 
     * @param permission
     * @return
     */
    public abstract boolean hasPermission(String permission);

    /**
     * Get the item in hand, if any, this may return null
     * 
     * @return
     */
    public abstract CItem getItemInHand();

    /**
     * Get an array with all groups this player is in. This might not work with
     * the greater idiocy of permission plugins but I show you how much fucks I
     * give about that:
     * 
     * @return
     */
    public abstract String[] getGroups();

    /**
     * Set the player in creative/normal mode
     * 
     * @param creative
     */
    public abstract void setCreative(int creative);

    /**
     * Check if a player is in creative mode
     * 
     * @return
     */
    public abstract boolean isInCreativeMode();

    /**
     * Empty the full player inventory
     */
    public abstract void clearInventory();

    /**
     * Get the full player inventory as item array
     * 
     * @return
     */
    public abstract CItem[] getInventory();

    /**
     * Set player inventory
     * 
     * @param items
     */
    public abstract void setInventory(CItem[] items);

    /**
     * Teleport a player to the position v in world world
     * 
     * @param v
     * @param world
     */
    public abstract void teleportTo(Vector v);

    /**
     * Check if this player is an admin
     * 
     * @return
     */
    public abstract boolean isAdmin();
    
    /**
     * Check if this player is allowed to modify a block at a given location
     * @param location
     * @return
     */
    public boolean canModifyBlock(Location location) {
        Cuboid cube = (Cuboid) RegionManager.get().getActiveCuboidNode(location, false);
        if(hasPermission("cIgnoreRestrictions") || cube.playerIsAllowed(getName(), getGroups())) {
            return true;
        }
        
        if(cube.getProperty("protection") == Cuboid.Status.ALLOW) {
            return false;
        }
        return true;
    }
    
    /**
     * Check if this player is allowed to use an item in his hand
     * @param location
     * @param item
     * @return
     */
    public boolean canUseItem(Location location, CItem item) {
        Cuboid cube = (Cuboid) RegionManager.get().getActiveCuboidNode(location, false);
        if(hasPermission("cIgnoreRestrictions")) {
            return true;
        }
        
        if(cube.getProperty("restrict-items") == Status.ALLOW) {
            return cube.isItemRestricted(item.getId());
        }
        return true;
    }
    
    public boolean canMoveTo(Location location) {
        Cuboid cube = (Cuboid) RegionManager.get().getActiveCuboidNode(location, false);
        if(hasPermission("cIgnoreRestrictions") || cube.playerIsAllowed(getName(), getGroups())) {
            return true;
        }
        if(cube.getProperty("enter-cuboid") == Status.DENY) {
            return false;
        }
        return true;
    }

}
