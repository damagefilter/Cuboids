package net.playblack.cuboids.loaders;

import java.util.ArrayList;

/**
 * Interface for loading foreign Cuboid file formats.
 *
 * @author Chris
 */
public interface Loader {
    /**
     * Return the cuboid version that is beeing loaded
     *
     * @return
     */
    public String getImplementationVersion();

    /**
     * Load cuboids using a load routine similar to the original one
     *
     * @return
     */
    public ArrayList<CuboidShell> load();
}
