package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

import java.util.Arrays;

/**
 * Disallow item in a cuboid
 *
 * @author Chris
 */
public class CmodRestrictItem extends CBaseCommand {

    public CmodRestrictItem() {
        super("Allow an item in cuboid: " + ColorManager.Yellow + "/cmod restrictitem <area> <item name or item id>", 3);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().disallowItem(player, Arrays.copyOfRange(command, 1, command.length));
    }
}
