import java.util.HashSet;

import net.playblack.cuboids.actions.BlockActionHandler;
import net.playblack.cuboids.actions.BrushHandler;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.Vector;


/**
 * Listens to block events events
 * @author Chris
 *
 */
public class BlockListener extends PluginListener {
    
    private long theTime = 0L; //to nerf the armswing hooks sensitivity
    @Override
    public boolean onBlockRightClick(Player player, Block b, Item itemInHand) {
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
        
        BlockActionHandler.explainPosition(cplayer, p);
        boolean pointResult = BlockActionHandler.handleSetPoints(
                cplayer, 
                p, 
                true);
        if(!pointResult) {
            return BlockActionHandler.handleOperableItems(
                    cplayer, 
                    p, 
                    b.getType());
        }
        else {
            return true;
        }
    }
    
    @Override
    public void onArmSwing(Player player) {
        if(System.currentTimeMillis() <= theTime+200) {
            //break the operation if not enough time has passed.
            //this is applied to prevent onArmSwing from beeing called uncontrollably
            return;
        }
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
        HitBlox hb = new HitBlox(player);
        Block b = hb.getFaceBlock();
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
        BlockActionHandler.handleSetPoints(cplayer, p, false);
        BrushHandler.handleBrush(cplayer, p);
        theTime = System.currentTimeMillis(); //Set time counter
    }
    
    @Override
    public boolean onBlockDestroy(Player player, Block b) {
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
        
        boolean pointResult = BlockActionHandler.handleSetPoints(
                cplayer, 
                p, 
                true);
        if(!pointResult) {
            return BlockActionHandler.handleOperableItems(
                    cplayer, 
                    p, 
                    b.getType());
        }
        else {
            return true;
        }
    }
    
    @Override
    public boolean onBlockPlace(Player player, Block blockPlaced, Block b, Item itemInHand) {
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
        
        return !CuboidInterface.getInstance().canModifyBlock(cplayer, p);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean onExplode(Block b, OEntity entity, HashSet blocksaffected) {
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
        CWorld world = CServer.getServer().getWorld(b.getWorld().getName(), b.getWorld().getType().getId());
        BlockActionHandler.handleExplosions(world, p);
    }
    
    @Override
    public boolean onIgnite(Block b, Player player) {
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
        return !BlockActionHandler.handleIgnition(cplayer, p, cplayer.getWorld(), b.getStatus());
    }
}
