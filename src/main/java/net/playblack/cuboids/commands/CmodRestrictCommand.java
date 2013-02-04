package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Restrict command in a cuboid
 * 
 * @author Chris
 * 
 */
public class CmodRestrictCommand extends CBaseCommand {

    public CmodRestrictCommand() {
        super("Restrict a command in your area: " + ColorManager.Yellow
                + "/cmod <area> restrictcommand <command,command ...>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().restrictCommand(player, command,
                command[1]);
    }
}
