package net.playblack.cuboids.impl.canarymod;

import net.canarymod.api.GameMode;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Item;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.blocks.CItem;
import net.playblack.cuboids.gameinterface.CInventory;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.Location;
import net.playblack.mcutils.Vector;

public class CanaryPlayer extends CPlayer {

    Player player;
    CanaryWorld world;
    String[] groups;

    public CanaryPlayer(Player player) {
        this.player = player;
        this.world = new CanaryWorld(player.getWorld());
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
    public CWorld getWorld() {
        // Player has switched worlds
        if (player.getWorld().getName() != player.getWorld().getName()) {
            this.world = new CanaryWorld(player.getWorld());
        } else if (world.getDimension() != player.getWorld().getType().getId()) {
            this.world = new CanaryWorld(player.getWorld());
        }
        return world;
    }

    @Override
    public Vector getPosition() {
        return new Vector(player.getX(), player.getY(), player.getZ());
    }

    @Override
    public Location getLocation() {
        return new Location(player.getX(), player.getY(), player.getZ(), player.getWorld().getType().getId(), player.getWorld().getName());
    }

    @Override
    public void setPosition(Vector v) {
        player.setX(v.getX());
        player.setY(v.getY());
        player.setZ(v.getZ());
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
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean isMob() {
        return false;
    }

    @Override
    public boolean isAnimal() {
        return false;
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
    public CItem getItemInHand() {
        Item i = player.getItemHeld();
        if (i == null) {
            return new CItem(0, (short) 0, 0);
        }
        return new CItem(i.getId(), (short) i.getDamage(), i.getSlot());
    }

    @Override
    public String[] getGroups() {
        if (!groups[0].equals(player.getGroup().getName())) {
            groups = new String[]{player.getGroup().getName()};
        }
        return groups;
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

    @Override
    public boolean isInCreativeMode() {
        return player.getMode().getId() == 1;
    }

    @Override
    public void clearInventory() {
        player.getInventory().clearContents();
    }

    @Override
    public CInventory getInventory(int mode) {
        return SessionManager.get().getPlayerInventory(getName(), mode);
    }

    @Override
    public CInventory getCurrentInventory() {
        return new CanaryInventory(player.getInventory());
    }

    @Override
    public void setInventory(CInventory items) {
        if (items == null) {
            player.getInventory().clearContents();
            return;
        }
        try {
            if (items.hasItems()) {
                ((CanaryInventory) items).setThisContents();
            } else {
                player.getInventory().clearContents();
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            Debug.logStack(e);
        }
    }

    @Override
    public void setInventoryForMode(CInventory inv, int mode) {
        SessionManager.get().setPlayerInventory(getName(), mode, inv);
    }

    @Override
    public void teleportTo(Vector v) {
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
}
