package net.playblack.cuboids.gameinterface;

import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.Config;
import net.playblack.cuboids.HealThread;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;

import java.util.concurrent.TimeUnit;

public abstract class CPlayer {

    protected boolean wasCreativeWhenEnteringRegion = false;

    protected Region currentRegion;
    protected boolean adminCreative; //this is if creative wasn't set by cuboids

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
    public abstract Item getItemInHand();

    /**
     * Get an array with all groups this player is in. This might not work with
     * the greater idiocy of permission plugins but I show you how much fucks I
     * give about that:
     *
     * @return
     */
    public abstract String[] getGroups();

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
     *
     * @param mode the inventory for what mode?
     * @return CItem[] representing the inventory
     */
    public abstract Inventory getInventory(int mode);

    /**
     * Returns the inventory for this player right as it currently is
     */
    public abstract Inventory getCurrentInventory();

    /**
     * Set player inventory
     *
     * @param items
     */
    public abstract void setInventory(Inventory items);

    /**
     * Set the inventory for a specified mode.
     * This will not change the players actual inventory but only
     * put the inventory into a map for later referencing it
     *
     * @param inv
     * @param mode
     */
    public abstract void setInventoryForMode(Inventory inv, int mode);

    /**
     * Teleport a player to the position v in world world
     *
     * @param v
     */
    public abstract void teleportTo(Vector3D v);

    /**
     * Check if this player is an admin
     *
     * @return
     */
    public abstract boolean isAdmin();

    /**
     * Get the name of this player
     *
     * @return the name
     */
    public abstract String getName();

    public abstract World getWorld();

    public abstract void setPosition(Vector3D v);

    public abstract Location getLocation();

    public abstract double getX();

    public abstract double getY();

    public abstract double getZ();

    public abstract double getPitch();

    public abstract double getRotation();

    public abstract float getHealth();

    public abstract void setHealth(float health);

    /**
     * Check if this player is allowed to modify a block at a given location
     *
     * @param location
     * @return
     */
    public boolean canModifyBlock(Location location) {
        Region cube = RegionManager.get().getActiveRegion(location, false);
        if (hasPermission("cuboids.super.admin") || cube.playerIsAllowed(getName(), getGroups())) {
            return true;
        }

        return cube.getProperty("protection") != Status.DENY;
    }

    /**
     * Check if this player is allowed to use an item in his hand
     *
     * @param item
     * @return
     */
    public boolean canUseItem(Item item) {
        if (hasPermission("cuboids.super.admin") || currentRegion == null) {
            return true;
        }

        if (currentRegion.getProperty("restrict-items") == Status.ALLOW) {
            return currentRegion.isItemRestricted(item.getType().getMachineName());
        }
        return true;
    }

    /**
     * Check if this player can go wherever it's about to go
     *
     * @param location
     * @return
     */
    public boolean canMoveTo(Location location) {
        Region cube = RegionManager.get().getActiveRegion(location, false);
        if (hasPermission("cuboids.super.admin") || cube.playerIsAllowed(getName(), getGroups())) {
            return true;
        }
        return cube.getProperty("enter-cuboid") != Status.DENY;
    }

    /**
     * Check if this player is inside any region
     *
     * @return
     */
    public boolean isInRegion() {
        return currentRegion != null;
    }

    /**
     * Check if the player currently is inside any region
     *
     * @param r
     * @return
     */
    public boolean isInRegion(Region r) {
        return currentRegion == null || currentRegion == r;
    }

    /**
     * Check if this player is allowed in the region he is in.
     * Returns true if player is in no region.
     *
     * @return
     */
    public boolean isInRegionWhitelist() {
        if (currentRegion == null) {
            return true;
        }
        return currentRegion.playerIsAllowed(getName(), getGroups());
    }

    /**
     * Put this player into a new region.
     * This will send farewell and welcome messages if applicable.
     * If you pass null, the player will be filed as "not in a region"
     *
     * @param r
     */
    public void setRegion(Region r) {
        if (r == null) {
            sendFarewell();
            setGameMode(0);
            currentRegion = null;
            return;
        }

        if (!r.equals(currentRegion)) {
            if (currentRegion != null && !currentRegion.isParentOf(r)) {
                sendFarewell();
            }
            if (r.getProperty("creative") != Status.ALLOW) {
                setGameMode(0);
            }
            else if (r.getProperty("creative") == Status.ALLOW) {
                adminCreative = false;
                setGameMode(1);
            }
            if (r.getProperty("healing") == Status.ALLOW) {
                CuboidInterface.get().getThreadManager().schedule(new HealThread(this, r, CuboidInterface.get().getThreadManager(), Config.get().getHealPower(), Config.get().getHealDelay()), 0, TimeUnit.SECONDS);
            }
            if (currentRegion != null && !currentRegion.isChildOf(r)) {
                currentRegion = r;
                sendWelcome();
            }
            else if (currentRegion == null) {
                currentRegion = r;
                sendWelcome();
            }
            else {
                currentRegion = r;
            }
        }

    }

    /**
     * get the recent game mode of the player
     *
     * @return
     */
    public abstract int getGameMode();

    /**
     * Switch the players game mode.<br>
     * <ul>
     * <li>0 = survival</li> <li>1 = creative</li> <li>2 = adventure</li>
     * </ul>
     *
     * @implementation Make sure to save and swap inventory accordingly and check if a mode was already set
     * by non-cuboid circumstances and do not change mode if so!
     */
    public abstract void setGameMode(int mode);

    /**
     * Return the Region that has last been set to this player
     * This may return null if the player is not inside any region
     *
     * @return
     */
    public Region getCurrentRegion() {
        return currentRegion;
    }

    private void sendFarewell() {
        if (currentRegion != null && currentRegion.getFarewell() != null) {
            sendMessage(currentRegion.getFarewell());
        }
    }

    private void sendWelcome() {
        if (currentRegion != null && currentRegion.getWelcome() != null) {
            sendMessage(currentRegion.getWelcome());
        }
    }
}
