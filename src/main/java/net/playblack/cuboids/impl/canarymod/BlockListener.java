package net.playblack.cuboids.impl.canarymod;

import net.canarymod.api.entity.Fireball;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.entity.living.monster.Creeper;
import net.canarymod.api.world.blocks.Block;
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
import net.playblack.cuboids.actions.operators.BlockModificationsOperator;
import net.playblack.cuboids.actions.operators.BrushOperator;
import net.playblack.cuboids.actions.operators.ExplosionType;
import net.playblack.cuboids.actions.operators.OperableItemsOperator;
import net.playblack.cuboids.actions.operators.SelectionOperator;
import net.playblack.mcutils.Vector;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockListener implements PluginListener {

    private HashMap<String, Long> armSwingTimings = new HashMap<String, Long>();

    // Following is a list of operators
    // They are used to delegate callbacks to logical chunks of code
    BlockModificationsOperator blockModificationsOperator = new BlockModificationsOperator();
    BrushOperator brushOperator = new BrushOperator();
    OperableItemsOperator operableItemsOperator = new OperableItemsOperator();
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
        ArrayList<Location> blocks = new ArrayList<Location>();

        for (Block x : hook.getAffectedBlocks()) {
            blocks.add(x.getLocation());
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
        if(blockModificationsOperator.onExplode(l, type, blocks)) {
            hook.setCanceled();
            return;
        }

        //Not cancelled, process the list of blocks and remove those that should stay
        List<Block> blocksaffected = hook.getAffectedBlocks();
        for (Location m : blocks) {
            for (int i = 0; i < blocksaffected.size(); ) {
                Block x = blocksaffected.get(i);
                Vector3D tmp = x.getLocation();
                if (Vector.samePosition2D(m, tmp)) {
                    blocksaffected.remove(i);
                }
                else {
                    i++;
                }
            }
        }
    }

    @HookHandler
    public void onIgnite(IgnitionHook hook) {
        if (blockModificationsOperator.onIgnite(hook.getCause(), hook.getPlayer(), hook.getBlock().getLocation())) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onFlow(FlowHook hook) {
        if (blockModificationsOperator.onLiquidFlow(hook.getBlockTo().getLocation(), hook.getBlockFrom().getType().getMachineName().endsWith("lava"))) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onBlockPhysics(BlockPhysicsHook hook) {
        if (blockModificationsOperator.onBlockPhysics(hook.getBlock().getLocation())) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onBlockUpdate(BlockUpdateHook hook) {
        if (blockModificationsOperator.onBlockUpdate(hook.getBlock().getType(), hook.getNewBlockType(), hook.getBlock().getLocation())) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onEndermanPickup(EndermanPickupBlockHook hook) {
        if (blockModificationsOperator.onEndermanPickup(hook.getBlock().getLocation())) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onHangingEntityDestroy(HangingEntityDestroyHook hook) {
        if (blockModificationsOperator.onEntityHangingDestroy(hook.getPlayer(), hook.getPainting().getLocation())) {
            hook.setCanceled();
        }
    }
}
