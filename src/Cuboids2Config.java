import java.util.ArrayList;
import java.util.HashMap;

/**
 * This object holds the information for Cuboids2, that is everythign you can find in the properties.
 * @author Chris
 */
public class Cuboids2Config {

	//Plugin Specific information:
	private String name="Cuboids2";
	private String version = "1.7.0";
	private boolean verbose = false;
	
	//Behaviour settings
	private boolean useDoubleAction=false;
	private boolean allowProtection = true;
	private boolean allowCreeperSecure = true;
	private boolean allowSanctuary = true;
	private boolean allowSanctuarySpawnAnimals = true;
	private boolean allowHealing = true;
	private boolean allowPvp = true;
	private boolean allowFreebuild = false;
	private boolean allowFireSpreadBlock = false;
	
	//v 1.2.0
	private boolean allowTntSecure = false;
	private boolean allowLavaControl = false;
	private boolean allowWaterControl = false;
	private boolean allowFarmland = false;
	
	//v 1.4.0
	private boolean allowRestriction = false;
	
	//v 1.8.0
	private boolean allowHmobs = false;
	
	private boolean globalDisablePvp = true;
	private boolean globalDisableCreeperSecure = true;
	private boolean globalFirespreadBlock = false;
	private boolean globalSanctuary=false;
	private boolean globalSanctuaryAnimalSpawn = false;
	private boolean globalTntSecure=false;
	private boolean globalProtection=false;
	private boolean stopWaterFlow = false;
	private boolean stopLavaFlow = false;
	
	private int 	maxBlockBagSize = 750000; //this isn't used anywhere atm.
	private int 	regionItem;
	
	private int 	remoteRegionItem; //hitBlox block retrieval
	
	private int		inspectorItem;
	
	private int		sculptItem;
	
	private String dataSource;
	
	private HashMap<String, String> sqlConfig = new HashMap<String, String>(0); //mysql connection info
	private int		healPower;
	
	private long 	healDelay;
	
	private long	saveDelay;
	
	private boolean autoParent = false;
	
	//Undo config
	private int undoSteps = 5;
	private boolean jumpingUndo = false;
	private boolean allowUndo = false;
	
	//Cuboids Default Settings
	private boolean isCreeperSecureDefault = false;
	private boolean isSanctuaryDefault = false;
	private boolean isSanctuaryAnimalSpawnDefault = false;
	private boolean isHealingDefault = false;
	private boolean isAllowedPvpDefault = false;
	private boolean isProtectedDefault = false;
	private boolean isFreebuildDefault = false;
	private boolean isFireSpreadBlockDefault = false;	
	private boolean isLavaControlDefault = false;
	private boolean isWaterControlDefault = false;
	private boolean isFarmlandDefault = false;
	private boolean isTntSecureDefault = false;
	private boolean isRestrictionDefault = false;
	private boolean isHmobDefault = false;
	
	
	
	/**
	 * List of operable items to protect
	 */
	private ArrayList<Integer> operables = new ArrayList<Integer>(0); 
	
	private PropertiesFile props = null;
	private PropertiesFile dataSourceProps = null;
	private PropertiesFile pluginSettings = null;
	
	public Cuboids2Config() {
		props 			= new PropertiesFile("plugins/cuboids2/cuboid.properties");
		dataSourceProps = new PropertiesFile("plugins/cuboids2/data.properties");
		pluginSettings 	= new PropertiesFile("plugins/cuboids2/settings.properties");
		
		/*
		 * PLUGIN SETTINGS
		 */
		useDoubleAction = pluginSettings.getBoolean("use-double-action-tool", true);
		autoParent = pluginSettings.getBoolean("auto-parent", true);
		undoSteps = pluginSettings.getInt("undo-steps", 10);
		jumpingUndo = pluginSettings.getBoolean("jumping-undo", false);
		allowUndo = pluginSettings.getBoolean("allow-undo", true);
		
		regionItem = pluginSettings.getInt("selection-item", 294);
		remoteRegionItem = pluginSettings.getInt("remote-selection-item", 268);
		inspectorItem = pluginSettings.getInt("inspector-item", 283);
		sculptItem = pluginSettings.getInt("sculpt-tool-item", 352);
		
		healDelay = pluginSettings.getLong("heal-delay", 3);
		saveDelay = pluginSettings.getLong("autosave-intervall", 30);
		healPower = pluginSettings.getInt("heal-power", 1);
		
		allowProtection = pluginSettings.getBoolean("allow-protection", true);
		allowCreeperSecure = pluginSettings.getBoolean("allow-creeper-secure", true);
		allowSanctuary = pluginSettings.getBoolean("allow-sanctuary", true);
		allowSanctuarySpawnAnimals = pluginSettings.getBoolean("allow-sanctuary-animal-spawn", true);
		allowHealing = pluginSettings.getBoolean("allow-healing", true);
		allowPvp = pluginSettings.getBoolean("allow-pvp", true);
		allowFreebuild = pluginSettings.getBoolean("allow-freebuild", true);
		allowFireSpreadBlock = pluginSettings.getBoolean("allow-firespread-block", true);	
		allowTntSecure = pluginSettings.getBoolean("allow-tnt-secure", true); 
		allowLavaControl = pluginSettings.getBoolean("allow-stop-lava-flow", true);
		allowWaterControl = pluginSettings.getBoolean("allow-stop-water-flow", true);
		allowFarmland = pluginSettings.getBoolean("allow-farmland", true);
		allowRestriction = pluginSettings.getBoolean("allow-restriction", true);
		allowHmobs = pluginSettings.getBoolean("allow-hmobs", false);
		
		verbose = pluginSettings.getBoolean("verbose-logging",false);
		
		/*
		 * DATA SOURCE
		 */
		dataSource = dataSourceProps.getString("data-source", "flatfile");
		//CONFIGURE mysql connection
		if(dataSource.equalsIgnoreCase("mysql")) {
			sqlConfig.put("url", dataSourceProps.getString("sql-url","localhost"));
			//sqlConfig.put("database", dataSourceProps.getString("sql-db","minecraft"));
			sqlConfig.put("user", dataSourceProps.getString("sql-user","root"));
			sqlConfig.put("passwd", dataSourceProps.getString("sql-passwd","root"));
		}
		/*
		 * CUBOID DEFAULT SETTINGS
		 */
		//cuboid defaults
		isProtectedDefault = props.getBoolean("default-protection", true);
		isCreeperSecureDefault = props.getBoolean("default-creeper-secure", true);
		isSanctuaryDefault = props.getBoolean("default-sanctuary", false);
		isSanctuaryAnimalSpawnDefault = props.getBoolean("default-sanctuary-animal-spawn", true);
		isHealingDefault = props.getBoolean("default-healing", false);
		isAllowedPvpDefault = props.getBoolean("default-pvp-allowed", true);
		isFreebuildDefault = props.getBoolean("default-freebuild", false);
		isFireSpreadBlockDefault = props.getBoolean("default-firespread-block", false);
		
		isLavaControlDefault = props.getBoolean("default-stop-lava-flow", false);
		isWaterControlDefault = props.getBoolean("default-stop-water-flow", false);
		isFarmlandDefault = props.getBoolean("default-farmland", true);
		isTntSecureDefault = props.getBoolean("default-tnt-secure", true);
		isRestrictionDefault = props.getBoolean("default-restriction", false);
		
		isHmobDefault = props.getBoolean("default-hmob", false);

		stopLavaFlow = props.getBoolean("stop-lava-flow-global", false);
		stopWaterFlow = props.getBoolean("stop-water-flow-global", false);
		
		
		//Global Settings
		globalDisablePvp = props.getBoolean("disable-pvp-global", false);
		globalDisableCreeperSecure = props.getBoolean("disable-creeper-secure-global", false);
		globalSanctuary = props.getBoolean("sanctuary-global", false);
		globalSanctuaryAnimalSpawn = props.getBoolean("sanctuary-animal-spawn-global", true);
		globalTntSecure = props.getBoolean("tnt-secure-global", false);
		globalFirespreadBlock = props.getBoolean("firespread-block-global", false);
		globalProtection = props.getBoolean("protection-global", false);
		
		String itemTmp = props.getString("operable-items");
		String[] itemSplit = itemTmp.split(",");
		for(int i = 0; i < itemSplit.length; i++) {
			try {
				operables.add(Integer.parseInt(itemSplit[i]));
			}
			catch(NumberFormatException e) {
				Cuboids2.eventLog.logMessage("Cuboids2: Failed to load the ist of operable items. You failed at writing numbers and commas. gratz!", "SEVERE");
				//e.printStackTrace();
			}
		}
	}
	
	/**
	 * Update a config setting, this works for booleans only
	 * @param key
	 * @param value
	 */
	public void updateSetting(String key, boolean value) {
		if(props == null) {
			props = new PropertiesFile("plugins/cuboids2/cuboid.properties");
		}
		props.setBoolean(key, value);
	}
	
	
	//Version and name
	
	public String getName() {
		return name;
	}
	
	public String getVersion() {
		return version;
	}
	
	
	//Behavior
	public boolean useDoubleAction() {
		return useDoubleAction;
	}
	
	public String dataSource() {
		return dataSource;
	}
	
	public HashMap<String, String> dataSourceConfig() {
		return sqlConfig;
	}
	
	public boolean allowPvp() {
		return allowPvp;
	}
	public boolean allowProtection() {
		return allowProtection;
	}
	
	public boolean allowCreeperSecure() {
		return allowCreeperSecure;
	}
	
	public boolean allowSanctuary() {
		return allowSanctuary;
	}
	
	public boolean allowSanctuarySpawnAnimals() {
		return allowSanctuarySpawnAnimals;
	}
	
	public boolean allowHealing() {
		return allowHealing;
	}
	
	public boolean allowFreebuild() {
		return allowFreebuild;
	}
	
	public boolean allowFireSpreadBlock() {
		return allowFireSpreadBlock;
	}
	
	/////////////////////////////v 1.2.0
	public boolean allowLavaControl() {
		return allowLavaControl;
	}
	
	public boolean allowWaterControl() {
		return allowWaterControl;
	}
	
	public boolean allowTntSecure() {
		return allowTntSecure;
	}
	
	public boolean allowFarmland() {
		return allowFarmland;
	}
	
	//////////////////////////////////v 1.4.0
	
	public boolean allowRestriction() {
		return allowRestriction;
	}
	
	public boolean allowHmobs() {
		return allowHmobs;
	}
	
	public boolean autoParent() {
		return autoParent;
	}
	
	public int getUndoSteps() {
		return undoSteps;
	}
	
	public boolean allowUndo() {
		return allowUndo;
	}
	

	public boolean isJumpingUndo() {
		return jumpingUndo;
	}

	//
	// GLOBALS
	//
	public boolean globalDisablePvp() {
		return globalDisablePvp;
	}
	
	public boolean globalProtection() {
		return globalProtection;
	}
	public boolean globalDisableCreeperSecure() {
		return globalDisableCreeperSecure;
	}
	
	public boolean globalSanctuary() {
		return globalSanctuary;
	}
	
	public boolean globalTntSecure() {
		return globalTntSecure;
	}
	
	public boolean globalSanctuaryAnimalSpawn() {
		return globalSanctuaryAnimalSpawn;
	}
	
	public boolean globalFirespreadBlock() {
		return globalFirespreadBlock;
	}
	
	
	//
	//LIQUDS
	//
	public boolean stopLavaFlowGlobal() {
		return stopLavaFlow;
	}
	
	public boolean stopWaterFlowGlobal() {
		return stopWaterFlow;
	}
	public int getRegionItem() {
		return regionItem;
	}
	
	public int getRemoteSelectionItem() {
		return remoteRegionItem;
	}
	
	public int getInspectorItem() {
		return inspectorItem;
	}
	
	public int getSculptItem() {
		return sculptItem;
	}
	
	
	//Cuboid Settings
	
	public boolean isLavaControlDefault() {
		return isLavaControlDefault;
	}
	
	public boolean isWaterControlDefault() {
		return isWaterControlDefault;
	}
	
	public boolean isFarmlandDefault() {
		return isFarmlandDefault;
	}
	
	public boolean isTntSecureDefault() {
		return isTntSecureDefault;
	}
	
	public boolean isProtectedDefault() {
		return isProtectedDefault;
	}
	
	public boolean isCreeperSecureDefault() {
		return isCreeperSecureDefault;
	}
	
	public boolean isSanctuaryDefault() {
		return isSanctuaryDefault;
	}
	
	public boolean isSanctuaryAnimalSpawnDefault() {
		return isSanctuaryAnimalSpawnDefault;
	}
	
	
	public boolean isHealingDefault() {
		return isHealingDefault;
	}
	
	public boolean isAllowedPvpDefault() {
		return isAllowedPvpDefault;
	}
	
	public boolean isFireSpreadBlockDefault() {
		return isFireSpreadBlockDefault;
	}
	
	public boolean isFreebuildDefault() {
		return isFreebuildDefault;
	}
	
	public boolean isRestrictionDefault() {
		return isRestrictionDefault;
	}
	
	public boolean isHmobDefault() {
		return isHmobDefault;
	}
	
	public long getHealDelay() {
		return (healDelay*6000);
	}
	
	public long getSaveDelay() {
		return (saveDelay*60000);
	}
	
	public int getHealPower() {
		return healPower;
	}
	

	public int getMaxBlockBagSize() {
		return maxBlockBagSize;
	}
	
	/*
	 * SETTERS FOR TOGGABLE CONFIG SETTINGS
	 */
	/**
	 * False if you want to enable global pvp
	 * @param pvp
	 */
	public void setGlobalPvp(boolean pvp) {
		globalDisablePvp = pvp;
	}
	
	public void setGlobalFirespreadBlock(boolean firespread) {
		globalFirespreadBlock = firespread;
	}
	
	public void setGlobalDisableCreeperSecure(boolean creeper) {
		globalDisableCreeperSecure = creeper;
	}
	
	public void setTntGlobal(boolean tnt) {
		globalTntSecure = tnt;
	}
	
	public void setGlobalSanctuary(boolean sanctuary) {
		globalSanctuary = sanctuary;
	}
	
	public void setGlobalSanctuarySpawnAnimals(boolean spawn) {
		globalSanctuaryAnimalSpawn = spawn;
	}
	
	public void setGlobalProtection(boolean protection) {
		globalProtection = protection;
	}
	
	public void setStopLavaFlow(boolean flow) {
		stopLavaFlow = flow;
	}
	
	public void setStopWaterFlow(boolean flow) {
		stopWaterFlow = flow;
	}
	
	public boolean isVerbose() {
		return verbose;
	}
	
	//END
	
	/**
	 * Retrieve the area settings as boolean array list
	 * Used for copying properties to child cuboids when adding new ones.
	 * @return
	 */
	public HashMap<String,Boolean> getDefaultCuboidSettings(Player player) {
		HashMap<String,Boolean> flags = new HashMap<String, Boolean>();
		if(allowPvp && (player.canUseCommand("/cpvp") || player.canUseCommand("/cIgnoreRestrictions"))) {
			flags.put("allowPvp",this.isAllowedPvpDefault);
		}
		else {
			flags.put("allowPvp",true);
		}
		
		if(allowCreeperSecure && (player.canUseCommand("/ccreeper") || player.canUseCommand("/cIgnoreRestrictions"))) {
			flags.put("creeperSecure",this.isCreeperSecureDefault);
		}
		else {
			flags.put("creeperSecure",false);
		}
		
		if(allowHealing && (player.canUseCommand("/cheal") || player.canUseCommand("/cIgnoreRestrictions"))) {
			flags.put("healing",this.isHealingDefault);	
		}
		else {
			flags.put("healing",false);
		}
		
		if(allowProtection && (player.canUseCommand("/cprotection") || player.canUseCommand("/cIgnoreRestrictions"))) {
			flags.put("protection",this.isProtectedDefault);
		}
		else {
			flags.put("protection",false);
		}
		
		if(allowSanctuary && (player.canUseCommand("/csanctuary") || player.canUseCommand("/cIgnoreRestrictions"))) {
			flags.put("sanctuary",this.isSanctuaryDefault);
		}
		else {
			flags.put("sanctuary",false);
		}
		
		if(allowFreebuild && (player.canUseCommand("/cfreebuild") || player.canUseCommand("/cIgnoreRestrictions"))) {
			flags.put("freeBuild", this.isFreebuildDefault);
		}
		else {
			flags.put("freeBuild", false);
		}
		
		if(allowFireSpreadBlock && (player.canUseCommand("/cfirespread") || player.canUseCommand("/cIgnoreRestrictions"))) {
			flags.put("blockFireSpread", this.isFireSpreadBlockDefault);
		}
		else {
			flags.put("blockFireSpread", false);
		}
		
		if(allowLavaControl && (player.canUseCommand("/cliquidcontrol") || player.canUseCommand("/cIgnoreRestrictions"))) {
			flags.put("lavaControl", this.isLavaControlDefault);
		}
		else {
			flags.put("lavaControl", false);
		}
		
		if(allowWaterControl && (player.canUseCommand("/cliquidcontrol") || player.canUseCommand("/cIgnoreRestrictions"))) {
			flags.put("waterControl", this.isLavaControlDefault);
		}
		else {
			flags.put("waterControl", false);
		}
		
		if(allowTntSecure && (player.canUseCommand("/ctnt") || player.canUseCommand("/cIgnoreRestrictions"))) {
			flags.put("tntSecure", this.isTntSecureDefault);
		}
		else {
			flags.put("tntSecure", false);
		}
		
		if(allowFarmland && (player.canUseCommand("/cfarmland") || player.canUseCommand("/cIgnoreRestrictions"))) {
			flags.put("farmland", this.isFarmlandDefault);
		}
		else {
			flags.put("farmland", false);
		}
		
		if(allowRestriction && (player.canUseCommand("/crestrict") || player.canUseCommand("/cIgnoreRestrictions"))) {
			flags.put("restriction", this.isRestrictionDefault);
		}
		else {
			flags.put("restriction", false);
		}
		
		if(allowHmobs && (player.canUseCommand("/chmob")) || player.canUseCommand("/cIgnoreRestrictions")) {
			flags.put("hmob", this.isHmobDefault);
		}
		else {
			flags.put("hmob", false);
		}
		
		flags.put("sanctuarySpawnAnimals", this.isSanctuaryAnimalSpawnDefault);
		return flags;
		}
	
	/**
	 * Checks if the given item id is within the operables list
	 * @param i
	 * @return true if so, false otherwise
	 */
	public boolean isInOperables(int i) {
		Integer in = new Integer(i);
		return operables.contains(in);
	}
}
