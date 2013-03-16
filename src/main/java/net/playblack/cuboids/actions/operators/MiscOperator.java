package net.playblack.cuboids.actions.operators;

import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.ItemDropEvent;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.mcutils.Location;

public class MiscOperator implements ActionListener {

    @ActionHandler
    public void onItemDrop(ItemDropEvent event) {
        Location l = event.getPlayer().getLocation();
        Region r = RegionManager.get().getActiveRegion(l, false);
        if(r.getProperty("creative") == Status.ALLOW) {
            event.cancel();
        }
    }
    
    static {
        ActionManager.registerActionListener("Cuboids2", new MiscOperator());
    }
}
