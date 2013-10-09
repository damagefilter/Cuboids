package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;

/**
 * Set ceiling level of current selection
 *
 * @author Chris
 */
public class Cceiling extends CBaseCommand {

    public Cceiling() {
        super("Set ceiling level, use -r: player pos + height: " + ColorManager.Yellow + "/cceiling <height> [-r]", 2, 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }

        int height = ToolBox.parseInt(command[1]);
        if (height == -1) {
            MessageSystem.failMessage(player, "negativeNumber");
        }
        CuboidSelection sel = SelectionManager.get()
                .getPlayerSelection(player.getName());
        sel.sortEdges(false);
        Vector origin = sel.getOrigin();
        if ((command.length == 3) && command[2].equalsIgnoreCase("-r")) {
            origin.setY(player.getY() + height);
        }
        else {
            origin.setY(height);
        }
        sel.setOrigin(origin);
        MessageSystem.successMessage(player, "ceilingSet");
    }
}
