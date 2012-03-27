package net.playblack.mcutils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Custom properties file manager
 * @author Chris
 *
 */
public class PropsFile {
    String path;
    private HashMap<String,String> propsRaw;
    
    /**
     * Load the properties file
     * @param path
     */
    public PropsFile(String path) {
        this.path = path;
        File f = new File(path);
        if(!f.exists()) {
            f.mkdirs();
        }
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(path);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        DataInputStream inStream = new DataInputStream(fstream);
        BufferedReader prop = new BufferedReader(new InputStreamReader(inStream));
        propsRaw = new HashMap<String,String>();
        try {
            String line = null;
            while((line = prop.readLine()) != null) {
                if(line.startsWith("#")) {
                    continue;
                }
                String[] split = line.split("=");
                propsRaw.put((split[0]), split[1]); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                prop.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Return a boolean value
     * @param key
     * @param substitute
     * @return
     */
    public boolean getBoolean(String key, boolean substitute) {
        if(propsRaw.containsKey(key)) {
            return Boolean.parseBoolean(propsRaw.get(key));
        }
        return substitute;
    }
    
    /**
     * Get an integer value
     * @param key
     * @param substitute
     * @return
     */
    public int getInt(String key, int substitute) {
        if(propsRaw.containsKey(key)) {
            return Integer.parseInt(propsRaw.get(key));
        }
        return substitute;
    }
    
    /**
     * Get a long value
     * @param key
     * @param substitute
     * @return
     */
    public long getLong(String key, long substitute) {
        if(propsRaw.containsKey(key)) {
            return Long.parseLong(propsRaw.get(key));
        }
        return substitute;
    }
    /**
     * Return a String value
     * @param key
     * @param substitute
     * @return
     */
    public String getString(String key, String substitute) {
        if(propsRaw.containsKey(key)) {
            return propsRaw.get(key);
        }
        return substitute;
    }
    
}
