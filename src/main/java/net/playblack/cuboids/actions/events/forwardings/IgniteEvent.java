package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.hook.world.IgnitionHook;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.mcutils.CLocation;

public class IgniteEvent extends CuboidEvent implements Cancellable {

    private boolean isCancelled;
    private IgnitionHook.IgnitionCause source;
    private CLocation CLocation;
    private BlockType block;
    private Player player;

    public IgniteEvent(IgnitionHook.IgnitionCause source, CLocation CLocation, BlockType block, Player player) {
        this.source = source;
        this.CLocation = CLocation;
        this.block = block;
        this.player = player;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    public IgnitionHook.IgnitionCause getSource() {
        return source;
    }

    public CLocation getLocation() {
        return CLocation;
    }

    public BlockType getBlock() {
        return block;
    }

    public Player getPlayer() {
        return player;
    }
}
