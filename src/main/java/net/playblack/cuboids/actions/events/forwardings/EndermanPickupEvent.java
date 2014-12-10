package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.monster.Enderman;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;

public class EndermanPickupEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled;
    private BlockType block;
    private Enderman enderman;
    private Location location;

    public EndermanPickupEvent(Location location, BlockType block, Enderman enderman) {
        this.block = block;
        this.location = location;
        this.enderman = enderman;
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

    public Enderman getEnderman() {
        return enderman;
    }
}
