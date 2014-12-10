package net.playblack.cuboids.generators;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.history.HistoryObject;
import net.playblack.cuboids.selections.CuboidSelection;

import java.util.LinkedHashMap;

/**
 * Offset blocks in a direction by a distance
 *
 * @author Chris
 */
public class VectorOffsetGenerator extends BaseGen {

    private Vector3D position;
    private LinkedHashMap<Vector3D, BlockType> originalPositions;

    /**
     * The selection you pass along here will be written into the world!
     *
     * @param selection
     * @param world
     */
    public VectorOffsetGenerator(CuboidSelection selection, World world) {
        super(selection, world);
    }

    /**
     * The the offset vector we need to calculate the distance between clipbord
     * origin and current position
     *
     * @param offset
     */
    public void setOffsetVector(Vector3D offset) {
        this.position = offset;
    }

    /**
     * This returns a CuboidSelection containing the _final_ move result. That
     * means it contains the empty space and the moved blocks.
     *
     * @return
     */
    private void calculateOffset() {
        // selection.setOffset(position);
        double x_distance = position.getX() - selection.getOrigin().getX();

        double y_distance = position.getY() - selection.getOrigin().getY();

        double z_distance = position.getZ() - selection.getOrigin().getZ();
        CuboidSelection tmp = new CuboidSelection(selection.getOrigin(), selection.getOffset());
        synchronized (lock) {
            for (Vector3D key : selection.getBlockList().keySet()) {
                BlockType b = selection.getBlockList().get(key);
                tmp.setBlock(new Vector3D(key.getX() + x_distance, key.getY() + y_distance, key.getZ() + z_distance), b);
            }
            originalPositions = selection.getBlockList();
            selection.setBlockList(tmp.getBlockList());
        }
        // We spare us the recalculation of the bounding rectangle as
        // scanWorld() will take only the blocks regardless of
        // origin and offset if blocklist is not empty
    }

    @Override
    public boolean execute(Player player, boolean newHistory) throws BlockEditLimitExceededException, SelectionIncompleteException {
        // selection.clearBlocks(); //<- do not clear here, we need the blocks
        // for pasting, idiot!
        // scanWorld(false, true); //don't fetch again!
        calculateOffset();
        CuboidSelection world = scanWorld(true, true);
        if (world == null) {
            return false;
        }
        if (newHistory) {
            SessionManager.get().getPlayerHistory(player.getName()).remember(new HistoryObject(world, selection));
        }
        boolean result = modifyWorld(true);
        // Reset positions for pasting the stuff again.
        selection.setBlockList(originalPositions);
        return result;
    }
}
