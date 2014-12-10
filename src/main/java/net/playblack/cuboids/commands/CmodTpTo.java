package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
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
        Region targetCube = RegionManager.get().getRegionByName(command[1], player.getWorld().getName());
        if (targetCube == null) {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            return;
        }
        Vector3D target = Vector.getCenterPoint(targetCube.getOrigin(), targetCube.getOffset());

        CPlayer p = CServer.getServer().getPlayer(player.getName());
        if (player.hasPermission("cuboids.super.admin") || (player.hasPermission("cteleport") && targetCube.playerIsAllowed(player.getName(), p.getGroups()))) {
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
