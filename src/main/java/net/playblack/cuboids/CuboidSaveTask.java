package net.playblack.cuboids;

import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.EventLogger;

/**
 * Save cuboids.
 * 
 * @author Chris
 * 
 */
public class CuboidSaveTask implements Runnable {

    @Override
    public synchronized void run() {
        EventLogger.getInstance().logMessage("Saving nodes", "INFO");
        RegionManager.get().save(false, false);
        EventLogger.getInstance().logMessage(
                "Next save in " + Config.get().getSaveDelay()
                        + " minutes", "INFO");
    }
}
