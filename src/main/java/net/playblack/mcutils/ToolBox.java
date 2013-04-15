package net.playblack.mcutils;

import java.util.Arrays;
import java.util.HashSet;

import net.playblack.cuboids.gameinterface.CServer;

/**
 * This is a collection of useful stuff that we might or might not need
 *
 * @author Chris
 *
 */
public class ToolBox {
    private ToolBox() {

    }

    /**
     * Adjust the calculated block positions. This is used to correct some
     * positioning bugs
     *
     * @param pos
     * @return
     */
    public static void adjustWorldPosition(Vector pos) {
         if(pos.getBlockX() < 0) {
             pos.setX(pos.getBlockX()-1);
         }
         if(pos.getBlockZ() < 0) {
             pos.setZ(pos.getBlockZ()-1);
         }
//         return new Vector(pos.getX(), pos.getY(), pos.getZ());
//         NOTE: This is a dirty quick fix to work around the fact that all of
//         the sudden,
//         there is no issue with negative coordinates anymore
//         In case it somehow, magically, comes back again, I'm leaving this
//         here to uncomment again
//        return pos;
       // EDIT 2013: It came back again, lol
    }

    public static void adjustWorldPosition(Location loc) {
        if(loc.getBlockX() < 0) {
            loc.setX(loc.getBlockX() - 1);
        }

        if(loc.getBlockZ() < 0) {
            loc.setZ(loc.getBlockZ() - 1);
        }
    }

    /**
     * Adjust to canary rounding error thing.
     * This returns a new instance for region selection consistency reasons
     * @param p
     * @return
     */
    public static Vector adjustToCanaryPosition(Vector p) {
        Vector loc = new Vector(p);
        if(loc.getX() < -0.6D) {
            loc.setX(loc.getBlockX() - 1);
        }

        if(loc.getZ() < -0.6D) {
            loc.setZ(loc.getBlockZ() - 1);
        }
        return loc;
    }

    /**
     * Turn a String into a boolean value. If the string doesn't equal the words
     * true or false, this'll return false
     *
     * @param boo
     * @return boolean
     */
    public static boolean stringToBoolean(String boo) {
        if (boo == null) {
            return false;
        } else if (boo.equalsIgnoreCase("true")) {
            return true;
        } else if (boo.equalsIgnoreCase("false")) {
            return false;
        } else {
            return false;
        }
    }

    public static String stringToNull(String str) {
        if (str == null) {
            return null;
        }
        if (str.equalsIgnoreCase("null") || str.isEmpty() || str.equals(" ")) {
            return null;
        } else {
            return str;
        }
    }

    public static short convertType(String data) {
        short i = 0;
        try {
            // if that fails it must be a name
            i = (short) Integer.parseInt(data);
        } catch (NumberFormatException e) {
            short x = (short) CServer.getServer().getItemId(data);
            if (x == 0 && data.equalsIgnoreCase("air")) {
                return x;
            } else if (x > 0) {
                return x;
            } else {
                return -1;
            }
        }
        if (CServer.getServer().getItemName(i)
                .equalsIgnoreCase(String.valueOf(i))) {
            return -1;
        }
        return i;
    }

    public static byte convertData(String data) {
        byte i = 0;
        try {
            // if that fails it must be a name
            i = (byte) Integer.parseInt(data);
        } catch (NumberFormatException e) {
            i = -1;
        }
        return i;
    }

    public static int parseInt(String num) {
        int i = -1;
        try {
            i = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            i = -1;
        }
        return i;
    }

    /**
     * Merge 2 arrays into one. This will NOT omit duplicate entries
     * @param first
     * @param second
     * @return resultung array
     */
    public static <T> T[] mergeArrays(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * Merge 2 arrays into one, eliminating all duplicates.
     * TODO: Sure needs some performance improvements. HashSets ain't the best of all things for this task.
     * Too lazy right now though.
     * @param first
     * @param second
     * @return
     */
    public static <T> T[] safeMergeArrays(T[] first, T[] second, T[] result) {
        HashSet<T> res = new HashSet<T>();
        for(T tFirst : first) {
            res.add(tFirst);
        }
        for(T tSecond : second) {
            res.add(tSecond);
        }
        return res.toArray(result);
    }


}
