package net.playblack.cuboids.impl.canarymod;

import net.canarymod.api.entity.Fireball;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.entity.living.monster.Creeper;
import net.canarymod.api.entity.living.monster.Enderman;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.EndermanPickupBlockHook;
import net.canarymod.hook.entity.HangingEntityDestroyHook;
import net.canarymod.hook.player.BlockDestroyHook;
import net.canarymod.hook.player.BlockLeftClickHook;
import net.canarymod.hook.player.BlockPlaceHook;
import net.canarymod.hook.player.BlockRightClickHook;
import net.canarymod.hook.player.PlayerArmSwingHook;
import net.canarymod.hook.world.BlockPhysicsHook;
import net.canarymod.hook.world.BlockUpdateHook;
import net.canarymod.hook.world.ExplosionHook;
import net.canarymod.hook.world.FlowHook;
import net.canarymod.hook.world.IgnitionHook;
import net.canarymod.plugin.PluginListener;
import net.playblack.cuboids.InvalidPlayerException;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.ArmSwingEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockBreakEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockLeftClickEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockPhysicsEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockPlaceEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockRightClickEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockUpdateEvent;
import net.playblack.cuboids.actions.events.forwardings.EndermanPickupEvent;
import net.playblack.cuboids.actions.events.forwardings.EntityHangingDestroyEvent;
import net.playblack.cuboids.actions.events.forwardings.ExplosionEvent;
import net.playblack.cuboids.actions.events.forwardings.ExplosionEvent.ExplosionType;
import net.playblack.cuboids.actions.events.forwardings.IgniteEvent;
import net.playblack.cuboids.actions.events.forwardings.LiquidFlowEvent;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.mcutils.CLocation;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;

import java.util.HashMap;
import java.util.List;

public class BlockListener implements PluginListener {

    private HashMap<String, Long> armSwingTimings = new HashMap<String, Long>();

    @HookHandler
    public void blockRightClick(BlockRightClickHook hook) {
        Block b = hook.getBlockClicked();
        CLocation p = new CLocation(b.getX(), b.getY(), b.getZ(), hook.getPlayer().getWorld().getType().getId(), hook.getPlayer().getWorld().getName());

        BlockRightClickEvent event = new BlockRightClickEvent(hook.getPlayer(), b.getType(), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void leftClick(PlayerArmSwingHook hook) {
        if (!armSwingTimings.containsKey(hook.getPlayer().getName())) {
            armSwingTimings.put(hook.getPlayer().getName(), 0L);
        }
        long theTime = armSwingTimings.get(hook.getPlayer().getName());
        if (System.currentTimeMillis() <= theTime + 400) {
            return;
        }

        ActionManager.fireEvent(new ArmSwingEvent(hook.getPlayer()));
    }

    @HookHandler
    public void blockLeftClick(BlockLeftClickHook hook) {
        if (hook.getBlock() == null) {
            return;
        }
        Block b = hook.getBlock();
        Player player = hook.getPlayer();
        CLocation p = new CLocation(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld().getName());

        BlockLeftClickEvent event = new BlockLeftClickEvent(player, b.getType(), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void blockDestroy(BlockDestroyHook hook) {
        Block b = hook.getBlock();
        Player player = hook.getPlayer();
        CLocation p = new CLocation(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld().getName());
//        ToolBox.adjustWorldPosition(p);

        BlockBreakEvent event = new BlockBreakEvent(player, b.getType(), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void blockPlace(BlockPlaceHook hook) {
        Block b = hook.getBlockPlaced();
        Player player = hook.getPlayer();
        CLocation p = new CLocation(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld().getName());

        BlockPlaceEvent event = new BlockPlaceEvent(player, b.getType(), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
//            Logman.println("place was canceled");
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onExplosion(ExplosionHook hook) {
        //Assemble the list of blocks ...
        HashMap<CLocation, BlockType> blocks = new HashMap<CLocation, BlockType>();

        for (Block x : hook.getAffectedBlocks()) {
            blocks.put(new CLocation(x.getLocation()), x.getType());
        }

        Block b = hook.getBlock();
        ExplosionType type = ExplosionType.TNT;
        if (hook.getEntity() != null) {
            if (hook.getEntity() instanceof Fireball) {
                type = ExplosionType.GHAST_FIREBALL;
            }
            if (hook.getEntity() instanceof Creeper) {
                type = ExplosionType.CREEPER;
            }
        }

        CLocation l = new CLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        ExplosionEvent event = new ExplosionEvent(hook.getEntity(), l, type, blocks);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
            return;
        }
        //Not cancelled, process the list of blocks and remove those that should stay
        List<CLocation> protectedBlocks = event.getProtectedBlocks();
        List<Block> blocksaffected = hook.getAffectedBlocks();
        if (protectedBlocks != null) {
            for (CLocation m : protectedBlocks) {
                for (int i = 0; i < blocksaffected.size(); ) {
                    Block x = blocksaffected.get(i);
                    Vector tmp = new Vector(x.getX(), x.getY(), x.getZ());
//                    ToolBox.adjustWorldPosition(tmp);
                    if (m.samePosition2D(tmp)) {
                        blocksaffected.remove(i);
                    }
                    else {
                        i++;
                    }
                }
            }
        }
    }

    @HookHandler
    public void onIgnite(IgnitionHook hook) {
        Block b = hook.getBlock();
        Player player = hook.getPlayer();
        CLocation p = new CLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        ToolBox.adjustWorldPosition(p);

        IgniteEvent event = new IgniteEvent(hook.getCause(), p, b.getType(), player);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onFlow(FlowHook hook) {
        Block b = hook.getBlockFrom();
        Block to = hook.getBlockTo();
        CLocation p = new CLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        ToolBox.adjustWorldPosition(p);
        LiquidFlowEvent event = new LiquidFlowEvent(b.getType(), to.getType(), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onBlockPhysics(BlockPhysicsHook hook) {
        Block b = hook.getBlock();
        CLocation p = new CLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        ToolBox.adjustWorldPosition(p);
        BlockPhysicsEvent event = new BlockPhysicsEvent(b.getType(), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onBlockUpdate(BlockUpdateHook hook) {
        Block b = hook.getBlock();
        CLocation p = new CLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        ToolBox.adjustWorldPosition(p);
        BlockUpdateEvent event = new BlockUpdateEvent(b.getType(), hook.getNewBlockType(), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onEndermanPickup(EndermanPickupBlockHook hook) {
        Block b = hook.getBlock();
        Enderman entity = hook.getEnderman();
        CLocation l = new CLocation(b.getX(), b.getY(), b.getZ(), entity.getWorld().getType().getId(), entity.getWorld().getName());
        ToolBox.adjustWorldPosition(l);
        EndermanPickupEvent event = new EndermanPickupEvent(l, b.getType(), hook.getEnderman());
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onHangingEntityDestroy(HangingEntityDestroyHook hook) {
        CLocation loc = new CLocation(hook.getPainting().getX(), hook.getPainting().getY(), hook.getPainting().getZ(), hook.getPainting().getWorld().getType().getId(), hook.getPainting().getWorld().getName());

        EntityHangingDestroyEvent event = new EntityHangingDestroyEvent(hook.getPlayer(), loc);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }

    }
}
