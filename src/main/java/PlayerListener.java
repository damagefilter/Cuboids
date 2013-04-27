
import net.playblack.cuboids.InvalidPlayerException;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.EntityDamageEvent;
import net.playblack.cuboids.actions.events.forwardings.EntityDamageEvent.DamageSource;
import net.playblack.cuboids.actions.events.forwardings.ItemDropEvent;
import net.playblack.cuboids.actions.events.forwardings.PlayerWalkEvent;
import net.playblack.cuboids.blocks.CItem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;

/**
 * Listens to player events
 *
 * @author Chris
 *
 */
public class PlayerListener extends PluginListener {

    @Override
    public void onDisconnect(Player player) {
        CServer.getServer().removePlayer(player.getName());
    }

    @Override
    public void onBan(Player mod, Player player, String reason) {
        CServer.getServer().removePlayer(player.getName());
    }

    @Override
    public void onKick(Player mod, Player player, String reason) {
        CServer.getServer().removePlayer(player.getName());
    }

    @Override
    public void onPlayerMove(Player player, Location from, Location to) {
        net.playblack.mcutils.Location vTo = new net.playblack.mcutils.Location(to.x, to.y, to.z, to.dimension, to.world);
//        ToolBox.adjustWorldPosition(vTo);
        net.playblack.mcutils.Location vFrom = new net.playblack.mcutils.Location(from.x, from.y, from.z, from.dimension, from.world);
//        ToolBox.adjustWorldPosition(vFrom);
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            // Fallback
            cplayer = new CanaryPlayer(player);
        }

        PlayerWalkEvent event = new PlayerWalkEvent(cplayer, vFrom, vTo);
        ActionManager.fireEvent(event);
        //event.isCancelled() has no effect here
    }

    @Override
    public boolean onTeleport(Player player, Location from, Location to) {
        if (!player.getWorld().isChunkLoaded((int) to.x, (int) to.y, (int) to.z)) {
            player.getWorld().loadChunk((int) to.x, (int) to.y, (int) to.z);
        }
        net.playblack.mcutils.Location vTo = new net.playblack.mcutils.Location(to.x, to.y, to.z, to.dimension, to.world);
//        ToolBox.adjustWorldPosition(vTo);
        net.playblack.mcutils.Location vFrom = new net.playblack.mcutils.Location(from.x, from.y, from.z, from.dimension, from.world);
//        ToolBox.adjustWorldPosition(vFrom);
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().refreshPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            // Fallback
            cplayer = new CanaryPlayer(player);
        }
        PlayerWalkEvent event = new PlayerWalkEvent(cplayer, vFrom, vTo);
        ActionManager.fireEvent(event);
        if(event.isCancelled()) {
            return true;
        }
        return false; // allow tp
    }

    @Override
    public HookParametersDamage onDamage(HookParametersDamage hook) {
        if (hook.getAttacker() == null) {
            return hook;
        }

        CanaryBaseEntity a = new CanaryBaseEntity(hook.getAttacker());
        CanaryBaseEntity d = new CanaryBaseEntity(hook.getDefender());
        DamageSource ds = damageSourceFromCanary(hook.getDamageSource().getDamageType());
        EntityDamageEvent event = new EntityDamageEvent(a, d, ds, hook.getDamageAmount());
        ActionManager.fireEvent(event);
        if(event.isCancelled()) {
            hook.setCanceled();
        }
        return hook;
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

    @Override
    public boolean onItemDrop(Player player, ItemEntity item) {
        if (!(player == null)) {
            CPlayer cplayer;
            try {
                cplayer = CServer.getServer().getPlayer(player.getName());
            } catch (InvalidPlayerException e) {
                // Fallback
                cplayer = new CanaryPlayer(player);
            }
            ItemDropEvent event = new ItemDropEvent(new CItem(item.getId(), item.getItem().getDamage(), item.getItem().getAmount(), item.getItem().getSlot()), cplayer);
            ActionManager.fireEvent(event);
            if(event.isCancelled()) {
                return true;
            }
            return false;
        }
        return false;
    }

    private EntityDamageEvent.DamageSource damageSourceFromCanary(DamageType type) {
        switch(type) {
            case ANVIL:
                return DamageSource.FALLING_ANVIL;
            case CACTUS:
                return DamageSource.CACTUS;
            case CREEPER_EXPLOSION:
                return DamageSource.CREEPER_EXPLOSION;
            case ENDERPEARL:
                return DamageSource.ENDERPEARL;
            case ENTITY:
                return DamageSource.ENTITY;
            case EXPLOSION:
                return DamageSource.EXPLOSION;
            case FALL:
                return DamageSource.FALL;
            case FALLING_BLOCK:
                return DamageSource.FALLING_BLOCK;
            case FIRE:
                return DamageSource.FIRE;
            case FIRE_TICK:
                return DamageSource.FIRE_TICK;
            case LAVA:
                return DamageSource.LAVA;
            case LIGHTNING:
                return DamageSource.LIGHTING;
            case POTION:
                return DamageSource.POTION;
            case STARVATION:
                return DamageSource.STARVATION;
            case SUFFOCATION:
                return DamageSource.SUFFOCATION;
            case WATER:
                return DamageSource.WATER;
            case WITHER:
                return DamageSource.WITHER_SKULL;
            default:
                return DamageSource.GENERIC;
        }
    }
}
