import java.util.List;

import net.playblack.cuboids.InvalidPlayerException;
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
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld().getName());
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            //fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
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
        if(System.currentTimeMillis() <= theTime+200) {
            return;
        }
        
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            //fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
        HitBlox hb = new HitBlox(player);
        Block b = hb.getFaceBlock();
        if(b == null) {
            return;
        }
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld().getName());
        BlockActionHandler.handleSetPoints(cplayer, p, false, true);
        BrushHandler.handleBrush(cplayer, p);
        theTime = System.currentTimeMillis(); //Set time counter
    }
    
    @Override
    public boolean onBlockDestroy(Player player, Block b) {
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld().getName());
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            //fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
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
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld().getName());
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            //fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
        
        return !CuboidInterface.getInstance().canModifyBlock(cplayer, p);
    }
    
    @Override
    public boolean onBlockPlace(Player player, Block blockPlaced, Block b, Item itemInHand) {
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld().getName());
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            //fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
        return !CuboidInterface.getInstance().canModifyBlock(cplayer, p);
    }
    
    
    @SuppressWarnings("rawtypes")
    @Override
    public boolean onExplosion(Block b, BaseEntity e, List blocksaffected) {
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), e.getWorld().getType().getId(), e.getWorld().getName());
        return BlockActionHandler.handleExplosions(b.getStatus(), p);
    }

    
    @Override
    public boolean onIgnite(Block b, Player player) {
        WorldLocation p = new WorldLocation(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        CPlayer cplayer = null;
        if(player != null) {
            try {
                cplayer = CServer.getServer().getPlayer(player.getName());
            } catch (InvalidPlayerException e) {
                //fallback to manually get a player
                cplayer = new CanaryPlayer(player);
            }
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
