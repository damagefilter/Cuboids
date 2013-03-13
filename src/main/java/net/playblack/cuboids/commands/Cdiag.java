package net.playblack.cuboids.commands;

import java.util.ArrayList;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Cuboid Diagnostic tool. Shows list of all recorded areas at selected point 1
 * 
 * @author Chris
 * 
 */
public class Cdiag extends CBaseCommand {

    public Cdiag() {
        super("Diagnostics: " + ColorManager.Yellow + "/cdiag", 1);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        if (player.hasPermission("cIgnoreRestrictions")) {
            ArrayList<Region> nodes = RegionManager.get().getCuboidsContaining(player.getLocation(), player.getWorld().getName(), player.getWorld().getDimension());
            player.sendMessage(ColorManager.LightGreen + "Cuboids containnig your current location:");
            for (Region cube : nodes) {
                player.sendMessage(ColorManager.Yellow + "Name: " + ColorManager.LightGray + cube.getName());
                
                player.sendMessage(ColorManager.Yellow + "Parent: " + ColorManager.LightGray + cube.getParent().getName());
                player.sendMessage(ColorManager.DarkPurple + "------------------------------------------------");
            }
        }
    }
}
