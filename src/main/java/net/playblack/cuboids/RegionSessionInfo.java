package net.playblack.cuboids;

import net.canarymod.api.GameMode;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Inventory;
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
    /**
     * Was the game mode forced by something that was not cuboids area management?
     */
    private boolean forcedMode;
    private Player player;
    private Region currentRegion;

    public RegionSessionInfo(Player player) {
        this.player = player;
    }

    public Region getCurrentRegion() {
        return currentRegion;
    }

    public void setRegion(Region r) {
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
                setGameMode(GameMode.SURVIVAL);
            }
            else if (r.getProperty("creative") == Region.Status.ALLOW) {
                forcedMode = false; // TODO: This may have no effect, remove it?
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

    public void setInventory(Inventory items) {
        if (items == null) {
            player.getInventory().clearContents();
            return;
        }
        try {
            if (items.getContents().length > 0) {
                player.getInventory().setContents(items.getContents());
            }
            else {
                player.getInventory().clearContents();
            }

        }
        catch (ArrayIndexOutOfBoundsException e) {
            Debug.logStack(e);
        }
    }

    public Inventory getInventory(GameMode mode) {
        return SessionManager.get().getPlayerInventory(player.getName(), mode.getId());
    }

    public void setInventoryForMode(Inventory inv, GameMode mode) {
        SessionManager.get().setPlayerInventory(player.getName(), mode.getId(), inv);
    }

    private void setGameMode(GameMode newMode) {
        // Firstly assume we're not in forced mode
        if (forcedMode && (player.getMode() != newMode)) {
            forcedMode = false;
        }

        // This means we have been in this mode before
        if (currentRegion == null && (player.getMode() == newMode)) {
            forcedMode = true;
        }

        if (currentRegion != null) {
            if (currentRegion.getProperty("creative") != Region.Status.ALLOW && (player.getMode() == newMode)) {
                forcedMode = true;
            }
        }

        if (!forcedMode) {
            // Backup the inventory for later retrieval
            setInventoryForMode(player.getInventory(), player.getMode());
            player.setMode(newMode);
            setInventory(getInventory(newMode));
        }
//        if (mode == 1 && !adminCreative) {
//            setInventoryForMode(getCurrentInventory(), getGameMode());
//            player.setMode(GameMode.fromId(mode));
//            setInventory(getInventory(mode));
//        }
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
