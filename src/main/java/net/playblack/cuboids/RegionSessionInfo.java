package net.playblack.cuboids;

import net.canarymod.api.GameMode;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.Region;
import net.playblack.mcutils.Debug;

import java.util.concurrent.TimeUnit;

/**
 * Represents a per-player region info.
 * Contains player and current region and if player was creative and inventory stuff etc etc
 *
 */
public class RegionSessionInfo {
    private Player player;
    private Region currentRegion;

    public RegionSessionInfo(Player player) {
        this.player = player;
    }

    public Region getCurrentRegion() {
        return currentRegion;
    }

    public void setRegion(Region r) {
        if (r == null && currentRegion == null) { // In case he moves from global region to global region, do nothing
            return;
        }

        if (r == null) {
            sendFarewell();
            setGameMode(player.getWorld().getGameMode());
            currentRegion = null;
            return;
        }

        if (!r.equals(currentRegion)) {
            if (currentRegion != null && !currentRegion.isParentOf(r)) {
                sendFarewell();
            }
            if (r.getProperty("creative") != Region.Status.ALLOW) {
                setGameMode(player.getWorld().getGameMode());
            }
            else if (r.getProperty("creative") == Region.Status.ALLOW) {
                setGameMode(GameMode.CREATIVE);
            }
            if (r.getProperty("healing") == Region.Status.ALLOW) {
                CuboidInterface.get().getThreadManager().schedule(new HealThread(player, r, CuboidInterface.get().getThreadManager(), Config.get().getHealPower(), Config.get().getHealDelay()), 0, TimeUnit.SECONDS);
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

    public void setInventory(Item[] items) {
        if (items == null) {
            player.getInventory().clearContents();
            return;
        }
        try {
            if (items.length > 0) {
                player.getInventory().setContents(items);
            }
            else {
                player.getInventory().clearContents();
            }

        }
        catch (ArrayIndexOutOfBoundsException e) {
            Debug.logStack(e);
        }
    }

    public Item[] getInventory(GameMode mode) {
        return SessionManager.get().getPlayerInventory(player.getName(), mode);
    }

    public void backupModeInventory(Inventory inv, GameMode mode) {
        SessionManager.get().setPlayerInventory(player.getName(), mode, inv);
    }

    /**
     * Called before a new region is entered.
     * So currentRegion is still the old region (possibly creative region) we're going to leave.
     *
     * @param newMode
     */
    private void setGameMode(GameMode newMode) {
        if (player.hasPermission(Permissions.BYPASS$GAMEMODE)) {
            return;
        }

        // Backup the inventory for later retrieval
        backupModeInventory(player.getInventory(), player.getMode());
        player.setMode(newMode);
        setInventory(getInventory(newMode));
    }

    private void sendFarewell() {
        if (currentRegion != null && currentRegion.getFarewell() != null) {
            player.message(currentRegion.getFarewell());
        }
    }

    private void sendWelcome() {
        if (currentRegion != null && currentRegion.getWelcome() != null) {
            player.message(currentRegion.getWelcome());
        }
    }

}
