package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Move a Cuboid
 * @author Chris
 *
 */
public class CmodMove extends CBaseCommand {

    public CmodMove() {
        super("Move a Cuboid: "+ColorManager.Yellow+"/cmod <area> move/resize", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.getInstance().resize(player, command[1]);
    }
}
