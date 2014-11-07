package net.playblack.cuboids.loaders;

import net.playblack.cuboids.regions.Region;
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

    public java.util.List<String> tabuCommands();

    public java.util.List<String> getPlayerlist();

    public java.util.List<String> getGrouplist();

    public Vector getOrigin();

    public Vector getOffset();

    public boolean getFarmland();

    public boolean getHmob();

    public boolean getEnderControl();

    public boolean getPhysics();

    /**
     * Denotes if getRegion can be called directly on it or if you need to create one manually.
     * @return
     */
    public boolean canConvertDirectly();

    /**
     * Gets the region object if it is possible,
     * otherwise returns null.
     * @return
     */
    public Region getRegion();
}
