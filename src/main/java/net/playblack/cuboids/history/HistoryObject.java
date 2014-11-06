package net.playblack.cuboids.history;

import net.playblack.cuboids.blocks.CBlock;
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
    private LinkedHashMap<Vector, CBlock> blocksBefore;

    /**
     * The modification itselt as block list
     */
    private LinkedHashMap<Vector, CBlock> modifiedBlocks;

    /**
     * Create a defaulr history object
     */
    public HistoryObject() {
        blocksBefore = new LinkedHashMap<Vector, CBlock>(30);
        modifiedBlocks = new LinkedHashMap<Vector, CBlock>(30);

    }

    /**
     * Create a history object with a defined size for block lists.
     *
     * @param listSize
     */
    public HistoryObject(int listSize) {
        blocksBefore = new LinkedHashMap<Vector, CBlock>(listSize);
        modifiedBlocks = new LinkedHashMap<Vector, CBlock>(listSize);

    }

    /**
     * Construct a new History object with full block lists
     *
     * @param original
     * @param modified
     */
    public HistoryObject(LinkedHashMap<Vector, CBlock> original, LinkedHashMap<Vector, CBlock> modified) {
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
    public LinkedHashMap<Vector, CBlock> getBlocksBefore() {
        return blocksBefore;
    }

    /**
     * Set the blocks that are in the world before modifying
     *
     * @param b
     */
    public void setBlocksBefore(LinkedHashMap<Vector, CBlock> b) {
        blocksBefore = b;
    }

    /**
     * Get the blocks that are in the world before modifying
     *
     * @return
     */
    public LinkedHashMap<Vector, CBlock> getModifiedBlocks() {
        return modifiedBlocks;
    }

    /**
     * Set the blocks that are in the world before modifying
     *
     * @param b
     */
    public void setModifiedBlocks(LinkedHashMap<Vector, CBlock> b) {
        modifiedBlocks = b;
    }

    /**
     * Add a block to modified blocks list
     *
     * @param v
     * @param block
     */
    public void setBlock(Vector v, CBlock block) {
        modifiedBlocks.put(v, block);
    }

    /**
     * Get a block from the modifed blocks list
     *
     * @param v
     * @return
     */
    public CBlock getBlock(Vector v) {
        return modifiedBlocks.get(v);
    }

    /**
     * Set a block in the "original blocks" list.
     *
     * @param v
     * @param block
     */
    public void setBlockBefore(Vector v, CBlock block) {
        blocksBefore.put(v, block);
    }

    /**
     * Get a block from the "original blocks" list.
     *
     * @param v
     * @return
     */
    public CBlock getOriginalBlock(Vector v) {
        return blocksBefore.get(v);
    }
}
