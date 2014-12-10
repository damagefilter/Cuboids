package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.canarymod.hook.world.IgnitionHook;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;

public class IgniteEvent extends CuboidEvent implements Cancellable {

    private boolean isCancelled;
    private IgnitionHook.IgnitionCause source;
    private Location location;
    private BlockType block;
    private Player player;

    public IgniteEvent(IgnitionHook.IgnitionCause source, Location location, BlockType block, Player player) {
        this.source = source;
        this.location = location;
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

    public Location getLocation() {
        return location;
    }

    public BlockType getBlock() {
        return block;
    }

    public Player getPlayer() {
        return player;
    }
}
