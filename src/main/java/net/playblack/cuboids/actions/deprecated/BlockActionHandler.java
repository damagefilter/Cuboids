package net.playblack.cuboids.actions.deprecated;

import java.util.HashMap;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Vector;
import net.playblack.mcutils.Location;

public class BlockActionHandler {
    private static HashMap<String, Boolean> setOffsetList = new HashMap<String, Boolean>();

    /**
     * Check if player can operate restricted items
     * 
     * @param player
     * @param itemId
     * @return True if player can, false otherwise
     */
    public static boolean handleOperableItems(CPlayer player,
            Location position, int itemId) {
        if (player.hasPermission("cIgnoreRestrictions")) {
            return true;
        }
        if (CuboidInterface.get().playerIsAllowed(player, position)) {
            return true;
        }
        if (CuboidInterface.get().itemIsRestricted(position, itemId)) {
            return false;
        }
        if (Config.get().itemIsRestricted(itemId)) {
            return false;
        }
        return true;
    }

    /**
     * Returns false if a player can place a bucket, false otherwise
     * 
     * @param player
     * @param position
     * @return
     */
    public static boolean handleBucketPlacement(CPlayer player,
            Location position) {
        return CuboidInterface.get().isLiquidControlled(player,
                position);
    }

    /**
     * Check if a player can use a lighter
     * 
     * @param player
     * @param position
     * @return True if so, false otherwise
     */
    public static boolean handleLighter(CPlayer player, Location position) {
        if (player.hasPermission("cIgnoreRestrictions")
                || CuboidInterface.get().canStartFire(player, position)) {
            return true;
        }
        return false;
    }

    /**
     * Check if fire can spread!
     * 
     * @param position
     * @param world
     * @return
     */
    public static boolean handleFirespread(Location position, CWorld world) {
        return CuboidInterface.get().isFireProof(position);
    }

    /**
     * Set points for a player selection
     * 
     * @param player
     * @param point
     */
    private static boolean setFixedPointSingleAction(CPlayer player,
            Location point) {
        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!player.hasPermission("cselect")) {
                // MessageSystem.getInstance().failMessage(player,
                // "permissionDenied");
                return false;
            }
        }
        CuboidSelection selection = SelectionManager.getInstance()
                .getPlayerSelection(player.getName());
        if (!setOffsetList.containsKey(player.getName())) {
            setOffsetList.put(player.getName(), Boolean.valueOf(false));
        }
        if (setOffsetList.get(player.getName())) {
            selection.setOffset(point);
            MessageSystem.getInstance().yellowNote(player, "secondPointSet");
            MessageSystem.customMessage(player, ColorManager.LightGray,
                    point.explain());
            setOffsetList.put(player.getName(), Boolean.valueOf(false));
            return true;
        } else {
            selection.setOrigin(point);
            MessageSystem.getInstance().yellowNote(player, "firstPointSet");
            MessageSystem.customMessage(player, ColorManager.LightGray,
                    point.explain());
            setOffsetList.put(player.getName(), Boolean.valueOf(true));
            return true;
        }
    }

    /**
     * Set a specified point
     * 
     * @param player
     * @param point
     * @param setOffset
     */
    private static boolean setFixedPointDoubleAction(CPlayer player,
            Vector point, boolean setOffset) {
        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!player.hasPermission("cselect")) {
                // MessageSystem.getInstance().failMessage(player,
                // "permissionDenied");
                return false;
            }
        }
        CuboidSelection selection = SelectionManager.getInstance()
                .getPlayerSelection(player.getName());
        if (setOffset) {
            selection.setOffset(point);
            MessageSystem.getInstance().yellowNote(player, "secondPointSet");
            MessageSystem.customMessage(player, ColorManager.LightGray,
                    point.explain());
            setOffsetList.put(player.getName(), Boolean.valueOf(false));
            return true;
        } else {
            selection.setOrigin(point);
            MessageSystem.getInstance().yellowNote(player, "firstPointSet");
            MessageSystem.customMessage(player, ColorManager.LightGray,
                    point.explain());
            setOffsetList.put(player.getName(), Boolean.valueOf(true));
            return true;
        }
    }

    /**
     * This handles setting of points.
     * 
     * @param player
     * @param point
     * @param setOffset
     *            This must be true on rightclick!!! false otherwise! NOTE: This
     *            has NO EFFECT if double action tool is disabled!
     * @return false if no points had to be set!
     */
    public static boolean handleSetPoints(CPlayer player, Location point,
            boolean setOffset, boolean remote) {
        if ((player.getItemInHand().getId() == Config.get()
                .getRegionItem()) && !remote) {
            if (Config.get().isUseDoubleAction()) {
                return setFixedPointDoubleAction(player, point, setOffset);
            } else {
                return setFixedPointSingleAction(player, point);
            }
        }
        if ((player.getItemInHand().getId() == Config.get()
                .getRemoteRegionItem()) && remote) {
            return setFixedPointSingleAction(player, point);
        }
        return false;
    }

    /**
     * Explain cuboid in this region
     * 
     * @param player
     * @param point
     * @param setOffset
     *            True to set offset instead of origin
     */
    public static void explainLocal(CPlayer player) {
        if (player.getItemInHand().getId() == Config.get()
                .getInspectorItem()) {
            CuboidInterface.get().explainCuboid(player,
                    player.getLocation());
        }
    }

    /**
     * Explain a cuboid
     * 
     * @param player
     * @param position
     */
    public static void explainPosition(CPlayer player, Location position) {
        if (player.getItemInHand().getId() == Config.get()
                .getInspectorItem()) {
            CuboidInterface.get().explainCuboid(player, position);
        }
    }

    /**
     * Handle explosions
     * 
     * @param player
     * @param position
     * @return True if explosion must be stopped
     */
    public static boolean handleExplosions(int status, Location position) {
        // 1 = tnt, 2 = creeper
        CuboidInterface ci = CuboidInterface.get();
        if ((status == 1) && ci.isTntSecure(position)) {
            return true;
        } else if ((status == 2) && ci.isCreeperSecure(position)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the fire can be made
     * 
     * @param player
     * @param v
     * @param blockStatus
     *            2 = lighter, 3+ natural firespread - player may be null in
     *            this case
     * @return True if position can ignite, false otherwise
     */
    public static boolean handleIgnition(CPlayer player, Location v,
            int blockStatus) {
        CuboidInterface ci = CuboidInterface.get();
        if (blockStatus == 2) { // lighter shall be 2
            return ci.canModifyBlock(player, v);
        } else { // natural firespread
            return !ci.isFireProof(v);
        }
    }

    /**
     * Check if a block can flow
     * 
     * @param v
     * @param world
     * @return false if block can flow
     */
    public static boolean handleFlow(CBlock block, Location v) {
        return !CuboidInterface.get().hasFlowControl(block, v);
    }

    /**
     * Returns true if physics should not be done
     * 
     * @param position
     * @param world
     * @param blockId
     *            if this is not snd or gravel this method will return false
     * @return
     */
    public static boolean handlePhysics(Location position, CWorld world,
            int blockId) {
        if ((blockId == 12) || (blockId == 13)) {
            return CuboidInterface.get().isPhysicsControlled(position,
                    world);
        }
        return false;
    }

    /**
     * Returns true if endermen should not pick up blocks
     * 
     * @param position
     * @param world
     * @param blockId
     * @return
     */
    public static boolean handleEndermanPickup(Location position) {
        return CuboidInterface.get().isEnderControlled(position);
    }

    /**
     * Return true if there is farmland protection, false otherwise
     * 
     * @param point
     * @param world
     * @param type
     * @return
     */
    public static boolean handleFarmland(Location point, CWorld world,
            int type, int newType) {
        if (type == 60) {
            if (newType != 60) {
                return CuboidInterface.get().isFarmland(point);
            }
        }
        return false;

    }
}
