package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.world.blocks.BlockType;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.mcutils.CLocation;

public class BlockPhysicsEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled;
    private BlockType block;
    private CLocation CLocation;

    public BlockPhysicsEvent(BlockType block, CLocation CLocation) {
        this.block = block;
        this.CLocation = CLocation;
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
}
