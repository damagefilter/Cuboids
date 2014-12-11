package net.playblack.cuboids.regions;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.position.Vector3D;
import net.canarymod.user.Group;
import net.playblack.cuboids.RegionFlagRegister;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Region {

    /**
     * List of all child nodes
     */
    private ArrayList<Region> childs = new ArrayList<Region>(5);
    /**
     * Reference to this Cuboids parent
     */
    private Region parent;
    /**
     * The name of this cuboid
     */
    private String name;
    /**
     * The name of the world this cuboid sits in
     */
    private String world;

    /**
     * Cuboids priority. Cuboids with higher priority will be considered when areas clash
     */
    private int priority;
    /**
     * Map of all currently existing property
     */
    private HashMap<String, Status> properties;
    /**
     * List of allowed players
     */
    private ArrayList<String> players;
    /**
     * List of allowed groups
     */
    private ArrayList<String> groups;
    private Vector3D origin;
    private Vector3D offset;
    /**
     * Welcome / Farewell messages to display
     */
    private String welcome, farewell;
    /**
     * List of commands that should be denied in this area
     */
    private ArrayList<String> restrictedCommands = new ArrayList<String>();
    /**
     * List of restricted item IDs
     */
    private ArrayList<String> restrictedItems = new ArrayList<String>();

    public Region() {
        properties = new HashMap<String, Status>();
        players = new ArrayList<String>();
        groups = new ArrayList<String>();


    }


    public Region(Region parent) {
        this.parent = parent;
        if (parent != null) {
            parent.childs.add(this);
        }
    }

    /**
     * Get the parent cuboid for this guy
     *
     * @return
     */
    public Region getParent() {
        return parent;
    }

    /**
     * Set the parent region for the this region.
     * This will also update the parents child list and adjust priority levels
     *
     * @param cube
     */
    public void setParent(Region cube) {
        parent = cube;

        if (parent != null) {
            parent.childs.add(this);
            if (parent.priority >= priority) {
                this.priority = parent.priority + 1;
            }
        }
    }

    /**
     * Get the name of this cuboid
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this cuboid
     *
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
     * get this regiosn priority
     *
     * @return
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Set this regions priority and update all childs accordingly
     *
     * @param prio
     */
    public void setPriority(int prio) {
        int difference = prio - priority;
        this.priority = prio;
        for (Region child : childs) {
            child.setPriority(child.getPriority() + difference);
        }

    }


    public boolean equalsWorld(Region other) {
        return (world.equals(other.world));
    }

    public boolean equalsWorld(World world) {
        return (world.getFqName().equals(this.world));
    }

    public boolean equalsWorld(String world) {
        return world != null && (world.equals(this.world));
    }

    /**
     * Detach this region from its parent and update the parent
     */
    public void detach() {
        if (parent != null) {
            parent.detachChild(this);
            parent = null;
        }
    }

    /**
     * Remove a child from this regions child list
     *
     * @param c
     */
    void detachChild(Region c) {
        childs.remove(c);
    }

    /**
     * Detach all childs from this cuboid and update their parents.
     * Returns the list of all childs for further processing.
     */
    public ArrayList<Region> detachAllChilds() {
        //first override parents
        for (Region child : childs) {
            child.setParent(parent);
        }
        //return all childs
        return childs;
    }


    /**
     * Try finding a cuboid with the given name within the childs.
     * This is NOT very performant as it needs to query A LOT of childs!
     *
     * @param name
     * @return
     */
    public Region queryChilds(String name) {
        Region candidate;
        if (this.name.equals(name)) {
            return this;
        }
        if (parent != null && parent.name.equals(name)) {
            return parent;
        }
        for (Region c : childs) {
            if (c.getName().equals(name)) {
                return c;
            }
            candidate = c.queryChilds(name);
            if (candidate != null && candidate.getName().equals(name)) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Query for a cuboid at the specified location. This will return the cuboid that
     * should be taken into consideration when processing stuff.
     * That is: the one with the max priority or if prio clashes, then the smaller one
     *
     * @param loc
     * @return
     */
    public Region queryChilds(Location loc) {
        if (!isWithin(loc)) {
            return null;
        }
        Region current = this;
        for (Region c : childs) {
            if (c.isWithin(loc)) {
                if (current.getPriority() < c.getPriority()) {
                    current = c;
                }
                else if (current.getPriority() == c.getPriority()) {
                    current = current.getSize() > c.getSize() ? c : current;
                }

                Region check = c.queryChilds(loc);
                if (check.getPriority() > current.getPriority()) {
                    current = check;
                }
                else if (check.getPriority() == current.getPriority()) {
                    current = current.getSize() > check.getSize() ? check : current;
                }
            }
        }
        return current;
    }

    /**
     * Query the childs of this cuboid to see if they space-wise
     * contain the given cuboid, in order to find the given cuboids appropriate parent
     *
     * @param cube
     * @return
     */
    public Region queryChilds(Region cube) {
        Region current = this;
        for (Region c : childs) {
            if (cube.cuboidIsWithin(c, true)) {
                if (c.getPriority() > current.getPriority()) {
                    current = c;
                }
                else if (c.getPriority() == current.getPriority()) {
                    current = current.getSize() > c.getSize() ? c : current;
                }

                Region check = c.queryChilds(cube);
                if (check.getPriority() > current.getPriority()) {
                    current = check;
                }
                else if (check.getPriority() == current.getPriority()) {
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
     *
     * @return
     */
    public ArrayList<Region> fixChilds() {
        ArrayList<Region> detached = new ArrayList<Region>();
        for (Region child : childs) {
            if (!child.cuboidIsWithin(this, true)) {
                child.detach();
                detached.add(child);
            }
            detached.addAll(child.fixChilds());
        }

        return detached;
    }

    /**
     * Check if this is a root cuboid, that means it has no parent
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Recursively generates a list of childs and their childs etc.
     * and returns an unmodifiable view of this list
     *
     * @return
     */
    public List<Region> getChildsDeep(List<Region> collection) {
        for (Region r : childs) {
            collection.add(r);
            collection = r.getChildsDeep(collection);
        }
        if (!collection.contains(this)) {
            collection.add(this);
        }
        return collection;
    }

    /**
     * Set a property and its status for this cuboid
     *
     * @param name
     * @param value
     */
    public boolean setProperty(String name, Status value) {
        if (!RegionFlagRegister.isFlagValid(name)) {
            return false;
        }
        if (value == Status.INVALID_PROPERTY) {
            return false;
        }
        properties.put(name, value);
        return true;
    }

    public boolean removeProperty(String name) {
        if (properties.containsKey(name)) {
            properties.remove(name);
            return true;
        }
        return false;
    }

    /**
     * Get the status for a given property for this cuboid
     *
     * @param name
     * @return
     */
    public Status getProperty(String name) {
        if (properties.containsKey(name)) {
            return properties.get(name);
        }
        return Status.DEFAULT;
    }

    /**
     * Put this map of properties into the existing one of this cuboid,
     * this will override all already existing local values
     *
     * @param map
     */
    public void putAll(Map<String, Status> map) {
        properties.putAll(map);
    }

    public Map<String, Status> getAllProperties() {
        return new HashMap<String, Region.Status>(properties);
    }

    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Check if this region is in the parental hierarchy of the given region
     *
     * @param r
     * @return
     */
    public boolean isParentOf(Region r) {
        if (r == null) {
            return false;
        }
        return r.parentsToList(r, new ArrayList<Region>()).contains(this);
    }

    /**
     * Check if this Cuboid is a child of the given Cuboid
     *
     * @param r
     * @return
     */
    public boolean isChildOf(Region r) {
        if (r == null) {
            return false;
        }
        return r.getChildsDeep(new ArrayList<Region>()).contains(this);
    }

    /**
     * Add a group to the list of allowed groups.
     * This will recognize a comma seperated list of group names and auto-adds them
     *
     * @param group
     * @return
     */
    public boolean addGroup(String group) {
        if (group == null) {
            return false;
        }
        if (groups.contains(group)) {
            return false;
        }
        // group = group.substring(2);
        if (group.contains(",")) {
            addGroup(group.split(","));
            return true;
        }
        else {
            if (group.contains("g:")) {
                if (!group.equalsIgnoreCase("no_groups")) {
                    if (!group.equalsIgnoreCase("g:")) {
                        groups.add(group.substring(2).replace(" ", "").toLowerCase());
                        return true;
                    }
                }
            }
            else {
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
     * @param groups Array
     */
    public void addGroup(String[] groups) {
        for (String group : groups) {
            addGroup(group);
        }

    }

    /**
     * Remove a group name to the list of allowed group names.
     *
     * @param groupName String
     */
    public boolean removeGroup(String groupName) {
        if (groups.contains(groupName)) {
            groups.remove(groupName);
            return true;
        }
        else if (groups.contains("g:" + groupName)) {
            groups.remove("g:" + groupName);
            return true;
        }
        else if (groups.contains(groupName.substring(2))) {
            groups.remove(groupName.substring(2));
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Remove a couple of groups to the list of allowed groups names.
     *
     * @param groupNames Array
     */
    public void removeGroup(String[] groupNames) {
        for (String groupName : groupNames) {
            removeGroup(groupName);
        }
    }

    /**
     * Add a player name to the list of allowed player names.
     *
     * @param playerName String
     */
    public boolean addPlayer(String playerName) {
        if (playerName == null) {
            return false;
        }
        if (players.contains(playerName)) {
            return false;
        }
        if (playerName.contains(",")) {
            addPlayer(playerName.split(","));
            return true;
        }
        else {
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
     * @param playerNames Array
     */
    public void addPlayer(String[] playerNames) {
        for (String playerName : playerNames) {
            addPlayer(playerName);
        }

    }

    /**
     * Remove a player name to the list of allowed player names.
     *
     * @param playerName String
     */
    public boolean removePlayer(String playerName) {

        if (players.contains(playerName)) {
            players.remove(playerName);
            return true;
        }
        else if (players.contains("o:" + playerName)) {
            players.remove("o:" + playerName);
            return true;
        }
        else {
            return false;
        }

    }

    /**
     * Remove a couple of players to the list of allowed player names.
     *
     * @param playerNames Array
     */
    public void removePlayer(String[] playerNames) {
        for (String playerName : playerNames) {
            removePlayer(playerName);
        }
    }

    /**
     * Check if this Region is within the given one
     *
     * @param cube
     * @param complete
     * @return
     */
    public boolean cuboidIsWithin(Region cube, boolean complete) {
        if (this.equalsWorld(cube)) {
            if (complete) {
                return origin.isWithin(cube.getOrigin(), cube.getOffset()) && offset.isWithin(cube.getOrigin(), cube.getOffset());
            }
            else {
                return origin.isWithin(cube.getOrigin(), cube.getOffset()) || offset.isWithin(cube.getOrigin(), cube.getOffset());
            }
        }
        else {
            return false;
        }
    }

    /**
     * Check if the given Location is within this Region
     *
     * @param v
     * @return
     */
    public boolean isWithin(Location v) {
        if (!equalsWorld(v.getWorld())) {
            return false;
        }
        return v.isWithin(origin, offset);
    }

    /**
     * Calculate and get the volume size of this region
     *
     * @return
     */
    int getSize() {
        return (int) Vector.getDistance(origin.getBlockX(), offset.getBlockX()) * (int) Vector.getDistance(origin.getBlockY(), offset.getBlockY()) * (int) Vector.getDistance(origin.getBlockZ(), offset.getBlockZ());
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

    public boolean playerIsOwner(Player player) {
        return playerIsOwner(player.getName());
    }

    /**
     * Add a commands to the list of tabu commands.
     *
     * @param command String
     */
    public void addRestrictedCommand(String command) {
        if (!command.equalsIgnoreCase("no_commands")) {
            if (command.contains(",")) {
                addRestrictedCommand(command.split(","));
            }
            else {
                restrictedCommands.add(command);
            }
        }
    }

    /**
     * Add a couple of commands to the list of tabu commands.
     *
     * @param commands Array
     */
    public void addRestrictedCommand(String[] commands) {
        for (String command : commands) {
            if (!command.equalsIgnoreCase("no_commands")) {
                restrictedCommands.add(command);
            }
        }

    }

    /**
     * Remove a commands to the list of tabu commands.
     *
     * @param command String
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
        return restrictedCommands.contains(command) || restrictedCommands.contains(command.substring(1));
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
            Collections.addAll(restrictedItems, itemList);
        }
        restrictedItems.add(items);
    }

    public void addRestrictedItem(String[] items) {
        if (items == null) {
            return;
        }
        Collections.addAll(restrictedItems, items);
    }


    public void removeRestrictedItem(String name) {
        restrictedItems.remove(name);
    }


    public boolean isItemRestricted(String id) {
        return restrictedItems.contains(id);
    }

    public List<String> getRestrictedItems() {
        return restrictedItems;
    }

    /**
     * Check if a given player is allowed in this cuboid. This will take into account
     * ownership of this cuboid as well as groups
     *
     * @param player
     * @return True if player is allowed, false otherwise
     */
    @Deprecated
    public boolean playerIsAllowed(String player, String[] group) {
        if (playerIsOwner(player)) {
            return true;
        }

        for (String groupname : group) {
            if (groups.contains(groupname)) {
                return true;
            }
        }
        for (String aGroup : group) {
            if (groups.contains(aGroup.toLowerCase())) {
                return true;
            }
            else if (groups.contains("g:" + aGroup.toLowerCase())) {
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

    public boolean playerIsAllowed(Player player, Group[] groups) {
        if (playerIsOwner(player)) {
            return true;
        }

        for (Group group : groups) {
            if (this.groups.contains(group.getName()) || this.groups.contains("g:" + group.getName())) {
                return true;
            }
        }

        for (String listPlayer : players) {
            if (listPlayer.equalsIgnoreCase(player.getName())) {
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
        }
        else {
            return out;
        }
    }

    public List<String> getPlayers() {
        return new ArrayList<String>(this.players);
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
        }
        else {
            return out;
        }
    }

    public List<String> getGroups() {
       return new ArrayList<String>(this.groups);
    }

    public String getItemListAsNames() {
        StringBuilder items = new StringBuilder();
        for (String i : restrictedItems) {
            ItemType type = ItemType.fromString(i);
            items.append(type.getMachineName()).append(",");
        }
        if (items.length() == 0) {
            return "";
        }
        return items.toString();
    }

    /**
     * @return the origin
     */
    public Vector3D getOrigin() {
        return origin;
    }


    /* ************************************************
     *
     * GETTER / SETTER STUFF
     *
     * ************************************************/

    /**
     * @return the offset
     */
    public Vector3D getOffset() {
        return offset;
    }

    /**
     * Get the center point of this region.
     *
     * @return the center point
     */
    public Vector3D getRegionCenter() {
        return Vector3D.getCenterPoint(getOrigin(), getOffset());
    }

    /**
     * Sets the bounding box of this region and manages re-sorting
     * if the new size causes any inconsistency with parent relations
     *
     * @param origin the new origin
     * @param offset the new offset
     */
    public void setBoundingBox(Vector3D origin, Vector3D offset) {
        this.origin = Vector3D.getMinimum(origin, offset);
        this.offset = Vector3D.getMaximum(origin, offset);
        if (hasParent() && !cuboidIsWithin(this.parent, true)) {
            this.detach();
        }
        RegionManager.get().cleanParentRelations();
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
        if (welcome == null) {
            this.welcome = null;
            return;
        }
        //TODO: Use a regex instead? Can't think of any :S
        char[] chars = welcome.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            if ((i + 1 < chars.length) && (chars[i] == '&' && chars[i + 1] != ' ')) {
                chars[i] = '§';
            }
        }
        this.welcome = String.copyValueOf(chars);
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
        if (farewell == null) {
            this.farewell = null;
            return;
        }
        //TODO: Use a regex instead? Can't think of any :S
        char[] chars = farewell.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            if ((i + 1 < chars.length) && (chars[i] == '&' && chars[i + 1] != ' ')) {
                chars[i] = '§';
            }
        }
        this.farewell = String.copyValueOf(chars);
    }

    public String getFlagList() {
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (String key : properties.keySet()) {
            builder.append(ColorManager.Rose).append(key).append(": ").append(ColorManager.LightGreen).append(properties.get(key).name()).append(", ");
        }
        return builder.toString();
    }

    ArrayList<Region> parentsToList(Region r, ArrayList<Region> parents) {
        if (r.parent == null) {
            return parents;
        }
        if (!parents.contains(r.parent)) {
            parents.add(r.parent);
        }
        return parentsToList(r.parent, parents);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof Region) {
            Region c = (Region) other;
            return (c.name.equals(name)) && (c.getSize() == getSize()) && (c.world.equals(world));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getSize() + priority + world.hashCode();
    }

    public enum Status {
        ALLOW,
        DENY,
        INHERIT,
        DEFAULT,
        INVALID_PROPERTY;

        public static Status fromString(String str) {
            if (str.equalsIgnoreCase(ALLOW.name())) {
                return ALLOW;
            }
            else if (str.equalsIgnoreCase(DENY.name())) {
                return DENY;
            }
            else if (str.equalsIgnoreCase(INHERIT.name())) {
                return INHERIT;
            }
            else if (str.equalsIgnoreCase(DEFAULT.name())) {
                return DEFAULT;
            }
            else {
                return INVALID_PROPERTY;
            }
        }

        public static String toString(Status status) {
            switch (status) {
                case ALLOW:
                    return "ALLOW";
                case DENY:
                    return "DENY";
                case INHERIT:
                    return "INHERIT";
                case DEFAULT:
                    return "DEFAULT";
                case INVALID_PROPERTY:
                    return "INVALID_PROPERTY";
            }
            return "INVALID_PROPERTY";
        }

        /**
         * Returns allow or deny from a boolean value
         *
         * @param check
         * @return
         */
        public static Status fromBoolean(boolean check) {
            if (check) {
                return ALLOW;
            }
            else {
                return DENY;
            }
        }

        /**
         * Returns allow or default from boolean value
         *
         * @param check
         * @return
         */
        public static Status softFromBoolean(boolean check) {
            if (check) {
                return ALLOW;
            }
            else {
                return DEFAULT;
            }
        }
    }
}
