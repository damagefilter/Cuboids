package net.playblack.cuboids.history;

import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.selections.CuboidSelection;

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
    private LinkedHashMap<Vector3D, BlockType> blocksBefore;

    /**
     * The modification itselt as block list
     */
    private LinkedHashMap<Vector3D, BlockType> modifiedBlocks;

    /**
     * Create a defaulr history object
     */
    public HistoryObject() {
        blocksBefore = new LinkedHashMap<Vector3D, BlockType>(30);
        modifiedBlocks = new LinkedHashMap<Vector3D, BlockType>(30);

    }

    /**
     * Create a history object with a defined size for block lists.
     *
     * @param listSize
     */
    public HistoryObject(int listSize) {
        blocksBefore = new LinkedHashMap<Vector3D, BlockType>(listSize);
        modifiedBlocks = new LinkedHashMap<Vector3D, BlockType>(listSize);

    }

    /**
     * Construct a new History object with full block lists
     *
     * @param original
     * @param modified
     */
    public HistoryObject(LinkedHashMap<Vector3D, BlockType> original, LinkedHashMap<Vector3D, BlockType> modified) {
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
    public LinkedHashMap<Vector3D, BlockType> getBlocksBefore() {
        return blocksBefore;
    }

    /**
     * Set the blocks that are in the world before modifying
     *
     * @param b
     */
    public void setBlocksBefore(LinkedHashMap<Vector3D, BlockType> b) {
        blocksBefore = b;
    }

    /**
     * Get the blocks that are in the world before modifying
     *
     * @return
     */
    public LinkedHashMap<Vector3D, BlockType> getModifiedBlocks() {
        return modifiedBlocks;
    }

    /**
     * Set the blocks that are in the world before modifying
     *
     * @param b
     */
    public void setModifiedBlocks(LinkedHashMap<Vector3D, BlockType> b) {
        modifiedBlocks = b;
    }

    /**
     * Add a block to modified blocks list
     *
     * @param v
     * @param block
     */
    public void setBlock(Vector3D v, BlockType block) {
        modifiedBlocks.put(v, block);
    }

    /**
     * Get a block from the modifed blocks list
     *
     * @param v
     * @return
     */
    public BlockType getBlock(Vector3D v) {
        return modifiedBlocks.get(v);
    }

    /**
     * Set a block in the "original blocks" list.
     *
     * @param v
     * @param block
     */
    public void setBlockBefore(Vector3D v, BlockType block) {
        blocksBefore.put(v, block);
    }

    /**
     * Get a block from the "original blocks" list.
     *
     * @param v
     * @return
     */
    public BlockType getOriginalBlock(Vector3D v) {
        return blocksBefore.get(v);
    }
}
