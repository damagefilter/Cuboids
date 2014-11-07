package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Rename a Cuboid
 *
 * @author Chris
 */
public class CmodRename extends CBaseCommand {

    public CmodRename() {
        super("Rename a Cuboid: " + ColorManager.Yellow + "/cmod rename <old area> <new area name>", 3);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }

        Region node = RegionManager.get().getRegionByName(command[1], player.getWorld().getName(), player.getWorld().getType().getId());

        if (!player.hasPermission("cuboids.super.admin")) {
            if (!(node.playerIsOwner(player.getName()) || player.hasPermission("cuboids.super.areamod"))) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }

        RegionManager.get().removeRegion(node);
        node.setName(command[3]);
        RegionManager.get().addRegion(node);
        MessageSystem.successMessage(player, "cuboidRenamed");
    }
}
