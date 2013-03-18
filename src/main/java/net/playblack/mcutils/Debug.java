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
    private static Logger log;
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
        log.log(Level.INFO, message);
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
        log.log(Level.WARNING, msg);
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
        log.log(Level.SEVERE, msg);
    }
    
    /**
     * Log stacktrace
     * @param t
     */
    public static void logStack(Throwable t) {
        log.log(Level.WARNING, t.getMessage(), t);
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
     * 
     * @param level
     */
    public static void logCachedMessage(String level) {
        cacheRunning = false;
        log(cacheMessage.toString());
        cacheMessage = new StringBuilder();
    }
}
