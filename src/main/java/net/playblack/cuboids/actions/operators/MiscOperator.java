package net.playblack.cuboids.actions.operators;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;

public class MiscOperator implements ActionListener {

    /**
     * Returns true if the item dropping needs to be canceled
     *
     * @param itemLocation
     * @param playerLocation
     * @return
     */
    public boolean onItemDrop(Location itemLocation, Location playerLocation) {
        Region itemRegion = RegionManager.get().getActiveRegion(itemLocation, false);
        Region playerRegion = RegionManager.get().getActiveRegion(playerLocation, false);
        return itemRegion.getProperty("creative") == Status.ALLOW || playerRegion.getProperty("creative") == Status.ALLOW;
    }

    /**
     * Returns true if mob spawning should be stopped
     *
     * @param toSpawn
     * @return
     */
    public boolean onMobSpawn(Entity toSpawn) {
        if (!toSpawn.isMob()) {
            return false;
        }
        Region r = RegionManager.get().getActiveRegion(toSpawn.getLocation(), false);
        return r.getProperty("mob-spawn") == Status.DENY;
    }
}
