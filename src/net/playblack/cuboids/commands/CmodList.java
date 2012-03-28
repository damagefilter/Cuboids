package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ToolBox;

/**
 * List all cuboids in the recent world
 * @author Chris
 *
 */
public class CmodList extends BaseCommand {

    public CmodList() {
        super("List areas in the recent world: /cmod list [page]", 2,3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        int page = 1;
        if(command.length == 3) {
            page = ToolBox.parseInt(command[2]);
            if(page < 1) {
                page = 1;
            }
        }
        CuboidInterface.getInstance().displayCuboidList(player, page);
    }
}
