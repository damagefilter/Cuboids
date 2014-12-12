package net.playblack.cuboids;

//import java.util.concurrent.TimeUnit;

import net.playblack.cuboids.actions.operators.BlockModificationsOperator;
import net.playblack.cuboids.actions.operators.DamageOperator;
import net.playblack.cuboids.actions.operators.MiscOperator;
import net.playblack.cuboids.actions.operators.OperableItemsOperator;
import net.playblack.cuboids.actions.operators.PlayerMovementOperator;
import net.playblack.cuboids.actions.operators.SelectionOperator;
import net.playblack.cuboids.impl.canarymod.Cuboids;
import net.playblack.cuboids.loaders.Converter;
import net.playblack.cuboids.loaders.Loader;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Debug;
//import net.playblack.cuboids.regions.CuboidInterface;

/**
 * The Bootstrapper takes care of loading all the required components to run the
 * Plugin and also receives the server implementation.
 *
 * @author Chris
 */
public class Bootstrapper {

    private Loader[] loaders;

    /**
     * Expects the server implementation and a list of loaders for foreign
     * cuboids. Leave the list null if nothing should be loaded from foreign
     * sources
     *
     * @param loaders
     */
    public Bootstrapper(Loader[] loaders) {
        this.loaders = loaders;

    }

    public void bootstrap(Cuboids plugin) {
        // ------------------------------------------------------
        Config.setConfig(new Config(plugin));
        // ------------------------------------------------------
        boolean hasConverted = false;
        if (loaders != null) {
            Converter c = new Converter();
            for (Loader loader : loaders) {
                if (c.convertFiles(loader)) {
                    hasConverted |= true;
                }
            }
        }
        if (hasConverted) {
            Debug.cacheMessage("Detected and loaded legacy region files.", true);
        }
        // ------------------------------------------------------
        int loaded = RegionManager.get().load();
        Debug.cacheMessage("Loaded " + loaded + " regions.", true);
        Debug.logCachedMessage();

        new BlockModificationsOperator();
        new DamageOperator();
        new MiscOperator();
        new OperableItemsOperator();
        new PlayerMovementOperator();
        new SelectionOperator();
    }
}
