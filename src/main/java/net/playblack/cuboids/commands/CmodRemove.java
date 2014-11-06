package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Remove a new Cuboid
 *
 * @author Chris
 */
public class CmodRemove extends CBaseCommand {

    public CmodRemove() {
        super("Remove a new Cuboid: " + ColorManager.Yellow + "/cmod remove <area>", 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().removeCuboid(player, command[1]);
    }
}
