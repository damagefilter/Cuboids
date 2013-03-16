package net.playblack.cuboids.actions.events.forwardings;

import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.mcutils.Location;

public class EndermanPickupEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled;
    private CBlock block;
    private Location location;
    
    public EndermanPickupEvent(Location location, CBlock block) {
        this.block = block;
        this.location = location;
    }
    
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    public CBlock getBlock() {
        return block;
    }

    public Location getLocation() {
        return location;
    }
}
