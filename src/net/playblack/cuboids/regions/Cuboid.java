package net.playblack.cuboids.regions;

import java.util.ArrayList;
import java.util.HashMap;

import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.mcutils.Vector;

/**
 * This is a generic Cuboid area, comprising its start and end point,
 * aswell as a HashMap with any kind of properties in it
 * @author Chris Ksoll
 *
 */
public class Cuboid implements Region{
    
    public enum Status {
        ALLOW,
        DENY,
        INHERIT,
        INVALID_PROPERTY;
    }
    
    private HashMap<String, Status> properties;
    /** Reference to this Cuboids parent*/ //This'll get ugly when reading from datasource
    private Cuboid parent;
    
    /** The name of this cuboid*/
    private String name;
    
    /** List of allowed players*/
    private ArrayList<String> players;
    
    /** List of allowed groups*/
    private ArrayList<String> groups;
    
    /** The name of the world this cuboid sits in*/
    private String world;
    
    /** The ID of the dimension for this cuboid. This may always be 0 in some implementations */
    private int dimension;
    
    private Vector origin;
    private Vector offset;
    
    public Cuboid() {
        properties = new HashMap<String, Status>();
        players = new ArrayList<String>();
        groups = new ArrayList<String>();
        
        
    }
    
    /**
     * Set a property and its status for this cuboid
     * @param name
     * @param value
     */
    public void setProperty(String name, Status value) {
        properties.put(name, value);
    }
    
    /**
     * Get the status for a given property for this cuboid
     * @param name
     * @return
     */
    public Status getProperty(String name) {
        if(properties.containsKey(name)) {
            return properties.get(name);
        }
        return Status.INVALID_PROPERTY;
    }
    
    /**
     * Check if this cuboid has a given property.
     * @param name
     * @return
     */
    public boolean propertyExists(String name) {
        return properties.containsKey(name);
    }
    
    /**
     * Put this map of properties into the existing one of this cuboid,
     * this will override all already existing local values
     * @param map
     */
    public void putAll(HashMap<String, Status> map) {
        properties.putAll(map);
    }
    
    public HashMap<String, Status> getAllProperties() {
        return new HashMap<String, Cuboid.Status>(properties);
    }
    
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Add a group to the list of allowed groups
     * @param group
     * @return
     */
    public boolean addGroup(String group) {
        if (groups.contains(group)) {
            return false;
        }
        // group = group.substring(2);
        if (group.indexOf(",") >= 0) {
            addGroup(group.split(","));
            return true;
        } else {
            if (group.indexOf("g:") >= 0) {
                if (!group.equalsIgnoreCase("no_groups")) {
                    if (!group.equalsIgnoreCase("g:")) {
                        groups.add(group.substring(2).replace(" ", "")
                                .toLowerCase());
                        return true;
                    }
                }
            } else {
                // Could use some smuck up but heck, it's beeing used only
                // so-many times
                if (!group.equalsIgnoreCase("no_groups")) {
                    if (!group.replace(" ", "").isEmpty()) {
                        groups.add(group.replace(" ", "").toLowerCase());
                        return true;
                    }
                }
            }
            return false;
        }
    }

    /**
     * Add a couple of groups to the list of allowed groups.
     * 
     * @param groups
     *            Array
     */
    public void addGroup(String[] groups) {
        for (int i = 0; i < groups.length; i++) {
            addGroup(groups[i]);
        }

    }
    
    /**
     * Remove a group name to the list of allowed group names.
     * 
     * @param groupName
     *            String
     */
    public boolean removeGroup(String groupName) {
        if (groups.contains(groupName)) {
            groups.remove(groupName);
            return true;
        } else if (groups.contains("g:" + groupName)) {
            groups.remove("g:" + groupName);
            return true;
        } else if (groups.contains(groupName.substring(2))) {
            groups.remove(groupName.substring(2));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove a couple of groups to the list of allowed groups names.
     * 
     * @param groupNames
     *            Array
     */
    public void removeGroup(String[] groupNames) {
        for (int i = 0; i < groupNames.length; i++) {
            removeGroup(groupNames[i]);
        }
    }
    
    /**
     * Add a player name to the list of allowed player names.
     * 
     * @param playerName
     *            String
     */
    public boolean addPlayer(String playerName) {
        if (players.contains(playerName)) {
            return false;
        }
        if (playerName.indexOf(",") >= 0) {
            addPlayer(playerName.split(","));
            return true;
        } else {
            if (!playerName.equalsIgnoreCase("no_players")) {
                if (!playerName.equalsIgnoreCase("o:")) {
                    players.add(playerName.replace(" ", ""));
                    return true;
                }
                return false;
            }
            return false;
        }
    }

    /**
     * Add a couple of players to the list of allowed player names.
     * 
     * @param playerNames
     *            Array
     */
    public void addPlayer(String[] playerNames) {
        for (int i = 0; i < playerNames.length; i++) {
            addPlayer(playerNames[i]);
        }

    }
    
    /**
     * Remove a player name to the list of allowed player names.
     * 
     * @param playerName
     *            String
     */
    public boolean removePlayer(String playerName) {

        if (players.contains(playerName)) {
            players.remove(playerName);
            return true;
        } else if (players.contains("o:" + playerName)) {
            players.remove("o:" + playerName);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Remove a couple of players to the list of allowed player names.
     * 
     * @param playerNames
     *            Array
     */
    public void removePlayer(String[] playerNames) {
        for (int i = 0; i < playerNames.length; i++) {
            removePlayer(playerNames[i]);
        }
    }
    
    public boolean equalsWorld(CWorld world) {
        return (world.getDimension() == dimension) && (name.equals(world.getName()));
    }
    
    /* ************************************************
     * 
     * GETTER / SETTER STUFF
     * 
     * ************************************************/

    /**
     * @return the world
     */
    public String getWorld() {
        return world;
    }

    /**
     * @param world the world to set
     */
    public void setWorld(String world) {
        this.world = world;
    }

    /**
     * @return the dimension
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * @param dimension the dimension to set
     */
    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
    
    public void setParent(Cuboid cube) {
        parent = cube;
    }
    
    public Cuboid getParent() {
        return parent;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the origin
     */
    public Vector getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(Vector origin) {
        this.origin = origin;
    }

    /**
     * @return the offset
     */
    public Vector getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(Vector offset) {
        this.offset = offset;
    }
    
    public void setBoundingBox(Vector origin, Vector offset) {
        this.origin = origin;
        this.offset = offset;
    }
}
