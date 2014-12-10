package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Protect an area
 *
 * @author Chris
 */
public class Protect extends CBaseCommand {

    public Protect() {
        super("Protect an area:" + ColorManager.Yellow + " /protect <player/group..><area name>", 3);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        // Check for the proper permissions
        if (!(player.hasPermission("ccreate"))) {
            MessageSystem.failMessage(player, "permissionDenied");
            return;
        }
        CuboidSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
        if (!selection.isComplete()) {
            MessageSystem.failMessage(player, "selectionIncomplete");
            return;
        }
        selection.setWorld(player.getWorld().getName());
        Region cube = selection.toRegion(player, command);
        cube.setProperty("protection", Status.ALLOW); // force protection if is allowed
        if (CuboidInterface.get().addCuboid(cube)) {
            MessageSystem.successMessage(player, "cuboidCreated");
        }
        else {
            MessageSystem.failMessage(player, "cuboidNotCreated");
        }
    }
}
