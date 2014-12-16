package net.playblack.cuboids.actions.operators;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.canarymod.hook.world.IgnitionHook;
import net.playblack.cuboids.Permissions;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;

import java.util.Iterator;
import java.util.List;

public class BlockModificationsOperator {

    /**
     * Create a list of blocks that should not be affected by the explosion
     * @param positions
     * @param t
     */
    public void checkExplosionBlocks(List<Location> positions, ExplosionType t) {
        Iterator<Location> it = positions.iterator();

        while (it.hasNext()) {
            Location l = it.next();
            if (shouldCancelExplosion(l, t)) {
                it.remove();
            }
        }
    }

    public boolean shouldCancelExplosion(Location loc, ExplosionType type) {
        boolean creeperSecure = RegionManager.get().getActiveRegion(loc, false).getProperty("creeper-explosion") == Status.DENY && type == ExplosionType.CREEPER;
        boolean tntSecure = RegionManager.get().getActiveRegion(loc, false).getProperty("tnt-explosion") == Status.DENY && type == ExplosionType.TNT;
        return creeperSecure || tntSecure;
    }

    /**
     * Check if a player can use a lighter, eg. start a fire.
     *
     * @param player
     * @param point
     * @return
     */
    public boolean canUseLighter(Player player, Location point) {
        if (player.hasPermission(Permissions.ADMIN)) {
            return true;
        }
        Region r = RegionManager.get().getActiveRegion(point, false);
        return r.playerIsAllowed(player, player.getPlayerGroups()) || r.getProperty("firespread") != Status.DENY;
    }

    public boolean canDestroyPaintings(Player player, Location point) {
        if (player.hasPermission(Permissions.ADMIN)) {
            return true;
        }
        Region r = RegionManager.get().getActiveRegion(point, false);
        return r.playerIsAllowed(player, player.getPlayerGroups()) || r.getProperty("protection") != Status.ALLOW;
    }

    public boolean canEndermanUseBlock(Location location) {
        Region r = RegionManager.get().getActiveRegion(location, false);
        return r.getProperty("enderman-pickup") != Status.DENY;
    }

    // *******************************
    // Listener creation stuff
    // *******************************

    private boolean canModifyBlock(Player player, Location location) {
        return CuboidInterface.get().canModifyBlock(player, location);
    }

    /**
     * Returns true if the event should be canceled
     * @param player
     * @param location
     * @return
     */
    public boolean onBlockBreak(Player player, Location location) {
        return !canModifyBlock(player, location);
    }

    public boolean onBlockPlace(Player player, Location location) {
        return !canModifyBlock(player, location);
    }

    public boolean onExplode(Location location, ExplosionType explosionType, List<Location> affectedBlocks) {
        if (shouldCancelExplosion(location, explosionType)) {
            return true;
        }

        // Iterates over the given list and removes blocks that should not be destroyed
        checkExplosionBlocks(affectedBlocks, explosionType);
        return false;
    }

    public boolean onIgnite(IgnitionHook.IgnitionCause source, Player player, Location location) {

        if (source == IgnitionHook.IgnitionCause.FLINT_AND_STEEL) {
            if (!canUseLighter(player, location)) {
                return true;
            }
        }
        else {
            Region r = RegionManager.get().getActiveRegion(location, false);
            if (r.getProperty("firespread") == Status.DENY) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the hook should be canceled (flow should not happen)
     *
     * @param flowTo
     * @param isLavaFlow
     * @return
     */
    public boolean onLiquidFlow(Location flowTo, boolean isLavaFlow) {
        Region r = RegionManager.get().getActiveRegion(flowTo, false);
        return isLavaFlow && r.getProperty("lava-flow") == Status.DENY || r.getProperty("water-flow") == Status.DENY;

    }

    /**
     * Returns true if physics should not happen
     *
     * @param location
     * @return
     */
    public boolean onBlockPhysics(Location location) {
        Region r = RegionManager.get().getActiveRegion(location, false);
        return r.getProperty("physics") == Status.DENY;
    }

    /**
     * Returns true if the block update should not happen
     *
     * @param original
     * @param targetBlock
     * @param location
     * @return
     */
    public boolean onBlockUpdate(BlockType original, BlockType targetBlock, Location location) {
        boolean updatesFarmland = original.getMachineName().endsWith("farmland") && !targetBlock.getMachineName().endsWith("farmland");
        boolean updatesCrop = original.getMachineName().endsWith("wheat") && !targetBlock.getMachineName().endsWith("wheat");

        // TODO: Checking against wheat might prevent people from harvesting
        if (updatesCrop || updatesFarmland) {
            Region r = RegionManager.get().getActiveRegion(location, false);
            if (r.getProperty("crops-trampling") == Status.DENY) {
                return true;
            }
        }
        if (targetBlock.getMachineName().endsWith("fire")) { // new block is fire
            Region r = RegionManager.get().getActiveRegion(location, false);
            if (r.getProperty("firespread") == Status.DENY) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns true if the enderman at this location should not pick up the block
     *
     * @param location
     * @return
     */
    public boolean onEndermanPickup(Location location) {
        return !canEndermanUseBlock(location);
    }

    /**
     * Retrurns true if the hanging entity should not be destroyed
     * @param player
     * @param location
     * @return
     */
    public boolean onEntityHangingDestroy(Player player, Location location) {
        return !canDestroyPaintings(player, location);
    }
}
