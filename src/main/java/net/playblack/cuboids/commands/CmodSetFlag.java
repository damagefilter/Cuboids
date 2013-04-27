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
public class CmodSetFlag extends CBaseCommand {

    public CmodSetFlag() {
        super("Set a flag in the given area: " + ColorManager.Yellow + "/cmod flag set [area] <flag> <ALLOW|DENY|DEFAULT>", 3, 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        if (!player.hasPermission("cuboids.super.admin")) {
            if (!player.hasPermission("cuboids.flags."+command[command.length-2])) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }

        if(command[1].equalsIgnoreCase("global")) {
            Config.get().setGlobalProperty(command[4], Region.Status.fromString(command[1]));
            MessageSystem.successMessage(player, "globalFlagSet");
            return;
        }

        Region node = RegionManager.get().getRegionByName(command[command.length-3], player.getWorld().getName(), player.getWorld().getDimension());
        if(node == null) {
            node = Config.get().getGlobalSettings();
        }

        if (node.playerIsOwner(player.getName()) || player.hasPermission("cuboids.super.areamod") || player.hasPermission("cuboids.super.admin")) {
            if(node.setProperty(command[command.length-2], Region.Status.fromString(command[command.length-1]))) {
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
