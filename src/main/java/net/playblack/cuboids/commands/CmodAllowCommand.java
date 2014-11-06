package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;
import net.visualillusionsent.utils.StringUtils;

/**
 * Allow command in a cuboid
 *
 * @author Chris
 */
public class CmodAllowCommand extends CBaseCommand {

    public CmodAllowCommand() {
        super("Allow a command in your area:" + ColorManager.Yellow + " /cmod <area> allowcommand <command,command ...>", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().allowCommand(player, command, StringUtils.joinString(command, " ", 2));
    }
}
