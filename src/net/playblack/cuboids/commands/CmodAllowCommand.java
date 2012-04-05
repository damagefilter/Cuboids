package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * Allow command in a cuboid
 * @author Chris
 *
 */
public class CmodAllowCommand extends CBaseCommand {

    public CmodAllowCommand() {
        super("Remove a new Cuboid: /cmod <area> restrictcommand <command,command ...>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
       CuboidInterface.getInstance().allowCommand(player, command, command[1]);
    }
}
