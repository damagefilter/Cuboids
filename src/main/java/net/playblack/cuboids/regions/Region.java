package net.playblack.cuboids.regions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Location;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;

public class Region {
    
    /** List of all child nodes */
    protected ArrayList<Region> childs = new ArrayList<Region>(5);
    
    /** Reference to this Cuboids parent*/ //This'll get ugly when reading from datasource
    protected Region parent;
    
    /** The name of this cuboid*/
    protected String name;
    
    /** The name of the world this cuboid sits in*/
    protected String world;
    
    /** The ID of the dimension for this cuboid. This may always be 0 in some implementations */
    protected int dimension;
    
    /** Cuboids priority. Cuboids with higher priority will be considered when areas clash*/
    protected int priority;
    
    
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
        
        /**
         * Returns allow or deny from a boolean value
         * @param check
         * @return
         */
        public static Status fromBoolean(boolean check) {
            if(check) {
                return ALLOW;
            }
            else {
                return DENY;
            }
        }
        
        /**
         * Returns allow or default from boolean value
         * @param check
         * @return
         */
        public static Status softFromBoolean(boolean check) {
            if(check) {
                return ALLOW;
            }
            else {
                return DEFAULT;
            }
        }
    }
    
    
    /** Map of all currently existing property */
    private HashMap<String, Status> properties;
    
    /** Flag as changed to make it available for the saving thread */
    public boolean hasChanged = false;
    
    
    /** List of allowed players*/
    private ArrayList<String> players;
    
    /** List of allowed groups*/
    private ArrayList<String> groups;
    
    private Vector origin;
    private Vector offset;
    
    /** Welcome / Farewell messages to display*/
    private String welcome, farewell;
    
    /** List of commands that should be denied in this area */
    private ArrayList<String> restrictedCommands = new ArrayList<String>();
    
    /** List of restricted item IDs */
    private ArrayList<Integer> restrictedItems = new ArrayList<Integer>();
    
    
    public Region() {
        properties = new HashMap<String, Status>();
        players = new ArrayList<String>();
        groups = new ArrayList<String>();
        
        
    }
    
    public Region(Region parent) {
        this.parent = parent;
        if(parent != null) {
            parent.childs.add(this);
        }
    }
    
    
    /**
     * Adds a player that is in this area
     * 
     * @param playerName
     * @deprecated Regions do not contain information about players anymore. This information is generated on-the-fly now
     */
    @Deprecated
    public void addPlayerWithin(String playerName) {
        if (!playerName.equalsIgnoreCase("no_players")) {
            //Add into this cuboid
            if (!playerName.substring(2).isEmpty()) {
//                playersWithin.add(playerName);
            }
        }
    }
    
    /**
     * Recursively add player to this Region and its childs.
     * This will set the currentRegion in the player
     * Recursively goes down
     * @param player
     * @param loc
     */
    public void addPlayerWithin(CPlayer player, Location loc) {
        if (!player.getName().equalsIgnoreCase("no_players")) {
            //Add into this cuboid
            if (!player.getName().substring(2).isEmpty()) {
                if(isWithin(loc)) {
                    player.setRegion(this);
                }
            }
            for(Region child : childs) {
                if(child.isWithin(loc)) {
                    child.addPlayerWithin(player, loc);
                }
            }
        }
    }
    
    /**
     * Recursively remove player from this Region and its childs
     * @param player
     * @param toCheck
     */
    public void removePlayerWithin(CPlayer player, Location toCheck) {
        if(!isWithin(toCheck)) {
            if(parent != null && parent.isWithin(toCheck)) {
                player.setRegion(parent);
            }
            //Assume we're not in any region anymore.
            //If we are we 
            player.setRegion(null);
            for(Region child : childs) {
                child.removePlayerWithin(player, toCheck);
                
            }
        }
    }

    /**
     * Remove player from within list
     * 
     * @param playerName
     * @return
     */
    @Deprecated
    public boolean removePlayerWithin(String playerName) {
//        if (playersWithin.contains(playerName)) {
//            playersWithin.remove(playerName);
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }

    /**
     * Get all players that are within this cuboid as arrayList<string>
     * 
     * @return
     */
    @Deprecated
    public ArrayList<String> getPlayersWithin() {
        return null;
    }

    /**
     * Check if a player is within this region
     * @param playerName
     * @return
     */
    @Deprecated
    public boolean playerIsWithin(String playerName) {
//        if (playersWithin.contains(playerName)) {
//            return true;
//        } else {
//            return false;
//        }
        return false;
    }
    
    /**
     * Set the parent region for the this region.
     * This will also update the parents child list and adjust priority levels
     * @param cube
     */
    public void setParent(Region cube) {
        parent = cube;
        if(parent != null) {
            parent.childs.add(this);
        }
        if(parent.priority > priority) {
            this.priority = parent.priority + 1;
        }
    }
    
    /**
     * Get the parent cuboid for this guy
     * @return
     */
    public Region getParent() {
        return parent;
    }
    
    /**
     * Get the name of this cuboid
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the name of this cuboid
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
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
    
    /**
     * get this regiosn priority
     * @return
     */
    public int getPriority() {
        return priority;
    }
    
    /**
     * Set this regions priority and update all childs accordingly
     * @param prio
     */
    public void setPriority(int prio) {
        int difference = prio - priority;
        this.priority = prio;
        for(Region child : childs) {
            child.setPriority(child.getPriority() + difference);
            child.hasChanged = true;
        }
        
    }
    
    public boolean equalsWorld(CWorld world) {
        return (world.getDimension() == dimension) && (this.world.equals(world.getName()));
    }
    
    public boolean equalsWorld(Region other) {
        return (other.dimension == dimension) && (world.equals(other.world));
    }
    
    public boolean equalsWorld(String name, int dim) {
        return (dim == dimension) && (world.equals(name));
    }
    
    /**
     * Detach this region from its parent and update the parent
     */
    public void detach() {
        if(parent != null) {
            parent.detachChild(this);
            parent = null;
        }
    }
    /**
     * Add a region to the list of childs of tis cuboid
     * @param c
     */
    public void attachRegion(Region c) {
        c.setParent(this);
        childs.add(c);
    }
    
    /**
     * Remove a child from this regions child list
     * @param c
     */
    public void detachChild(Region c) {
        childs.remove(c);
    }
    
    /**
     * Detach all childs from this cuboid and update their parents.
     * Returns the list of all childs for further processing.
     */
    public ArrayList<Region> detachAllChilds() {
        //first override parents
        for(Region child : childs) {
            child.setParent(parent);
        }
        //return all childs
        return childs;
    }
    
    public void clearChilds() {
        childs.clear();
    }
    
    /**
     * Try finding a cuboid with the given name within the childs.
     * This is NOT very performant as it needs to query A LOT of childs!
     * @param name
     * @return
     */
    public Region queryChilds(String name) {
        Region candidate = null;
        if(this.name.equals(name)) {
            return this;
        }
        if(parent != null &&  parent.name.equals(name)) {
            return parent;
        }
        for(Region c : childs) {
            if(c.getName().equals(name)) {
                return c;
            }
            candidate = c.queryChilds(name);
            if(candidate != null && candidate.getName().equals(name)) {
                return candidate;
            }
        }
        return null;
    }
    
    /**
     * Query for a cuboid at the specified location. This will return the cuboid that
     * should be taken into consideration when processing stuff.
     * That is: the one with the max priority or if prio clashes, then the smaller one
     * @param loc
     * @param priority You should set this to 0 it's for internal stuffs
     * @return
     */
    public Region queryChilds(Location loc, int priority) {
        if(!isWithin(loc)) {
            return null;
        }
        Region current = null;
        if(parent != null && parent.isWithin(loc)) {
            current = parent;
        }
        for(Region c : childs) {
            if(c.isWithin(loc)) {
                if(current == null) {
                    current = c;
                }
                else {
                    if(current.getPriority() < c.getPriority()) {
                        current = c;
                    }
                    else if (current.getPriority() == c.getPriority()) {
                        current = current.getSize() > c.getSize() ? c : current;
                    }
                }
                
                Region check = c.queryChilds(loc, current.getPriority());
                if(check.getPriority() > current.getPriority()) {
                    current = check;
                }
                else if(check.getPriority() == current.getPriority()) {
                    current = current.getSize() > check.getSize() ? check : current;
                }
            }
        }
        return current;
    }
    
    /**
     * Query the childs of this cuboid to see if they space-wise
     * contain the given cuboid, in order to find the given cuboids appropriate parent
     * @param cube
     * @return
     */
    public Region queryChilds(Region cube) {
        Region current = null;
        if(parent != null && cube.cuboidIsWithin(parent, true)) {
            current = parent;
        }
        for(Region c : childs) {
            if(cube.cuboidIsWithin(c, true)) {
                if(current == null) {
                    current = c;
                }
                else {
                    if(c.getPriority() > current.getPriority()) {
                        current = c;
                    }
                    else if(c.getPriority() == current.getPriority()) {
                        current = current.getSize() > c.getSize() ? c : current;
                    }
                }
                
                Region check = c.queryChilds(cube);
                if(check.getPriority() > current.getPriority()) {
                    current = check;
                }
                else if(check.getPriority() == current.getPriority()) {
                    current = current.getSize() > check.getSize() ? check : current;
                }
            }
        }
        return current;
    }
    
    /**
     * Fix childs and remove those that are not 100%
     * inside this parent node. Return a list of those if they
     * end up without parent
     * @return
     */
    public ArrayList<Region> fixChilds() {
        ArrayList<Region> detached = new ArrayList<Region>();
        for(Region child : childs) {
            if(!child.cuboidIsWithin(this, true)) {
                child.detach();
                detached.add(child);
            }
            detached.addAll(child.fixChilds());
        }
        
        return detached;
    }
    /**
     * Check if this is the global settings Cuboid
     * @return
     */
    public boolean isGlobal() {
        return name.equals("__WORLD__");
    }
    
    
    
    /**
     * Check if this is a root cuboid, that means it has no parent
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Recursively generates a list of childs and their childs etc.
     * and returns an unmodifiable view of this list
     * @return
     */
    public List<Region> getChildsDeep(List<Region> collection) {
        for(Region r : childs) {
            
            collection.addAll(r.getChildsDeep(collection));
        }
        if(!collection.contains(this)) {
            collection.add(this);
        }
        return collection;
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
        return Status.DEFAULT;
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
        return new HashMap<String, Region.Status>(properties);
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
     * Check if this Region is within the given one
     * @param r
     * @param complete
     * @return
     */
    public boolean cuboidIsWithin(Region r, boolean complete) {
        Region cube = (Region)r;
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
     * Check if the given Location is within this Region
     * @param v
     * @return
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
    
    /**
     * Calculate and get the volume size of this region
     * @return
     */
    public int getSize() {
        return (int) Vector.getDistance(origin.getBlockX(), offset.getBlockX())
                * (int) Vector.getDistance(origin.getBlockY(), offset.getBlockY())
                * (int) Vector.getDistance(origin.getBlockZ(), offset.getBlockZ());
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
     * Check if a given player is allowed in this cuboid. This will take into account
     * ownership of this cuboid as well as groups
     * 
     * @param player
     * @return True if player is allowed, false otherwise
     */
    public boolean playerIsAllowed(String player, String[] group) {
        if(playerIsOwner(player)) {
            return true;
        }
        
        for (String groupname : group) {
            if (groups.contains(groupname)) {
                return true;
            }
        }
        for (int i = 0; i < group.length; i++) {
            if (groups.contains(group[i].toLowerCase())) {
                return true;
            } 
            else if (groups.contains("g:" + group[i].toLowerCase())) {
                return true;
            }
        }
        for (String listPlayer : players) {
            if (listPlayer.equalsIgnoreCase(player)) {
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
                builder.append(ColorManager.Rose).append(key).append(": ")
                .append(ColorManager.LightGreen)
                .append(properties.get(key).name())
                .append(", ");
            }
            else {
                count = 0;
                builder.append(";");
                
            }
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if(other == null) {
            return false;
        }
        if(other instanceof Region) {
            Region c = (Region)other;
            return (c.name.equals(name)) && (c.getSize() == getSize()) && (c.world.equals(world)) && (c.dimension == dimension);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return getSize() + priority + dimension;
    }
}
