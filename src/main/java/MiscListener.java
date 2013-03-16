import net.playblack.cuboids.InvalidPlayerException;
import net.playblack.cuboids.actions.deprecated.MiscHandler;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.CuboidInterface;

public class MiscListener extends PluginListener {

    @Override
    public boolean onMobSpawn(Mob mob) {
        return !MiscHandler.canSpawn(new CanaryMob(mob));
    }

    @Override
    public PluginLoader.HookResult canPlayerUseCommand(Player player,
            String command) {
        if (player.isAdmin()) {
            return PluginLoader.HookResult.ALLOW_ACTION;
        }
        String[] split = command.split(" ");
        CPlayer cplayer;
        try {
            cplayer = CServer.getServer().getPlayer(player.getName());
        } catch (InvalidPlayerException e) {
            cplayer = new CanaryPlayer(player);
        }
        if (CuboidInterface.get().commandIsRestricted(cplayer, split[0])) {
            return PluginLoader.HookResult.PREVENT_ACTION;
        } else {
            return PluginLoader.HookResult.DEFAULT_ACTION;
        }
    }
}
