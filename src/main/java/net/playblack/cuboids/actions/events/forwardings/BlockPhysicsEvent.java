package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;

public class BlockPhysicsEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled;
    private BlockType block;
    private Location location;

    public BlockPhysicsEvent(BlockType block, Location location) {
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

    public BlockType getBlock() {
        return block;
    }

    public Location getLocation() {
        return location;
    }
}
