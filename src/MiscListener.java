

import net.playblack.cuboids.actions.BlockActionHandler;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.Vector;


public class MiscListener extends PluginListener{
    
    @Override
    public boolean onMobSpawn(Mob mob) {
        return !CuboidInterface.getInstance().sanctuarySpawnsMobs(new CanaryMob(mob));
    }
    
    @Override
    public PluginLoader.HookResult canPlayerUseCommand(Player player, String command) {
        if(player.isAdmin()) {
            return PluginLoader.HookResult.ALLOW_ACTION;
        }
        String[] split = command.split(" ");
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
        if(CuboidInterface.getInstance().commandIsRestricted(cplayer, split[0])) {
            return PluginLoader.HookResult.PREVENT_ACTION;
        }
        else {
            return PluginLoader.HookResult.DEFAULT_ACTION;
        }
    }
    
    @Override
    public boolean onEndermanPickup(Enderman entity, Block block) {
        CWorld world = CServer.getServer().getWorld(entity.getWorld().getName(), entity.getWorld().getType().getId());
        return BlockActionHandler.handleEndermanPickup(new Vector(block.getX(), block.getY(), block.getZ()), world);
    }
}
