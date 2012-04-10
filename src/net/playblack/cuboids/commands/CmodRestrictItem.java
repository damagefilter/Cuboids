package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * Disallow item in a cuboid
 * @author Chris
 *
 */
public class CmodRestrictItem extends CBaseCommand {

    public CmodRestrictItem() {
        super("Allow an item in cuboid: /cmod <area> restrictitem <item name or item id>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
       CuboidInterface.getInstance().disallowItem(player, command);
    }
}
