package net.playblack.cuboids.commands;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;

/**
 * Protect an area
 * @author Chris
 *
 */
public class Protect extends BaseCommand {

    public Protect() {
        super("Protect an area: /protect <player/group..><area name>", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        //Check for the proper permissions
        MessageSystem ms = MessageSystem.getInstance();
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!(player.hasPermission("ccreate")) && player.hasPermission("cprotection")) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }
        CuboidSelection selection = SelectionManager.getInstance().getPlayerSelection(player.getName());
        if(!selection.isComplete()) {
            ms.failMessage(player, "selectionIncomplete");
            return;
        }
        selection.setWorld(player.getWorld().getName());
        CuboidE cube = selection.toCuboid(player, command);
        if(Config.getInstance().isAllowProtection()) {
            cube.setProtection(true); //force protection if is allowed regardless of other default settings
        }
        else {
            ms.notification(player, "FYI: The protection option is disabled. The Cuboid will still be created!");
        }
        if(CuboidInterface.getInstance().addCuboid(cube)) {
            ms.successMessage(player, "cuboidCreated");
        }
        else {
            ms.failMessage(player, "cuboidNotCreated");
        }
    }
}
