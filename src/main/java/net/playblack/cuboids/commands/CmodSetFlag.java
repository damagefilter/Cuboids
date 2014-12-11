package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.Permissions;
import net.playblack.cuboids.RegionFlagRegister;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Backup an area
 *
 * @author Chris
 */
public class CmodSetFlag extends CBaseCommand {

    public CmodSetFlag() {
        super("Set a flag in the given area: " + ColorManager.Yellow + "/cmod flag set [area] <flag> <ALLOW|DENY|DEFAULT>", 3, 4);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        if (!player.hasPermission(Permissions.ADMIN)) {
            if (!player.hasPermission(Permissions.REGION$FLAGS + command[command.length - 2])) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }

        Region node = RegionManager.get().getRegionByName(command[command.length - 3], player.getWorld().getFqName());
        String flagName = command[command.length - 2];
        Region.Status status = Region.Status.fromString(command[command.length - 1]);

        if (RegionFlagRegister.isFlagValid(flagName)) {
            if (node == null && player.hasPermission(Permissions.ADMIN)) {
                Config.get().setGlobalProperty(flagName, status);
                MessageSystem.successMessage(player, "globalFlagSet");
            }
            else if (node != null) {
                if (node.playerIsOwner(player.getName()) || player.hasPermission(Permissions.REGION$FLAGS) || player.hasPermission(Permissions.ADMIN)) {
                    if (node.setProperty(flagName, status)) {
                        MessageSystem.successMessage(player, "regionFlagSet");
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

    }
}
