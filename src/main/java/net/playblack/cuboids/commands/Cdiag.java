package net.playblack.cuboids.commands;

import java.util.ArrayList;

import net.playblack.cuboids.MessageSystem;
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
        ArrayList<Region> nodes = RegionManager.get().getCuboidsContaining(player.getLocation(), player.getWorld().getName(), player.getWorld().getDimension());
        MessageSystem.translateMessage(player, ColorManager.LightGreen, "cuboidContainingYou");
        for (Region cube : nodes) {
            player.sendMessage(ColorManager.Yellow + "Name: " + ColorManager.LightGray + cube.getName() + " : " + (cube.hasParent()? cube.getParent().getName() : "Global"));
        }
        player.sendMessage(ColorManager.DarkPurple + "------------------------------------------------");
        player.sendMessage(ColorManager.Gold + "I think you are here: " + (player.getCurrentRegion() != null ? player.getCurrentRegion().getName() : "Global"));
    }
}
