package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Vector;

/**
 * Set the parent of a cuboid
 * 
 * @author Chris
 * 
 */
public class CmodTpTo extends CBaseCommand {

    public CmodTpTo() {
        super(
                "Set cuboid parent: " + ColorManager.Yellow
                        + "/cmod <area> tpto", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        Region targetCube = RegionManager.get().getCuboidByName(
                command[1], player.getWorld().getName(),
                player.getWorld().getDimension());
        if (targetCube == null) {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            return;
        }
        Vector target = Vector.getCenterPoint(targetCube.getOrigin(),
                targetCube.getOffset());

        if (player.hasPermission("cIgnoreRestrictions")
                || (player.hasPermission("cteleport") && targetCube
                        .playerIsAllowed(player.getName(), player.getGroups()))) {
            if (!player.getWorld().isChunkLoaded(target)) {
                player.getWorld().loadChunk(target);
            }
            int y = player.getWorld().getHighestBlock(target.getBlockX(),
                    target.getBlockZ());
            target.setY(y);
            player.teleportTo(target);
            MessageSystem.successMessage(player, "playerTeleported");
        } else {
            MessageSystem.failMessage(player, "permissionDenied");
            return;
        }
    }
}
