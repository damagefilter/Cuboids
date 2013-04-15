package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.ToolBox;

/**
 * List all cuboids in the recent world
 *
 * @author Chris
 *
 */
public class CmodList extends CBaseCommand {

    public CmodList() {
        super("List areas in the recent world:" + ColorManager.Yellow + " /cmod list [page]", 1, 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        int page = 1;
        if (command.length == 3) {
            page = ToolBox.parseInt(command[1]);
            if (page < 1) {
                page = 1;
            }
        }
        CuboidInterface.get().displayCuboidList(player, page);
    }
}
