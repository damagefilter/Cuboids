package net.playblack.mcutils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The static log manager.
 * 
 * @author chris
 * 
 */
public class Debug {
    private static Logger log = Logger.getLogger("Minecraft");
    private static StringBuilder cacheMessage = new StringBuilder();
    private static boolean cacheRunning = false;

   /**
    * Log message
    * @param message
    */
    public static void log(String message) {
        if (cacheRunning) {
            cacheMessage(message, true);
            return;
        }
        log.log(Level.INFO, "[Cuboids2] " + message);
    }
    
    /**
     * Log warning
     * @param msg
     */
    public static void logWarning(String msg) {
        if (cacheRunning) {
            cacheMessage(msg, true);
            return;
        }
        log.log(Level.WARNING, "[Cuboids2] " + msg);
    }
    
    /**
     * Log error (severe)
     * @param msg
     */
    public static void logError(String msg) {
        if (cacheRunning) {
            cacheMessage(msg, true);
            return;
        }
        log.log(Level.SEVERE, "[Cuboids2] " + msg);
    }
    
    /**
     * Log stacktrace
     * @param t
     */
    public static void logStack(Throwable t) {
        log.log(Level.WARNING, "[Cuboids2] " + t.getMessage(), t);
    }
    
    public static void println(String msg) {
        System.out.println(msg);
    }
    
    public static void print(String msg) {
        System.out.print(msg);
    }

    /**
     * Append text to the logging cache. The reults will be filed as single
     * block entry when cache gets logged.
     * 
     * @param msg
     */
    public static void cacheMessage(String msg, boolean newLine) {
        if (!cacheRunning) {
            cacheRunning = true;
        }
        cacheMessage.append(msg);
        if (newLine) {
            cacheMessage.append(System.getProperty("line.separator"));
        }
    }

    /**
     * Log cached messages as one and clear the cache for new stuff
     */
    public static void logCachedMessage() {
        cacheRunning = false;
        log(cacheMessage.toString());
        cacheMessage = new StringBuilder();
    }
}
