package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.help.CommandHelper;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.ToolBox;

/**
 * Help
 * @author Chris
 *
 */
public class Chelp extends CBaseCommand {

    public Chelp() {
        super("Display this help:"+ColorManager.Yellow+" /chelp <term term term ...>[page]", 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }

        int page = ToolBox.parseInt(command[command.length-1]);
        if(page == -1) {
            page = 1;
        }
        CommandHelper.get().displayHelp(player, page, command);
    }
}
