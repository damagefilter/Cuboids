package net.playblack.cuboids;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.playblack.cuboids.datasource.BaseData;
import net.playblack.cuboids.datasource.XmlData;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.PropsFile;

/**
 * Configuration settings.
 *
 * @author Chris
 *
 */
public class Config {
    public enum Implementation {
        CANARY,
        CANARY_NEW,
        NOT_SET;
    }

    private Implementation impl = Implementation.NOT_SET;
//    private String name = "Cuboids";
//    private String version = "3.2.1";
    private String basePath = "plugins/cuboids/";

    // global settings go into this
    private Region global = new Region();


    private HashMap<String, String> sqlConfig = null;
    private boolean autoParent = true;

    // Cuboids Default Settings
    HashMap<String, Region.Status> defaultSettings = new HashMap<String, Region.Status>();

    ArrayList<Integer> restrictedItems;

    //The config files
    PropsFile pluginSetting;
    PropsFile cuboidSetting;
    PropsFile dsSetting;

    private static Config instance = null;

    private Config() {
        pluginSetting =  new PropsFile(basePath + "settings.properties");
        cuboidSetting =  new PropsFile(basePath + "cuboid.properties");
        dsSetting =      new PropsFile(basePath + "data.properties");

        //quickly init all settings properties (for saving reasons)
        getHealDelay();
        getHealPower();
        getInspectorItem();
        getRegionItem();
        getRemoteRegionItem();
        getSaveDelay();
        getSculptItem();
        getUndoSteps();
        isAllowUndo();
        isAutoParent();
        isUseDoubleAction();
        isVerbose();

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

        defaultSettings.put("water-bucket", cuboidSetting.getStatus("default-water-bucket", Region.Status.DEFAULT));

        defaultSettings.put("lava-bucket", cuboidSetting.getStatus("default-lava-bucket", Region.Status.DENY));

        defaultSettings.put("animal-damage", cuboidSetting.getStatus("default-animal-damage", Region.Status.DEFAULT));
        //Register all of cuboids own flags
        for(String key : defaultSettings.keySet()) {
            RegionFlagRegister.registerFlag(key);
        }


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
        global.setProperty("restrict-items", cuboidSetting.getStatus("global-restrict-items", Region.Status.DEFAULT));

        global.setProperty("water-bucket", cuboidSetting.getStatus("global-water-bucket", Region.Status.DEFAULT));

        global.setProperty("lava-bucket", cuboidSetting.getStatus("global-lava-bucket", Region.Status.DEFAULT));

        global.setProperty("animal-damage", cuboidSetting.getStatus("global-animal-damage", Region.Status.DEFAULT));

        String[] itemsList = cuboidSetting.getString("restricted-items", "").split(",");
        restrictedItems = new ArrayList<Integer>(itemsList.length);
        for (String i : itemsList) {
            int a = CServer.getServer().getItemId(i);
            if (a >= 0) {
                global.addRestrictedItem(a);
            }
        }

        String dataSource = dsSetting.getString("data-source", "xml");
        if (dataSource.equalsIgnoreCase("mysql")) {
            sqlConfig = new HashMap<String, String>(3);
            sqlConfig.put("url", dsSetting.getString("sql-url", "localhost"));
            sqlConfig.put("user", dsSetting.getString("sql-user", "hans die"));
            sqlConfig.put("passwd", dsSetting.getString("sql-passwd", "bratwurst"));
        }
        saveConfigs();
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
     * Returns the current base path, usually plugins/cuboids/.
     * Note that there's a trailing /
     * @return
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * @return the useDoubleAction
     */
    public boolean isUseDoubleAction() {
        // useDoubleAction = pluginSetting.getBoolean("use-double-action-tool", true);
        return pluginSetting.getBoolean("use-double-action-tool", true);
    }

    /**
     * @return the regionItem
     */
    public int getRegionItem() {
        return pluginSetting.getInt("selection-item", 294);
    }

    /**
     * @return the remoteRegionItem
     */
    public int getRemoteRegionItem() {
        return pluginSetting.getInt("remote-selection-item", 268);
    }

    /**
     * @return the inspectorItem
     */
    public int getInspectorItem() {
        return pluginSetting.getInt("inspector-item", 283);
    }

    /**
     * @return the sculptItem
     */
    public int getSculptItem() {
        return pluginSetting.getInt("sculpt-tool-item", 352);
    }

    /**
     * @return the healPower
     */
    public int getHealPower() {
        return pluginSetting.getInt("heal-power", 1);
    }

    /**
     * @return the healDelay
     */
    public int getHealDelay() {
        return pluginSetting.getInt("heal-delay", 3);
    }

    /**
     * @return the saveDelay
     */
    public int getSaveDelay() {
        return pluginSetting.getInt("autosave-intervall", 30);
    }

    /**
     * @return the verbose
     */
    public boolean isVerbose() {
        return pluginSetting.getBoolean("verbose", true);
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
     * @return the undoSteps
     */
    public int getUndoSteps() {
        return pluginSetting.getInt("undo-steps", 5);
    }

    /**
     * @return the allowUndo
     */
    public boolean isAllowUndo() {
        return pluginSetting.getBoolean("allow-undo", true);
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

    //TODO: More datasource stuff
    public BaseData getDataSource() {
//        if (sqlConfig == null) {
//            return new XmlData();
//        } else {
//            return new MysqlDataLegacy(sqlConfig);
//        }
        return new XmlData();
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

    public String getLang() {
        //TODO: From config
        return "en_EN";
    }

    /**
     * Save all configuration files
     */
    public void saveConfigs() {
        pluginSetting.save();
        cuboidSetting.save();
        dsSetting.save();
    }

    public boolean setGlobalProperty(String name, Region.Status value) {
        boolean result = global.setProperty(name, value);
        if(result) {
            RegionManager.get().updateGlobalSettings();
            return true;
        }
        return false;
    }

    public boolean removeGlobalProperty(String name) {
        boolean result = global.removeProperty(name);
        if(result) {
            RegionManager.get().updateGlobalSettings();
            return true;
        }
        return false;
    }

    public Implementation getImplementation() {
        return impl;
    }

    public void setImplementation(Implementation impl) {
        //Only allow setting the implementation once!
        if(impl == Implementation.NOT_SET) {
            return;
        }

        if(this.impl == Implementation.NOT_SET) {
            this.impl = impl;
        }
    }

}
