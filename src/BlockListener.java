import java.util.HashSet;

import net.playblack.cuboids.actions.BlockActionHandler;
import net.playblack.cuboids.actions.BrushHandler;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.CuboidInterface;
//import net.playblack.mcutils.EventLogger;
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
//        EventLogger.getInstance().logMessage("BlockRightClick...", "DEBUG");
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
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
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
//        EventLogger.getInstance().logMessage("Position" + p.toString(), "DEBUG");
        BlockActionHandler.handleSetPoints(cplayer, p, false, true);
        BrushHandler.handleBrush(cplayer, p);
        theTime = System.currentTimeMillis(); //Set time counter
    }
    
    @Override
    public boolean onBlockDestroy(Player player, Block b) {
//        EventLogger.getInstance().logMessage("BlockDestroy...", "DEBUG");
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
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
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
//        EventLogger.getInstance().logMessage("Player: "+cplayer.toString()+" ...", "DEBUG");
//        EventLogger.getInstance().logMessage("Position" + p.toString(), "DEBUG");
        
        return !CuboidInterface.getInstance().canModifyBlock(cplayer, p);
    }
    
    @Override
    public boolean onBlockPlace(Player player, Block blockPlaced, Block b, Item itemInHand) {
//        EventLogger.getInstance().logMessage("BlockPlace...", "DEBUG");
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
//        EventLogger.getInstance().logMessage("Player: "+cplayer.toString()+" ...", "DEBUG");
//        EventLogger.getInstance().logMessage("Position" + p.toString(), "DEBUG");
        return !CuboidInterface.getInstance().canModifyBlock(cplayer, p);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean onExplode(Block b, OEntity entity, HashSet blocksaffected) {
//        EventLogger.getInstance().logMessage("onExplode...", "DEBUG");
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
        CWorld world = CServer.getServer().getWorld(b.getWorld().getName(), b.getWorld().getType().getId());
//        EventLogger.getInstance().logMessage("World: "+world.toString()+" ...", "DEBUG");
//        EventLogger.getInstance().logMessage("Position" + p.toString(), "DEBUG");
        return BlockActionHandler.handleExplosions(world, p);
    }
    
    @Override
    public boolean onIgnite(Block b, Player player) {
        //EventLogger.getInstance().logMessage("onIgnite...", "DEBUG");
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
        CPlayer cplayer = null;
        CWorld world = null;
       // EventLogger.getInstance().logMessage("Position" + p.toString(), "DEBUG");
        if(player != null) {
            cplayer = CServer.getServer().getPlayer(player.getName());
            world = cplayer.getWorld();
        }
        else {
            world = CServer.getServer().getWorld(b.getWorld().getName(), b.getWorld().getType().getId());
        }
//        EventLogger.getInstance().logMessage("World: "+world.toString()+" ...", "DEBUG");
//        EventLogger.getInstance().logMessage("Player: "+cplayer.toString()+" ...", "DEBUG");
        return !BlockActionHandler.handleIgnition(cplayer, p, world, b.getStatus());
    }
    
    @Override
    public boolean onFlow(Block b, Block blockTo) {
        Vector p = new Vector(b.getX(), b.getY(), b.getZ());
        CBlock block = new CBlock(b.getType(), b.getData());
        CWorld world = CServer.getServer().getWorld(b.getWorld().getName(), b.getWorld().getType().getId());
        return !BlockActionHandler.handleFlow(block, p, world);
    }
    
    @Override
    public boolean onBlockPhysics(Block block, boolean placed) {
        return BlockActionHandler.handlePhysics(new Vector(block.getX(), block.getY(), block.getZ()), 
                CServer.getServer().getWorld(block.getWorld().getName(), block.getWorld().getType().getId()), 
                block.getType());
    }
    
    @Override
    public boolean onBlockUpdate(Block b, int newBlockId) {
        CWorld world = CServer.getServer().getWorld(b.getWorld().getName(), b.getWorld().getType().getId());
        return BlockActionHandler.handleFarmland(new Vector(b.getX(), b.getY(), b.getZ()), world, b.getType(), newBlockId);
    }
}
