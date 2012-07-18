package net.playblack.cuboids;

import java.util.ArrayList;
import java.util.HashMap;

import net.playblack.cuboids.datasource.BaseData;
import net.playblack.cuboids.datasource.FlatfileData;
import net.playblack.cuboids.datasource.MysqlData;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.PropsFile;

/**
 * Configuration settings.
 * @author Chris
 *
 */
public class Config {
    private String name="Cuboids2";
    private String version="2.3.0";
    private boolean verbose=false;
    
    //allow specific parts of the plugin:
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
  //global area settings go into this
    private CuboidE global = new CuboidE();
    
    //Plugin Settings
    private boolean useDoubleAction=false;
    private int regionItem;
    private int remoteRegionItem;
    private int inspectorItem;
    private int sculptItem;
    private HashMap<String, String> sqlConfig = null;// = new HashMap<String, String>(0); //<- do not init before we know if we need it at all! 
    private int healPower;
    private int healDelay;
    private int saveDelay;
    private boolean autoParent=true;
    
  //Undo config
    private int undoSteps = 5;
    private boolean allowUndo = false;

    //Cuboids Default Settings
    CuboidE defaultSettings = new CuboidE();
    
    ArrayList<Integer> restritedItems;
    
    private static Config instance = null;
    private Config() {
        PropsFile pluginSetting = new PropsFile("plugins/cuboids2/settings.properties");
        PropsFile cuboidSetting = new PropsFile("plugins/cuboids2/cuboid.properties");
        PropsFile dsSetting = new PropsFile("plugins/cuboids2/data.properties");
        
        //Read plugin settings!
        useDoubleAction = pluginSetting.getBoolean("use-double-action-tool", false);
        regionItem = pluginSetting.getInt("selection-item", 294);
        remoteRegionItem = pluginSetting.getInt("remote-selection-item", 268);
        inspectorItem = pluginSetting.getInt("inspector-item", 283);
        sculptItem = pluginSetting.getInt("sculpt-tool-item", 352);
        healPower = pluginSetting.getInt("heal-power", 1);
        healDelay = pluginSetting.getInt("heal-delay", 3);
        saveDelay = pluginSetting.getInt("autosave-intervall", 30);
        undoSteps = pluginSetting.getInt("undo-steps", 5);
        allowUndo = pluginSetting.getBoolean("allow-undo", true);
        
      //Enable/Disable plugin features
        allowProtection = pluginSetting.getBoolean("allow-protection", true);
        allowCreeperSecure = pluginSetting.getBoolean("allow-creeper-secure", true);
        allowSanctuary = pluginSetting.getBoolean("allow-sanctuary", true);
        allowSanctuarySpawnAnimals = pluginSetting.getBoolean("allow-sanctuary-animal-spawn", true);
        allowHealing = pluginSetting.getBoolean("allow-healing", true);
        allowPvp = pluginSetting.getBoolean("allow-pvp", true);
        allowFreebuild = pluginSetting.getBoolean("allow-freebuild", true);
        allowFireSpreadBlock = pluginSetting.getBoolean("allow-firespread-block", true);   
        allowTntSecure = pluginSetting.getBoolean("allow-tnt-secure", true); 
        allowLavaControl = pluginSetting.getBoolean("allow-stop-lava-flow", true);
        allowWaterControl = pluginSetting.getBoolean("allow-stop-water-flow", true);
        allowFarmland = pluginSetting.getBoolean("allow-farmland", true);
        allowRestriction = pluginSetting.getBoolean("allow-restriction", true);
        allowHmobs = pluginSetting.getBoolean("allow-hmobs", false);
        allowWelcome = pluginSetting.getBoolean("allow-welcome-message", true);
        allowFarewell = pluginSetting.getBoolean("allow-farewell-message", true);
        allowPhysics = pluginSetting.getBoolean("allow-physics-control", true);
        allowEnderControl = pluginSetting.getBoolean("allow-enderman-control", true);
        verbose = pluginSetting.getBoolean("verbose",true);
        
        //Default setting for new cuboids
        defaultSettings.setCreeperSecure(cuboidSetting.getBoolean("default-creeper-secure", true));
        defaultSettings.setSanctuary(cuboidSetting.getBoolean("default-sanctuary", false));
        defaultSettings.setSanctuarySpawnAnimals(cuboidSetting.getBoolean("default-sanctuary-animal-spawn", true));
        defaultSettings.setHealing(cuboidSetting.getBoolean("default-healing", false));
        defaultSettings.setAllowPvp(cuboidSetting.getBoolean("default-pvp-allowed", true));
        defaultSettings.setProtection(cuboidSetting.getBoolean("default-protection", true));
        defaultSettings.setFreeBuild(cuboidSetting.getBoolean("default-freebuild", false));
        defaultSettings.setBlockFireSpread(cuboidSetting.getBoolean("default-firespread-block", true));
        defaultSettings.setLavaControl(cuboidSetting.getBoolean("default-stop-lava-flow", false));
        defaultSettings.setWaterControl(cuboidSetting.getBoolean("default-stop-water-flow", false));
        defaultSettings.setFarmland(cuboidSetting.getBoolean("default-farmland", false));
        defaultSettings.setTntSecure(cuboidSetting.getBoolean("default-tnt-secure", false));
        defaultSettings.setRestriction(cuboidSetting.getBoolean("default-restriction", false));
        defaultSettings.sethMob(cuboidSetting.getBoolean("default-hmob", false));
        defaultSettings.setEnderControl(cuboidSetting.getBoolean("default-enderman-control", false));
        defaultSettings.setPhysics(cuboidSetting.getBoolean("default-physics-control", false));
         
        //Global Settings
        global.setName("__GLOBAL__");
        global.setAllowPvp(cuboidSetting.getBoolean("disable-pvp-global", false));
        global.setCreeperSecure(cuboidSetting.getBoolean("disable-creeper-secure-global", false));
        global.setSanctuary(cuboidSetting.getBoolean("sanctuary-global", false));
        global.setSanctuarySpawnAnimals(cuboidSetting.getBoolean("sanctuary-animal-spawn-global", true));
        global.setTntSecure(cuboidSetting.getBoolean("tnt-secure-global", false));
        global.setBlockFireSpread(cuboidSetting.getBoolean("firespread-block-global", false));
        global.setProtection(cuboidSetting.getBoolean("protection-global", false));
        global.setLavaControl(cuboidSetting.getBoolean("stop-lava-flow-global", false));
        global.setWaterControl(cuboidSetting.getBoolean("stop-water-flow-global", false));
        global.setPhysics(cuboidSetting.getBoolean("physics-control-global", false));
        global.setEnderControl(cuboidSetting.getBoolean("enderman-control-global", false));
        
        String[] itemsList = cuboidSetting.getString("restricted-items", "").split(",");
        restritedItems = new ArrayList<Integer>(itemsList.length);
        for(String i : itemsList) {
            int a = CServer.getServer().getItemId(i);
            if(a >= 0) {
                restritedItems.add(a);
            }
        }
        
        String dataSource = dsSetting.getString("data-source", "flatfile");
        if(dataSource.equalsIgnoreCase("mysql")) {
            sqlConfig = new HashMap<String,String>(3);
            sqlConfig.put("url", dsSetting.getString("sql-url","localhost"));
            sqlConfig.put("user", dsSetting.getString("sql-user","root"));
            sqlConfig.put("passwd", dsSetting.getString("sql-passwd","root"));
        }
        
    }

    public static Config getInstance() {
        if(instance == null) {
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
     * @param verbose the verbose to set
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
    
    public CuboidE getDefaultCuboidSetting(CPlayer player) {
        CuboidE flags = new CuboidE();
        //PVP
        if(allowPvp && (player.hasPermission("cpvp") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setAllowPvp(defaultSettings.isAllowedPvp());
        } else {
            flags.setAllowPvp(true);
        }
        
        //CREEPER
        if(allowCreeperSecure && (player.hasPermission("ccreeper") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setCreeperSecure(defaultSettings.isCreeperSecure());
        } else {
            flags.setCreeperSecure(false);
        }
        
        //HEALING
        if(allowHealing && (player.hasPermission("cheal") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setHealing(defaultSettings.isHealingArea());
        } else {
            flags.setHealing(false);
        }
        
        //PROTECTION
        if(allowProtection && (player.hasPermission("cprotection") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setProtection(defaultSettings.isProtected());
        } else {
            flags.setProtection(false);
        }
        
        //SANCTUARY
        if(allowSanctuary && (player.hasPermission("csanctuary") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setSanctuary(defaultSettings.isSanctuary());
        } else {
            flags.setSanctuary(false);
        }
        
        //SANCTUARY ANIMAL  THING
        if(allowSanctuarySpawnAnimals && (player.hasPermission("csanctuary") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setSanctuarySpawnAnimals(defaultSettings.sanctuarySpawnAnimals());
        } else {
            flags.setSanctuarySpawnAnimals(true);
        }
        
        //FREEBUILD
        if(allowFreebuild && (player.hasPermission("cfreebuild") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setFreeBuild(defaultSettings.isFreeBuild());
        } else {
            flags.setFreeBuild(false);
        }
        
        //FIRESPREAD
        if(allowFireSpreadBlock && (player.hasPermission("cfirespread") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setBlockFireSpread(defaultSettings.isBlockFireSpread());
        } else {
            flags.setBlockFireSpread(false);
        }
        
        //LAVA CONTROL
        if(allowLavaControl && (player.hasPermission("cliquids") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setLavaControl(defaultSettings.isLavaControl());
        } else {
            flags.setLavaControl(false);
        }
        
        //WATER CONTROL
        if(allowWaterControl && (player.hasPermission("cliquids") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setWaterControl(defaultSettings.isWaterControl());
        } else {
            flags.setWaterControl(false);
        }
        
        //TNT SECURE
        if(allowTntSecure && (player.hasPermission("ctnt") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setTntSecure(defaultSettings.isTntSecure());
        } else {
            flags.setTntSecure(false);
        }
        
        //FARMLAND
        if(allowFarmland && (player.hasPermission("cfarmland") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setFarmland(defaultSettings.isFarmland());
        } else {
            flags.setFarmland(false);
        }
        
        //RESTRICTION
        if(allowRestriction && (player.hasPermission("crestriction") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setRestriction(defaultSettings.isRestricted());
        } else {
            flags.setRestriction(false);
        }
        
        //HMOBS
        if(allowHmobs && (player.hasPermission("chmob") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.sethMob(defaultSettings.ishMob());
        } else {
            flags.sethMob(false);
        }
        
        //PHYSICS
        if(allowPhysics && (player.hasPermission("cphysics") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setPhysics(defaultSettings.isPhysicsDisabled());
        } else {
            flags.setPhysics(false);
        }
        //ENDERCONTROL
        if(allowEnderControl && (player.hasPermission("cendercontrol") || player.hasPermission("cIgnoreRestrictions"))) {
            flags.setEnderControl(defaultSettings.hasEnderControl());
        } else {
            flags.setEnderControl(false);
        }
        return flags;
    }
    
    public CuboidE getGlobalSettings() {
        return global;
    }
    
    public BaseData getDataSource() {
        if(sqlConfig == null) {
            return new FlatfileData(EventLogger.getInstance());
        }
        else {
            return new MysqlData(sqlConfig, EventLogger.getInstance());
        }
    }

    /**
     * Check if the item is on the global restricted items list
     * @param itemId
     * @return
     */
    public boolean itemIsRestricted(int itemId) {
        return restritedItems.contains(Integer.valueOf(itemId));
    }
    
}
