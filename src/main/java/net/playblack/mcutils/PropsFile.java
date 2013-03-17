package net.playblack.mcutils;

import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.visualillusionsent.utils.PropertiesFile;
import net.visualillusionsent.utils.UtilityException;



/**
 * Custom properties file manager
 * 
 * @author Chris
 * 
 */
public class PropsFile {
    PropertiesFile propsFile;
    String path;
    public int startingHashCode;

    /**
     * Load the properties file
     * 
     * @param path
     */
    public PropsFile(String path) {
        propsFile = new PropertiesFile(path);
        startingHashCode = propsFile.hashCode();
        this.path = path;
    }

    /**
     * Return a boolean value
     * 
     * @param key
     * @param substitute
     * @return
     */
    public boolean getBoolean(String key, boolean substitute) {
        boolean b = substitute;
        try {
            b = propsFile.getBoolean(key);
        }
        catch(UtilityException e) {
            propsFile.setBoolean(key, substitute);
        }
        return b;
    }

    /**
     * Get an integer value
     * 
     * @param key
     * @param substitute
     * @return
     */
    public int getInt(String key, int substitute) {
        int i = substitute;
        try {
            i = propsFile.getInt(key);
        }
        catch(UtilityException e) {
            propsFile.setInt(key, substitute);
        }
        return i;
    }

    /**
     * Get a long value
     * 
     * @param key
     * @param substitute
     * @return
     */
    public long getLong(String key, long substitute) {
        long i = substitute;
        try {
            i = propsFile.getInt(key);
        }
        catch(UtilityException e) {
            propsFile.setLong(key, substitute);
        }
        return i;
    }

    /**
     * Return a String value
     * 
     * @param key
     * @param substitute
     * @return
     */
    public String getString(String key, String substitute) {
        String i = substitute;
        try {
            i = propsFile.getString(key);
        }
        catch(UtilityException e) {
            propsFile.setString(key, substitute);
        }
        return i;
    }
    
    public Region.Status getStatus(String key, Region.Status substitute) {
        String i = substitute.name();
        
        try {
            i = propsFile.getString(i);
        }
        catch(UtilityException e) {
            propsFile.setString(key, substitute.name());
        }
        return Status.fromString(i);
    }
    
    public void save() {
        propsFile.save();
    }
    
    public boolean hasChangedSinceLoad() {
        return startingHashCode != propsFile.hashCode();
    }

}
