package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * Allow item in a cuboid
 * @author Chris
 *
 */
public class CmodAllowItem extends CBaseCommand {

    public CmodAllowItem() {
        super("Allow an item in cuboid: /cmod <area> allowitem <item name or item id>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
       CuboidInterface.getInstance().allowItem(player, command);
    }
}
