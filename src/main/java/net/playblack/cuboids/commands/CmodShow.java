package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Show a Cuboid
 *
 * @author Ekranos
 */
public class CmodShow extends CBaseCommand {

    public CmodShow() {
        super("Show a Cuboid: " + ColorManager.Yellow + "/cmod show area", 2);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().showCommand(player, command[1]);
    }
}
