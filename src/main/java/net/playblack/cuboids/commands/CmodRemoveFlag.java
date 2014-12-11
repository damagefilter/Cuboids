package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.Permissions;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Backup an area
 *
 * @author Chris
 */
public class CmodRemoveFlag extends CBaseCommand {

    public CmodRemoveFlag() {
        super("Set a flag in the given area: " + ColorManager.Yellow + "/cmod flags remove [area] <flag>", 2);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }

        if (command.length == 2) {
            Config.get().removeGlobalProperty(command[1]);
            MessageSystem.successMessage(player, "globalFlagRemoved");
            return;
        }

        Region node = RegionManager.get().getRegionByName(command[1], player.getWorld().getName());
        if (node == null) {
            MessageSystem.failMessage(player, "noCuboidFound");
            return;
        }

        if (node.playerIsOwner(player.getName()) || player.hasPermission(Permissions.ADMIN) || player.hasPermission(Permissions.REGION$EDIT$ANY)) {
            if (node.removeProperty(command[2])) {
                MessageSystem.successMessage(player, "regionFlagRemoved");
            }
            else {
                MessageSystem.failMessage(player, "invalidRegionFlagValue");
            }
        }
        else {
            MessageSystem.failMessage(player, "playerNotOwner");
        }
    }
}
