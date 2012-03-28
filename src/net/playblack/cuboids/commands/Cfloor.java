package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;


/**
 * Set floor level of current selection
 * @author Chris
 *
 */
public class Cfloor extends BaseCommand {

    public Cfloor() {
        super("Set floorlevel, use -r: player pos - height: /cfloor <height> [-r]", 2,3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        MessageSystem ms = MessageSystem.getInstance();
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!(player.hasPermission("cselect") && player.hasPermission("ccreate"))) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }

        int height = ToolBox.parseInt(command[1]);
        if(height == -1) {
            ms.failMessage(player, "negativeNumber");
        }
        CuboidSelection sel = SelectionManager.getInstance().getPlayerSelection(player.getName());
        sel.sortEdges(false);
        Vector origin = sel.getOffset();
        if((command.length == 3) && command[2].equalsIgnoreCase("-r")) {
            origin.setY(player.getY()-height);
        }
        else {
            origin.setY(height);
        }
        sel.setOffset(origin);
        ms.successMessage(player, "floorSet");
    }
}
