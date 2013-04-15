package net.playblack.cuboids.commands;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Backup an area
 *
 * @author Chris
 *
 */
public class CmodRemoveFlag extends CBaseCommand {

    public CmodRemoveFlag() {
        super("Set a flag in the given area: " + ColorManager.Yellow + "/cmod flags remove [area] <flag>", 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }

        if(command.length == 2) {
            Config.get().removeGlobalProperty(command[1]);
            MessageSystem.successMessage(player, "globalFlagRemoved");
            return;
        }

        Region node = RegionManager.get().getRegionByName(command[1], player.getWorld().getName(), player.getWorld().getDimension());
        if(node == null) {
            MessageSystem.failMessage(player, "noCuboidFound");
            return;
        }

        if (node.playerIsOwner(player.getName()) || player.hasPermission("cAreaMod") || player.hasPermission("cIgnoreRestrictions")) {
            if(node.removeProperty(command[2])) {
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
