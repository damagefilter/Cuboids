package net.playblack.cuboids.actions;

import java.util.HashMap;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.Vector;
public class BlockActionHandler {
    private static HashMap<String, Boolean> setOffsetList = new HashMap<String, Boolean>();
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
    
    
    /**
     * Set points for a player selection
     * @param player
     * @param point
     * @param setOffset True to set offset instead of origin
     */
    private static boolean setFixedPointSingleAction(CPlayer player, Vector point) {
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!player.hasPermission("cselect")) {
               // MessageSystem.getInstance().failMessage(player, "permissionDenied");
                return false;
            }
        }
        CuboidSelection selection = SelectionManager.getInstance().getPlayerSelection(player.getName());
        if(!setOffsetList.containsKey(player.getName())) {
            setOffsetList.put(player.getName(), Boolean.valueOf(false));
        }
        if(setOffsetList.get(player.getName())) {
            selection.setOffset(point);
            MessageSystem.getInstance().yellowNote(player, "secondPointSet");
            setOffsetList.put(player.getName(), Boolean.valueOf(false));
            return true;
        }
        else {
            selection.setOrigin(point);
            MessageSystem.getInstance().yellowNote(player, "firstPointSet");
            setOffsetList.put(player.getName(), Boolean.valueOf(true));
            return true;
        }
    }
    
    /**
     * Set a specified point
     * @param player
     * @param point
     * @param setOffset
     */
    private static boolean setFixedPointDoubleAction(CPlayer player, Vector point, boolean setOffset) {
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!player.hasPermission("cselect")) {
               // MessageSystem.getInstance().failMessage(player, "permissionDenied");
                return false;
            }
        }
        CuboidSelection selection = SelectionManager.getInstance().getPlayerSelection(player.getName());
        if(setOffset) {
            selection.setOffset(point);
            MessageSystem.getInstance().yellowNote(player, "secondPointSet");
            setOffsetList.put(player.getName(), Boolean.valueOf(false));
            return true;
        }
        else {
            selection.setOrigin(point);
            MessageSystem.getInstance().yellowNote(player, "firstPointSet");
            setOffsetList.put(player.getName(), Boolean.valueOf(true));
            return true;
        }
    }
    
    
    /**
     * This handles setting of points. 
     * @param player
     * @param point
     * @param setOffset This must be true on rightclick!!! false otherwise!
     * NOTE: This has NO EFFECT if double action tool is disabled!
     */
    public static boolean handleSetPoints(CPlayer player, Vector point, boolean setOffset) {
        if(player.getItemInHand().getId() == Config.getInstance().getRegionItem()) {
            if(Config.getInstance().isUseDoubleAction()) {
                return setFixedPointDoubleAction(player, point, setOffset);
            }
            else {
                return setFixedPointSingleAction(player, point);
            }
        }
        if(player.getItemInHand().getId() == Config.getInstance().getRemoteRegionItem()) {
            return setFixedPointSingleAction(player, point);
        }
        return false;
    }
    
    /**
     * Explain cuboid in this region
     * @param player
     * @param point
     * @param setOffset True to set offset instead of origin
     */
    public static void explainLocal(CPlayer player) {
        if(player.getItemInHand().getId() == Config.getInstance().getInspectorItem()) {
            CuboidInterface.getInstance().explainCuboid(player, player.getPosition());
        }
    }
    
    /**
     * Explain a cuboid
     * @param player
     * @param position
     */
    public static void explainPosition(CPlayer player, Vector position) {
        if(player.getItemInHand().getId() == Config.getInstance().getInspectorItem()) {
            CuboidInterface.getInstance().explainCuboid(player, position);
        }
    }
    
    /**
     * Check if a player can place a block
     * @param player
     * @param position
     * @return
     */
    public static boolean handleExplosions(CWorld world, Vector position) {
        CuboidInterface ci = CuboidInterface.getInstance();
        if(ci.isCreeperSecure(position, world)) {
            return true;
        }
        else if(ci.isTntSecure(position, world)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Returns true if the fire can be made
     * @param player
     * @param v
     * @param blockStatus 2 = lighter, 3+ natural firespread - player may be null in this case
     */
    public static boolean handleIgnition(CPlayer player, Vector v, CWorld world, int blockStatus) {
        CuboidInterface ci = CuboidInterface.getInstance();
        if(blockStatus == 2) { //lighter shall be 2
            if(ci.canModifyBlock(player, v)) {
                return true;
            }
            return false;
        }
        else if(blockStatus > 2) { //natural firespread
            return ci.isFireProof(v, world);
        }
        return true;
    }
}
