package net.playblack.cuboids.actions;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.WorldLocation;
public class PlayerMovementHandler {
    
    /**
     * Handle if a player can trespass an area or not.
     * @param player
     */
    public static void handleAreaTrespassing(CPlayer player, WorldLocation origin, WorldLocation target) {
        if(!CuboidInterface.getInstance().canEnter(player, target)) {
            player.teleportTo(origin);
        }
    }
    
    /**
     * Handle the adding and removing of players into/from cuboid areas
     * @param player
     * @param target
     */
    public static void handleCuboidAreas(CPlayer player, WorldLocation origin, WorldLocation target) {
        CuboidInterface.getInstance().addPlayerWithin(player, target);
        CuboidInterface.getInstance().removePlayerWithin(player, origin, target);
    }
}