import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.CuboidInterface;


public class MiscListener extends PluginListener{
    
    @Override
    public boolean onMobSpawn(Mob mob) {
        return CuboidInterface.getInstance().sanctuarySpawnsMobs(new CanaryMob(mob));
    }
    
    @Override
    public PluginLoader.HookResult canPlayerUseCommand(Player player, String command) {
        if(player.isAdmin()) {
            return PluginLoader.HookResult.DEFAULT_ACTION;
        }
        String[] split = command.split(" ");
        CPlayer cplayer = CServer.getServer().getPlayer(player.getName());
        if(!CuboidInterface.getInstance().commandIsRestricted(cplayer, split[0])) {
            return PluginLoader.HookResult.PREVENT_ACTION;
        }
        else {
            return PluginLoader.HookResult.DEFAULT_ACTION;
        }
    }
}
