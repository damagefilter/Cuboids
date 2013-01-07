package net.playblack.cuboids.regions;

import java.util.ArrayList;
import java.util.HashMap;

import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Location;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;

/**
 * This is a generic Cuboid area, comprising its start and end point,
 * aswell as a HashMap with any kind of properties in it
 * @author Chris Ksoll
 *
 */
public class Cuboid extends Region{
    
    public enum Status {
        ALLOW,
        DENY,
        INHERIT,
        DEFAULT,
        INVALID_PROPERTY;
        
        public static Status fromString(String str) {
            if(str.equalsIgnoreCase(ALLOW.name())) {
                return ALLOW;
            }
            else if(str.equalsIgnoreCase(DENY.name())) {
                return DENY;
            }
            else if(str.equalsIgnoreCase(INHERIT.name())) {
                return INHERIT;
            }
            else if(str.equalsIgnoreCase(DEFAULT.name())) {
                return DEFAULT;
            }
            else {
                return INVALID_PROPERTY;
            }
        }
    }
    
    private HashMap<String, Status> properties;
    public boolean hasChanged = false;
    
    
    /** List of allowed players*/
    private ArrayList<String> players;
    
    /** List of allowed groups*/
    private ArrayList<String> groups;
    
    /** Players that are currently within this area*/
    private ArrayList<String> playersWithin;
    
    private Vector origin;
    private Vector offset;
    
    /** Welcome / Farewell messages to display*/
    private String welcome, farewell;
    /** List of commands that should be denied in this area */
    private ArrayList<String> restrictedCommands = new ArrayList<String>();
    /** List of restricted item IDs */
    private ArrayList<Integer> restrictedItems = new ArrayList<Integer>();
    
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
    
    /**
     * Check if this cuboid is inside another
     * 
     * @param v1
     *            Other Cuboid Point 1
     * @param v2
     *            Other Cuboid Point 2
     * @param complete
     *            true: Cuboid must be completely inside, false: Cuboid can be
     *            inside only partially
     * @return true if cuboid is inside another, false otherwise
     */
    public boolean cuboidIsWithin(Vector v1, Vector v2, boolean complete) {
        Vector min = Vector.getMinor(v1, v2);
        Vector max = Vector.getMajor(v1, v2);
        if (complete == true) {
            if (origin.isWithin(min, max) && offset.isWithin(min, max)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (origin.isWithin(min, max) || offset.isWithin(min, max)) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    /**
     * Check if this cuboid is inside the given one
     * 
     * @param cube
     * @param complete
     *            true to check if it is inside with both edges
     * @return
     */
    public boolean cuboidIsWithin(Cuboid cube, boolean complete) {
        if (this.equalsWorld(cube)) {
            Vector min = Vector.getMinimum(cube.getOrigin(),
                    cube.getOffset());
            Vector max = Vector.getMaximum(cube.getOrigin(),
                    cube.getOffset());

            if (complete == true) {
                if (origin.isWithin(min, max) && offset.isWithin(min, max)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (origin.isWithin(min, max) || offset.isWithin(min, max)) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
    
    /**
     * Checks if a given vector v is within this cuboid.
     * 
     * @param v
     * @return True: Point is within this cuboid, false otherwise
     */
    public boolean isWithin(Location v) {
        if (!v.getWorld().equals(this.world)
                || !(v.getDimension() == this.dimension)) {
            return false;
        }
        Vector min = Vector.getMinimum(origin, offset);
        Vector max = Vector.getMaximum(origin, offset);
        if (v.isWithin(min, max)) {
            return true;
        } else {
            return false;
        }
    }
    
    public int getSize() {
        return (int) Vector.getDistance(origin.getBlockX(), offset.getBlockX())
                * (int) Vector.getDistance(origin.getBlockY(), offset.getBlockY())
                * (int) Vector.getDistance(origin.getBlockZ(), offset.getBlockZ());
    }
    
    /**
     * Adds a player that is in this area
     * 
     * @param playerName
     */
    public void addPlayerWithin(String playerName) {
        if (!playerName.equalsIgnoreCase("no_players")) {
            if (!playerName.substring(2).isEmpty()) {
                playersWithin.add(playerName);
            }
        }
    }

    /**
     * Remove player from within list
     * 
     * @param playerName
     * @return
     */
    public boolean removePlayerWithin(String playerName) {
        if (playersWithin.contains(playerName)) {
            playersWithin.remove(playerName);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get all players that are within this cuboid as arrayList<string>
     * 
     * @return
     */
    public ArrayList<String> getPlayersWithin() {
        return playersWithin;
    }

    public boolean playerIsWithin(String playerName) {
        if (playersWithin.contains(playerName)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Check if a given player is the owner of this cuboid. That is to say, if
     * his name is inside the list of players as o:.
     * 
     * @param player
     * @return True if player is allowed, false otherwise
     */
    public boolean playerIsOwner(String player) {
        for (String listPlayer : players) {
            if (listPlayer.equalsIgnoreCase("o:" + player)) {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Add a commands to the list of tabu commands.
     * 
     * @param command
     *            String
     */
    public void addRestrictedCommand(String command) {
        if (!command.equalsIgnoreCase("no_commands")) {
            if (command.indexOf(",") >= 0) {
                addRestrictedCommand(command.split(","));
            } else {
                restrictedCommands.add(command);
            }
        }
    }

    /**
     * Add a couple of commands to the list of tabu commands.
     * 
     * @param commands
     *            Array
     */
    public void addRestrictedCommand(String[] commands) {
        for (int i = 0; i < commands.length; i++) {
            if (!commands[i].equalsIgnoreCase("no_commands")) {
                restrictedCommands.add(commands[i]);
            }
        }

    }

    /**
     * Remove a commands to the list of tabu commands.
     * 
     * @param command
     *            String
     */
    public void removeRestrictedCommand(String command) {
        if (restrictedCommands.contains(command)) {
            restrictedCommands.remove(command);
        }
    }

    /**
     * Return the arraylist containing restricted commands
     * 
     * @return
     */
    public ArrayList<String> getRestrictedCommands() {
        return this.restrictedCommands;
    }

    public boolean commandIsRestricted(String command) {
        if (restrictedCommands.contains(command)
                || restrictedCommands.contains(command.substring(1))) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Add items to regions restricted items list
     * 
     * @param id
     */
    public void addRestrictedItem(int id) {
        if (id < 0) {
            return;
        }
        restrictedItems.add(Integer.valueOf(id));
    }

    /**
     * Add one or more comma seperated items from string to the restricted items
     * list
     * 
     * @param items
     */
    public void addRestrictedItem(String items) {
        if (items == null) {
            return;
        }
        if (items.contains(",")) {
            String[] itemList = items.split(",");
            for (String item : itemList) {
                addRestrictedItem(ToolBox.parseInt(item));
            }
        }
        addRestrictedItem(ToolBox.parseInt(items));
    }

    /**
     * Remove items to regions restricted items list
     * 
     * @param id
     */
    public void removeRestrictedItem(int id) {
        restrictedItems.remove(Integer.valueOf(id));
    }

    /**
     * Check if item is restricted
     * 
     * @param id
     * @return true if item is restricted, false otherwise
     */
    public boolean isItemRestricted(int id) {
        if (restrictedItems.contains(Integer.valueOf(id))) {
            return true;
        }
        return false;
    }

    public ArrayList<Integer> getRestrictedItems() {
        return restrictedItems;
    }

    /**
     * Check if a given player is allowed in this cuboid. This also checks for
     * the player group and the player name
     * 
     * @param player
     * @return True if player is allowed, false otherwise
     */
    public boolean playerIsAllowed(String player, String[] group) {
        for (String groupname : group) {
            if (groups.contains(groupname)) {
                return true;
            }
        }
        for (int i = 0; i < group.length; i++) {
            if (groups.contains(group[i].toLowerCase())) {
                return true;
            } else if (groups.contains("g:" + group[i].toLowerCase())) {
                return true;
            }
        }
        for (String listPlayer : players) {
            if (listPlayer.equalsIgnoreCase(player)
                    || listPlayer.equalsIgnoreCase("o:" + player)) {
                return true;
            }
        }
        return false;
    }
    
    public String getPlayerList() {
        StringBuilder players = new StringBuilder();
        for (int i = 0; i < this.players.size(); i++) {
            if (i > 0) {
                players.append(",");
            }
            players.append(this.players.get(i));
        }
        String out = players.toString();
        if (out.length() == 0) {
            return "no_players";
        } else {
            return out;
        }
    }
    
    public String getGroupList() {
        StringBuilder groups = new StringBuilder();
        for (int i = 0; i < this.groups.size(); i++) {
            if (i > 0) {
                groups.append(",");
            }
            groups.append(this.groups.get(i));
        }
        String out = groups.toString();
        if (out.length() == 0) {
            return "no_groups";
        } else {
            return out;
        }
    }
    
    public String getItemListAsNames() {
        StringBuilder items = new StringBuilder();
        for (Integer i : restrictedItems) {
            items.append(CServer.getServer().getItemName(i.intValue())).append(
                    ",");
        }
        if (items.length() == 0) {
            return "";
        }
        return items.toString();
    }
    
    
    /* ************************************************
     * 
     * GETTER / SETTER STUFF
     * 
     * ************************************************/

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

    /**
     * @return the welcome
     */
    public String getWelcome() {
        return welcome;
    }

    /**
     * @param welcome the welcome to set
     */
    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    /**
     * @return the farewell
     */
    public String getFarewell() {
        return farewell;
    }

    /**
     * @param farewell the farewell to set
     */
    public void setFarewell(String farewell) {
        this.farewell = farewell;
    }

    public String getFlagList() {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for(String key : properties.keySet()) {
            if(count <= 3) {
                builder.append(ColorManager.Rose).append(key).append(": ").append(ColorManager.LightGreen).append(properties.get(key).name());
            }
            else {
                count = 0;
                builder.append(";");
                
            }
        }
        return builder.toString();
    }

}
