package net.playblack.cuboids.actions.events.forwardings;

import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.mcutils.Location;

public class LiquidFlowEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled;
    private CBlock sourceBlock;
    private CBlock targetBlock;
    private Location location;

    public LiquidFlowEvent(CBlock sourceBlock, CBlock targetBlock, Location location) {
        this.sourceBlock = sourceBlock;
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
     * Is this a lava flow event?
     *
     * @return
     */
    public boolean isLavaFlow() {
        return sourceBlock.getType() == 10 || sourceBlock.getType() == 11;
    }

    /**
     * Is this a water flow event?
     *
     * @return
     */
    public boolean isWaterFlow() {
        return sourceBlock.getType() == 8 || sourceBlock.getType() == 9;
    }

    public CBlock getSourceBlock() {
        return sourceBlock;
    }

    public CBlock getTargetBlock() {
        return targetBlock;
    }

    public Location getLocation() {
        return location;
    }
}
