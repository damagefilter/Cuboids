package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * Restrict command in a cuboid
 * @author Chris
 *
 */
public class CmodRestrictCommand extends CBaseCommand {

    public CmodRestrictCommand() {
        super("Remove a new Cuboid: /cmod <area> allowcommand <command,command ...>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
       CuboidInterface.getInstance().restrictCommand(player, command, command[1]);
    }
}
