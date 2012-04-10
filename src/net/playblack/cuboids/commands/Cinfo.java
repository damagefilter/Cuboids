package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * Print cuboid information
 * @author Chris
 *
 */
public class Cinfo extends CBaseCommand {

    public Cinfo() {
        super("Explain a Cuboid: /cinfo", 1);
    }
    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
       CuboidInterface.getInstance().explainCuboid(player, player.getPosition());
    }
}
