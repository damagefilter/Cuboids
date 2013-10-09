package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Expand cuboid selection
 *
 * @author Chris
 */
public class Cexpand extends CBaseCommand {

    public Cexpand() {
        super("Expand the current cuboid selection: " + ColorManager.Yellow + "/cexpand", 1);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        SelectionManager.get().getPlayerSelection(player.getName()).expandVert();
        MessageSystem.successMessage(player, "selectionExpanded");
    }
}
