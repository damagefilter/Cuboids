package net.playblack.cuboids.selections;

import java.util.LinkedHashMap;

import net.playblack.cuboids.blocks.CBlock;
import net.playblack.mcutils.Vector;

/**
 * Interface for a generic selection
 * 
 * @author Chris
 * 
 */
public interface ISelection {

    /**
     * Get the world for this selection
     * 
     * @return
     */
    public String getWorld();

    /**
     * Set the world for this selection
     * 
     * @param world
     */
    public void setWorld(String world);

    /**
     * Empty the block list
     */
    public void clearBlocks();

    /**
     * Set a block in this selection
     * 
     * @param v
     * @param b
     */
    public void setBlock(Vector v, CBlock b);

    /**
     * Get a block from the specified coordinate storage in this selection. May
     * return null
     * 
     * @param v
     * @return
     */
    public CBlock getBlock(Vector v);

    /**
     * Get the whole block list from this selection
     * 
     * @return
     */
    public LinkedHashMap<Vector, CBlock> getBlockList();

    /**
     * Override the whole block list from this selection
     * 
     * @param newList
     */
    public void setBlockList(LinkedHashMap<Vector, CBlock> newList);

    /**
     * Get the size of this selection, that is: number of stored blocks
     * 
     * @return
     */
    public long getSize();

    /**
     * Get the boundary size. That is: the volume of this selection (number of
     * blocks in the world that are encompassed by this selection)
     * 
     * @return
     */
    public long getBoundarySize();
}
