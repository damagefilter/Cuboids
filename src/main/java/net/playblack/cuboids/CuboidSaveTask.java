package net.playblack.cuboids;

import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Debug;

/**
 * Save cuboids.
 *
 * @author Chris
 */
public class CuboidSaveTask implements Runnable {

    @Override
    public synchronized void run() {
        Debug.log("Saving nodes");
        RegionManager.get().save(false, false);
        Debug.log("Next save in " + Config.get().getSaveDelay() + " minutes");
    }
}
