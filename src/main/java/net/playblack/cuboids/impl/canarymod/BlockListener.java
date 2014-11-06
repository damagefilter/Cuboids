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
import net.playblack.cuboids.actions.events.forwardings.IgniteEvent.FireSource;
import net.playblack.cuboids.actions.events.forwardings.LiquidFlowEvent;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.mcutils.Location;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;

import java.util.HashMap;
import java.util.List;

public class BlockListener implements PluginListener {

    private HashMap<String, Long> armSwingTimings = new HashMap<String, Long>();

    @HookHandler
    public void blockRightClick(BlockRightClickHook hook) {
        Block b = hook.getBlockClicked();
        Location p = new Location(b.getX(), b.getY(), b.getZ(), hook.getPlayer()
                                                                    .getWorld()
                                                                    .getType()
                                                                    .getId(), hook.getPlayer().getWorld().getName());
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(hook.getPlayer().getName());
        }
        catch (InvalidPlayerException e) {
            // fallback to manually get a player
            cplayer = new CanaryPlayer(hook.getPlayer());
        }
        BlockRightClickEvent event = new BlockRightClickEvent(cplayer, new CBlock(b.getTypeId(), b.getData()), p);
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

        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(hook.getPlayer().getName());
        }
        catch (InvalidPlayerException e) {
            // fallback to manually get a player
            cplayer = new CanaryPlayer(hook.getPlayer());
        }
        ActionManager.fireEvent(new ArmSwingEvent(cplayer));
    }

    @HookHandler
    public void blockLeftClick(BlockLeftClickHook hook) {
        if (hook.getBlock() == null) {
            return;
        }
        Block b = hook.getBlock();
        Player player = hook.getPlayer();
        Location p = new Location(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld()
                                                                                                           .getName());
//        ToolBox.adjustWorldPosition(p);
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        }
        catch (InvalidPlayerException e) {
            // fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
        BlockLeftClickEvent event = new BlockLeftClickEvent(cplayer, new CBlock(b.getTypeId(), b.getData()), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void blockDestroy(BlockDestroyHook hook) {
        Block b = hook.getBlock();
        Player player = hook.getPlayer();
        Location p = new Location(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld()
                                                                                                           .getName());
//        ToolBox.adjustWorldPosition(p);
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        }
        catch (InvalidPlayerException e) {
            // fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
        BlockBreakEvent event = new BlockBreakEvent(cplayer, new CBlock(b.getTypeId(), b.getData()), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void blockPlace(BlockPlaceHook hook) {
        Block b = hook.getBlockPlaced();
        Player player = hook.getPlayer();
        Location p = new Location(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld()
                                                                                                           .getName());
//        ToolBox.adjustWorldPosition(p);
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        }
        catch (InvalidPlayerException e) {
            // fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
        BlockPlaceEvent event = new BlockPlaceEvent(cplayer, new CBlock(b.getTypeId(), b.getData()), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
//            Logman.println("place was canceled");
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onExplosion(ExplosionHook hook) {
        //Assemble the list of blocks ...
        HashMap<Location, CBlock> blocks = new HashMap<Location, CBlock>();

        for (Block x : hook.getAffectedBlocks()) {
            Location l = new Location(x.getX(), x.getY(), x.getZ(), x.getWorld().getType().getId(), x.getWorld()
                                                                                                     .getName());
//            ToolBox.adjustWorldPosition(l);
            blocks.put(l, new CBlock(x.getTypeId(), x.getData()));
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

        Location l = new Location(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        ExplosionEvent event = new ExplosionEvent(new CanaryBaseEntity(hook.getEntity()), l, type, blocks);
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
        Location p = new Location(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        ToolBox.adjustWorldPosition(p);
        CPlayer cplayer = null;
        if (player != null) {
            try {
                cplayer = CServer.getServer().getPlayer(player.getName());
            }
            catch (InvalidPlayerException e) {
                // fallback to manually get a player
                cplayer = new CanaryPlayer(player);
            }
        }
        IgniteEvent event = new IgniteEvent(FireSource.fromInt(hook.getCause().ordinal()), p, new CBlock(b.getTypeId(), b.getData()), cplayer);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onFlow(FlowHook hook) {
        Block b = hook.getBlockFrom();
        Block to = hook.getBlockTo();
        Location p = new Location(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        ToolBox.adjustWorldPosition(p);
        LiquidFlowEvent event = new LiquidFlowEvent(new CBlock(b.getTypeId(), b.getData()), new CBlock(to.getTypeId(), to
                .getData()), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onBlockPhysics(BlockPhysicsHook hook) {
        Block b = hook.getBlock();
        Location p = new Location(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        ToolBox.adjustWorldPosition(p);
        BlockPhysicsEvent event = new BlockPhysicsEvent(new CBlock(b.getTypeId(), b.getData()), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onBlockUpdate(BlockUpdateHook hook) {
        Block b = hook.getBlock();
        Location p = new Location(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        ToolBox.adjustWorldPosition(p);
        BlockUpdateEvent event = new BlockUpdateEvent(new CBlock(b.getTypeId(), b.getData()), new CBlock(hook.getNewBlockId()), p);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onEndermanPickup(EndermanPickupBlockHook hook) {
        Block b = hook.getBlock();
        Enderman entity = hook.getEnderman();
        Location l = new Location(b.getX(), b.getY(), b.getZ(), entity.getWorld().getType().getId(), entity.getWorld()
                                                                                                           .getName());
        ToolBox.adjustWorldPosition(l);
        EndermanPickupEvent event = new EndermanPickupEvent(l, new CBlock(b.getTypeId(), b.getData()));
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onHangingEntityDestroy(HangingEntityDestroyHook hook) {
        CPlayer player;

        Location loc = new Location(
                hook.getPainting().getX(),
                hook.getPainting().getY(),
                hook.getPainting().getZ(),
                hook.getPainting().getWorld().getType().getId(),
                hook.getPainting().getWorld().getName());
        try {
            player = CServer.getServer().getPlayer(hook.getPlayer().getName());
        }
        catch (InvalidPlayerException e) {
            player = new CanaryPlayer(hook.getPlayer());
        }
        EntityHangingDestroyEvent event = new EntityHangingDestroyEvent(player, loc);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }

    }
}
