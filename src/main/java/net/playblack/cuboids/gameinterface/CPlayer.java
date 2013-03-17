package net.playblack.cuboids.gameinterface;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.HealThread;
import net.playblack.cuboids.blocks.CItem;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.mcutils.Location;
import net.playblack.mcutils.Vector;

public abstract class CPlayer implements IBaseEntity {

    protected HashMap<Integer, CItem[]> inventories = new HashMap<Integer, CItem[]>();
    
    Region currentRegion;
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
     * Switch the players game mode.<br>
     * <ul>
     * <li>0 = survival</li> <li>1 = creative</li> <li>2 = adventure</li>
     * </ul>
     * @implementation Make sure to save and swap inventory accordingly and check if a mode was already set
     * by non-cuboid circumstances and do not change mode if so!
     * @param creative
     */
    public abstract void setGameMode(int mode);

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
     * Get the full player inventory as item array,
     * depending on what game mode,
     * @see CPlayer.setGameMode()
     * @param mode the inventory for what mode? 
     * 
     * @return CItem[] representing the inventory
     */
    public abstract CItem[] getInventory(int mode);

    /**
     * Returns the inventory for this player right as it currently is
     */
    public abstract CItem[] getCurrentInventory();
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
        Region cube = (Region) RegionManager.get().getActiveRegion(location, false);
        if(hasPermission("cIgnoreRestrictions") || cube.playerIsAllowed(getName(), getGroups())) {
            return true;
        }
        
        if(cube.getProperty("protection") == Region.Status.ALLOW) {
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
    public boolean canUseItem(CItem item) {
        if(hasPermission("cIgnoreRestrictions") || currentRegion == null) {
            return true;
        }
        
        if(currentRegion.getProperty("restrict-items") == Status.ALLOW) {
            return currentRegion.isItemRestricted(item.getId());
        }
        return true;
    }
    
    /**
     * Check if this player can go wherever it's about to go
     * @param location
     * @return
     */
    public boolean canMoveTo(Location location) {
        Region cube = (Region) RegionManager.get().getActiveRegion(location, false);
        if(hasPermission("cIgnoreRestrictions") || cube.playerIsAllowed(getName(), getGroups())) {
            return true;
        }
        if(cube.getProperty("enter-cuboid") == Status.DENY) {
            return false;
        }
        return true;
    }
    
    /**
     * Check if this player is inside any region
     * @return
     */
    public boolean isInRegion() {
        return currentRegion != null;
    }
    
    /**
     * Check if the player currently is inside any region
     * @param r
     * @return
     */
    public boolean isInRegion(Region r) {
        return currentRegion == null || currentRegion == r;
    }
    
    /**
     * Check if this player is allowed in the region he is in.
     * Returns true if player is in no region.
     * @return
     */
    public boolean isInRegionWhitelist() {
        if(currentRegion == null) {
            return true;
        }
        return currentRegion.playerIsAllowed(getName(), getGroups());
    }
    
    /**
     * Put this player into a new region.
     * This will send farewell and welcome messages if applicable.
     * If you pass null, the player will be filed as "not in a region"
     * @param r
     */
    public void setRegion(Region r) {
        if(r == currentRegion) {
            return;
        }
        sendFarewell();
        if(r == null) {
            currentRegion = null;
            return;
        }
        if(currentRegion != r) {
            if(r.getProperty("creative") != Status.ALLOW) {
                setGameMode(0);
                
            }
            else {
                setGameMode(1);
            }
            if(r.getProperty("healing") == Status.ALLOW) {
                CuboidInterface.get().getThreadManager().schedule(new HealThread(
                      this, r, 
                      CuboidInterface.get().getThreadManager(), Config.get().getHealPower(),
                      Config.get().getHealDelay()), 
                  0,
                  TimeUnit.SECONDS);
            }
            currentRegion = r;
        }
        sendWelcome();
    }
    
    /**
     * Return the Region that has last been set to this player
     * This may return null if the player is not inside any region
     * @return
     */
    public Region getCurrentRegion() {
        return currentRegion;
    }
    
    private void sendFarewell() {
        if(currentRegion != null && currentRegion.getFarewell() != null) {
            sendMessage(currentRegion.getFarewell());
        }
    }
    
    private void sendWelcome() {
        if(currentRegion != null && currentRegion.getWelcome() != null) {
            sendMessage(currentRegion.getWelcome());
        }
    }
}
