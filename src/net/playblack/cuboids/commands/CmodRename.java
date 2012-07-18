package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidNode;
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

        CuboidNode node = RegionManager.getInstance().getCuboidNodeByName(
                command[1], player.getWorld().getName(),
                player.getWorld().getDimension());

        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!(node.getCuboid().playerIsOwner(player.getName()) || player
                    .hasPermission("cAreaMod"))) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }

        RegionManager.getInstance().removeCuboid(node.getCuboid(), true);
        node.getCuboid().setName(command[3]);
        node.updateChilds();
        RegionManager.getInstance().addNode(node);
        ms.successMessage(player, "cuboidRenamed");
    }
}
