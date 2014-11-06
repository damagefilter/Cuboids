package net.playblack.cuboids.actions.operators;

import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.BlockBreakEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockPhysicsEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockPlaceEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockUpdateEvent;
import net.playblack.cuboids.actions.events.forwardings.EndermanPickupEvent;
import net.playblack.cuboids.actions.events.forwardings.EntityHangingDestroyEvent;
import net.playblack.cuboids.actions.events.forwardings.ExplosionEvent;
import net.playblack.cuboids.actions.events.forwardings.ExplosionEvent.ExplosionType;
import net.playblack.cuboids.actions.events.forwardings.IgniteEvent;
import net.playblack.cuboids.actions.events.forwardings.IgniteEvent.FireSource;
import net.playblack.cuboids.actions.events.forwardings.LiquidFlowEvent;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BlockModificationsOperator implements ActionListener {

    /**
     * Create a list of blocks that should not be affected by the explosion
     *
     * @param positions
     * @return
     */
    public List<Location> checkExplosionBlocks(Set<Location> positions, ExplosionType t) {
        ArrayList<Location> toRemove = new ArrayList<Location>();
        for (Location l : positions) {
            if (shouldCancelExplosion(l, t)) {
                toRemove.add(l);
            }
        }
        return toRemove;
    }

    public boolean shouldCancelExplosion(Location loc, ExplosionType type) {
        boolean creeperSecure = RegionManager.get()
                                             .getActiveRegion(loc, false)
                                             .getProperty("creeper-explosion") == Status.DENY && type == ExplosionType.CREEPER;
        boolean tntSecure = RegionManager.get()
                                         .getActiveRegion(loc, false)
                                         .getProperty("tnt-explosion") == Status.DENY && type == ExplosionType.TNT;
        return creeperSecure || tntSecure;
    }

    /**
     * Check if a player can use a lighter, eg. start a fire.
     *
     * @param player
     * @param point
     * @return
     */
    public boolean canUseLighter(CPlayer player, Location point) {
        if (player.hasPermission("cuboids.super.admin")) {
            return true;
        }
        Region r = RegionManager.get().getActiveRegion(point, false);
        return r.playerIsAllowed(player.getName(), player.getGroups()) || r.getProperty("firespread") != Status.DENY;
    }

    public boolean canDestroyPaintings(CPlayer player, Location point) {
        if (player.hasPermission("cuboids.super.admin")) {
            return true;
        }
        Region r = RegionManager.get().getActiveRegion(point, false);
        return r.playerIsAllowed(player.getName(), player.getGroups()) || r.getProperty("protection") != Status.DENY;
    }

    public boolean canEndermanUseBlock(Location location) {
        Region r = RegionManager.get().getActiveRegion(location, false);
        return r.getProperty("enderman-pickup") != Status.DENY;
    }

    // *******************************
    // Listener creation stuff
    // *******************************
    @ActionHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().canModifyBlock(event.getLocation())) {
            event.cancel();
        }
    }

    @ActionHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().canModifyBlock(event.getLocation())) {
            event.cancel();
        }
    }

    @ActionHandler
    public void onEntityExplode(ExplosionEvent event) {
        if (shouldCancelExplosion(event.getLocation(), event.getExplosionType())) {
            event.cancel();
            return;
        }
        //Remove blocks from protected regions but do the rest of the explosion
        HashMap<Location, CBlock> markedBlocks = event.getAffectedBlocks();
        //List of blocks that need to be removed
        event.setProtectedBlocks(checkExplosionBlocks(markedBlocks.keySet(), event.getExplosionType()));
    }

    @ActionHandler
    public void onIgnite(IgniteEvent event) {

//        Debug.log(event.getLocation().toString());
        if (event.getSource() == FireSource.LIGHTER) {
            if (!canUseLighter(event.getPlayer(), event.getLocation())) {
                event.cancel();
            }
        }
        else {
            Region r = RegionManager.get().getActiveRegion(event.getLocation(), false);
            if (r.getProperty("firespread") == Status.DENY) {
                event.cancel();
            }
        }
    }

    @ActionHandler
    public void onLiquidFlow(LiquidFlowEvent event) {
        Region r = RegionManager.get().getActiveRegion(event.getLocation(), false);
        if (event.isWaterFlow() && r.getProperty("water-flow") == Status.DENY) {
            event.cancel();
        }
        if (event.isLavaFlow() && r.getProperty("lava-flow") == Status.DENY) {
            event.cancel();
        }
    }

    @ActionHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        Region r = RegionManager.get().getActiveRegion(event.getLocation(), false);
        if (r.getProperty("physics") == Status.DENY) {
            event.cancel();
        }
    }

    @ActionHandler
    public void onBlockUpdate(BlockUpdateEvent event) {
        if (event.getBlock().getType() == 60 && event.getTargetBlock().getType() != 60) {
            Region r = RegionManager.get().getActiveRegion(event.getLocation(), false);
            if (r.getProperty("crops-trampling") == Status.DENY) {
                event.cancel();
            }
        }
        if (event.getTargetBlock().getType() == 51) { // new block is fire
            Region r = RegionManager.get().getActiveRegion(event.getLocation(), false);
            if (r.getProperty("firespread") == Status.DENY) {
                event.cancel();
            }
        }
    }

    @ActionHandler
    public void onEndermanPickup(EndermanPickupEvent event) {
        if (!canEndermanUseBlock(event.getLocation())) {
            event.cancel();
        }
    }

    @ActionHandler
    public void onEntityHangingDestroy(EntityHangingDestroyEvent event) {
        if (!canDestroyPaintings(event.getPlayer(), event.getLocation())) {
            event.cancel();
        }
    }

    static {
        ActionManager.registerActionListener("Cuboids", new BlockModificationsOperator());
    }
}
