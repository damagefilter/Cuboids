package net.playblack.cuboids;

import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.mcutils.EventLogger;

/**
 * The Bootstrapper takes care of loading all the required 
 * components to run the cuboidlib and also receives the server implementation.
 * @author Chris
 *
 */
public class Bootstrapper {
    public Bootstrapper(CServer server) {
        EventLogger log = EventLogger.getInstance();
        log.logMessage("Loading ...", "INFO");
        log.cacheMessage("Server interface... ");
        CServer.setServer(server);
        log.cacheMessage("done!");
        log.logCachedMessage("INFO");
    }
}
