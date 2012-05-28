package net.playblack.cuboids.commands;

import java.util.ArrayList;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Cuboid Diagnostic tool. Shows list of all recorded areas at selected point 1
 * @author Chris
 *
 */
public class Cdiag extends CBaseCommand {

    public Cdiag() {
        super("Diagnostics: "+ColorManager.Yellow+"/cdiag", 1);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        if(player.hasPermission("cIgnoreRestrictions")) {
            ArrayList<CuboidE> nodes = RegionManager.getInstance().getCuboidsContaining(player.getPosition(), player.getWorld().getName(), player.getWorld().getDimension());
            player.sendMessage(ColorManager.LightGreen+"Cuboids containnig your current location:");
            for(CuboidE cube : nodes) {
                player.sendMessage(ColorManager.Yellow+"Name: "+ColorManager.LightGray+cube.getName());
                player.sendMessage(ColorManager.Yellow+"Parent: "+ColorManager.LightGray+cube.getParent());
                player.sendMessage(ColorManager.DarkPurple+"------------------------------------------------");
            }
        }
    }
}
