package net.playblack.cuboids.blockoperators;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.BlockType;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.history.HistoryObject;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.Vector;

/**
 * Generate walls along the line of a cuboid selection
 *
 * @author Chris
 */
public class WallsGenerator extends BaseGen {

    private boolean onlyWalls;
    private BlockType wallMaterial;
    private BlockType floorMaterial;
    private BlockType ceilingMaterial;

    /**
     * The selection you pass along here will be written into the world!
     *
     * @param selection
     * @param world
     */
    public WallsGenerator(CuboidSelection selection, World world) {
        super(selection, world);
    }

    /**
     * Set true to build only the walls not the floor and ceiling
     *
     * @param wo
     */
    public void setWallsOnly(boolean wo) {
        onlyWalls = wo;
    }

    /**
     * Set the block (Material) the walls shall be made of
     *
     * @param block
     */
    public void setWallMaterial(BlockType block) {
        wallMaterial = block;
    }

    /**
     * Set the material the floor shall be made of
     *
     * @param block
     */
    public void setFloorMaterial(BlockType block) {
        floorMaterial = block;
    }

    /**
     * Set the material the ceiling shall be made of
     *
     * @param block
     */
    public void setCeilingMaterial(BlockType block) {
        ceilingMaterial = block;
    }

    private CuboidSelection createWalls() {
        if (!selection.isComplete()) {
            return null;
        }
        selection.sortEdges(true);
        CuboidSelection tmp = new CuboidSelection(selection.getOrigin(), selection.getOffset());
        synchronized (lock) {
            for (int x = tmp.getOrigin().getBlockX(); x <= tmp.getOffset().getBlockX(); x++) {
                for (int y = tmp.getOrigin().getBlockY(); y <= tmp.getOffset().getBlockY(); y++) {

                    tmp.setBlock(new Vector(x, y, tmp.getOrigin().getBlockZ()), wallMaterial);
                    tmp.setBlock(new Vector(x, y, tmp.getOffset().getBlockZ()), wallMaterial);
                }
            }

            for (int y = tmp.getOrigin().getBlockY(); y <= tmp.getOffset().getBlockY(); y++) {
                for (int z = tmp.getOrigin().getBlockZ(); z <= tmp.getOffset().getBlockZ(); z++) {

                    tmp.setBlock(new Vector(tmp.getOrigin().getBlockX(), y, z), wallMaterial);
                    tmp.setBlock(new Vector(tmp.getOffset().getBlockX(), y, z), wallMaterial);
                }
            }

            if (!onlyWalls) {
                for (int x = tmp.getOrigin().getBlockX(); x <= tmp.getOffset().getBlockX(); x++) {
                    for (int z = tmp.getOrigin().getBlockZ(); z <= tmp.getOffset().getBlockZ(); z++) {

                        tmp.setBlock(new Vector(x, tmp.getOrigin().getBlockY(), z), floorMaterial);
                        tmp.setBlock(new Vector(x, tmp.getOffset().getBlockY(), z), ceilingMaterial);
                    }
                }
            }
        }
        return tmp;
    }

    @Override
    public boolean execute(Player player, boolean newHistory) throws BlockEditLimitExceededException, SelectionIncompleteException {
        selection.clearBlocks();
        selection = createWalls();
        if (selection == null) {
            return false;
        }
        CuboidSelection world = scanWorld(true, true);
        if (world == null) {
            return false;
        }
        if (newHistory) {
            SessionManager.get().getPlayerHistory(player.getName()).remember(new HistoryObject(world, selection));
        }
        boolean result = modifyWorld(true);
        return result;
    }
}
