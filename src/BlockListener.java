import java.util.HashSet;

import net.playblack.cuboids.actions.BlockActionHandler;
import net.playblack.cuboids.actions.BrushHandler;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.WorldLocation;


/**
 * Listens to block events events
 * @author Chris
 *
 */
public class BlockListener extends PluginListener {
    
    private long theTime = 0L; //to nerf the armswing hooks sensitivity
    @Override
    public boolean onBlockRightClick(Player player, Block b, Item itemInHand) {
//        EventLogger.getInstance().logMessage("BlockRightClick...", "DEBUG");
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
//        EventLogger.getInstance().logMessage("Player: "+cplayer.toString()+" ...", "DEBUG");
//        EventLogger.getInstance().logMessage("Position: "+p.toString(), "DEBUG");
//        EventLogger.getInstance().logMessage("Handling right click action", "DEBUG");
        BlockActionHandler.explainPosition(cplayer, p);
        boolean pointResult = BlockActionHandler.handleSetPoints(
                cplayer, 
                p, 
                true, false);
        if(!pointResult) {
            return !BlockActionHandler.handleOperableItems(
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
//        EventLogger.getInstance().logMessage("ArmSwing...", "DEBUG");
        if(System.currentTimeMillis() <= theTime+200) {
            //break the operation if not enough time has passed.
            //this is applied to prevent onArmSwing from beeing called uncontrollably
            return;
        }
        
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
//        EventLogger.getInstance().logMessage("Player: "+cplayer.toString()+" ...", "DEBUG");
        HitBlox hb = new HitBlox(player);
        Block b = hb.getFaceBlock();
        if(b == null) {
            return;
        }
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
//        EventLogger.getInstance().logMessage("Position" + p.toString(), "DEBUG");
        BlockActionHandler.handleSetPoints(cplayer, p, false, true);
        BrushHandler.handleBrush(cplayer, p);
        theTime = System.currentTimeMillis(); //Set time counter
    }
    
    @Override
    public boolean onBlockDestroy(Player player, Block b) {
//        EventLogger.getInstance().logMessage("BlockDestroy...", "DEBUG");
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
//        EventLogger.getInstance().logMessage("Player: "+cplayer.toString()+" ...", "DEBUG");
//        EventLogger.getInstance().logMessage("Position" + p.toString(), "DEBUG");
        boolean pointResult = BlockActionHandler.handleSetPoints(
                cplayer, 
                p, 
                false, false);
        if(!pointResult) {
            return !BlockActionHandler.handleOperableItems(
                    cplayer, 
                    p, 
                    b.getType());
        }
        else {
            return true;
        }
    }
    
    @Override
    public boolean onBlockBreak(Player player, Block b) {
//        EventLogger.getInstance().logMessage("BlockBreak...", "DEBUG");
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
//        EventLogger.getInstance().logMessage("Player: "+cplayer.toString()+" ...", "DEBUG");
//        EventLogger.getInstance().logMessage("Position" + p.toString(), "DEBUG");
        
        return !CuboidInterface.getInstance().canModifyBlock(cplayer, p);
    }
    
    @Override
    public boolean onBlockPlace(Player player, Block blockPlaced, Block b, Item itemInHand) {
//        EventLogger.getInstance().logMessage("BlockPlace...", "DEBUG");
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
//        EventLogger.getInstance().logMessage("Player: "+cplayer.toString()+" ...", "DEBUG");
//        EventLogger.getInstance().logMessage("Position" + p.toString(), "DEBUG");
        return !CuboidInterface.getInstance().canModifyBlock(cplayer, p);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean onExplode(Block b, OEntity entity, HashSet blocksaffected) {
//        EventLogger.getInstance().logMessage("onExplode...", "DEBUG");
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        CWorld world = CServer.getServer().getWorld(b.getWorld().getName(), b.getWorld().getType().getId());
//        EventLogger.getInstance().logMessage("World: "+world.toString()+" ...", "DEBUG");
//        EventLogger.getInstance().logMessage("Position" + p.toString(), "DEBUG");
        return BlockActionHandler.handleExplosions(world, b.getStatus(), p);
    }
    
    @Override
    public boolean onIgnite(Block b, Player player) {
        //EventLogger.getInstance().logMessage("onIgnite...", "DEBUG");
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        CPlayer cplayer = null;
       // EventLogger.getInstance().logMessage("Position" + p.toString(), "DEBUG");
        if(player != null) {
            cplayer = CServer.getServer().getPlayer(player.getName());
        }
        return !BlockActionHandler.handleIgnition(cplayer, p, b.getStatus());
    }
    
    @Override
    public boolean onFlow(Block b, Block blockTo) {
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        CBlock block = new CBlock(b.getType(), b.getData());
        return !BlockActionHandler.handleFlow(block, p);
    }
    
    @Override
    public boolean onBlockPhysics(Block b, boolean placed) {
        return BlockActionHandler.handlePhysics(new WorldLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName()),
                CServer.getServer().getWorld(b.getWorld().getName(), b.getWorld().getType().getId()), 
                b.getType());
    }
    
    @Override
    public boolean onBlockUpdate(Block b, int newBlockId) {
        CWorld world = CServer.getServer().getWorld(b.getWorld().getName(), b.getWorld().getType().getId());
        return BlockActionHandler.handleFarmland(new WorldLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName()), world, b.getType(), newBlockId);
    }
}
