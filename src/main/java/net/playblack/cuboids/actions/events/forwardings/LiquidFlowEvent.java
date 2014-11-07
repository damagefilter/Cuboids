package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.world.blocks.BlockType;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.mcutils.CLocation;

public class LiquidFlowEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled;
    private BlockType sourceBlock;
    private BlockType targetBlock;
    private CLocation CLocation;

    public LiquidFlowEvent(BlockType sourceBlock, BlockType targetBlock, CLocation CLocation) {
        this.sourceBlock = sourceBlock;
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
     * Is this a lava flow event?
     *
     * @return
     */
    public boolean isLavaFlow() {
        return sourceBlock.getMachineName().endsWith("lava");
    }

    /**
     * Is this a water flow event?
     *
     * @return
     */
    public boolean isWaterFlow() {
        return sourceBlock.getMachineName().endsWith("water");
    }

    public BlockType getSourceBlock() {
        return sourceBlock;
    }

    public BlockType getTargetBlock() {
        return targetBlock;
    }

    public CLocation getLocation() {
        return CLocation;
    }
}
