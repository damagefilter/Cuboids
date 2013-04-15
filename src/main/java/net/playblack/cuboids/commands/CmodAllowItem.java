package net.playblack.cuboids.commands;

import java.util.Arrays;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Allow item in a cuboid
 *
 * @author Chris
 *
 */
public class CmodAllowItem extends CBaseCommand {

    public CmodAllowItem() {
        super("Allow an item in cuboid:" + ColorManager.Yellow + " /cmod allowitem <area> <item name or item id>", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().allowItem(player, Arrays.copyOfRange(command, 2, command.length));
    }
}
