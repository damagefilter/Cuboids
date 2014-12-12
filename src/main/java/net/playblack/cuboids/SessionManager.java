package net.playblack.cuboids;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Inventory;
import net.playblack.cuboids.history.HistoryTimeline;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.selections.CuboidSelection;

import java.util.HashMap;

/**
 * Contains management stuff for managing edithistories for players, a player
 * factory and player clipboards that contain world data
 *
 * @author Chris
 */
public class SessionManager {
    private static SessionManager instance = null;
    private HashMap<String, HistoryTimeline> playerHistories = new HashMap<String, HistoryTimeline>(Canary.getServer().getMaxPlayers());
    private HashMap<String, CuboidSelection> playerClipboard = new HashMap<String, CuboidSelection>(Canary.getServer().getMaxPlayers());
    private HashMap<String, RegionSessionInfo> playerRegions = new HashMap<String, RegionSessionInfo>(Canary.getServer().getMaxPlayers());
    private HashMap<String, HashMap<Integer, Inventory>> inventories = new HashMap<String, HashMap<Integer, Inventory>>();

    private SessionManager() {

    }

    public static SessionManager get() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Get the region the given player (name) is currently in.
     * This may be null if the player is in no particular region (or is in global)
     * @param player
     * @return
     */
    public Region getRegionForPlayer(String player) {
        if (playerRegions.containsKey(player)) {
            return playerRegions.get(player).getCurrentRegion();
        }
        return null;
    }



    /**
     * Set the region for the given player (name).
     * This can be null to signify that the player is in no particular region.
     *
     * @param player
     * @param r
     */
    public void setRegionForPlayer(Player player, Region r) {
        if (!playerRegions.containsKey(player.getName())) {
            playerRegions.put(player.getName(), new RegionSessionInfo(player));
        }
        playerRegions.get(player.getName()).setRegion(r);
    }

    /**
     * Check if there is anyone in the given region.
     * (A player, that is)
     *
     * @param r
     * @return
     */
    public boolean isRegionPopulated(Region r) {
        for (Player p : Canary.getServer().getPlayerList()) {
            if (playerIsInRegion(p.getName(), r)) {
                return true;
            }
        }
        return false;
    }

    public boolean playerIsInRegion(String player, Region region) {
        Region r = playerRegions.get(player).getCurrentRegion();
        return region == null && r == null || r.equals(region);
    }

    public Inventory getPlayerInventory(String player, int mode) {
        HashMap<Integer, Inventory> inv = inventories.get(player);
        if (inv != null) {
            return inv.get(Integer.valueOf(mode));
        }
        return null;
    }

    public void setPlayerInventory(String player, int mode, Inventory inventory) {
        if (!inventories.containsKey(player)) {
            inventories.put(player, new HashMap<Integer, Inventory>());
        }
        inventories.get(player).put(mode, inventory);
    }

    public boolean inventoryExists(String player, int mode) {
        HashMap<Integer, Inventory> inv = inventories.get(player);
        return inv != null && inv.containsKey(mode);
    }

    /**
     * Get the history timeline for a player
     *
     * @param name
     * @return
     */
    public HistoryTimeline getPlayerHistory(String name) {
        if (!playerHistories.containsKey(name)) {
            playerHistories.put(name, new HistoryTimeline());
        }
        return playerHistories.get(name);
    }

    /**
     * Set a new and empty timeline for this player
     *
     * @param name
     */
    public void setHistoryTimeline(String name) {
        playerHistories.put(name, new HistoryTimeline());
    }

    /**
     * Get the current clipboard of the player
     *
     * @param name the player name
     * @return
     */
    public CuboidSelection getClipboard(String name) {
        return playerClipboard.get(name);
    }

    /**
     * Put the clipboard of this player
     *
     * @param name
     */
    public void setClipboard(String name, CuboidSelection sel) {
        playerClipboard.put(name, sel);
    }
}
