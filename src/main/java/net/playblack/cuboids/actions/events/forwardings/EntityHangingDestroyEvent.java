package net.playblack.cuboids.actions.events.forwardings;

import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.mcutils.Location;

public class EntityHangingDestroyEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled = false;
    private CPlayer player;
    private Location location;

    public EntityHangingDestroyEvent(CPlayer player, Location location) {
        this.player = player;
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

    public Location getLocation() {
        return location;
    }
}
