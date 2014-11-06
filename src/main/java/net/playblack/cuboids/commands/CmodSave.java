package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Save a single or all cuboids
 *
 * @author Chris
 */
public class CmodSave extends CBaseCommand {


    public CmodSave() {
        super("Save cuboid(s): " + ColorManager.Yellow + "/cmod save [area]", 1, 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        if (command.length == 1) {
            CuboidInterface.get().saveAll(player);
        }
        else {
            CuboidInterface.get().saveCuboid(player, command[1]);
        }
    }
}
