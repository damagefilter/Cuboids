package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.mcutils.CLocation;
import net.playblack.mcutils.Vector;

public class PlayerWalkEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled = false;

    private CLocation from, to;
    private Player player;

    public PlayerWalkEvent(Player player, CLocation from, CLocation to) {
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
    public CLocation getFrom() {
        return from;
    }

    /**
     * Get the location where the player is coming from
     *
     * @return
     */
    public CLocation getTo() {
        return to;
    }

    /**
     * Get the movement distance
     *
     * @return
     */
    public double getDistance() {
        return Vector.getDistance(to, from);
    }

    /**
     * Get the squared movement distance (faster that getDistance!)
     *
     * @return
     */
    public double getSqrDistance() {
        return from.getSquareDistance(to);
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
