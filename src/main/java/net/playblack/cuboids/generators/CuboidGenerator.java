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

/**
 * Generate Cuboids, fill selections, replace blocks inside selections
 *
 * @author Chris
 */
public class CuboidGenerator extends BaseGen {

    private BlockType block;
    private BlockType toReplace;
    private boolean replace = false;

    public CuboidGenerator(CuboidSelection selection, World world) {
        super(selection, world);
    }

    /**
     * Set the block you want to set
     *
     * @param block
     */
    public void setBlock(BlockType block) {
        this.block = block;
    }

    /**
     * If you want to do a replace, this is the block that will be replaced with
     * the other block
     *
     * @param block
     */
    public void setBlockToReplace(BlockType block) {
        toReplace = block;
    }

    /**
     * Set true if you want to do a replace action. By default this is false and
     * for a cfill it doesn't need to be set!
     *
     * @param r
     */
    public void setReplace(boolean r) {
        replace = r;
    }

    @Override
    public boolean execute(Player player, boolean newHistory) throws BlockEditLimitExceededException, SelectionIncompleteException {
        selection.clearBlocks();
        scanWorld(false, true);

        if (replace) {
            for (Vector3D position : selection.getBlockList().keySet()) {
                if (selection.getBlock(position).getMachineName().equals(toReplace.getMachineName())) {
                    selection.setBlock(position, block);
                }
            }
        }
        else {
            for (Vector3D position : selection.getBlockList().keySet()) {
                selection.setBlock(position, block);
            }
        }
        if (newHistory) {
            CuboidSelection world = scanWorld(true, true);
            SessionManager.get().getPlayerHistory(player.getName()).remember(new HistoryObject(world, selection));
        }
        return modifyWorld(true);
    }
}
