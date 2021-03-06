package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.Permissions;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Vector;

/**
 * Set the parent of a cuboid
 *
 * @author Chris
 */
public class CmodTpTo extends CBaseCommand {

    public CmodTpTo() {
        super("Set cuboid parent: " + ColorManager.Yellow + "/cmod tpto <area>", 2);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        Region targetCube = RegionManager.get().getRegionByName(command[1], player.getWorld().getFqName());
        if (targetCube == null) {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            MessageSystem.customFailMessage(player, "Region name: " + command[1]);
            return;
        }
        Vector3D target = Vector.getCenterPoint(targetCube.getOrigin(), targetCube.getOffset());

        if (player.hasPermission(Permissions.ADMIN) || (player.hasPermission(Permissions.REGION$TELEPORT) && targetCube.playerIsAllowed(player, player.getPlayerGroups()))) {
            if (!player.getWorld().isChunkLoaded(target.getBlockX(), target.getBlockZ())) {
                player.getWorld().loadChunk(target.getBlockX(), target.getBlockZ());
            }
            int y = player.getWorld().getHighestBlockAt(target.getBlockX(), target.getBlockZ());
            target.setY(y);
            player.teleportTo(target);
            MessageSystem.successMessage(player, "playerTeleported");
        }
        else {
            MessageSystem.failMessage(player, "permissionDenied");
        }
    }
}
