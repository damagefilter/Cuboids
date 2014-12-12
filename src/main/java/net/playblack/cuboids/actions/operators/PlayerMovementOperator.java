package net.playblack.cuboids.actions.operators;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.Permissions;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;

public class PlayerMovementOperator {

    private boolean canMoveTo(Player player, Location location) {
        Region cube = RegionManager.get().getActiveRegion(location, false);
        if (player.hasPermission(Permissions.ADMIN) || cube.playerIsAllowed(player, player.getPlayerGroups())) {
            return true;
        }
        return cube.getProperty("enter-cuboid") != Region.Status.DENY;
    }
    public void onPlayerMove(Player player, Location from, Location to) {
        if (!canMoveTo(player, to)) {
            player.teleportTo(from);
        }
        CuboidInterface.get().handleRegionsForPlayer(player, from, to);
    }

    /**
     * Returns true if the TP should not happen
     * @param player
     * @param from
     * @param to
     * @return
     */
    public boolean onPlayerTeleport(Player player, Location from, Location to) {
        return !canMoveTo(player, to);
    }
}
