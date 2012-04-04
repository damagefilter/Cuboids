package net.playblack.cuboids.converters;

import java.util.ArrayList;

import net.playblack.mcutils.Vector;

/**
 * This is a wrapper for foreign cuboid formats (D and F for example).
 * Implementations wrap around a specific cuboid class.
 * @author Chris
 *
 */
public interface CuboidShell {
    public abstract boolean getProtection();
    public abstract boolean getRestricted();
    public abstract boolean getPvp();
    public abstract boolean getHealing();
    public abstract boolean getCreeper();
    public abstract boolean getSanctuary();
    public abstract boolean getWaterControl();
    public abstract boolean getLavaControl();
    public abstract boolean getCreative();
    public abstract boolean getFireProof();
    public abstract boolean getTntSecure();
    public abstract boolean getAnimalSpawn();
    public abstract int getDimension();
    public abstract int getPriority();
    public abstract String getWorld();
    public abstract String getName();
    public abstract String getFarewell();
    public abstract String getWelcome();
    public abstract ArrayList<String> tabuCommands();
    public abstract ArrayList<String> getPlayerlist();
    public abstract ArrayList<String> getGrouplist();
    public abstract Vector getOrigin();
    public abstract Vector getOffset();
    public abstract boolean getFarmland();
    public abstract boolean getHmob();
}
