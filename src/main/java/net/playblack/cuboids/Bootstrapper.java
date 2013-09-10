package net.playblack.cuboids;

//import java.util.concurrent.TimeUnit;

import net.playblack.cuboids.Config.Implementation;
import net.playblack.cuboids.actions.operators.BlockModificationsOperator;
import net.playblack.cuboids.actions.operators.DamageOperator;
import net.playblack.cuboids.actions.operators.MiscOperator;
import net.playblack.cuboids.actions.operators.OperableItemsOperator;
import net.playblack.cuboids.actions.operators.PlayerMovementOperator;
import net.playblack.cuboids.actions.operators.SelectionOperator;
import net.playblack.cuboids.converters.Converter;
import net.playblack.cuboids.converters.Loader;
import net.playblack.cuboids.datasource.FlatfileDataLegacy;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Debug;
//import net.playblack.cuboids.regions.CuboidInterface;

/**
 * The Bootstrapper takes care of loading all the required components to run the
 * Plugin and also receives the server implementation.
 *
 * @author Chris
 *
 */
public class Bootstrapper {

    private CServer server;
    private Loader[] loaders;
    private Implementation impl;
    /**
     * Expects the server implementation and a list of loaders for foreign
     * cuboids. Leave the list null if nothing should be loaded from foreign
     * sources
     *
     * @param server
     * @param loaders
     */
    public Bootstrapper(CServer server, Loader[] loaders, Implementation impl) {
        this.server = server;
        this.loaders = loaders;
        this.impl = impl;

    }

    @SuppressWarnings("unused")
    public void bootstrap() {
     // ------------------------------------------------------
        CServer.setServer(server);
        Config.get().setImplementation(impl); // init this thing for a first time
        // ------------------------------------------------------
        boolean hasConverted = false;
        if (loaders != null) {
            Converter c = new Converter();
            for (Loader loader : loaders) {
                if (c.convertFiles(loader)) {
                    if (hasConverted == false) {
                        hasConverted = true;
                    }
                }
            }
        }
        if (hasConverted) {
            Debug.cacheMessage("Loaded CuboidPlugin files", false);
        }
        // ------------------------------------------------------
        int loaded = RegionManager.get().load();
        Debug.cacheMessage("Loaded " + loaded + " regions.", true);
        Debug.logCachedMessage();
        FlatfileDataLegacy.cleanupFiles();

        new BlockModificationsOperator();
        new DamageOperator();
        new MiscOperator();
        new OperableItemsOperator();
        new PlayerMovementOperator();
        new SelectionOperator();
    }
}
