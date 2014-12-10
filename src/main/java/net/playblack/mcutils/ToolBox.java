package net.playblack.mcutils;

import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.exceptions.InvalidInputException;
import net.playblack.cuboids.gameinterface.CServer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * This is a collection of useful stuff that we might or might not need
 *
 * @author Chris
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
    public static void adjustWorldPosition(Vector3D pos) {
        if (pos.getBlockX() < 0) {
            pos.setX(pos.getBlockX() - 1);
        }
        if (pos.getBlockZ() < 0) {
            pos.setZ(pos.getBlockZ() - 1);
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
//        Canary.log.info("Adjusting block: " + loc.toString());
        if (loc.getBlockX() < 0) {
            loc.setX(loc.getBlockX() - 1);
        }

        if (loc.getBlockZ() < 0) {
            loc.setZ(loc.getBlockZ() - 1);
        }
    }

    /**
     * Adjust to canary rounding error thing.
     * This returns a new instance for region selection consistency reasons
     *
     * @param p
     * @return
     */
    public static Vector3D adjustToCanaryPosition(Vector3D p) {
        Vector3D loc = new Vector3D(p);
        if (loc.getX() < -0.6D) {
            loc.setX(loc.getBlockX() - 1);
        }

        if (loc.getZ() < -0.6D) {
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
        }
        else if (boo.equalsIgnoreCase("true")) {
            return true;
        }
        else if (boo.equalsIgnoreCase("false")) {
            return false;
        }
        else {
            return false;
        }
    }

    public static String stringToNull(String str) {
        if (str == null) {
            return null;
        }
        if (str.equalsIgnoreCase("null") || str.isEmpty() || str.equals(" ")) {
            return null;
        }
        else {
            return str;
        }
    }

    public static BlockType parseBlock(String input) throws InvalidInputException {
        String[] split = input.split(":");

        if (split.length > 1) {
            // we have a namespace, check if input wasn't legacy
            if (isNumeric(split[0]) || isNumeric(split[1])) {
                throw new InvalidInputException("Given block data were numbers. Use minecraft names instead!");
            }
            return BlockType.fromString(input);
        }
        else {
            return BlockType.fromString("minecraft:" + input);
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static short convertType(String data) {
        short i;
        try {
            // if that fails it must be a name
            i = (short) Integer.parseInt(data);
        }
        catch (NumberFormatException e) {
            short x = (short) CServer.getServer().getItemId(data);
            if (x == 0 && data.equalsIgnoreCase("air")) {
                return x;
            }
            else if (x > 0) {
                return x;
            }
            else {
                return -1;
            }
        }
        if (String.valueOf(i).equalsIgnoreCase(CServer.getServer().getItemName(i))) {
            return -1;
        }
        return i;
    }

    public static byte convertData(String data) {
        byte i;
        try {
            // if that fails it must be a name
            i = (byte) Integer.parseInt(data);
        }
        catch (NumberFormatException e) {
            i = -1;
        }
        return i;
    }

    public static int parseInt(String num) {
        int i;
        try {
            i = Integer.parseInt(num);
        }
        catch (NumberFormatException e) {
            i = -1;
        }
        return i;
    }

    /**
     * Merge 2 arrays into one. This will NOT omit duplicate entries
     *
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
     *
     * @param first
     * @param second
     * @return
     */
    public static <T> T[] safeMergeArrays(T[] first, T[] second, T[] result) {
        HashSet<T> res = new HashSet<T>();
        Collections.addAll(res, first);
        Collections.addAll(res, second);
        return res.toArray(result);
    }


}
