package net.playblack.cuboids.impl.canarymod;

import net.canarymod.api.DamageType;
import net.canarymod.api.PlayerReference;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Location;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.DamageHook;
import net.canarymod.hook.player.BanHook;
import net.canarymod.hook.player.DisconnectionHook;
import net.canarymod.hook.player.ItemDropHook;
import net.canarymod.hook.player.KickHook;
import net.canarymod.hook.player.PlayerMoveHook;
import net.canarymod.hook.player.TeleportHook;
import net.canarymod.plugin.PluginListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.EntityDamageEvent;
import net.playblack.cuboids.actions.events.forwardings.EntityDamageEvent.DamageSource;
import net.playblack.cuboids.actions.events.forwardings.ItemDropEvent;
import net.playblack.cuboids.actions.events.forwardings.PlayerWalkEvent;
import net.playblack.cuboids.gameinterface.CServer;

public class PlayerListener implements PluginListener {

    @HookHandler
    public void onDisconnect(DisconnectionHook hook) {
        CServer.getServer().removePlayer(hook.getPlayer().getName());
    }

    @HookHandler
    public void onBan(BanHook hook) {
        PlayerReference player = hook.getBannedPlayer();
        if (player == null) {
            return;
        }
        CServer.getServer().removePlayer(player.getName());
    }

    @HookHandler
    public void onKick(KickHook hook) {
        CServer.getServer().removePlayer(hook.getKickedPlayer().getName());
    }

    @HookHandler
    public void onPlayerMove(PlayerMoveHook hook) {
        Location to = hook.getTo();
        Location from = hook.getFrom();
        Player player = hook.getPlayer();

        PlayerWalkEvent event = new PlayerWalkEvent(player, from, to);
        ActionManager.fireEvent(event);
    }

    @HookHandler
    public void onTeleport(TeleportHook hook) {
        Player player = hook.getPlayer();
        Location to = hook.getDestination();
        Location from = player.getLocation();
        if (!player.getWorld().isChunkLoaded((int) to.getX(), (int) to.getZ())) {
            player.getWorld().loadChunk((int) to.getX(), (int) to.getZ());
        }
        // TODO: Might need position adjustments
        PlayerWalkEvent event = new PlayerWalkEvent(player, from, to);
        ActionManager.fireEvent(event);
        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onDamage(DamageHook hook) {
        if (hook.getAttacker() == null) {
            return;
        }

        DamageSource ds = damageSourceFromCanary(hook.getDamageSource().getDamagetype(), true);
        EntityDamageEvent event = new EntityDamageEvent(hook.getAttacker(), hook.getDefender(), ds, hook.getDamageDealt());
        ActionManager.fireEvent(event);

        if (event.isCancelled()) {
            hook.setCanceled();
        }
    }

    //TODO: Check if this is covered by block-right-click!
//    @Override
//    public boolean onItemUse(Player player, Block blockPlaced,
//            Block blockClicked, Item item) {
//        if (player.canUseCommand("/cuboids.super.admin")) {
//            return false; // allow
//        }
//        if (blockPlaced == null) {
//            blockPlaced = blockClicked;
//        } else if (blockClicked == null) {
//            blockClicked = blockPlaced;
//        }
//        if ((blockClicked == null) && (blockPlaced == null)) {
//            // pretty dirty fallback but there's no other position that would be
//            // "valid enough"
//            blockPlaced = new Block(0, (int) player.getX(),
//                    (int) player.getY(), (int) player.getZ());
//        }
//        CPlayer cplayer;
//        try {
//            cplayer = CServer.getServer().getPlayer(player.getName());
//        } catch (InvalidPlayerException e) {
//            // Fallback
//            cplayer = new CanaryPlayer(player);
//        }
//        net.playblack.mcutils.Location v = new net.playblack.mcutils.Location(blockPlaced.getX(),
//                blockPlaced.getY(), blockPlaced.getZ(), blockPlaced.getWorld()
//                        .getName());
//
//        // 25d == flint&steel
//        if (item.getItemId() == 259) {
//            return !BlockActionHandler.handleLighter(cplayer,
//                    new net.playblack.mcutils.Location(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ(), blockClicked.getWorld().getName()));
//        }
//        // buckets
//        if (item.getItemId() == 326 || item.getItemId() == 327) {
//            return BlockActionHandler.handleBucketPlacement(cplayer, v);
//        }
//        return !BlockActionHandler.handleOperableItems(cplayer, v,
//                item.getItemId());
//    }
    @HookHandler
    public void onItemDrop(ItemDropHook hook) {
        if (!(hook.getPlayer() == null)) {
            ItemDropEvent event = new ItemDropEvent(hook.getItem().getItem(), hook.getPlayer(), hook.getItem().getLocation());
            ActionManager.fireEvent(event);
            if (event.isCancelled()) {
                hook.setCanceled();
            }
        }
    }

    private EntityDamageEvent.DamageSource damageSourceFromCanary(DamageType type, boolean hasAttacker) {
        switch (type) {
            case ANVIL:
                return DamageSource.FALLING_ANVIL;
            case ARROW:
                return DamageSource.ENTITY;
            case CACTUS:
                return DamageSource.CACTUS;
            case ENCHANTMENT:
                return DamageSource.GENERIC;
            case EXPLOSION:
                return hasAttacker ? DamageSource.CREEPER_EXPLOSION : DamageSource.EXPLOSION;
            case FALL:
                return DamageSource.FALL;
            case FALLING_BLOCK:
                return DamageSource.FALLING_BLOCK;
            case FIRE:
                return DamageSource.FIRE;
            case FIREBALL:
                return DamageSource.FIRE;
            case FIRE_TICK:
                return DamageSource.FIRE_TICK;
            case GENERIC:
                return DamageSource.GENERIC;
            case LAVA:
                return DamageSource.LAVA;
            case MOB:
            case PLAYER:
                return DamageSource.ENTITY;
            case POTION:
                return DamageSource.POTION;
            case STARVATION:
                return DamageSource.STARVATION;
            case SUFFOCATION:
                return DamageSource.SUFFOCATION;
            case THROWN:
                return DamageSource.ENDERPEARL;
            case VOID:
                return DamageSource.GENERIC;
            case WATER:
                return DamageSource.WATER;
            case WITHER:
                return DamageSource.WITHER_SKULL;
            default:
                return DamageSource.GENERIC;

        }
    }
}
