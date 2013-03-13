package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Load cuboid areas edges into player selection
 * 
 * @author Chris
 * 
 */
public class CmodLoad extends CBaseCommand {

    public CmodLoad() {
        super("Load areas points into your selection:" + ColorManager.Yellow
                + " /cmod <area> loadpoints", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        Region cube = RegionManager.get().getCuboidByName(command[1],
                player.getWorld().getName(), player.getWorld().getDimension());
        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!player.hasPermission("cAreaMod")) {
                if (!player.hasPermission("cselect")) {
                    MessageSystem.getInstance().failMessage(player,
                            "permissionDenied");
                    return;
                }
            }
        }
        CuboidSelection selection = SelectionManager.getInstance()
                .getPlayerSelection(player.getName());
        selection.setOrigin(cube.getOrigin());
        selection.setOffset(cube.getOffset());
        MessageSystem.getInstance().successMessage(player, "pointsLoaded");
    }
}
