package net.playblack.cuboids;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import net.playblack.cuboids.datasource.BaseData;
import net.playblack.cuboids.datasource.FlatfileData;
import net.playblack.cuboids.datasource.MysqlData;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.Cuboid;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.PropsFile;

/**
 * Configuration settings.
 * 
 * @author Chris
 * 
 */
public class Config {
    private String name = "Cuboids2";
    private String version = "3.0.0";
    private boolean verbose = false;

    // --- REMOVE THOSE
    private boolean allowProtection = true;
    private boolean allowCreeperSecure = true;
    private boolean allowSanctuary = true;
    private boolean allowSanctuarySpawnAnimals = true;
    private boolean allowHealing = true;
    private boolean allowPvp = true;
    private boolean allowFreebuild = false;
    private boolean allowFireSpreadBlock = false;
    private boolean allowTntSecure = false;
    private boolean allowLavaControl = false;
    private boolean allowWaterControl = false;
    private boolean allowFarmland = false;
    private boolean allowRestriction = false;
    private boolean allowHmobs = false;
    private boolean allowWelcome = false;
    private boolean allowFarewell = false;
    private boolean allowPhysics = false;
    private boolean allowEnderControl = false;
    // --- UP TO HERE
    
    // global settings go into this
    private CuboidE global = new CuboidE();

    // Plugin Settings
    private boolean useDoubleAction = false;
    private int regionItem;
    private int remoteRegionItem;
    private int inspectorItem;
    private int sculptItem;
    private HashMap<String, String> sqlConfig = null;// = new HashMap<String,
                                                     // String>(0); //<- do not
                                                     // init before we know if
                                                     // we need it at all!
    private int healPower;
    private int healDelay;
    private int saveDelay;
    private boolean autoParent = true;

    // Undo config
    private int undoSteps = 5;
    private boolean allowUndo = false;

    // Cuboids Default Settings
    HashMap<String, Cuboid.Status> defaultSettings = new HashMap<String, Cuboid.Status>();

    ArrayList<Integer> restrictedItems;

    private static Config instance = null;

    private Config() {
        PropsFile pluginSetting = new PropsFile(
                "plugins/cuboids2/settings.properties");
        PropsFile cuboidSetting = new PropsFile(
                "plugins/cuboids2/cuboid.properties");
        PropsFile dsSetting = new PropsFile("plugins/cuboids2/data.properties");

        // Read plugin settings!
        useDoubleAction = pluginSetting.getBoolean("use-double-action-tool",
                false);
        regionItem = pluginSetting.getInt("selection-item", 294);
        remoteRegionItem = pluginSetting.getInt("remote-selection-item", 268);
        inspectorItem = pluginSetting.getInt("inspector-item", 283);
        sculptItem = pluginSetting.getInt("sculpt-tool-item", 352);
        healPower = pluginSetting.getInt("heal-power", 1);
        healDelay = pluginSetting.getInt("heal-delay", 3);
        saveDelay = pluginSetting.getInt("autosave-intervall", 30);
        undoSteps = pluginSetting.getInt("undo-steps", 5);
        allowUndo = pluginSetting.getBoolean("allow-undo", true);

        // Enable/Disable plugin features
        allowProtection = pluginSetting.getBoolean("allow-protection", true);
        allowCreeperSecure = pluginSetting.getBoolean("allow-creeper-secure",
                true);
        allowSanctuary = pluginSetting.getBoolean("allow-sanctuary", true);
        allowSanctuarySpawnAnimals = pluginSetting.getBoolean(
                "allow-sanctuary-animal-spawn", true);
        allowHealing = pluginSetting.getBoolean("allow-healing", true);
        allowPvp = pluginSetting.getBoolean("allow-pvp", true);
        allowFreebuild = pluginSetting.getBoolean("allow-freebuild", true);
        allowFireSpreadBlock = pluginSetting.getBoolean(
                "allow-firespread-block", true);
        allowTntSecure = pluginSetting.getBoolean("allow-tnt-secure", true);
        allowLavaControl = pluginSetting.getBoolean("allow-stop-lava-flow",
                true);
        allowWaterControl = pluginSetting.getBoolean("allow-stop-water-flow",
                true);
        allowFarmland = pluginSetting.getBoolean("allow-farmland", true);
        allowRestriction = pluginSetting.getBoolean("allow-restriction", true);
        allowHmobs = pluginSetting.getBoolean("allow-hmobs", false);
        allowWelcome = pluginSetting.getBoolean("allow-welcome-message", true);
        allowFarewell = pluginSetting
                .getBoolean("allow-farewell-message", true);
        allowPhysics = pluginSetting.getBoolean("allow-physics-control", true);
        allowEnderControl = pluginSetting.getBoolean("allow-enderman-control",
                true);
        verbose = pluginSetting.getBoolean("verbose", true);

        // Default setting for new cuboids
        
        //default-creeper-secure
        defaultSettings.put("creeper-explosion", cuboidSetting.getStatus("default-creeper-explosion", Cuboid.Status.DENY));
        
        //default-sanctuary
        defaultSettings.put("mob-damage", cuboidSetting.getStatus("default-mob-damage", Cuboid.Status.ALLOW));
        
        //default sanctuary too
        defaultSettings.put("mob-spawn", cuboidSetting.getStatus("default-mob-spawn", Cuboid.Status.ALLOW));
        
        //default-sanctuary-animal-spawn
        defaultSettings.put("animal-spawn", cuboidSetting.getStatus("default-animal-spawn", Cuboid.Status.ALLOW));
        
        //default-healing
        defaultSettings.put("healing", cuboidSetting.getStatus("default-healing", Cuboid.Status.DENY));
        
        //default-pvp-allowed
        defaultSettings.put("pvp-damage", cuboidSetting.getStatus("default-pvp-damage", Cuboid.Status.ALLOW));
        
        //default-protection
        defaultSettings.put("protection", cuboidSetting.getStatus("default-protection", Cuboid.Status.ALLOW));

        //default-freebuild
        defaultSettings.put("creative", cuboidSetting.getStatus("default-creative", Cuboid.Status.DENY));
        
        //default-firespread-block
        defaultSettings.put("firespread", cuboidSetting.getStatus("default-firespread", Cuboid.Status.ALLOW));
        
        //default-stop-lava-flow
        defaultSettings.put("lava-flow", cuboidSetting.getStatus("default-lava-flow", Cuboid.Status.ALLOW));
        
        //default-stop-water-flow
        defaultSettings.put("water-flow", cuboidSetting.getStatus("default-water-flow", Cuboid.Status.ALLOW));
        
        //default-farmland
        defaultSettings.put("crops-trampling", cuboidSetting.getStatus("default-crops-trampling", Cuboid.Status.DENY));
        
        //default-tnt-secure
        defaultSettings.put("tnt-explosion", cuboidSetting.getStatus("default-tnt-explosion", Cuboid.Status.ALLOW));
        
        //default-restriction
        defaultSettings.put("enter-cuboid", cuboidSetting.getStatus("default-enter-cuboid", Cuboid.Status.ALLOW));
        
        //default-hmob
        defaultSettings.put("more-mobs", cuboidSetting.getStatus("default-more-mobs", Cuboid.Status.DENY));

        //default-enderman-control
        defaultSettings.put("enderman-pickup", cuboidSetting.getStatus("default-enderman-pickup", Cuboid.Status.DENY));
        
        //default-physics-control
        defaultSettings.put("physics", cuboidSetting.getStatus("default-physics", Cuboid.Status.ALLOW));

        // Global Settings
        global.setName("__GLOBAL__");
        global.setAllowPvp(cuboidSetting
                .getBoolean("disable-pvp-global", false));
        global.setCreeperSecure(cuboidSetting.getBoolean(
                "disable-creeper-secure-global", false));
        global.setSanctuary(cuboidSetting.getBoolean("sanctuary-global", false));
        global.setSanctuarySpawnAnimals(cuboidSetting.getBoolean(
                "sanctuary-animal-spawn-global", true));
        global.setTntSecure(cuboidSetting
                .getBoolean("tnt-secure-global", false));
        global.setBlockFireSpread(cuboidSetting.getBoolean(
                "firespread-block-global", false));
        global.setProtection(cuboidSetting.getBoolean("protection-global",
                false));
        global.setLavaControl(cuboidSetting.getBoolean("stop-lava-flow-global",
                false));
        global.setWaterControl(cuboidSetting.getBoolean(
                "stop-water-flow-global", false));
        global.setPhysics(cuboidSetting.getBoolean("physics-control-global",
                false));
        global.setEnderControl(cuboidSetting.getBoolean(
                "enderman-control-global", false));

        String[] itemsList = cuboidSetting.getString("restricted-items", "")
                .split(",");
        restrictedItems = new ArrayList<Integer>(itemsList.length);
        for (String i : itemsList) {
            int a = CServer.getServer().getItemId(i);
            if (a >= 0) {
                restrictedItems.add(a);
            }
        }

        String dataSource = dsSetting.getString("data-source", "flatfile");
        if (dataSource.equalsIgnoreCase("mysql")) {
            sqlConfig = new HashMap<String, String>(3);
            sqlConfig.put("url", dsSetting.getString("sql-url", "localhost"));
            sqlConfig.put("user", dsSetting.getString("sql-user", "root"));
            sqlConfig.put("passwd", dsSetting.getString("sql-passwd", "root"));
        }

    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public void updateGlobalSettings(CuboidE props) {
        global = props;
    }

    /**
     * @return the allowProtection
     */
    public boolean isAllowProtection() {
        return allowProtection;
    }

    /**
     * @return the allowCreeperSecure
     */
    public boolean isAllowCreeperSecure() {
        return allowCreeperSecure;
    }

    /**
     * @return the allowSanctuary
     */
    public boolean isAllowSanctuary() {
        return allowSanctuary;
    }

    /**
     * @return the allowSanctuarySpawnAnimals
     */
    public boolean isAllowSanctuarySpawnAnimals() {
        return allowSanctuarySpawnAnimals;
    }

    /**
     * @return the allowHealing
     */
    public boolean isAllowHealing() {
        return allowHealing;
    }

    /**
     * @return the allowPvp
     */
    public boolean isAllowPvp() {
        return allowPvp;
    }

    /**
     * @return the allowFreebuild
     */
    public boolean isAllowFreebuild() {
        return allowFreebuild;
    }

    /**
     * @return the allowFireSpreadBlock
     */
    public boolean isAllowFireSpreadBlock() {
        return allowFireSpreadBlock;
    }

    /**
     * @return the allowTntSecure
     */
    public boolean isAllowTntSecure() {
        return allowTntSecure;
    }

    /**
     * @return the allowLavaControl
     */
    public boolean isAllowLavaControl() {
        return allowLavaControl;
    }

    /**
     * @return the allowWaterControl
     */
    public boolean isAllowWaterControl() {
        return allowWaterControl;
    }

    /**
     * @return the allowFarmland
     */
    public boolean isAllowFarmland() {
        return allowFarmland;
    }

    /**
     * @return the allowRestriction
     */
    public boolean isAllowRestriction() {
        return allowRestriction;
    }

    /**
     * @return the allowHmobs
     */
    public boolean isAllowHmobs() {
        return allowHmobs;
    }

    /**
     * @return the allowWelcome
     */
    public boolean isAllowWelcome() {
        return allowWelcome;
    }

    /**
     * @return the allowFarewell
     */
    public boolean isAllowFarewell() {
        return allowFarewell;
    }

    /**
     * @return the useDoubleAction
     */
    public boolean isUseDoubleAction() {
        return useDoubleAction;
    }

    /**
     * @return the regionItem
     */
    public int getRegionItem() {
        return regionItem;
    }

    /**
     * @return the remoteRegionItem
     */
    public int getRemoteRegionItem() {
        return remoteRegionItem;
    }

    /**
     * @return the inspectorItem
     */
    public int getInspectorItem() {
        return inspectorItem;
    }

    /**
     * @return the sculptItem
     */
    public int getSculptItem() {
        return sculptItem;
    }

    /**
     * @return the healPower
     */
    public int getHealPower() {
        return healPower;
    }

    /**
     * @return the healDelay
     */
    public int getHealDelay() {
        return healDelay;
    }

    /**
     * @return the saveDelay
     */
    public int getSaveDelay() {
        return saveDelay;
    }

    /**
     * @return the verbose
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * @param verbose
     *            the verbose to set
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * @return the sqlConfig
     */
    public HashMap<String, String> getSqlConfig() {
        return sqlConfig;
    }

    /**
     * @return the autoParent
     */
    public boolean isAutoParent() {
        return autoParent;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return the undoSteps
     */
    public int getUndoSteps() {
        return undoSteps;
    }

    /**
     * @return the allowUndo
     */
    public boolean isAllowUndo() {
        return allowUndo;
    }

    public boolean isAllowPhysics() {
        return allowPhysics;
    }

    public boolean isAllowEnderControl() {
        return allowEnderControl;
    }

    public Cuboid getDefaultCuboidSetting(CPlayer player) {
        Cuboid flags = new Cuboid();
        HashMap<String, Cuboid.Status> temp = new HashMap<String, Cuboid.Status>(defaultSettings);
        Iterator<String> it = temp.keySet().iterator();
        
        while(it.hasNext()) {
            String key = it.next();
            if(!player.hasPermission(key)) {
                it.remove();
            }
        }
        flags.putAll(temp);
        return flags;
    }

    public CuboidE getGlobalSettings() {
        return global;
    }

    public BaseData getDataSource() {
        if (sqlConfig == null) {
            return new FlatfileData(EventLogger.getInstance());
        } else {
            return new MysqlData(sqlConfig, EventLogger.getInstance());
        }
    }

    /**
     * Check if the item is on the global restricted items list
     * 
     * @param itemId
     * @return
     */
    public boolean itemIsRestricted(int itemId) {
        return restrictedItems.contains(Integer.valueOf(itemId));
    }

}
