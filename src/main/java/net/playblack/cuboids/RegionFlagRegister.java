package net.playblack.cuboids;

import java.util.ArrayList;

/**
 * A Register for filing valid flags.
 * This class mainly is for userland to show a user all available flags.
 * Additionally it is used to prevent adding of typo-derped flags
 * and keep the flag lists clean of unused flags
 * @author chris
 *
 */
public class RegionFlagRegister {
    private static ArrayList<String> knownFlags = new ArrayList<String>();
    
    /**
     * Make a flag known to Cuboids.
     * This is mostly called in any DataSource to catch all the existing flags.
     * As extension plugin you should use the implemented frameworks enable() method,
     * to register your own flags
     * @param flag
     */
    public static void registerFlag(String flag) {
        if(!knownFlags.contains(flag)) {
            knownFlags.add(flag);
        }
    }
    
    /**
     * Check if a flag is known and registered.
     * @param flag
     * @return
     */
    public static boolean isFlagValid(String flag) {
        return knownFlags.contains(flag);
    }
    
    /**
     * Get a String[] of all valid registered flags 
     * @return
     */
    public static String[] getRegisteredFlags() {
        String[] ret = new String[knownFlags.size()];
        return knownFlags.toArray(ret);
    }
}
