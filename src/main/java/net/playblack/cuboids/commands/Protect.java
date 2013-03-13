package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Protect an area
 * 
 * @author Chris
 * 
 */
public class Protect extends CBaseCommand {

    public Protect() {
        super("Protect an area:" + ColorManager.Yellow
                + " /protect <player/group..><area name>", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        // Check for the proper permissions
        MessageSystem ms = MessageSystem.getInstance();
        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!(player.hasPermission("ccreate"))
                    && player.hasPermission("cprotection")) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }
        CuboidSelection selection = SelectionManager.getInstance()
                .getPlayerSelection(player.getName());
        if (!selection.isComplete()) {
            ms.failMessage(player, "selectionIncomplete");
            return;
        }
        selection.setWorld(player.getWorld().getName());
        Region cube = selection.toCuboid(player, command);
        cube.setDimension(player.getWorld().getDimension());
        cube.setProperty("protection", Status.ALLOW); // force protection if is allowed
        if (CuboidInterface.get().addCuboid(cube)) {
            ms.successMessage(player, "cuboidCreated");
        } else {
//            ms.failMessage(player, "selectionIncomplete");
            ms.failMessage(player, "cuboidNotCreated");
        }
    }
}
