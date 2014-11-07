package net.playblack.cuboids.actions.operators;

import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.EntitySpawnEvent;
import net.playblack.cuboids.actions.events.forwardings.ItemDropEvent;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.CLocation;

public class MiscOperator implements ActionListener {

    @ActionHandler
    public void onItemDrop(ItemDropEvent event) {
        Region itemRegion = RegionManager.get().getActiveRegion(new CLocation(event.getItemDropLocation()), false);
        Region playerRegion = RegionManager.get().getActiveRegion(new CLocation(event.getPlayer().getLocation()), false);
        if (itemRegion.getProperty("creative") == Status.ALLOW || playerRegion.getProperty("creative") == Status.ALLOW) {
            event.cancel();
        }
    }

    @ActionHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if (!event.getEntity().isMob()) {
            return;
        }
        Region r = RegionManager.get().getActiveRegion(new CLocation(event.getEntity().getLocation()), false);
        if (r.getProperty("mob-spawn") == Status.DENY) {
            event.cancel();
        }
    }

    static {
        ActionManager.registerActionListener("Cuboids", new MiscOperator());
    }
}
