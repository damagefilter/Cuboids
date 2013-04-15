package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Set the parent of a cuboid
 *
 * @author Chris
 *
 */
public class CmodParent extends CBaseCommand {

    public CmodParent() {
        super("Set cuboid parent:" + ColorManager.Yellow + " /cmod parent <area> <parent_name>", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().setParent(player, command[1], command[2]);
    }
}
