package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.Permissions;
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

        Region node = RegionManager.get().getRegionByName(command[1], player.getWorld().getFqName());
        if (!player.hasPermission(Permissions.ADMIN)) {
            if (!(node.playerIsOwner(player.getName()) || player.hasPermission(Permissions.REGION$EDIT$ANY))) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }
        node.setName(command[3]);
        MessageSystem.successMessage(player, "cuboidRenamed");
    }
}
