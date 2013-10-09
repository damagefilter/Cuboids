package net.playblack.cuboids.actions.events.forwardings;

import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.mcutils.Location;

public class BlockUpdateEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled;
    private CBlock block;
    private CBlock targetBlock;
    private Location location;

    public BlockUpdateEvent(CBlock block, CBlock targetBlock, Location location) {
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
    public CBlock getBlock() {
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
    public CBlock getTargetBlock() {
        return targetBlock;
    }
}
