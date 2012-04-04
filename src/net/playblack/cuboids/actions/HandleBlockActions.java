package net.playblack.cuboids.actions;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Vector;
public class HandleBlockActions {
    
    /**
     * Check if player can operate restricted items
     * @param player
     * @param itemId
     * @return True if player can, false otherwise
     */
    public static boolean handleOperableItems(CPlayer player, Vector position, int itemId) {
        if(player.hasPermission("cIgnoreRestrictions")) {
            return true;
        }
        CuboidE cube = RegionManager.getInstance().getActiveCuboid( position, 
                                                                    player.getWorld().getName(), 
                                                                    player.getWorld().getDimension()).getCuboid();
        if(cube.playerIsAllowed(player.getName(), player.getGroups())) {
            return true;
        }
        if(cube.isItemRestricted(itemId)) {
            return false;
        }
        return true;
    }
    
    /**
     * Check if a player can use a lighter
     * @param player
     * @param position
     * @return True if so, false otherwise
     */
    public static boolean handleLighter(CPlayer player, Vector position) {
        if(player.hasPermission("cIgnoreRestrictions") || CuboidInterface.getInstance().canStartFire(player, position)) {
            return true;
        }
        return false;
    }
    
    /**
     * Check if fire can spread!
     * @param position
     * @param world
     * @return
     */
    public static boolean handleFirespread(Vector position, CWorld world) {
        return CuboidInterface.getInstance().isFireProof(position, world);
    }
}
