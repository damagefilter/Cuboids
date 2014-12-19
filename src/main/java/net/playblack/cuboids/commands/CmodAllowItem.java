package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

import java.util.Arrays;

/**
 * Allow item in a cuboid
 *
 * @author Chris
 */
public class CmodAllowItem extends CBaseCommand {

    public CmodAllowItem() {
        super("Allow an item in cuboid:" + ColorManager.Yellow + " /cmod allowitem <area> <item name or item id>", 3);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().allowItem(player, Arrays.copyOfRange(command, 1, command.length));
    }
}
