package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;

/**
 * Fired when Cuboids catched a rightclick-on-block event
 *
 * @author chris
 */
public class BlockRightClickEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled = false;
    private Player player;
    private BlockType clickedBlock;
    private Location clickedCLocation;

    /**
     * Constructs a new RightClickEvent. Pass null for arguments that don't apply in the given situation!
     *
     * @param player
     * @param block
     * @param CLocation
     */
    public BlockRightClickEvent(Player player, BlockType block, Location CLocation) {
        this.player = player;
        this.clickedBlock = block;
        this.clickedCLocation = CLocation;
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
     *
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the right-clicked block. This may return null if there was no clicked block (or air)
     *
     * @return
     */
    public BlockType getBlock() {
        return clickedBlock;
    }

    /**
     * get the location that was clicked at, this will always return a valid Location
     *
     * @return
     */
    public Location getLocation() {
        return clickedCLocation;
    }
}
