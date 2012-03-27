package net.playblack.cuboids.blockoperators;

import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.Vector;

public abstract class BaseGen implements IShapeGen {
    
    protected CuboidSelection selection;
    protected CWorld world;
    protected Object lock = new Object();
    
    public BaseGen(CuboidSelection selection, CWorld world) {
        this.selection = selection;
        this.world = world;
    }
    /**
     * Modify the world with the already computed block changes!
     */
    protected boolean modifyWorld() {
        if(selection == null || (!selection.isComplete())) {
            return false;
        }
        //NOTE: World must have been scanned before this operation!
        synchronized (lock) {
            for(Vector v : selection.getBlockList().keySet()) {
                changeBlock(selection.getBlock(v), v, world);
            }
        }
        return true;
    }
    
    /**
     * Preload a chunk in the world
     * @param v
     * @param world
     */
    protected void preloadChunk(Vector v, CWorld world) {
        if (!world.isChunkLoaded(v.getBlockX(), v.getBlockY(), v.getBlockZ())) {
            world.loadChunk(v.getBlockX(), v.getBlockY(), v.getBlockZ());
        }
    }
    
    /**
     * Softly change a block in the world. 
     * Pre-checks in this method make heavy block modifications more lightweight
     * because it checks if a bloick really needs to be changed
     * @param block
     * @param coords
     * @param world
     */
    protected void changeBlock(CBlock block, Vector coords, CWorld world) {
        preloadChunk(coords, world);
        CBlock testBlock = world.getBlockAt(coords);
        
        //If the block in the world is the same as the one we want to set, we don't need to set - return
        if((testBlock.getType() == block.getType()) && (testBlock.getData() == block.getData())) {
            return;
        }
        
        //Types are not equal so, we can change that 
        if(testBlock.getType() != block.getType()) {
            world.setBlockAt(block.getType(), coords);
        }
        
        //Data is not equal so we can change that too
        if(testBlock.getData() != (Byte)block.getData()) {
            world.setBlockData(block.getData(), coords);
        }
    }
    
    /**
     * Fill the current selection with blocks that are currently in the world
     */
    protected void scanWorld() {
        if(selection == null || (!selection.isComplete())) {
            return;
        }
        CuboidSelection tmp = new CuboidSelection(selection.getOrigin(), selection.getOffset());
        if(selection.getBlockList().isEmpty()) {
            int length_x = (int)Vector.getDistance(selection.getOrigin().getX(), selection.getOffset().getX())+1;
            int length_y = (int)Vector.getDistance(selection.getOrigin().getY(), selection.getOffset().getY())+1;
            int length_z = (int)Vector.getDistance(selection.getOrigin().getZ(), selection.getOffset().getZ())+1;
            //We use that to calculate the blocks we want to put
            Vector min = Vector.getMinimum(tmp.getOrigin(), tmp.getOffset());
            
            Vector size = new Vector();
            
            size.setX(length_x);
            size.setY(length_y);
            size.setZ(length_z);
            synchronized(lock) {
                for(int x = 0; x < length_x; ++x) {     
                    for(int y = 0; y < length_y; ++y) {     
                        for(int z = 0; z < length_z; ++z) {
                            Vector current = new Vector(min.getX()+x, min.getY()+y, min.getZ()+z);
                            tmp.setBlock(current, world.getBlockAt(current));
                        }
                    }
                }
            }
        }
        
        else {
            synchronized(lock) {
                for(Vector key : selection.getBlockList().keySet()) { 
                    tmp.setBlock(key, world.getBlockAt(key));
                }
            }
        }
        selection.setBlockList(tmp.getBlockList());
    }
}
