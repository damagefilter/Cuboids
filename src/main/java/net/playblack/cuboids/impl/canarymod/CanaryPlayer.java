package net.playblack.cuboids.impl.canarymod;

import net.canarymod.api.GameMode;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.mcutils.Debug;

public class CanaryPlayer extends CPlayer {

    Player player;
    String[] groups;

    public CanaryPlayer(Player player) {
        this.player = player;
        groups = new String[]{player.getGroup().getName()};
    }

    @Override
    public float getHealth() {
        return player.getHealth();
    }

    @Override
    public void setHealth(float health) {
        player.setHealth(health);
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public World getWorld() {
        return player.getWorld();
    }

    @Override
    public void setPosition(Vector3D v) {
        player.setX(v.getX());
        player.setY(v.getY());
        player.setZ(v.getZ());
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    public double getX() {
        return player.getX();
    }

    @Override
    public double getY() {
        return player.getY();
    }

    @Override
    public double getZ() {
        return player.getZ();
    }

    @Override
    public double getPitch() {
        return player.getPitch();
    }

    @Override
    public double getRotation() {
        return player.getRotation();
    }

    @Override
    public void sendMessage(String message) {
        player.message(message);
    }

    @Override
    public void notify(String message) {
        player.notice(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public Item getItemInHand() {
        return player.getItemHeld();
    }

    @Override
    public String[] getGroups() {
        if (!groups[0].equals(player.getGroup().getName())) {
            groups = new String[]{player.getGroup().getName()};
        }
        return groups;
    }

    @Override
    public boolean isInCreativeMode() {
        return player.getMode().getId() == 1;
    }

    @Override
    public void clearInventory() {
        player.getInventory().clearContents();
    }

    @Override
    public Inventory getInventory(int mode) {
        return SessionManager.get().getPlayerInventory(getName(), mode);
    }

    @Override
    public Inventory getCurrentInventory() {
        return player.getInventory();
    }

    @Override
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

    @Override
    public void setInventoryForMode(Inventory inv, int mode) {
        SessionManager.get().setPlayerInventory(getName(), mode, inv);
    }

    @Override
    public void teleportTo(Vector3D v) {
        player.teleportTo(v.getX(), v.getY(), v.getZ());
    }

    @Override
    public boolean isAdmin() {
        return player.isAdmin();
    }

    @Override
    public int getGameMode() {
        return player.getMode().getId();
    }

    @Override
    public void setGameMode(int mode) {
        if (adminCreative && !isInCreativeMode()) {
            adminCreative = false;
        }

        if (currentRegion == null && isInCreativeMode()) {
            adminCreative = true;
        }

        if (currentRegion != null) {
            if (currentRegion.getProperty("creative") != Status.ALLOW && isInCreativeMode()) {
                adminCreative = true;
            }
        }

        if (mode == 0) {
            if (!adminCreative) {
                setInventoryForMode(getCurrentInventory(), getGameMode());
                player.setMode(GameMode.fromId(mode));
                setInventory(getInventory(mode));
            }
        }
        if (mode == 1 && !adminCreative) {
            setInventoryForMode(getCurrentInventory(), getGameMode());
            player.setMode(GameMode.fromId(mode));
            setInventory(getInventory(mode));
        }
    }
}
