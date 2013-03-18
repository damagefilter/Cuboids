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
        super("Set a flag in the given area: " + ColorManager.Yellow + "/cmod <area> flag set <flag> <ALLOW|DENY|DEFAULT>", 6);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!player.hasPermission(command[4])) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }
        
        if(command[1].equalsIgnoreCase("global")) {
            Config.get().setGlobalProperty(command[4], Region.Status.fromString(command[5]));
            MessageSystem.successMessage(player, "globalFlagSet");
            return;
        }
        
        Region node = RegionManager.get().getRegionByName(command[1], player.getWorld().getName(), player.getWorld().getDimension());
        if(node == null) {
            MessageSystem.failMessage(player, "noCuboidFound");
            return;
        }
        
        if (node.playerIsOwner(player.getName()) || player.hasPermission("cAreaMod") || player.hasPermission("cIgnoreRestrictions")) {
            if(node.setProperty(command[4], Region.Status.fromString(command[5]))) {
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
