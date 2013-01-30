package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Load a single cuboid
 * 
 * @author Chris
 * 
 */
public class Cload extends CBaseCommand {
    public Cload() {
        super("Load a cuboid: " + ColorManager.Yellow + "/cload <area>", 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().loadCuboid(player, command[1]);
    }
}
