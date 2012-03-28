package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * Print cuboid information
 * @author Chris
 *
 */
public class CmodInfo extends BaseCommand {

    public CmodInfo() {
        super("Explain a Cuboid: /cmod <area> info", 3);
    }
    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
       CuboidInterface.getInstance().explainCuboid(player, command[1]);
    }
}
