package net.playblack.cuboids.generators;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.history.HistoryObject;
import net.playblack.cuboids.selections.CuboidSelection;

/**
 * Process the given cuboidselection into the world
 *
 * @author Chris
 */
public class GenericGenerator extends BaseGen {

    /**
     * The selection you pass along here will be written into the world!
     *
     * @param selection
     * @param world
     */
    public GenericGenerator(CuboidSelection selection, World world) {
        super(selection, world);
    }

    @Override
    public boolean execute(Player player, boolean newHistory) throws BlockEditLimitExceededException, SelectionIncompleteException {
        if (newHistory) {
            CuboidSelection world = scanWorld(true, false);
            SessionManager.get().getPlayerHistory(player.getName()).remember(new HistoryObject(world, selection));
        }
        boolean result = modifyWorld(false);
        return result;
    }
}
