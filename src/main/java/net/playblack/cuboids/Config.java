package net.playblack.cuboids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.playblack.cuboids.datasource.BaseData;
import net.playblack.cuboids.datasource.XmlData;
import net.playblack.cuboids.datasource.MysqlDataLegacy;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
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
    private String basePath = "plugins/cuboids2/";
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
    private Region global = new Region();

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
    HashMap<String, Region.Status> defaultSettings = new HashMap<String, Region.Status>();

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

        verbose = pluginSetting.getBoolean("verbose", true);

        // Default setting for new cuboids
        
        //default-creeper-secure
        defaultSettings.put("creeper-explosion", cuboidSetting.getStatus("default-creeper-explosion", Region.Status.DENY));
        
        //default-sanctuary
        defaultSettings.put("mob-damage", cuboidSetting.getStatus("default-mob-damage", Region.Status.ALLOW));
        
        //default sanctuary too
        defaultSettings.put("mob-spawn", cuboidSetting.getStatus("default-mob-spawn", Region.Status.ALLOW));
        
        //default-sanctuary-animal-spawn
        defaultSettings.put("animal-spawn", cuboidSetting.getStatus("default-animal-spawn", Region.Status.ALLOW));
        
        //default-healing
        defaultSettings.put("healing", cuboidSetting.getStatus("default-healing", Region.Status.DENY));
        
        //default-pvp-allowed
        defaultSettings.put("pvp-damage", cuboidSetting.getStatus("default-pvp-damage", Region.Status.ALLOW));
        
        //default-protection
        defaultSettings.put("protection", cuboidSetting.getStatus("default-protection", Region.Status.ALLOW));

        //default-freebuild
        defaultSettings.put("creative", cuboidSetting.getStatus("default-creative", Region.Status.DENY));
        
        //default-firespread-block
        defaultSettings.put("firespread", cuboidSetting.getStatus("default-firespread", Region.Status.ALLOW));
        
        //default-stop-lava-flow
        defaultSettings.put("lava-flow", cuboidSetting.getStatus("default-lava-flow", Region.Status.ALLOW));
        
        //default-stop-water-flow
        defaultSettings.put("water-flow", cuboidSetting.getStatus("default-water-flow", Region.Status.ALLOW));
        
        //default-farmland
        defaultSettings.put("crops-trampling", cuboidSetting.getStatus("default-crops-trampling", Region.Status.DENY));
        
        //default-tnt-secure
        defaultSettings.put("tnt-explosion", cuboidSetting.getStatus("default-tnt-explosion", Region.Status.ALLOW));
        
        //default-restriction
        defaultSettings.put("enter-cuboid", cuboidSetting.getStatus("default-enter-cuboid", Region.Status.ALLOW));
        
        //default-hmob
        defaultSettings.put("more-mobs", cuboidSetting.getStatus("default-more-mobs", Region.Status.DENY));

        //default-enderman-control
        defaultSettings.put("enderman-pickup", cuboidSetting.getStatus("default-enderman-pickup", Region.Status.DENY));
        
        //default-physics-control
        defaultSettings.put("physics", cuboidSetting.getStatus("default-physics", Region.Status.ALLOW));
        
        //switch on/off item restriction
        defaultSettings.put("restrict-items", cuboidSetting.getStatus("default-restrict-items", Region.Status.DEFAULT));

        
        
        // Global Settings
        global.setName("__GLOBAL__");
        
        //disable-pvp-global
        global.setProperty("pvp-damage", cuboidSetting.getStatus("global-pvp-damage", Status.ALLOW));
        
        //disable-creeper-secure-global
        global.setProperty("creeper-explosion", cuboidSetting.getStatus("global-creeper-explosion", Status.ALLOW));
        
        //sanctuary-global
        global.setProperty("mob-damage", cuboidSetting.getStatus("global-mob-damage", Status.ALLOW));
        
        //sanctuary-global
        global.setProperty("mob-spawn", cuboidSetting.getStatus("global-mob-spawn", Status.ALLOW));
        
        //sanctuary-animal-spawn-global
        global.setProperty("animal-spawn", cuboidSetting.getStatus("global-animal-spawn", Status.ALLOW));
        
        //tnt-secure-global
        global.setProperty("tnt-explosion", cuboidSetting.getStatus("global-tnt-explosion", Status.ALLOW));
        
        //firespread-block-global
        global.setProperty("firespread", cuboidSetting.getStatus("global-firespread", Region.Status.ALLOW));
        
        //protection-global
        global.setProperty("protection", cuboidSetting.getStatus("global-protection", Region.Status.DEFAULT));
        
        //stop-lava-flow-global
        global.setProperty("lava-flow", cuboidSetting.getStatus("global-lava-flow", Region.Status.DEFAULT));
        
        //stop-lava-flow-global
        global.setProperty("water-flow", cuboidSetting.getStatus("global-water-flow", Region.Status.DEFAULT));
        
        //default-physics-control
        global.setProperty("physics", cuboidSetting.getStatus("global-physics", Region.Status.ALLOW));
        
        //default-enderman-control
        global.setProperty("enderman-pickup", cuboidSetting.getStatus("global-enderman-pickup", Region.Status.DEFAULT));
        
      //switch on/off item restriction
        defaultSettings.put("restrict-items", cuboidSetting.getStatus("global-restrict-items", Region.Status.DEFAULT));

        String[] itemsList = cuboidSetting.getString("restricted-items", "").split(",");
        restrictedItems = new ArrayList<Integer>(itemsList.length);
        for (String i : itemsList) {
            int a = CServer.getServer().getItemId(i);
            if (a >= 0) {
                global.addRestrictedItem(a);
            }
        }

        String dataSource = dsSetting.getString("data-source", "flatfile");
        if (dataSource.equalsIgnoreCase("mysql")) {
            sqlConfig = new HashMap<String, String>(3);
            sqlConfig.put("url", dsSetting.getString("sql-url", "localhost"));
            sqlConfig.put("user", dsSetting.getString("sql-user", "hans die"));
            sqlConfig.put("passwd", dsSetting.getString("sql-passwd", "bratwurst"));
        }

    }

    public static Config get() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public void updateGlobalSettings(Region props) {
        global = props;
    }

    /**
     * Returns the current base path, usually plugins/cuboids2/.
     * Note that there's a trailing /
     * @return
     */
    public String getBasePath() {
        return basePath;
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

    public Region getDefaultCuboidSetting(CPlayer player) {
        Region flags = new Region();
        HashMap<String, Region.Status> temp = new HashMap<String, Region.Status>(defaultSettings);
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

    public Region getGlobalSettings() {
        return global;
    }

    public BaseData getDataSource() {
        if (sqlConfig == null) {
            return new XmlData(EventLogger.getInstance());
        } else {
            return new MysqlDataLegacy(sqlConfig, EventLogger.getInstance());
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
