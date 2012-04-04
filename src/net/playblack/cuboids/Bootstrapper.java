package net.playblack.cuboids;

import net.playblack.cuboids.converters.Converter;
import net.playblack.cuboids.converters.Loader;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.EventLogger;

/**
 * The Bootstrapper takes care of loading all the required 
 * components to run the cuboidlib and also receives the server implementation.
 * @author Chris
 *
 */
public class Bootstrapper {
    /**
     * Expects the server implementation and a list of loaders for foreign cuboids.
     * Leave the list null if nothing should be loaded from foreign sources
     * @param server
     * @param loaders
     */
    public Bootstrapper(CServer server, Loader[] loaders) {
        EventLogger log = EventLogger.getInstance();
        log.logMessage("Loading ...", "INFO");
        
        //------------------------------------------------------
        log.cacheMessage("Configuration ...", true);
        Config.getInstance(); //init this thing for a first time
        log.cacheMessage("done!", true);
        log.logCachedMessage("INFO");
        
        //------------------------------------------------------
        log.cacheMessage("Server interface... ", false);
        CServer.setServer(server);
        log.cacheMessage("done!", true);
        log.logCachedMessage("INFO");
        
        //------------------------------------------------------
        log.cacheMessage("Tasks ...", true);
        CuboidInterface.getInstance(); //init this thing for a first time
        log.cacheMessage("done!", true);
        log.logCachedMessage("INFO");
        
        //------------------------------------------------------
        log.cacheMessage("Foreign Cuboid files... ", true);
        boolean hasConverted = false;
        if(loaders != null) {
            Converter c = new Converter();
            for(Loader loader : loaders) {
                log.cacheMessage("Loading "+loader.getImplementationVersion()+"... ", false);
                if(c.convertFiles(loader)) {
                    if(hasConverted == false) {
                        hasConverted = true;
                    }
                }
            }
        }
        if(hasConverted) {
            log.cacheMessage("done", true);
        }
        else {
            log.cacheMessage("Nothing foreign to load found", true);
        }
        log.logCachedMessage("INFO");
        
        //------------------------------------------------------
        log.cacheMessage("Native Cuboid Nodes...", true);
        RegionManager.getInstance().load();
        log.cacheMessage("done!", true);
        log.logCachedMessage("INFO");
    }
}
