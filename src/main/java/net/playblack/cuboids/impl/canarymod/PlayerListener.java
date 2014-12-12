package net.playblack.cuboids.impl.canarymod;

import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.DamageHook;
import net.canarymod.hook.player.ItemDropHook;
import net.canarymod.hook.player.PlayerMoveHook;
import net.canarymod.hook.player.TeleportHook;
import net.canarymod.plugin.PluginListener;
import net.playblack.cuboids.actions.operators.DamageOperator;
import net.playblack.cuboids.actions.operators.MiscOperator;
import net.playblack.cuboids.actions.operators.PlayerMovementOperator;

public class PlayerListener implements PluginListener {

    MiscOperator miscOperator = new MiscOperator();
    DamageOperator damageOperator = new DamageOperator();
    PlayerMovementOperator playerMovementOperator = new PlayerMovementOperator();

    @HookHandler
    public void onPlayerMove(PlayerMoveHook hook) {
        playerMovementOperator.onPlayerMove(hook.getPlayer(), hook.getFrom(), hook.getTo());
    }

    @HookHandler
    public void onTeleport(TeleportHook hook) {
        if (playerMovementOperator.onPlayerTeleport(hook.getPlayer(), hook.getCurrentLocation(), hook.getDestination())) {
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onDamage(DamageHook hook) {
        if (hook.getAttacker() == null) {
            return;
        }
        if (damageOperator.onDamage(hook.getAttacker(), hook.getDefender())) {
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
            if (miscOperator.onItemDrop(hook.getItem().getLocation(), hook.getPlayer().getLocation())) {
                hook.setCanceled();
            }
        }
    }
}
