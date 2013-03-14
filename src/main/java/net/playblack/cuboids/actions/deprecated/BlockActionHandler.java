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
            CuboidInterface.get().explainRegion(player,
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
            CuboidInterface.get().explainRegion(player, position);
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
