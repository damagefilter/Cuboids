package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Load a single cuboid
 *
 * @author Chris
 */
public class CmodLoad extends CBaseCommand {
    public CmodLoad() {
        super("Load a cuboid: " + ColorManager.Yellow + "/cmod load <area>", 2);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().loadCuboid(player, command[1]);
    }
}
