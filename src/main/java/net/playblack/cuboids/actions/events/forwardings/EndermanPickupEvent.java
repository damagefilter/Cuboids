package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.monster.Enderman;
import net.canarymod.api.world.blocks.BlockType;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.mcutils.CLocation;

public class EndermanPickupEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled;
    private BlockType block;
    private Enderman enderman;
    private CLocation CLocation;

    public EndermanPickupEvent(CLocation CLocation, BlockType block, Enderman enderman) {
        this.block = block;
        this.CLocation = CLocation;
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

    public CLocation getLocation() {
        return CLocation;
    }

    public Enderman getEnderman() {
        return enderman;
    }
}
