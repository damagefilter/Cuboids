package net.playblack.cuboids.actions.events.forwardings;

import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.mcutils.Location;

/**
 * Fired when Cuboids catched a rightclick-on-block event
 * @author chris
 *
 */
public class BlockRightClickEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled = false;
    private CPlayer player;
    private CBlock clickedBlock;
    private Location clickedLocation;
    
    /**
     * Constructs a new RightClickEvent. Pass null for arguments that don't apply in the given situation!
     * @param player
     * @param block
     * @param location
     * @param ent
     */
    public BlockRightClickEvent(CPlayer player, CBlock block, Location location) {
        this.player = player;
        this.clickedBlock = block;
        this.clickedLocation = location;
    }
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }
    
    /**
     * Get the right-clicking player
     * @return
     */
    public CPlayer getPlayer() {
        return player;
    }
    
    /**
     * Get the right-clicked block. This may return null if there was no clicked block (or air)
     * @return
     */
    public CBlock getBlock() {
        return clickedBlock;
    }

    /**
     * get the location that was clicked at, this will always return a valid Location
     * @return
     */
    public Location getLocation() {
        return clickedLocation;
    }
}
