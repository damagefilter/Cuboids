package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;

public class PlayerWalkEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled = false;

    private Vector3D from, to;
    private Player player;

    public PlayerWalkEvent(Player player, Vector3D from, Vector3D to) {
        this.from = from;
        this.to = to;
        this.player = player;
    }

    /**
     * Get the player that is moving here
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the location the player will be in next
     *
     * @return
     */
    public Vector3D getFrom() {
        return from;
    }

    /**
     * Get the location where the player is coming from
     *
     * @return
     */
    public Vector3D getTo() {
        return to;
    }

    /**
     * Get the movement distance
     *
     * @return
     */
    public double getDistance() {
        return Vector3D.getDistance(to, from);
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }
}
