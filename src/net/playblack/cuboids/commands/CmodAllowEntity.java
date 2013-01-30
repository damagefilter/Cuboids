package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Allow entity in a cuboid
 * 
 * @author Chris
 * 
 */
public class CmodAllowEntity extends CBaseCommand {

    public CmodAllowEntity() {
        super("Allow an entity in cuboid:" + ColorManager.Yellow
                + " /cmod <area> allow <player g:group o:owner>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().allowEntity(player, command);
    }
}
