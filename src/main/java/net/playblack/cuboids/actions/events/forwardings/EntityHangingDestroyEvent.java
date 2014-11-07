package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.mcutils.CLocation;

public class EntityHangingDestroyEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled = false;
    private Player player;
    private CLocation CLocation;

    public EntityHangingDestroyEvent(Player player, CLocation CLocation) {
        this.player = player;
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

    public CLocation getLocation() {
        return CLocation;
    }
}
