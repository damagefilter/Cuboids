package net.playblack.cuboids.generators;

import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class BaseGen implements IShapeGen {

    protected final Object lock = new Object();
    protected CuboidSelection selection;
    protected World world;
    // List of said blocks
    HashMap<Vector3D, BlockType> placeLast = new HashMap<Vector3D, BlockType>();
    /**
     * List of block id's that need to be put last into the world
     */
    private ArrayList<Integer> queueables = new ArrayList<Integer>();

    public BaseGen(CuboidSelection selection, World world) {
        this.selection = selection;
        this.world = world;
        queueables.add(6);
        queueables.add(27);
        queueables.add(28);
        queueables.add(43);
        queueables.add(44);
        queueables.add(50);
        queueables.add(54);
        queueables.add(64);
        queueables.add(65);
        queueables.add(66);
        queueables.add(69);
        queueables.add(75);
        queueables.add(76);
        queueables.add(77);
        queueables.add(78);
        queueables.add(93);
        queueables.add(94);
    }

    /**
     * Modify the world with the already computed block changes!
     */
    protected boolean modifyWorld(boolean requireSelectionComplete) {
        if ((selection == null) || selection.getBlockList().isEmpty()) {
            return false;
        }
        if (requireSelectionComplete && !selection.isComplete()) {
            return false;
        }
        // NOTE: World must have been scanned before this operation!

        // Process blocks, 1st run
        synchronized (lock) {
            for (Vector3D v : selection.getBlockList().keySet()) {
                changeBlock(selection.getBlock(v), v, world, false);
            }
        }

        // process queued blocks
        synchronized (lock) {
            for (Vector3D v : placeLast.keySet()) {
                changeBlock(placeLast.get(v), v, world, true);
            }
            placeLast.clear();
        }
        return true;
    }

    /**
     * Preload a chunk in the world
     *
     * @param v
     * @param world
     */
    protected void preloadChunk(Vector3D v, World world) {
        if (!world.isChunkLoaded(v.getBlockX(), v.getBlockY(), v.getBlockZ())) {
            world.loadChunk(v.getBlockX(), v.getBlockZ());
        }
    }

    /**
     * Softly change a block in the world. Pre-checks in this method make heavy
     * block modifications more lightweight because it checks if a bloick really
     * needs to be changed
     *
     * @param block
     * @param coords
     * @param world
     */
    protected void changeBlock(BlockType block, Vector3D coords, World world, boolean queuedRun) {
        preloadChunk(coords, world);
        BlockType testBlock = world.getBlockAt(coords).getType();

        // If the block in the world is the same as the one we want to set, we
        // don't need to set - return
        if (testBlock == block) {
            return;
        }
        if (queueables.contains((int) block.getId()) && !queuedRun) {
            // queue for later placement if we're not in the queued run already
            placeLast.put(coords, block);
            return;
        }
        world.setBlockAt(coords, block);
    }

    /**
     * Fill the current selection with blocks that are currently in the world
     *
     * @param returnSelection true if you want to return the selection instead of
     *                        overwriting the blocks of the internal one
     * @throws BlockEditLimitExceededException
     * @throws SelectionIncompleteException
     */
    protected CuboidSelection scanWorld(boolean returnSelection, boolean requireCompleteSelection) throws BlockEditLimitExceededException, SelectionIncompleteException {
        if (selection == null) {
            return null;
        }
        if (requireCompleteSelection && !selection.isComplete()) {
            throw new SelectionIncompleteException("Selection was not complete in " + this.getClass().getSimpleName());
        }
        if (selection.getBlockList().isEmpty() && selection.isComplete()) {
            double areaVolume = selection.getBoundarySize();
            if (areaVolume > 700000) {
                throw new BlockEditLimitExceededException("Too many blocks to process in " + this.getClass().getSimpleName() + " (" + areaVolume + " blocks)");
            }
        }
        else if (selection.getBlockList().size() > 700000) {
            throw new BlockEditLimitExceededException("Too many blocks to process in " + this.getClass().getSimpleName() + " (" + selection.getBlockList().size() + " blocks)");
        }
        CuboidSelection tmp = new CuboidSelection();
        if (selection.getBlockList().isEmpty()) {
            tmp.setOffset(selection.getOffset());
            tmp.setOrigin(selection.getOrigin());
            int length_x = (int) Vector.getDistance(selection.getOrigin().getX(), selection.getOffset().getX()) + 1;
            int length_y = (int) Vector.getDistance(selection.getOrigin().getY(), selection.getOffset().getY()) + 1;
            int length_z = (int) Vector.getDistance(selection.getOrigin().getZ(), selection.getOffset().getZ()) + 1;
            // We use that to calculate the blocks we want to put
            Vector3D min = Vector3D.getMinimum(tmp.getOrigin(), tmp.getOffset());

            synchronized (lock) {
                for (int x = 0; x < length_x; ++x) {
                    for (int y = 0; y < length_y; ++y) {
                        for (int z = 0; z < length_z; ++z) {
                            Vector3D current = new Vector3D(min.getX() + x, min.getY() + y, min.getZ() + z);
                            tmp.setBlock(current, world.getBlockAt(min.getBlockX() + x, min.getBlockY() + y, min.getBlockZ() + z).getType());
                        }
                    }
                }
            }
        }

        else {
            synchronized (lock) {
                for (Vector3D key : selection.getBlockList().keySet()) {
                    tmp.setBlock(key, world.getBlockAt(key.getBlockX(), key.getBlockY(), key.getBlockZ()).getType());
                }
            }
        }
        if (returnSelection) {
            return tmp;
        }
        selection.setBlockList(tmp.getBlockList());
        return null;
    }

    /**
     * Return a cuboid selection with the current world content of the world you
     * passed along in the constructor!
     *
     * @return
     * @throws BlockEditLimitExceededException
     * @throws SelectionIncompleteException
     */
    public CuboidSelection getWorldContent() throws BlockEditLimitExceededException, SelectionIncompleteException {
        return scanWorld(true, true);
    }
}
