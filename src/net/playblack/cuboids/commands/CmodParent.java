package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * Set the parent of a cuboid
 * @author Chris
 *
 */
public class CmodParent extends BaseCommand {

    public CmodParent() {
        super("Set cuboid parent: /cmod <area> parent <parent_name>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
       CuboidInterface.getInstance().setParent(player, command[1], command[3]);
    }
}
