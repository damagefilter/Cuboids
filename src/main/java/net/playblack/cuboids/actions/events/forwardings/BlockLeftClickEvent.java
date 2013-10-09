package net.playblack.cuboids.actions.events.forwardings;

import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.mcutils.Location;

public class BlockLeftClickEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled = false;
    private CPlayer player;
    private CBlock block;
    private Location location;

    public BlockLeftClickEvent(CPlayer player, CBlock block, Location location) {
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

    public CPlayer getPlayer() {
        return player;
    }

    public CBlock getBlock() {
        return block;
    }

    public Location getLocation() {
        return location;
    }
}
