package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * Allow entity in a cuboid
 * @author Chris
 *
 */
public class CmodAllowEntity extends BaseCommand {

    public CmodAllowEntity() {
        super("Allow an entity in cuboid: /cmod <area> allow <player g:group o:owner>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
       CuboidInterface.getInstance().allowEntity(player, command);
    }
}
