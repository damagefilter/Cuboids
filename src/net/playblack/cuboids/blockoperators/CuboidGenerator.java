package net.playblack.cuboids.blockoperators;

import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.history.HistoryObject;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.Vector;

/**
 * Generate Cuboids, fill selections, replace blocks inside selections
 * @author Chris
 *
 */
public class CuboidGenerator extends BaseGen {

    private CBlock block;
    private CBlock toReplace;
    private boolean replace = false;
    public CuboidGenerator(CuboidSelection selection, CWorld world) {
        super(selection, world);
    }
    
    /**
     * Set the block you want to set
     * @param block
     */
    public void setBlock(CBlock block) {
        this.block = block;
    }
    
    /**
     * If you want to do a replace, this is the block that will be replaced with the other block
     * @param block
     */
    public void setBlockToReplace(CBlock block) {
        toReplace = block;
    }
    
    /**
     * Set true if you want to do a replace action.
     * By default this is false and for a cfill it doesn't need to be set!
     * @param r
     */
    public void setReplace(boolean r) {
        replace = r;
    }
    
    @Override
    public boolean execute(CPlayer player, boolean newHistory) {
        CuboidSelection original = scanWorld(true);
        selection.setBlockList(original.getBlockList());
        if(replace) {
            for(Vector position : selection.getBlockList().keySet()) {
                if(selection.getBlock(position).equals(toReplace)) {
                    selection.setBlock(position, block);
                }
            }
        }
        else {
            for(Vector position : selection.getBlockList().keySet()) {
                selection.setBlock(position, block);
            }
        }
        if(newHistory) {
            SessionManager.getInstance().getPlayerHistory(player.getName()).remember(new HistoryObject(original, selection));
        }
        boolean result = modifyWorld();
        return result;
    }
}
