package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.Permissions;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Load cuboid areas edges into player selection
 *
 * @author Chris
 */
public class CmodLoadPoints extends CBaseCommand {

    public CmodLoadPoints() {
        super("Load areas points into your selection:" + ColorManager.Yellow + " /cmod loadpoints <area>", 2);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        Region cube = RegionManager.get().getRegionByName(command[1], player.getWorld().getName());
        if (!player.hasPermission(Permissions.ADMIN)) {
            if (!player.hasPermission(Permissions.REGION$META)) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }

        CuboidSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
        selection.setOrigin(cube.getOrigin());
        selection.setOffset(cube.getOffset());
        MessageSystem.successMessage(player, "pointsLoaded");
    }
}
