package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Print cuboid information
 *
 * @author Chris
 *
 */
public class CmodInfo extends CBaseCommand {

    public CmodInfo() {
        super("Explain a Cuboid:" + ColorManager.Yellow + " /cmod info <area>", 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().explainRegion(player, command[1]);
    }
}
