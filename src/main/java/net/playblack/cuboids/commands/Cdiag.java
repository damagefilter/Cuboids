package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.SessionManager;
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

        MessageSystem.translateMessage(player, ColorManager.LightGreen, "cuboidContainingYou");
        for (Region cube : nodes) {
            player.message(ColorManager.Yellow + "Name: " + ColorManager.LightGray + cube.getName() + " : " + (cube.hasParent() ? cube.getParent().getName() : "Global"));
        }
        player.message(ColorManager.DarkPurple + "--------------------------------------------");
        Region r = SessionManager.get().getRegionForPlayer(player.getName());
        player.message(ColorManager.Gold + "I think you are here: " + (r != null ? r.getName() : "Global"));
    }
}
