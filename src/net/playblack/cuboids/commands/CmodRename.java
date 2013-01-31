package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Rename a Cuboid
 * 
 * @author Chris
 * 
 */
public class CmodRename extends CBaseCommand {

    public CmodRename() {
        super("Rename a Cuboid: " + ColorManager.Yellow
                + "/cmod <old area> rename <new area name>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        MessageSystem ms = MessageSystem.getInstance();

        Region node = RegionManager.get().getCuboidByName(
                command[1], player.getWorld().getName(),
                player.getWorld().getDimension());

        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!(node.playerIsOwner(player.getName()) || player
                    .hasPermission("cAreaMod"))) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }

        RegionManager.get().removeRegion(node, true);
        node.setName(command[3]);
        RegionManager.get().addRegion(node);
        ms.successMessage(player, "cuboidRenamed");
    }
}
