package net.playblack.cuboids.gameinterface;

import net.playblack.cuboids.blocks.CItem;

public abstract class CPlayer implements IBaseEntity{
    
    /**
     * Send a message to the player
     * @param message
     */
    public abstract void sendMessage(String message);
    
    /**
     * Notify the player (send him a red message)
     * @param message
     */
    public abstract void notify(String message);
    
    /**
     * Execute a permissions check on the player
     * @param permission
     * @return
     */
    public abstract boolean hasPermission(String permission);
    
    /**
     * Get the item in hand, if any, this may return null
     * @return
     */
    public abstract CItem getItemInHand();
    
    /**
     * Get an array with all groups this player is in.
     * This might not work with the greater idiocy of permission plugins
     * but I show you how much fucks I give about that:
     * @return
     */
    public abstract String[] getGroups();
    
    /**
     * Set the player in creative/normal mode
     * @param creative
     */
    public abstract void setCreative(int creative);
    
    /**
     * Check if a player is in creative mode
     * @return
     */
    public abstract boolean isInCreativeMode();
    
    /**
     * Empty the full player inventory
     */
    public abstract void clearInventory();
    
    /**
     * Get the full player inventory as item array
     * @return
     */
    public abstract CItem[] getInventory();
    
    /**
     * Set player inventory
     * @param items
     */
    public abstract void setInventory(CItem[] items);
    
}
