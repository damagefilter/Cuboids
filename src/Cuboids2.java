import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.playblack.EventLogger;
import com.playblack.cuboid.CuboidMessages;
import com.playblack.cuboid.CuboidSelection;
import com.playblack.cuboid.CuboidUndoList;
import com.playblack.cuboid.tree.CuboidTreeHandler;
import com.playblack.datasource.BaseData;
import com.playblack.datasource.*;


public class Cuboids2 extends Plugin{

	public static CuboidListener listener = new CuboidListener();
	private CommandListener commands = new CommandListener();
	private BlockListener blocks = new BlockListener();
	private PlayerListener players = new PlayerListener(); //is static becuz we need the canPlayerUsecommand thing
	private ArrayList<PluginRegisteredListener> registeredListeners = new ArrayList<PluginRegisteredListener>();
	//Create custom hooks object
	public CuboidHooks hooks = new CuboidHooks();
	/*
	 * STUFF THAT IS SHARED BY MULTIPLE LISTENERS
	 */
	public static BlockOperator blockOp = new BlockOperator();
	public static HashMap<String, CuboidSelection> sel = new HashMap<String, CuboidSelection>();
	public static CuboidContentInterface content = new CuboidContentInterface();
	public static CuboidMessages msg = new CuboidMessages();
	public static EventLogger eventLog = new EventLogger(Logger.getLogger("Minecraft"));
	public static Cuboids2Config cfg = new Cuboids2Config();
	//TODO: Differentiate between faltfile and mysql data backend here and include the 
	//corresponding dataSource!
	public static BaseData ds;
	public static CuboidTreeHandler treeHandler;
	public static CuboidProtectionInterface cuboids;
	public static CuboidUndoList undoList;
	
	
	/*
	 * SOME SHARED MEMBERS 
	 */
	public static boolean noBackup = false;
	
	
	private boolean isInit = false;
	
	/**
	 * Return the name of this plugin
	 */
	@Override
	public String getName() {
		return cfg.getName();
	}
	@Override
	public void disable() {
		//etc.getServer().messageAll("Disabling Cuboids2!");
		listener.saveCuboidData();
		treeHandler.cancelSaves();
		unregisterListeners();
		registeredListeners.clear();
		eventLog.logMessage(msg.messages.get("onDisable"), "INFO");
		//log.log(Level.INFO, msg.messages.get("onDisable"));
		isInit = false;
	}

	@Override
	public void enable() {
		if(cfg.dataSource().equalsIgnoreCase("mysql")) {
			ds = new MysqlData(cfg.dataSourceConfig(), eventLog);
		}
		else {
			ds = new FlatfileData(eventLog);
		}
		treeHandler = new CuboidTreeHandler(eventLog, ds);
		cuboids = new CuboidProtectionInterface(eventLog);
		undoList = new CuboidUndoList(cfg.getUndoSteps());
		eventLog.logMessage(msg.messages.get("onEnable") + ", Version " + cfg.getVersion(), "INFO");
		//log.log(Level.INFO, msg.messages.get("onEnable")+" - Version "+cfg.getVersion());
		//TODO: Wrap this up ?
		etc.getInstance().addCommand("/cmod <name> create", "Create a new Cuboid Area with default settings");
		etc.getInstance().addCommand("/cmod <name> remove (or delete)", "Remove Cuboid Area");
		etc.getInstance().addCommand("/cmod <name> allow <player/group>", "Allow player(s) or group(s) (flag \"g:\") in this area");
		etc.getInstance().addCommand("/cmod <name> disallow <player/group>", "Disallow player(s) or group(s) (flag \"g:\") in this area");
		etc.getInstance().addCommand("/cmod <name> toggle", "Toggles area options on or off");
		etc.getInstance().addCommand("/cmod <name> resize (or move)", "Moves cuboid area to a new selection");
		etc.getInstance().addCommand("/cmod <name> prio (or priority)", "Set areas priority");
		etc.getInstance().addCommand("/cmod <name> parent", "Set areas parent area");
		etc.getInstance().addCommand("/cmod <name> info", "Print infos about the given area");
		etc.getInstance().addCommand("/cmod list", "Show a list of cuboids in the current world");
		etc.getInstance().addCommand("/protect <names> <area name>", "Shortcut: Creates new area with protection flag set with players/groups given");
		etc.getInstance().addCommand("/highprotect <names> <area name>", "Shortcut: Creates new area, expanded vertically, with protection flag set with players/groups given");
		etc.getInstance().addCommand("/cfloor <number>", "Sets the floor level of an already made selection");
		etc.getInstance().addCommand("/cceiling <number>", "Sets the ceiling level of an already made selection");	
		if(isInit == false) {
			initialize();
		}
	}
	
	@Override
	public void initialize() {
		if(isInit == false) {
			registerHook(players, "PLAYER_MOVE", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.PLAYER_MOVE, listener, this, PluginListener.Priority.MEDIUM);
			registerHook(blocks, "BLOCK_RIGHTCLICKED", "HIGH");
			//etc.getLoader().addListener(PluginLoader.Hook.BLOCK_RIGHTCLICKED, listener, this, PluginListener.Priority.HIGH);
			registerHook(blocks, "BLOCK_DESTROYED", "HIGH");
			//etc.getLoader().addListener(PluginLoader.Hook.BLOCK_DESTROYED, listener, this, PluginListener.Priority.HIGH);
			registerHook(blocks, "BLOCK_PLACE", "HIGH");
			//etc.getLoader().addListener(PluginLoader.Hook.BLOCK_PLACE, listener, this, PluginListener.Priority.HIGH);
			registerHook(blocks, "BLOCK_BROKEN", "HIGH");
			//etc.getLoader().addListener(PluginLoader.Hook.BLOCK_BROKEN, listener, this, PluginListener.Priority.HIGH);
			registerHook(commands, "COMMAND", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.COMMAND, commands, this, PluginListener.Priority.MEDIUM);
			registerHook(blocks, "EXPLODE", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.EXPLODE, listener, this, PluginListener.Priority.MEDIUM);
			registerHook(players, "DAMAGE", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.DAMAGE, listener, this, PluginListener.Priority.MEDIUM);
			registerHook(listener, "MOB_SPAWN", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.MOB_SPAWN, listener, this, PluginListener.Priority.MEDIUM);
			registerHook(blocks, "ARM_SWING", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener, this, PluginListener.Priority.MEDIUM);
			registerHook(listener, "COMMAND_CHECK", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.COMMAND_CHECK, listener, this, PluginListener.Priority.MEDIUM);
			
			//Block fire spread
			registerHook(blocks, "IGNITE", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.IGNITE, listener, this, PluginListener.Priority.MEDIUM);
			registerHook(players, "ITEM_USE", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.ITEM_USE, listener, this, PluginListener.Priority.MEDIUM);

			//Removing players from areas other than when they move out of it
			registerHook(players, "KICK", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.KICK, listener, this, PluginListener.Priority.MEDIUM);
			registerHook(players, "BAN", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.BAN, listener, this, PluginListener.Priority.MEDIUM);
			registerHook(players, "DISCONNECT", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.DISCONNECT, listener, this, PluginListener.Priority.MEDIUM);
			registerHook(players, "TELEPORT", "MEDIUM");
			//etc.getLoader().addListener(PluginLoader.Hook.TELEPORT, listener, this, PluginListener.Priority.MEDIUM);
			registerHook(listener, "FLOW", "MEDIUM");
			//Add custom listener:
			etc.getLoader().addCustomListener(hooks);
			isInit = true;
		}	
	}
	
	/**
	 * This wraps the registration of a listener at the system conveniently.
	 * for minimum refactoring when canary gets recoded
	 * @param listener The listener Object
	 * @param hook The hook type to register to as String
	 * @param prio The priority type as String
	 */
	private void registerHook(PluginListener listener, String hook, String prio) {
		registeredListeners.add(etc.getLoader().addListener(getHookType(hook), listener, this, getPriorityType(prio)));	
	}
	
	/**
	 * Removes all registered listeners from canary inclusing custom hook
	 */
	private void unregisterListeners() {
		for(PluginRegisteredListener reg : registeredListeners) {
			etc.getLoader().removeListener(reg);
		}
		etc.getLoader().removeCustomListener("CuboidAPI");
	}
	
	/**
	 * Convert a String to a Priority level type
	 * @param prio
	 * @return
	 */
	private PluginListener.Priority getPriorityType(String prio) {
		for(PluginListener.Priority priority : PluginListener.Priority.values()) {
			if(priority.toString().equalsIgnoreCase(prio)) {
				return priority;
			}
		}
		return null;
	}
	
	/**
	 * Convert a string to a hook type
	 * @param dd
	 * @return
	 */
	private PluginLoader.Hook getHookType(String dd) {
		if(dd == null) {
			return null;
		}
		for(PluginLoader.Hook hook : PluginLoader.Hook.values()) {
			if(hook.toString().equalsIgnoreCase(dd)) {
				return hook;
			}
		}
		//PluginLoader.Hook.FLOW
		return null;
	}
	
	/**
	 * This loads data from database. 
	 */
	public static void loadFromDatabase() {
		MysqlData ds = new MysqlData(cfg.dataSourceConfig(), eventLog);
		ds.loadAll(treeHandler);
	}
	
	/**
	 * This loads data from flatfile. 
	 */
	public static void loadFromFlatFile() {
		FlatfileData ds = new FlatfileData(eventLog);
		ds.loadAll(treeHandler);
	}
}
