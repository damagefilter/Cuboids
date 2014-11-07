package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.world.blocks.BlockType;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.mcutils.CLocation;

public class BlockUpdateEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled;
    private BlockType block;
    private BlockType targetBlock;
    private CLocation CLocation;

    public BlockUpdateEvent(BlockType block, BlockType targetBlock, CLocation CLocation) {
        this.block = block;
        this.targetBlock = targetBlock;
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

    /**
     * The original block before the update
     *
     * @return
     */
    public BlockType getBlock() {
        return block;
    }

    public CLocation getLocation() {
        return CLocation;
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
