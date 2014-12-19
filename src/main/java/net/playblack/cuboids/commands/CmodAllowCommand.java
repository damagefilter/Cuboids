package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;
import net.visualillusionsent.utils.StringUtils;

import java.util.Arrays;

/**
 * Allow command in a cuboid
 *
 * @author Chris
 */
public class CmodAllowCommand extends CBaseCommand {

    public CmodAllowCommand() {
        super("Allow a command in your area:" + ColorManager.Yellow + " /cmod allowcommand <area> <command,command ...>", 3);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().allowCommand(player, Arrays.copyOfRange(command, 2, command.length), command[1]);
    }
}
