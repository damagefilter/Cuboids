package net.playblack.cuboids.blockoperators;

import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.history.HistoryObject;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.Vector;

/**
 * Offset blocks in a direction by a distance
 * @author Chris
 *
 */
public class OffsetGenerator extends BaseGen {

    private int direction;
    private int distance;
    /**
     * The selection you pass along here will be written into the world!
     * @param selection
     * @param world
     */
    public OffsetGenerator(CuboidSelection selection, CWorld world) {
        super(selection, world);
    }
    
    /**
     * Set direction. This returns false if the cardinal direction is not recognized!
     * @param dir
     */
    public boolean setDirection(String dir) {
        direction = -1;
        if(dir.equalsIgnoreCase("SOUTH")) { direction=0; }
        else if(dir.equalsIgnoreCase("EAST")) { direction=1; }
        else if(dir.equalsIgnoreCase("NORTH")) { direction=2; }
        else if(dir.equalsIgnoreCase("WEST")) { direction=3; }
        else if(dir.equalsIgnoreCase("UP")) { direction=4; }
        else if(dir.equalsIgnoreCase("DOWN")) { direction=5; }
        if(direction == -1) {
            return false;
        }
        return true;
    }
    
    /**
     * Set the offset distance
     * @param distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }
    
    private void recalculateBoundingRectangle(CuboidSelection tmp) {
        switch(direction) {
            case 0:
                tmp.setOrigin(new Vector(tmp.getOrigin().getX(), tmp.getOrigin().getY(), tmp.getOrigin().getZ()-distance));
                tmp.setOffset(new Vector(tmp.getOffset().getX(), tmp.getOffset().getY(), tmp.getOffset().getZ()-distance));
                break;
            case 1:
                tmp.setOrigin(new Vector(tmp.getOrigin().getX()-distance, tmp.getOrigin().getY(), tmp.getOrigin().getZ()));
                tmp.setOffset(new Vector(tmp.getOffset().getX()-distance, tmp.getOffset().getY(), tmp.getOffset().getZ()));
            case 2:
                tmp.setOrigin(new Vector(tmp.getOrigin().getX(), tmp.getOrigin().getY(), tmp.getOrigin().getZ()+distance));
                tmp.setOffset(new Vector(tmp.getOffset().getX(), tmp.getOffset().getY(), tmp.getOffset().getZ()+distance));
                break;
            case 3:
                tmp.setOrigin(new Vector(tmp.getOrigin().getX()+distance, tmp.getOrigin().getY(), tmp.getOrigin().getZ()));
                tmp.setOffset(new Vector(tmp.getOffset().getX()+distance, tmp.getOffset().getY(), tmp.getOffset().getZ()));
                break;
            case 4:
                tmp.setOrigin(new Vector(tmp.getOrigin().getX(), tmp.getOrigin().getY()+distance, tmp.getOrigin().getZ()));
                tmp.setOffset(new Vector(tmp.getOffset().getX(), tmp.getOffset().getY()+distance, tmp.getOffset().getZ()));
                break;
            case 5:
                tmp.setOrigin(new Vector(tmp.getOrigin().getX(), tmp.getOrigin().getY()-distance, tmp.getOrigin().getZ()));
                tmp.setOffset(new Vector(tmp.getOffset().getX(), tmp.getOffset().getY()-distance, tmp.getOffset().getZ()));
                break;
            }
    }
    /**
     * This returns a CuboidSelection containing the _final_ move result.
     * That means it contains the empty space and the moved blocks.
     * @param sel
     * @return
     */
    private void calculateOffset() {
       // CuboidSelection tmp = new CuboidSelection(selection);
        CBlock air = new CBlock(0,0);
        for(Vector key : selection.getBlockList().keySet()) {
            CBlock original = selection.getBlock(key);
            Vector originalPosition = new Vector(key);
            switch(direction) {
                case 0:
                    key = new Vector(key.getX(), key.getY(), key.getZ()-distance);
                    break;
                case 1:
                    key = new Vector(key.getX()-distance, key.getY(), key.getZ());
                case 2:
                    key = new Vector(key.getX(), key.getY(), key.getZ()+distance);
                    break;
                case 3:
                    key = new Vector(key.getX()+distance, key.getY(), key.getZ());
                    break;
                case 4:
                    key = new Vector(key.getX(), key.getY()+distance, key.getZ());
                    break;
                case 5:
                    key = new Vector(key.getX(), key.getY()-distance, key.getZ());
                    break;
                }
            selection.setBlock(key, original);
            //Set the old position to be nothing
            selection.setBlock(originalPosition, air);
        }
        recalculateBoundingRectangle(selection);
    }
    @Override
    public boolean execute(CPlayer player, boolean newHistory) {
        selection.clearBlocks();
        scanWorld(false, true);
        calculateOffset();
        CuboidSelection world = scanWorld(true, true);
        if(world == null) {
            return false;
        }
        if(newHistory) {
            SessionManager.getInstance().getPlayerHistory(player.getName()).remember(new HistoryObject(world, selection));
        }
        boolean result = modifyWorld(true);
        return result;
    }
}
