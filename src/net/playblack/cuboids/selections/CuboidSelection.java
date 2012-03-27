package net.playblack.cuboids.selections;

import java.util.LinkedHashMap;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.mcutils.Vector;

/**
 * Cuboid shaped selection
 * @author Christoph Ksoll
 *
 */
public class CuboidSelection implements ISelection {

    protected LinkedHashMap<Vector,CBlock> blockList;
    
    protected String world = "__NOWORLD__";
    
    protected Vector origin;
    protected Vector offset;
    
    /**
     * Empty CTOR
     */
    public CuboidSelection() {
        origin = new Vector(0,0,0);
        offset = new Vector(0,0,0);
        blockList = new LinkedHashMap<Vector,CBlock>((int)Vector.getAreaVolume(origin, offset));
    }
    
    /**
     * Construct with vectors
     * @param v1
     * @param v2
     */
    public CuboidSelection(Vector v1, Vector v2) {
        origin = v1;
        offset = v2;
        blockList = new LinkedHashMap<Vector,CBlock>((int)Vector.getAreaVolume(origin, offset));
    }
    
    /**
     * Construct with vectors and a ready-to-use block list
     * @param v1
     * @param v2
     * @param blocks
     */
    public CuboidSelection(Vector v1, Vector v2, LinkedHashMap<Vector,CBlock> blocks) {
        origin = v1;
        offset = v2;
        blockList = blocks;
    }
    public CuboidSelection(LinkedHashMap<Vector, CBlock> blocks) {
        blockList = blocks;
    }

    /**
     * Set the current origin vector
     * @param o
     */
    public void setOrigin(Vector o) {
        this.origin = o;
    }
    
    /**
     * Get the current origin vector
     * @return
     */
    public Vector getOrigin() {
        return origin;
    }
    
    /**
     * Set current offset Vector
     * @param o
     */
    public void setOffset(Vector o) {
        this.offset = o;
    }
    
    /**
     * Get current offset vector
     * @return
     */
    public Vector getOffset() {
        return offset;
    }
    
    @Override
    public void setBlock(Vector v, CBlock b) {
        blockList.put(v, b);
    }

    @Override
    public CBlock getBlock(Vector v) {
        return blockList.get(v);
    }

    @Override
    public LinkedHashMap<Vector, CBlock> getBlockList() {
        return blockList;
    }

    @Override
    public void setBlockList(LinkedHashMap<Vector, CBlock> newList) {
        this.blockList = newList;
        
    }

    @Override
    public long getSize() {
        return blockList.size();
    }

    @Override
    public long getBoundarySize() {
        return (long)Vector.getAreaVolume(origin, offset);
    }

    @Override
    public String getWorld() {
        return null;
    }

    @Override
    public void setWorld(String world) {
        this.world = world;
        
    }
    
    /**
     * Sort the selection points.
     * @param offsetFirst true: offset has the greater components - false: origin has the greater components
     */
    public void sortEdges(boolean offsetFirst) {
        if(offsetFirst) {
            sortEdgesOffsetFirst();
        }
        else {
            Vector or_temp = Vector.getMaximum(origin, offset);
            Vector off_temp = Vector.getMinimum(origin, offset);
            origin = or_temp;
            offset = off_temp;
        }
    }

    /**
    * Sort the selection edges so offset is the greater one and origin the smaller
    */
    private void sortEdgesOffsetFirst() {
        Vector or_temp = Vector.getMaximum(origin, offset);
        Vector off_temp = Vector.getMinimum(origin, offset);
        origin = off_temp;
        offset = or_temp;
    }

    /**
     * Check if this selection is complete.
     * @return
     */
    public boolean isComplete() {
        if((origin != null) && offset != null) {
            return true;
        }
        return false;
    }
    
    @Override
    public void clearBlocks() {
        blockList.clear();
        
    }
    
    /**
     * Expand the selection vertically from top to bottom
     */
    public void expandVert() {
        sortEdges(false);
        origin.setY(255);
        offset.setY(0);
    }

    /**
     * Turn this selection into a CuboidE.
     * The result is a cuboidE object with the default settings.
     * @return
     */
    public CuboidE toCuboid(CPlayer player, String[] playerlist) {
        CuboidE cube = new CuboidE();
        String name = null;
        cube.setPoints(this.origin, this.offset);
        //start at second element for the first would be /highprotect
        for (int i = 1; i < playerlist.length; i++) {
            if(i == (playerlist.length - 1)) { //last element is name!
                name = playerlist[i];
                continue;
            }
          if (playerlist[i].indexOf("o:") != -1) {
            cube.addPlayer(playerlist[i]);
          }
          else if (playerlist[i].indexOf("g:") != -1) {
            cube.addGroup(playerlist[i]);
          }
          else {
            cube.addPlayer(playerlist[i]);
          }
        }
        cube.setWorld(world);
        cube.setName(name);
        cube.overwriteProperties(Config.getInstance().getDefaultCuboidSetting(player));
        
        return cube;
    }

}
