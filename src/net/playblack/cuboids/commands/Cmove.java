package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.blockoperators.GenericGenerator;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;


/**
 * Move the contents of a selection, leaving empty space.
 * @author Chris
 *
 */
public class Cmove extends BaseCommand {

    public Cmove() {
        super("Move the contents of a selection: /cmove <distance> <NORTH/EAST/SOUTH/WEST/UP/DOWN>", 1);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        MessageSystem ms = MessageSystem.getInstance();
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!player.hasPermission("cWorldMod")) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }
        CuboidSelection sel = SelectionManager.getInstance().getPlayerSelection(player.getName());
        
    }
}
