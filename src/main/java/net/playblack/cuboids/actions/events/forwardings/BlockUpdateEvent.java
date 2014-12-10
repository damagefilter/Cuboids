package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;

public class BlockUpdateEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled;
    private BlockType block;
    private BlockType targetBlock;
    private Location location;

    public BlockUpdateEvent(BlockType block, BlockType targetBlock, Location location) {
        this.block = block;
        this.targetBlock = targetBlock;
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

    /**
     * The original block before the update
     *
     * @return
     */
    public BlockType getBlock() {
        return block;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * This is what the original block will change into after the update
     *
     * @return
     */
    public BlockType getTargetBlock() {
        return targetBlock;
    }
}
