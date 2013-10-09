package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

import java.util.Arrays;

/**
 * Restrict command in a cuboid
 *
 * @author Chris
 */
public class CmodRestrictCommand extends CBaseCommand {

    public CmodRestrictCommand() {
        super("Restrict a command in your area: " + ColorManager.Yellow + "/cmod restrictcommand <area> <command,command ...>", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().restrictCommand(player, Arrays.copyOfRange(command, 2, command.length), command[1]);
    }
}
