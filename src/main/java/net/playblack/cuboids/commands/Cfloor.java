package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;

/**
 * Set floor level of current selection
 *
 * @author Chris
 */
public class Cfloor extends CBaseCommand {

    public Cfloor() {
        super("Set floorlevel, use -r: player pos - height:" + ColorManager.Yellow + " /cfloor <height> [-r]", 2, 3);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }

        int height = ToolBox.parseInt(command[1]);
        if (height == -1) {
            MessageSystem.failMessage(player, "negativeNumber");
        }
        CuboidSelection sel = SelectionManager.get().getPlayerSelection(player.getName());
        sel.sortEdges(false);
        Vector origin = sel.getOffset();
        if ((command.length == 3) && command[2].equalsIgnoreCase("-r")) {
            origin.setY(player.getY() - height);
        }
        else {
            origin.setY(height);
        }
        sel.setOffset(origin);
        MessageSystem.successMessage(player, "floorSet");
    }
}
