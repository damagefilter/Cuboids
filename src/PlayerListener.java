import net.playblack.cuboids.actions.BlockActionHandler;
import net.playblack.cuboids.actions.PlayerMovementHandler;
import net.playblack.cuboids.actions.MiscHandler;
import net.playblack.cuboids.actions.ItemDropHandler;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.WorldLocation;


/**
 * Listens to player events
 * @author Chris
 *
 */
public class PlayerListener extends PluginListener {
    
    @Override
    public void onDisconnect(Player player) {
        RegionManager.getInstance().removeFromAllAreas(player.getName());
        CServer.getServer().removePlayer(player.getName());
    }
    
    @Override
    public void onBan(Player mod, Player player, String reason) {
        RegionManager.getInstance().removeFromAllAreas(player.getName());
        CServer.getServer().removePlayer(player.getName());
    }
    
    @Override
    public void onKick(Player mod, Player player, String reason) {
        RegionManager.getInstance().removeFromAllAreas(player.getName());
        CServer.getServer().removePlayer(player.getName());
    }
    
    @Override
    public void onPlayerMove(Player player, Location from, Location to) {
        WorldLocation vTo = new WorldLocation((int)to.x, (int)to.y, (int)to.z, to.dimension, to.world);
        WorldLocation vFrom = new WorldLocation((int)from.x, (int)from.y, (int)from.z, from.dimension, from.world);
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
        
        PlayerMovementHandler.handleAreaTrespassing(cplayer, vFrom, vTo);
        PlayerMovementHandler.handleCuboidAreas(cplayer, vFrom, vTo);  
    }
    
    @Override
    public boolean onTeleport(Player player, Location from, Location to) {
        if (!player.getWorld().isChunkLoaded((int)to.x, (int)to.y, (int)to.z)) {
            player.getWorld().loadChunk((int)to.x, (int)to.y, (int)to.z);
        }
        WorldLocation vTo = new WorldLocation((int)to.x, (int)to.y, (int)to.z, to.dimension, to.world);
        WorldLocation vFrom = new WorldLocation((int)from.x, (int)from.y, (int)from.z, from.dimension, from.world);
        CPlayer cplayer = CServer.getServer().refreshPlayer(player.getName());
        
        PlayerMovementHandler.handleCuboidAreas(cplayer, vFrom, vTo);
        return false; //allow tp    
    }
    
    @Override
    public boolean onDamage(PluginLoader.DamageType type, BaseEntity attacker, BaseEntity defender, int amount) {
        if(attacker == null) {
            return false;
        }
        if(defender.isPlayer()) {
            if(attacker.isMob()) {
                Player p = defender.getPlayer();
                return !MiscHandler.handleMobDamage(
                        new WorldLocation((int)p.getX(), (int)p.getY(), (int)p.getZ(), p.getWorld().getType().getId(), p.getWorld().getName()), 
                        CServer.getServer().getWorld(p.getWorld().getName(), p.getWorld().getType().getId()));
            }
            if(attacker.isPlayer()) {
                return !MiscHandler.handlePvpDamage(
                        CServer.getServer().getPlayer(attacker.getPlayer().getName()), 
                        CServer.getServer().getPlayer(defender.getPlayer().getName()));
            }
        }
        return false;
    }

    
    @Override
    public boolean onItemUse(Player player, Block blockPlaced, Block blockClicked, Item item) {
        if(player.canUseCommand("/cIgnoreRestrictions")) {
            return false; //allow
        }
        if(blockPlaced == null) {
            blockPlaced = blockClicked;
        }
        else if(blockClicked == null) {
            blockClicked = blockPlaced;
        }
        if((blockClicked == null) && (blockPlaced == null)) {
            //pretty dirty fallback but there's no other position that would be "valid enough"
            blockPlaced = new Block(0, (int)player.getX(), (int)player.getY(), (int)player.getZ());
        }
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
        WorldLocation v = new WorldLocation(blockPlaced.getX(), blockPlaced.getY(), blockPlaced.getZ(), blockPlaced.getWorld().getName());
        
        
        //25d == flint&steel
        if(item.getItemId() == 259) {
            return !BlockActionHandler.handleLighter(cplayer, new WorldLocation(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ(), blockClicked.getWorld().getName()));
        }
        //buckets
        if(item.getItemId() == 326 || item.getItemId() == 327) {
            return BlockActionHandler.handleBucketPlacement(cplayer, v);
        }
        return !BlockActionHandler.handleOperableItems(cplayer, v, item.getItemId());
    }
    
    @Override
    public boolean onItemDrop(Player player, ItemEntity item) {
        if(!(player == null)) {
            CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
            return ItemDropHandler.handleItemDrop(cplayer, cplayer.getLocation());
        }
        return false;
    }
    
    @Override
    public void onPlayerRespawn(Player player, Location vTo) {
        Location vFrom = player.getLocation();
        if(player.getName().equals("damagefilter")) {
            player.sendMessage(Colors.Rose+"PLAYER RESPAWN: ");
          player.sendMessage(Colors.Yellow+"From: " + Colors.LightGray + vFrom.x + ", " + vFrom.y + ", " + vFrom.z + ", " + vFrom.world + ", " + vFrom.dimension);
          player.sendMessage(Colors.Yellow+"To:   " + Colors.LightGray + vTo.x + ", " + vTo.y + ", " + vTo.z + ", " + vTo.world + ", " + vTo.dimension);
      }
    }
}
