package net.playblack.cuboids.actions.events.forwardings;

import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.mcutils.Location;
import net.playblack.mcutils.Vector;

public class PlayerWalkEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled = false;
    
    private Location from, to;
    private CPlayer player;
    
    public PlayerWalkEvent(CPlayer player, Location from, Location to) {
        this.from = from; 
        this.to = to;
        this.player = player;
    }
    
    /**
     * Get the player that is moving here
     * @return
     */
    public CPlayer getPlayer() {
        return player;
    }
    
    /**
     * Get the location the player will be in next
     * @return
     */
    public Location getFrom() {
        return from;
    }
    
    /**
     * Get the location where the player is coming from
     * @return
     */
    public Location getTo() {
        return to;
    }
    
    /**
     * Get the movement distance
     * @return
     */
    public double getDistance() {
        return Vector.getDistance(to, from);
    }
    
    /**
     * Get the squared movement distance (faster that getDistance!)
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
