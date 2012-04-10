package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.SelectionManager;

/**
 * Expand cuboid selection
 * @author Chris
 *
 */
public class Cexpand extends CBaseCommand {

    public Cexpand() {
        super("Expand the current cuboid selection: /cexpand", 1);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        MessageSystem ms = MessageSystem.getInstance();
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!player.hasPermission("cselect")) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }
        
        SelectionManager.getInstance().getPlayerSelection(player.getName()).expandVert();
        ms.successMessage(player, "selectionExpanded");
    }
}
