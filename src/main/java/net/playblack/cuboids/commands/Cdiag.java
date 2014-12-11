package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;

import java.util.ArrayList;

/**
 * Cuboid Diagnostic tool. Shows list of all recorded areas at selected point 1
 *
 * @author Chris
 */
public class Cdiag extends CBaseCommand {

    public Cdiag() {
        super("Diagnostics: " + ColorManager.Yellow + "/cdiag", 1);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }

        ArrayList<Region> nodes = RegionManager.get().getCuboidsContaining(player.getLocation());

        Player p = CServer.getServer().getPlayer(player.getName());
        MessageSystem.translateMessage(player, ColorManager.LightGreen, "cuboidContainingYou");
        for (Region cube : nodes) {
            p.sendMessage(ColorManager.Yellow + "Name: " + ColorManager.LightGray + cube.getName() + " : " + (cube.hasParent() ? cube.getParent().getName() : "Global"));
        }
        p.sendMessage(ColorManager.DarkPurple + "------------------------------------------------");
        p.sendMessage(ColorManager.Gold + "I think you are here: " + (p.getCurrentRegion() != null ? p.getCurrentRegion().getName() : "Global"));
    }
}
