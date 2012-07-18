package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidE;
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
        CuboidE targetCube = RegionManager.getInstance().getCuboidByName(
                command[1], player.getWorld().getName(),
                player.getWorld().getDimension());
        if (targetCube == null) {
            MessageSystem.getInstance().failMessage(player,
                    "cuboidNotFoundOnCommand");
            return;
        }
        Vector target = Vector.getCenterPoint(targetCube.getFirstPoint(),
                targetCube.getSecondPoint());

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
            MessageSystem.getInstance().successMessage(player,
                    "playerTeleported");
        } else {
            MessageSystem.getInstance().failMessage(player, "permissionDenied");
            return;
        }
    }
}
