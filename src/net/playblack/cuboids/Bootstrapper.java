package net.playblack.cuboids;

//import java.util.concurrent.TimeUnit;

import net.playblack.cuboids.converters.Converter;
import net.playblack.cuboids.converters.Loader;
import net.playblack.cuboids.datasource.FlatfileDataLegacy;
import net.playblack.cuboids.gameinterface.CServer;
//import net.playblack.cuboids.regions.CuboidInterface;
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
        log.cacheMessage("Loading Cuboids2 ...", true);

        //------------------------------------------------------
       // log.cacheMessage("Server interface... ", false);
        CServer.setServer(server);
        //log.cacheMessage("done!", false);
        log.logCachedMessage("INFO");
        
      //------------------------------------------------------
        Config.getInstance(); //init this thing for a first time
        log.cacheMessage("Version ... "+Config.getInstance().getVersion(), true);
        //log.cacheMessage("Configuration ...", false);
        //log.cacheMessage("done!", false);
        log.logCachedMessage("INFO");
        
        //------------------------------------------------------
        log.cacheMessage("Tasks ...", false);
        //Debug code
//        CuboidInterface.getInstance().getThreadmanager().scheduleAtFixedRate(new Runnable() {
//            
//            @Override
//            public void run() {
//                EventLogger.getInstance().logMessage("Still alive!", "DEBUG");
//                
//            }
//        }, 1, 1, TimeUnit.MINUTES); //init this thing for a first time
        log.cacheMessage("done!", false);
        log.logCachedMessage("INFO");
        
        //------------------------------------------------------
        log.cacheMessage("Foreign Cuboid files... ", true);
        boolean hasConverted = false;
        if(loaders != null) {
            Converter c = new Converter();
            for(Loader loader : loaders) {
                log.cacheMessage("Checking for "+loader.getImplementationVersion()+"... ", true);
                if(c.convertFiles(loader)) {
                    if(hasConverted == false) {
                        hasConverted = true;
                    }
                }
            }
        }
        if(hasConverted) {
            log.cacheMessage("done", false);
        }
        else {
            log.cacheMessage("Nothing foreign to load found", false);
        }
        log.logCachedMessage("INFO");
        
        //------------------------------------------------------
        log.cacheMessage("Native Cuboid Nodes...", true);
        RegionManager.getInstance().load();
        log.cacheMessage("done!", false);
        log.logCachedMessage("INFO");
        FlatfileDataLegacy.cleanupFiles();
    }
}
