package net.playblack.cuboids.commands;

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
        super("Allow an item in cuboid: " + ColorManager.Yellow + "/cmod <area> restrictitem <item name or item id>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().disallowItem(player, command);
    }
}
