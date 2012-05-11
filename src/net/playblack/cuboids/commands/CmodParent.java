package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Set the parent of a cuboid
 * @author Chris
 *
 */
public class CmodParent extends CBaseCommand {

    public CmodParent() {
        super("Set cuboid parent:"+ColorManager.Yellow+" /cmod <area> parent <parent_name>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
       CuboidInterface.getInstance().setParent(player, command[1], command[3]);
    }
}
