package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.Permissions;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;

import java.util.Arrays;

/**
 * High-Protect an area
 *
 * @author Chris
 */
public class Highprotect extends CBaseCommand {

    public Highprotect() {
        super("High-Protect an area:" + ColorManager.Yellow + " /highprotect <player/group..><area name>", 3);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        // Check for the proper permissions
        if (!player.hasPermission(Permissions.ADMIN)) {
            if (!player.hasPermission(Permissions.REGION$CREATE)) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }

        CuboidSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
        if (!selection.isComplete()) {
            MessageSystem.failMessage(player, "selectionIncomplete");
            return;
        }
        selection.expandVert();
        selection.setWorld(player.getWorld().getName());
        Region cube = selection.toRegion(player, Arrays.copyOfRange(command, 1, command.length));
        cube.setProperty("protection", Status.ALLOW);
        if (CuboidInterface.get().addCuboid(cube)) {
            MessageSystem.successMessage(player, "cuboidCreated");
        }
        else {
            MessageSystem.failMessage(player, "cuboidNotCreated");
        }
    }
}
