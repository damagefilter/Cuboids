package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

import java.util.Arrays;

/**
 * Disallow entity in a cuboid
 *
 * @author Chris
 */
public class CmodDisallowEntity extends CBaseCommand {

    public CmodDisallowEntity() {
        super("Disallow an entity in cuboid:" + ColorManager.Yellow + " /cmod disallow <area> <player g:group>", 3);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().disallowEntity(player, Arrays.copyOfRange(command, 1, command.length));
    }
}
