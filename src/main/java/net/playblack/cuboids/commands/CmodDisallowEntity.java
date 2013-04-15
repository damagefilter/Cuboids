package net.playblack.cuboids.commands;

import java.util.Arrays;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Disallow entity in a cuboid
 *
 * @author Chris
 *
 */
public class CmodDisallowEntity extends CBaseCommand {

    public CmodDisallowEntity() {
        super("Disallow an entity in cuboid:" + ColorManager.Yellow + " /cmod disallow <area> <player g:group>", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().disallowEntity(player, Arrays.copyOfRange(command, 2, command.length));
    }
}
