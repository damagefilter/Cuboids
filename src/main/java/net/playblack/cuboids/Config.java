package net.playblack.cuboids;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.config.Configuration;
import net.canarymod.plugin.PluginException;
import net.playblack.cuboids.datasource.BaseData;
import net.playblack.cuboids.datasource.CanaryDbData;
import net.playblack.cuboids.impl.canarymod.Cuboids;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;
import net.visualillusionsent.utils.PropertiesFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Configuration settings.
 *
 * @author Chris
 */
public class Config {

    private static Config instance = null;
    private static Cuboids cuboidsInstance = null;
    // Cuboids Default Settings
    HashMap<String, Region.Status> defaultSettings = new HashMap<String, Region.Status>();
    ArrayList<Integer> restrictedItems;
    //The config files
    PropertiesFile pluginSetting;
    PropertiesFile cuboidSetting;
    PropertiesFile dsSetting;
    // global settings go into this
    private Region global = new Region();
    private HashMap<String, String> sqlConfig = null;

    private Region.Status getStatus(String name, Region.Status def) {
        Region.Status s = Status.fromString(name);
        if (s == Status.INVALID_PROPERTY) {
            s = def;
        }
        return s;
    }
    public Config(Cuboids plugin) {
        cuboidsInstance = plugin;
        pluginSetting = Configuration.getPluginConfig(plugin, "plugin-config");
//        pluginSetting = new PropsFile(basePath + "settings.properties");
//        cuboidSetting = new PropsFile(basePath + "cuboid.properties");
        cuboidSetting = Configuration.getPluginConfig(plugin, "region-config");
//        dsSetting = new PropsFile(basePath + "data.properties");
        dsSetting = Configuration.getPluginConfig(plugin, "datasource-config");

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
        defaultSettings.put("creeper-explosion", Status.fromString(cuboidSetting.getString("default-creeper-explosion", "DENY")));

        //default-sanctuary
        defaultSettings.put("mob-damage", Status.fromString(cuboidSetting.getString("default-mob-damage", "ALLOW")));

        //default sanctuary too
        defaultSettings.put("mob-spawn", Status.fromString(cuboidSetting.getString("default-mob-spawn", "ALLOW")));

        //default-sanctuary-animal-spawn
        defaultSettings.put("animal-spawn", Status.fromString(cuboidSetting.getString("default-animal-spawn", "ALLOW")));

        //default-healing
        defaultSettings.put("healing", Status.fromString(cuboidSetting.getString("default-healing", "DENY")));

        //default-pvp-allowed
        defaultSettings.put("pvp-damage", Status.fromString(cuboidSetting.getString("default-pvp-damage", "ALLOW")));

        //default-protection
        defaultSettings.put("protection", Status.fromString(cuboidSetting.getString("default-protection", "ALLOW")));

        //default-freebuild
        defaultSettings.put("creative", Status.fromString(cuboidSetting.getString("default-creative", "DENY")));

        //default-firespread-block
        defaultSettings.put("firespread", Status.fromString(cuboidSetting.getString("default-firespread", "ALLOW")));

        //default-stop-lava-flow
        defaultSettings.put("lava-flow", Status.fromString(cuboidSetting.getString("default-lava-flow", "ALLOW")));

        //default-stop-water-flow
        defaultSettings.put("water-flow", Status.fromString(cuboidSetting.getString("default-water-flow", "ALLOW")));

        //default-farmland
        defaultSettings.put("crops-trampling", Status.fromString(cuboidSetting.getString("default-crops-trampling", "DENY")));

        //default-tnt-secure
        defaultSettings.put("tnt-explosion", Status.fromString(cuboidSetting.getString("default-tnt-explosion", "ALLOW")));

        //default-restriction
        defaultSettings.put("enter-cuboid", Status.fromString(cuboidSetting.getString("default-enter-cuboid", "ALLOW")));

        //default-hmob
        defaultSettings.put("more-mobs", Status.fromString(cuboidSetting.getString("default-more-mobs", "DENY")));

        //default-enderman-control
        defaultSettings.put("enderman-pickup", Status.fromString(cuboidSetting.getString("default-enderman-pickup", "DENY")));

        //default-physics-control
        defaultSettings.put("physics", Status.fromString(cuboidSetting.getString("default-physics", "ALLOW")));

        //switch on/off item restriction
        defaultSettings.put("restrict-items", Status.fromString(cuboidSetting.getString("default-restrict-items", "DEFAULT")));

        defaultSettings.put("water-bucket", Status.fromString(cuboidSetting.getString("default-water-bucket", "DEFAULT")));

        defaultSettings.put("lava-bucket", Status.fromString(cuboidSetting.getString("default-lava-bucket", "DENY")));

        defaultSettings.put("animal-damage", Status.fromString(cuboidSetting.getString("default-animal-damage", "DEFAULT")));
        //Register all of cuboids own flags
        for (String key : defaultSettings.keySet()) {
            RegionFlagRegister.registerFlag(key);
        }


        // Global Settings
        global.setName("__GLOBAL__");

        //disable-pvp-global
        global.setProperty("pvp-damage", Status.fromString(cuboidSetting.getString("global-pvp-damage", "ALLOW")));

        //disable-creeper-secure-global
        global.setProperty("creeper-explosion", Status.fromString(cuboidSetting.getString("global-creeper-explosion", "ALLOW")));

        //sanctuary-global
        global.setProperty("mob-damage", Status.fromString(cuboidSetting.getString("global-mob-damage", "ALLOW")));

        //sanctuary-global
        global.setProperty("mob-spawn", Status.fromString(cuboidSetting.getString("global-mob-spawn", "ALLOW")));

        //sanctuary-animal-spawn-global
        global.setProperty("animal-spawn", Status.fromString(cuboidSetting.getString("global-animal-spawn", "ALLOW")));

        //tnt-secure-global
        global.setProperty("tnt-explosion", Status.fromString(cuboidSetting.getString("global-tnt-explosion", "ALLOW")));

        //firespread-block-global
        global.setProperty("firespread", Status.fromString(cuboidSetting.getString("global-firespread", "ALLOW")));

        //protection-global
        global.setProperty("protection", Status.fromString(cuboidSetting.getString("global-protection", "DEFAULT")));

        //stop-lava-flow-global
        global.setProperty("lava-flow", Status.fromString(cuboidSetting.getString("global-lava-flow", "DEFAULT")));

        //stop-lava-flow-global
        global.setProperty("water-flow", Status.fromString(cuboidSetting.getString("global-water-flow", "DEFAULT")));

        //default-physics-control
        global.setProperty("physics", Status.fromString(cuboidSetting.getString("global-physics", "ALLOW")));

        //default-enderman-control
        global.setProperty("enderman-pickup", Status.fromString(cuboidSetting.getString("global-enderman-pickup", "DEFAULT")));

        //switch on/off item restriction
        global.setProperty("restrict-items", Status.fromString(cuboidSetting.getString("global-restrict-items", "DEFAULT")));

        global.setProperty("water-bucket", Status.fromString(cuboidSetting.getString("global-water-bucket", "DEFAULT")));

        global.setProperty("lava-bucket", Status.fromString(cuboidSetting.getString("global-lava-bucket", "DEFAULT")));

        global.setProperty("animal-damage", Status.fromString(cuboidSetting.getString("global-animal-damage", "DEFAULT")));

        String[] itemsList = cuboidSetting.getString("restricted-items", "").split(",");
        restrictedItems = new ArrayList<Integer>(itemsList.length);
        for (String i : itemsList) {
            global.addRestrictedItem(i);
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
            throw new PluginException("Cuboids config was not set before calling it! Nag the author to fix it!");
        }
        return instance;
    }

    public static void setConfig(Config instance) {
        if (Config.instance != null) {
            return;
        }
        Config.instance = instance;
    }

    public static Cuboids getPlugin() {
        return cuboidsInstance;
    }

    public void updateGlobalSettings(Region props) {
        global = props;
    }

    /**
     * Returns the current base path, usually plugins/cuboids/. Note that
     * there's a trailing /
     *
     * @return
     */
    public String getBasePath() {
        String basePath = "plugins/cuboids/";
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
    public String getRegionItem() {
        return pluginSetting.getString("selection-item", "minecraft:golden_hoe");
    }

    /**
     * @return the remoteRegionItem
     */
    public String getRemoteRegionItem() {
        return pluginSetting.getString("remote-selection-item", "minecraft:wooden_sword");
    }

    /**
     * @return the inspectorItem
     */
    public String getInspectorItem() {
        return pluginSetting.getString("inspector-item", "minecraft:golden_hoe");
    }

    /**
     * @return the sculptItem
     */
    public String getSculptItem() {
        return pluginSetting.getString("sculpt-tool-item", "minecraft:bone");
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
        // NOTICE: Non-autoparenting mode is not supported anymore
        return true;
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

    public Region getDefaultCuboidSetting(Player player) {
        Region flags = new Region();
        HashMap<String, Region.Status> temp = new HashMap<String, Region.Status>(defaultSettings);
        Iterator<String> it = temp.keySet().iterator();

        while (it.hasNext()) {
            String key = it.next();
            if (!player.hasPermission(Permissions.REGION$FLAGS + "." + key)) {
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
        return new CanaryDbData();
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
        boolean result = RegionManager.get().setGlobalProperty(name, value);
        if (result) {
            this.global.setProperty(name, value);
            this.cuboidSetting.setString("global-" + name, value.toString());
            this.cuboidSetting.save();
            return true;
        }
        return false;
    }

    public boolean removeGlobalProperty(String name) {
        boolean result = RegionManager.get().unsetGlobalProperty(name);
        if (result) {
            this.global.removeProperty(name);
            this.cuboidSetting.removeKey("global-" + name);
            this.cuboidSetting.save();
            return true;
        }
        return false;
    }
}
