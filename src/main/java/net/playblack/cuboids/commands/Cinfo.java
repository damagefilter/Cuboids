package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Print cuboid information
 *
 * @author Chris
 */
public class Cinfo extends CBaseCommand {

    public Cinfo() {
        super("Explain a Cuboid: " + ColorManager.Yellow + "/cinfo", 1);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        Location l = player.getLocation();
        CuboidInterface.get().explainRegion(player, l, false);
    }
}
