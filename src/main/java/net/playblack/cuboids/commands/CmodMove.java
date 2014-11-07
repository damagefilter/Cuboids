package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Move a Cuboid
 *
 * @author Chris
 */
public class CmodMove extends CBaseCommand {

    public CmodMove() {
        super("Move a Cuboid: " + ColorManager.Yellow + "/cmod <area> move/resize", 3);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().resize(player, command[1]);
    }
}
