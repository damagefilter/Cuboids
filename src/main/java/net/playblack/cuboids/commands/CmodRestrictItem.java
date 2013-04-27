package net.playblack.cuboids.commands;

import java.util.Arrays;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Disallow item in a cuboid
 *
 * @author Chris
 *
 */
public class CmodRestrictItem extends CBaseCommand {

    public CmodRestrictItem() {
        super("Allow an item in cuboid: " + ColorManager.Yellow + "/cmod restrictitem <area> <item name or item id>", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().disallowItem(player, Arrays.copyOfRange(command, 1, command.length));
    }
}
