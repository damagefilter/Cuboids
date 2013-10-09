package net.playblack.cuboids.converters;

import net.playblack.mcutils.Vector;

import java.util.ArrayList;

/**
 * This is a wrapper for foreign cuboid formats (D and F for example).
 * Implementations wrap around a specific cuboid class.
 *
 * @author Chris
 */
public interface CuboidShell {
    public boolean getProtection();

    public boolean getRestricted();

    public boolean getPvp();

    public boolean getHealing();

    public boolean getCreeper();

    public boolean getSanctuary();

    public boolean getWaterControl();

    public boolean getLavaControl();

    public boolean getCreative();

    public boolean getFireProof();

    public boolean getTntSecure();

    public boolean getAnimalSpawn();

    public int getDimension();

    public int getPriority();

    public String getWorld();

    public String getName();

    public String getFarewell();

    public String getWelcome();

    public ArrayList<String> tabuCommands();

    public ArrayList<String> getPlayerlist();

    public ArrayList<String> getGrouplist();

    public Vector getOrigin();

    public Vector getOffset();

    public boolean getFarmland();

    public boolean getHmob();

    public boolean getEnderControl();

    public boolean getPhysics();
}
