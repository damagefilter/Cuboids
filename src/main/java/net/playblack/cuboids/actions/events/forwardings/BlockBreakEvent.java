package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.BlockType;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.mcutils.CLocation;

public class BlockBreakEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled = false;
    private Player player;
    private BlockType block;
    private CLocation CLocation;

    public BlockBreakEvent(Player player, BlockType block, CLocation CLocation) {
        this.player = player;
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

    public Player getPlayer() {
        return player;
    }

    public BlockType getBlock() {
        return block;
    }

    public CLocation getLocation() {
        return CLocation;
    }
}
