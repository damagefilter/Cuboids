package net.playblack.cuboids.history;

import net.canarymod.api.world.blocks.BlockType;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.Vector;

import java.util.LinkedHashMap;

/**
 * Represents a part of block editing history
 *
 * @author Chris
 */
public class HistoryObject {
    /**
     * Blocks before this modification has been made
     */
    private LinkedHashMap<Vector, BlockType> blocksBefore;

    /**
     * The modification itselt as block list
     */
    private LinkedHashMap<Vector, BlockType> modifiedBlocks;

    /**
     * Create a defaulr history object
     */
    public HistoryObject() {
        blocksBefore = new LinkedHashMap<Vector, BlockType>(30);
        modifiedBlocks = new LinkedHashMap<Vector, BlockType>(30);

    }

    /**
     * Create a history object with a defined size for block lists.
     *
     * @param listSize
     */
    public HistoryObject(int listSize) {
        blocksBefore = new LinkedHashMap<Vector, BlockType>(listSize);
        modifiedBlocks = new LinkedHashMap<Vector, BlockType>(listSize);

    }

    /**
     * Construct a new History object with full block lists
     *
     * @param original
     * @param modified
     */
    public HistoryObject(LinkedHashMap<Vector, BlockType> original, LinkedHashMap<Vector, BlockType> modified) {
        blocksBefore = original;
        modifiedBlocks = modified;
    }

    /**
     * Create new HistoryObject by 2 CuboidSelections. Probably the most common
     * way of making history
     *
     * @param original
     * @param modified
     */
    public HistoryObject(CuboidSelection original, CuboidSelection modified) {
        blocksBefore = original.getBlockList();
        modifiedBlocks = modified.getBlockList();
    }

    /**
     * Get the blocks that are in the world before modifying
     *
     * @return
     */
    public LinkedHashMap<Vector, BlockType> getBlocksBefore() {
        return blocksBefore;
    }

    /**
     * Set the blocks that are in the world before modifying
     *
     * @param b
     */
    public void setBlocksBefore(LinkedHashMap<Vector, BlockType> b) {
        blocksBefore = b;
    }

    /**
     * Get the blocks that are in the world before modifying
     *
     * @return
     */
    public LinkedHashMap<Vector, BlockType> getModifiedBlocks() {
        return modifiedBlocks;
    }

    /**
     * Set the blocks that are in the world before modifying
     *
     * @param b
     */
    public void setModifiedBlocks(LinkedHashMap<Vector, BlockType> b) {
        modifiedBlocks = b;
    }

    /**
     * Add a block to modified blocks list
     *
     * @param v
     * @param block
     */
    public void setBlock(Vector v, BlockType block) {
        modifiedBlocks.put(v, block);
    }

    /**
     * Get a block from the modifed blocks list
     *
     * @param v
     * @return
     */
    public BlockType getBlock(Vector v) {
        return modifiedBlocks.get(v);
    }

    /**
     * Set a block in the "original blocks" list.
     *
     * @param v
     * @param block
     */
    public void setBlockBefore(Vector v, BlockType block) {
        blocksBefore.put(v, block);
    }

    /**
     * Get a block from the "original blocks" list.
     *
     * @param v
     * @return
     */
    public BlockType getOriginalBlock(Vector v) {
        return blocksBefore.get(v);
    }
}
