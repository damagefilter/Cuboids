package net.playblack.cuboids.actions;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.CuboidNode;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Location;

public class PlayerMovementHandler {

    /**
     * Handle if a player can trespass an area or not.
     * 
     * @param player
     */
    public static void handleAreaTrespassing(CPlayer player,
            Location origin, Location target) {
        if (!CuboidInterface.getInstance().canEnter(player, target)) {
            player.teleportTo(origin);
        }
    }

    /**
     * Handle the adding and removing of players into/from cuboid areas
     * 
     * @param player
     * @param target
     * @param isTeleport
     *            True if this was called from a teleport hook
     */
    public static void handleCuboidAreas(CPlayer player, Location origin,
            Location target, boolean isTeleport) {
        CuboidInterface.getInstance().addPlayerWithin(player, target);
        CuboidInterface.getInstance()
                .removePlayerWithin(player, origin, target);
        if (isTeleport) {
            RegionManager.getInstance().removeFromAllAreas(player.getName(),
                    player.getLocation());
            CuboidNode targetNode = RegionManager.getInstance()
                    .getActiveCuboid(target, true);
            if (targetNode == null || !targetNode.getCuboid().isFreeBuild()) {
                if (player.isInCreativeMode()
                        && !player.hasPermission("cIgnoreRestrictions")) { // Ignore
                                                                           // this
                                                                           // if
                                                                           // player
                                                                           // has
                                                                           // administrative
                                                                           // level
                    player.setCreative(0);
                    player.setInventory(CuboidInterface.getInstance().playerInventories
                            .get(player.getName()));
                }
            }
        }
    }
}