package net.playblack.cuboids;

import java.util.HashMap;

import net.playblack.cuboids.regions.CuboidE;
import net.playblack.mcutils.PropsFile;

public class Cuboids2Config {
    private String name="Cuboids2";
    private String verion="2.0.0";
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
  //global area settings go into this
    private CuboidE global = new CuboidE();
  //TODO: replace these single settings against a cuboidE
//    private boolean globalDisablePvp = true;
//    private boolean globalDisableCreeperSecure = true;
//    private boolean globalFirespreadBlock = false;
//    private boolean globalSanctuary=false;
//    private boolean globalSanctuaryAnimalSpawn = false;
//    private boolean globalTntSecure=false;
//    private boolean globalProtection=false;
//    private boolean stopWaterFlow = false;
//    private boolean stopLavaFlow = false;
    
    //Plugin Settings
    private boolean useDoubleAction=false;
    private int regionItem;
    private int remoteRegionItem;
    private int inspectorItem;
    private int sculptItem;
    private String dataSource; //TODO: only temporary read it from config!
    private HashMap<String, String> sqlConfig;// = new HashMap<String, String>(0); //<- do not init before we know if we need it at all! 
    private int healPower;
    private int healDelay;
    private int saveDelay;
    private boolean autoParent=true;
    
  //Undo config
    private int undoSteps = 5;
    private boolean allowUndo = false;

    //Cuboids Default Settings
    CuboidE defaultSettings = new CuboidE();
    //TODO: replace these single settings against a cuboidE
//    private boolean isCreeperSecureDefault = false;
//    private boolean isSanctuaryDefault = false;
//    private boolean isSanctuaryAnimalSpawnDefault = false;
//    private boolean isHealingDefault = false;
//    private boolean isAllowedPvpDefault = false;
//    private boolean isProtectedDefault = false;
//    private boolean isFreebuildDefault = false;
//    private boolean isFireSpreadBlockDefault = false;
//    private boolean isLavaControlDefault = false;
//    private boolean isWaterControlDefault = false;
//    private boolean isFarmlandDefault = false;
//    private boolean isTntSecureDefault = false;
//    private boolean isRestrictionDefault = false;
//    private boolean isHmobDefault = false;
    
    public Cuboids2Config() {
        PropsFile pluginSetting = new PropsFile("plugins/cuboids2/setting.properties");
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
        
        defaultSettings.setCreeperSecure(pluginSetting.getBoolean("default-creeper-secure", true));
        defaultSettings.setSanctuary(pluginSetting.getBoolean("default-sanctuary", false));
        defaultSettings.setSanctuarySpawnAnimals(pluginSetting.getBoolean("default-sanctuary-animal-spawn", true));
        defaultSettings.setHealing(pluginSetting.getBoolean("default-healing", false));
        defaultSettings.setAllowPvp(pluginSetting.getBoolean("default-pvp-allowed", true));
        defaultSettings.setProtection(pluginSetting.getBoolean("default-protection", true));
        
        /*
         * isProtectedDefault = props.getBoolean("default-protection", true);
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
         */
        dataSource = dsSetting.getString("data-source", "flatfile");
        
    }
}
