package net.playblack.cuboids.blockoperators;

import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.history.HistoryObject;
import net.playblack.cuboids.selections.CuboidSelection;

/**
 * Process the given cuboidselection into the world
 * @author Chris
 *
 */
public class GenericGenerator extends BaseGen {

    /**
     * The selection you pass along here will be written into the world!
     * @param selection
     * @param world
     */
    public GenericGenerator(CuboidSelection selection, CWorld world) {
        super(selection, world);
    }
    @Override
    public boolean execute(CPlayer player, boolean newHistory) {
        CuboidSelection world = scanWorld(true);
        
        if(newHistory) {
            SessionManager.getInstance().getPlayerHistory(player.getName()).remember(new HistoryObject(world, selection));
        }
        boolean result = modifyWorld();
        return result;
    }
    
    /**
     * Return a cuboid selection with the current world content of the world you passed along in the constructor!
     * @return
     */
    public void getWorldContent(CuboidSelection sel) {
        sel = scanWorld(true);
    }
}
