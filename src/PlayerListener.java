import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.actions.BlockActionHandler;
import net.playblack.cuboids.actions.HandleDamage;
import net.playblack.cuboids.actions.HandlePlayerMovement;
import net.playblack.cuboids.actions.ItemDropHandler;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Vector;


/**
 * Listens to player events
 * @author Chris
 *
 */
public class PlayerListener extends PluginListener {
    
    @Override
    public void onDisconnect(Player player) {
        RegionManager.getInstance().removeFromAllAreas(player.getName());
    }
    
    @Override
    public void onBan(Player mod, Player player, String reason) {
        RegionManager.getInstance().removeFromAllAreas(player.getName());
    }
    
    @Override
    public void onKick(Player mod, Player player, String reason) {
        RegionManager.getInstance().removeFromAllAreas(player.getName());
    }
    
    @Override
    public void onPlayerMove(Player player, Location from, Location to) {
        Vector vTo = new Vector(to.x, to.y, to.z);
        Vector vFrom = new Vector(from.x, from.y, from.z);
        CPlayer cplayer = SessionManager.getInstance().getPlayer(player.getName());
        
        HandlePlayerMovement.handleAreaTrespassing(cplayer, vFrom, vTo);
        HandlePlayerMovement.handleCuboidAreas(cplayer, vFrom, vTo);  
    }
    
    @Override
    public boolean onTeleport(Player player, Location from, Location to) {
        if (!player.getWorld().isChunkLoaded((int)to.x, (int)to.y, (int)to.z)) {
            player.getWorld().loadChunk((int)to.x, (int)to.y, (int)to.z);
        }
        Vector vTo = new Vector(to.x, to.y, to.z);
        Vector vFrom = new Vector(from.x, from.y, from.z);
        CPlayer cplayer = SessionManager.getInstance().getPlayer(player.getName());
        
        HandlePlayerMovement.handleCuboidAreas(cplayer, vFrom, vTo);
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
                return !HandleDamage.handleMobDamage(
                        new Vector(p.getX(), p.getY(), p.getZ()), 
                        CServer.getServer().getWorld(p.getWorld().getName(), p.getWorld().getType().getId()));
            }
            if(attacker.isPlayer()) {
                return !HandleDamage.handlePvpDamage(
                        CServer.getServer().getPlayer(attacker.getName()), 
                        CServer.getServer().getPlayer(defender.getName()));
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
        Vector v = new Vector(blockPlaced.getX(), blockPlaced.getY(), blockPlaced.getZ());
        
        
        //25d == flint&steel
        if(item.getItemId() == 259) {
            return !BlockActionHandler.handleLighter(cplayer, new Vector(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ()));
        }
        //buckets
        if(item.getItemId() == 326 || item.getItemId() == 327) {
            return BlockActionHandler.handleBucketPlacement(cplayer, v);
        }
        return !BlockActionHandler.handleOperableItems(cplayer, v, item.getItemId());
    }
    
    @Override
    public boolean onItemDrop(Player player, ItemEntity item) {
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
        return ItemDropHandler.handleItemDrop(cplayer, cplayer.getPosition());
    }
}
