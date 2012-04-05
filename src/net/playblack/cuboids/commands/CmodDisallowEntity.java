package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * Disallow entity in a cuboid
 * @author Chris
 *
 */
public class CmodDisallowEntity extends CBaseCommand {

    public CmodDisallowEntity() {
        super("Disallow an entity in cuboid: /cmod <area> allow <player g:group>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
       CuboidInterface.getInstance().disallowEntity(player, command);
    }
}
