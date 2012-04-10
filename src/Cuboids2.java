import net.playblack.cuboids.Bootstrapper;
import net.playblack.cuboids.converters.Loader;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.RegionManager;


public class Cuboids2 extends Plugin {

    @Override
    public void disable() {
        CuboidInterface.getInstance().killTasks();
        RegionManager.getInstance().save(false, true);
    }

    @Override
    public void enable() {
        Loader[] loaders = new Loader[] {new CuboidDLoader()};
        new Bootstrapper(new CanaryServer(), loaders);
        
        PlayerListener playerListener = new PlayerListener();
        BlockListener blockListener = new BlockListener();
        MiscListener miscListener = new MiscListener();
        CommandListener commandListener = new CommandListener();
        
        etc.getLoader().addListener(PluginLoader.Hook.PLAYER_MOVE, playerListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_RIGHTCLICKED, blockListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_DESTROYED, blockListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_PLACE, blockListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_BROKEN, blockListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.COMMAND, commandListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.EXPLODE, blockListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.DAMAGE, playerListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.MOB_SPAWN, miscListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.COMMAND_CHECK, miscListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.IGNITE, blockListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.ITEM_USE, playerListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, blockListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.KICK, playerListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.BAN, playerListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.DISCONNECT, playerListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.TELEPORT, playerListener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.FLOW, blockListener, this, PluginListener.Priority.MEDIUM);
    }

}
