package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;

public class BlockPlaceEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled = false;
    private Player player;
    private BlockType block;
    private Location location;

    public BlockPlaceEvent(Player player, BlockType block, Location location) {
        this.player = player;
        this.block = block;
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

    public Player getPlayer() {
        return player;
    }

    public BlockType getBlock() {
        return block;
    }

    public Location getLocation() {
        return location;
    }
}
