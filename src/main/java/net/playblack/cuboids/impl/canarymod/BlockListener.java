package net.playblack.cuboids.impl.canarymod;

import net.canarymod.api.entity.Fireball;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.entity.living.monster.Creeper;
import net.canarymod.api.entity.living.monster.Enderman;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.position.Vector3D;
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
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.BlockPhysicsEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockUpdateEvent;
import net.playblack.cuboids.actions.events.forwardings.EndermanPickupEvent;
import net.playblack.cuboids.actions.events.forwardings.EntityHangingDestroyEvent;
import net.playblack.cuboids.actions.events.forwardings.ExplosionEvent;
import net.playblack.cuboids.actions.operators.ExplosionType;
import net.playblack.cuboids.actions.events.forwardings.IgniteEvent;
import net.playblack.cuboids.actions.events.forwardings.LiquidFlowEvent;
import net.playblack.cuboids.actions.operators.BlockModificationsOperator;
import net.playblack.cuboids.actions.operators.BrushOperator;
import net.playblack.cuboids.actions.operators.DamageOperator;
import net.playblack.cuboids.actions.operators.MiscOperator;
import net.playblack.cuboids.actions.operators.OperableItemsOperator;
import net.playblack.cuboids.actions.operators.PlayerMovementOperator;
import net.playblack.cuboids.actions.operators.SelectionOperator;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;

import java.util.HashMap;
import java.util.List;

public class BlockListener implements PluginListener {

    private HashMap<String, Long> armSwingTimings = new HashMap<String, Long>();

    // Following is a list of operators
    // They are used to delegate callbacks to logical chunks of code
    BlockModificationsOperator blockModificationsOperator = new BlockModificationsOperator();
    BrushOperator brushOperator = new BrushOperator();
    DamageOperator damageOperator = new DamageOperator();
    MiscOperator miscOperator = new MiscOperator();
    OperableItemsOperator operableItemsOperator = new OperableItemsOperator();
    PlayerMovementOperator playerMovementOperator = new PlayerMovementOperator();
    SelectionOperator selectionOperator = new SelectionOperator();

    @HookHandler
    public void onBlockRightClick(BlockRightClickHook hook) {
        Block b = hook.getBlockClicked();
        if (operableItemsOperator.onBlockClick(hook.getPlayer(), b.getType(), b.getLocation())) {
            hook.setCanceled();
        }
        if (selectionOperator.onBlockRightClick(hook.getPlayer(), b.getLocation())) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onArmSwing(PlayerArmSwingHook hook) {
        if (!armSwingTimings.containsKey(hook.getPlayer().getName())) {
            armSwingTimings.put(hook.getPlayer().getName(), 0L);
        }
        long theTime = armSwingTimings.get(hook.getPlayer().getName());
        if (System.currentTimeMillis() <= theTime + 400) {
            return;
        }

        brushOperator.onArmSwing(hook.getPlayer());
        selectionOperator.onArmSwing(hook.getPlayer());
    }

    @HookHandler
    public void onBlockLeftClick(BlockLeftClickHook hook) {
        if (hook.getBlock() == null) {
            return;
        }
        Block b = hook.getBlock();
        Player player = hook.getPlayer();
        Location p = b.getLocation();

        if (operableItemsOperator.onBlockClick(player, b.getType(), p)) {
            hook.setCanceled();
        }
        if (selectionOperator.onBlockLeftClick(player, p)) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onBlockDestroy(BlockDestroyHook hook) {
        Block b = hook.getBlock();
        Player player = hook.getPlayer();
        Location p = b.getLocation();
        if (blockModificationsOperator.onBlockBreak(player, p)) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onBlockPlace(BlockPlaceHook hook) {
        Block b = hook.getBlockPlaced();
        Player player = hook.getPlayer();
        Location p = b.getLocation();
        if (blockModificationsOperator.onBlockPlace(player, p)) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onExplosion(ExplosionHook hook) {
        //Assemble the list of blocks ...
        HashMap<Location, BlockType> blocks = new HashMap<Location, BlockType>();

        for (Block x : hook.getAffectedBlocks()) {
            blocks.put(x.getLocation(), x.getType());
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

        Location l = b.getLocation();
        ExplosionEvent event = new ExplosionEvent(hook.getEntity(), l, type, blocks);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
            return;
        }
        //Not cancelled, process the list of blocks and remove those that should stay
        List<Location> protectedBlocks = event.getProtectedBlocks();
        List<Block> blocksaffected = hook.getAffectedBlocks();
        if (protectedBlocks != null) {
            for (Location m : protectedBlocks) {
                for (int i = 0; i < blocksaffected.size(); ) {
                    Block x = blocksaffected.get(i);
                    Vector3D tmp = new Vector3D(x.getX(), x.getY(), x.getZ());
//                    ToolBox.adjustWorldPosition(tmp);
                    if (Vector.samePosition2D(m, tmp)) {
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
        Location p = b.getLocation();
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
        Location p = b.getLocation();

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
        Location p = b.getLocation();

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
        Location p = b.getLocation();
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
        Location l = b.getLocation();
        ToolBox.adjustWorldPosition(l);
        EndermanPickupEvent event = new EndermanPickupEvent(l, b.getType(), hook.getEnderman());
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onHangingEntityDestroy(HangingEntityDestroyHook hook) {
        Location loc = hook.getPainting().getLocation();

        EntityHangingDestroyEvent event = new EntityHangingDestroyEvent(hook.getPlayer(), loc);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }

    }
}
