package net.playblack.cuboids.gameinterface;

import net.playblack.cuboids.blocks.CBlock;
import net.playblack.mcutils.Vector;

/**
 * Adapter for a world object. 
 * Implement this to map to your local world
 * @author chris
 *
 */
public abstract class CWorld {
    /**
     * Get world name
     * @return
     */
    public abstract String getName();
    /**
     * Get this worlds ID
     * @return
     */
    public abstract int getId();
    
    /**
     * Get the block form the current position
     * @implementation This must check for chests, double chests and signs and send the corresponding block! This must also do position corrections for block positions
     * @param position
     * @return
     */
    public abstract CBlock getBlockAt(Vector position);
    
    /**
     * Get a block form the current x,y,z position
     * @implementation This must check for chests, double chests and signs and send the corresponding block! This must also do position corrections for block positions
     * @param x
     * @param y
     * @param z
     * @return
     */
    public abstract CBlock getBlockAt(int x, int y, int z);
    
    /**
     * Check if a chunk is loaded
     * @param position
     * @return
     */
    public abstract boolean isChunkLoaded(Vector position);
    
    /**
     * Check if a chunk is loaded
     * @param x
     * @param y
     * @param z
     * @return
     */
    public abstract boolean isChunkLoaded(int x, int y, int z);
    /**
     * Load a chunk there
     * @param position
     */
    public abstract void loadChunk(Vector position);
    /**
     * Load a chunk there
     * @param x
     * @param y
     * @param z
     */
    public abstract void loadChunk(int x, int y, int z);
    /**
     * Get current RELATIVE time of this world
     * @return
     */
    public abstract long getTime();
    
    /**
     * Get the highest valid y position for this x-z coordinate
     * @param x
     * @param z
     * @return
     */
    public abstract int getHighestBlock(int x, int z);
    
    /**
     * Set block type and data in a world
     * @implementation This must check for chests, double chests and sign contents after placing a block!
     * @param type
     * @param data
     * @param v
     */
    public abstract void setBlockAt(short type, byte data, Vector v);
    
    /**
     * Set a block type in the world
     * @implementation This must check for chests, double chests and sign contents after placing a block!
     * @param type
     * @param v
     */
    public abstract void setBlockAt(short type, Vector v);
    
    /**
     * Set a block at the given position
     * @implementation This must check for chests, double chests and sign contents after placing a block!
     * @param position
     * @param block
     */
    public abstract void setBlockAt(Vector position, CBlock block);
    
    /**
     * Set the block data in a world
     * @param data
     * @param v
     */
    public abstract void setBlockData(byte data, Vector v);
}
