import java.util.HashMap;
import java.util.List;

import net.playblack.cuboids.InvalidPlayerException;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.ArmSwingEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockBreakEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockLeftClickEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockPhysicsEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockRightClickEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockUpdateEvent;
import net.playblack.cuboids.actions.events.forwardings.ExplosionEvent;
import net.playblack.cuboids.actions.events.forwardings.IgniteEvent;
import net.playblack.cuboids.actions.events.forwardings.LiquidFlowEvent;
import net.playblack.cuboids.actions.events.forwardings.ExplosionEvent.ExplosionType;
import net.playblack.cuboids.actions.events.forwardings.IgniteEvent.FireSource;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.Location;

/**
 * Listens to block events
 * 
 * @author Chris
 * 
 */
public class BlockListener extends PluginListener {

    private long theTime = 0L; // to nerf the armswing hooks sensitivity

    @Override
    public boolean onBlockRightClick(Player player, Block b, Item itemInHand) {
        Location p = new Location(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld().getName());
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            // fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
        BlockRightClickEvent event = new BlockRightClickEvent(cplayer, new CBlock(b.getType(), b.getData()), p);
        ActionManager.fireEvent(event);
        if(event.isCancelled()) {
            return true;
        }
        return false;
    }

    @Override
    public void onArmSwing(Player player) {
        if (System.currentTimeMillis() <= theTime + 200) {
            return;
        }

        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            // fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
        ActionManager.fireEvent(new ArmSwingEvent(cplayer));
        //TODO: Handle brush
//        BrushHandler.handleBrush(cplayer, p);
        theTime = System.currentTimeMillis(); // Set time counter
    }

    @Override
    public boolean onBlockDestroy(Player player, Block b) {
        Location p = new Location(b.getX(), b.getY(), b.getZ(),player.getWorld().getType().getId(), player.getWorld().getName());
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            // fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
        BlockLeftClickEvent event = new BlockLeftClickEvent(cplayer, new CBlock(b.getType(), b.getData()), p);
        ActionManager.fireEvent(event);
        if(event.isCancelled()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onBlockBreak(Player player, Block b) {
        Location p = new Location(b.getX(), b.getY(), b.getZ(), player.getWorld().getType().getId(), player.getWorld().getName());
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            // fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
        BlockBreakEvent event = new BlockBreakEvent(cplayer, new CBlock(b.getType(),  b.getData()), p);
        ActionManager.fireEvent(event);
        if(event.isCancelled()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onBlockPlace(Player player, Block blockPlaced, Block b,
            Item itemInHand) {
        Location p = new Location(b.getX(), b.getY(), b.getZ(),
                player.getWorld().getType().getId(), player.getWorld()
                        .getName());
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            // fallback to manually get a player
            cplayer = new CanaryPlayer(player);
        }
        return !CuboidInterface.get().canModifyBlock(cplayer, p);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public boolean onExplosion(Block b, BaseEntity e, List blocksaffected) {
        //Assemble the list of blocks ...
        HashMap<Location, CBlock> blocks = new HashMap<Location, CBlock>();
        for(Block x : (List<Block>)blocksaffected) {
            Location l = new Location(x.getX(), x.getY(), x.getZ(), x.getWorld().getType().getId(), x.getWorld().getName());
            blocks.put(l, new CBlock(x.getType(), x.getData()));
        }
        Location l = new Location(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        
        ExplosionEvent event = new ExplosionEvent(new CanaryBaseEntity(e), l, ExplosionType.fromId(b.getStatus()), blocks);
        if(event.isCancelled()) {
            return true;
        }
        //Not cancelled, process the list of blocks and remove those that should stay
        blocks = event.getAffectedBlocks();
        for(Location m : blocks.keySet()) {
            for(int i = 0; i < blocksaffected.size(); ) {
                Block x = (Block) blocksaffected.get(i);
                if(m.samePosition(x.getX(), x.getY(), x.getZ())) {
                    blocksaffected.remove(i);
                }
                else {
                    i++;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onIgnite(Block b, Player player) {
        Location p = new Location(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        CPlayer cplayer = null;
        if (player != null) {
            try {
                cplayer = CServer.getServer().getPlayer(player.getName());
            } catch (InvalidPlayerException e) {
                // fallback to manually get a player
                cplayer = new CanaryPlayer(player);
            }
        }
        IgniteEvent event = new IgniteEvent(FireSource.fromInt(b.getStatus()), p, new CBlock(b.getType(), b.getData()), cplayer);
        ActionManager.fireEvent(event);
        if(event.isCancelled()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onFlow(Block b, Block to) {
        Location p = new Location(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        
        LiquidFlowEvent event = new LiquidFlowEvent(new CBlock(b.getType(), b.getData()), new CBlock(to.getType(), to.getData()), p);
        ActionManager.fireEvent(event);
        if(event.isCancelled()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onBlockPhysics(Block b, boolean placed) {
        Location p = new Location(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        BlockPhysicsEvent event = new BlockPhysicsEvent(new CBlock(b.getType(), b.getData()), p);
        ActionManager.fireEvent(event);
        if(event.isCancelled()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onBlockUpdate(Block b, int newBlockId) {
        Location p = new Location(b.getX(), b.getY(), b.getZ(), b.getWorld().getType().getId(), b.getWorld().getName());
        BlockUpdateEvent event = new BlockUpdateEvent(new CBlock(b.getType(), b.getData()), new CBlock(newBlockId), p);
        ActionManager.fireEvent(event);
        if(event.isCancelled()) {
            return true;
        }
        return false;
    }
}
