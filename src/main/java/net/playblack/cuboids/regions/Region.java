package net.playblack.cuboids.regions;

import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.world.World;
import net.playblack.cuboids.RegionFlagRegister;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.mcutils.CLocation;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Region {

    /**
     * Flag as changed to make it available for the saving thread
     */
    public boolean hasChanged = false;
    /**
     * List of all child nodes
     */
    protected ArrayList<Region> childs = new ArrayList<Region>(5);
    /**
     * Reference to this Cuboids parent
     */ //This'll get ugly when reading from datasource
    protected Region parent;
    /**
     * The name of this cuboid
     */
    protected String name;
    /**
     * The name of the world this cuboid sits in
     */
    protected String world;
    /**
     * The ID of the dimension for this cuboid. This may always be 0 in some implementations
     */
    protected int dimension;
    /**
     * Cuboids priority. Cuboids with higher priority will be considered when areas clash
     */
    protected int priority;
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
    private Vector origin;
    private Vector offset;
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
     * Recursively add player to this Region and its childs.
     * This will set the currentRegion in the player
     * Recursively goes down
     *
     * @param player
     * @param loc
     */
    public void addPlayerWithin(CPlayer player, CLocation loc) {
        if (!player.getName().equalsIgnoreCase("no_players")) {
            //Add into this cuboid
            if (!player.getName().substring(2).isEmpty()) {
                if (isWithin(loc)) {
                    player.setRegion(this);
                }
            }
            for (Region child : childs) {
                if (child.isWithin(loc)) {
                    child.addPlayerWithin(player, loc);
                }
            }
        }
    }

    /**
     * Recursively remove player from this Region and its childs
     *
     * @param player
     * @param toCheck
     */
    public void removePlayerWithin(CPlayer player, CLocation toCheck) {
        if (!isWithin(toCheck)) {
            if (parent != null && parent.isWithin(toCheck)) {
                player.setRegion(parent);
            }
            //Assume we're not in any region anymore.
            //If we are we
            player.setRegion(null);
            for (Region child : childs) {
                child.removePlayerWithin(player, toCheck);

            }
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
            child.hasChanged = true;
        }

    }

    public boolean equalsWorld(World world) {
        return (world.getType().getId() == dimension) && (this.world.equals(world.getName()));
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
        if (parent != null) {
            parent.detachChild(this);
            parent = null;
        }
    }

    /**
     * Add a region to the list of childs of tis cuboid
     *
     * @param c
     */
    public void attachRegion(Region c) {
        c.setParent(this);
        childs.add(c);
    }

    /**
     * Remove a child from this regions child list
     *
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
        for (Region child : childs) {
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
     * @param priority You should set this to 0 it's for internal stuffs
     * @return
     */
    public Region queryChilds(CLocation loc, int priority) {
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

                Region check = c.queryChilds(loc, current.getPriority());
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
     * Check if this is the global settings Cuboid
     *
     * @return
     */
    public boolean isGlobal() {
        return name.equals("__WORLD__");
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

    public List<Region> getChildsDeep() {
        ArrayList<Region> collection = new ArrayList<Region>();
        for (Region r : childs) {
            collection.addAll(r.getChildsDeep(collection));
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
     * Check if this cuboid has a given property.
     *
     * @param name
     * @return
     */
    public boolean propertyExists(String name) {
        return properties.containsKey(name);
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
        if (group.indexOf(",") >= 0) {
            addGroup(group.split(","));
            return true;
        }
        else {
            if (group.indexOf("g:") >= 0) {
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
        for (int i = 0; i < groups.length; i++) {
            addGroup(groups[i]);
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
        for (int i = 0; i < groupNames.length; i++) {
            removeGroup(groupNames[i]);
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
        if (playerName.indexOf(",") >= 0) {
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
        for (int i = 0; i < playerNames.length; i++) {
            addPlayer(playerNames[i]);
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
        for (int i = 0; i < playerNames.length; i++) {
            removePlayer(playerNames[i]);
        }
    }

    /**
     * Check if this cuboid is inside another
     *
     * @param v1       Other Cuboid Point 1
     * @param v2       Other Cuboid Point 2
     * @param complete true: Cuboid must be completely inside, false: Cuboid can be
     *                 inside only partially
     * @return true if cuboid is inside another, false otherwise
     */
    public boolean cuboidIsWithin(Vector v1, Vector v2, boolean complete) {
        Vector min = Vector.getMinor(v1, v2);
        Vector max = Vector.getMajor(v1, v2);
        if (complete) {
            return origin.isWithin(min, max) && offset.isWithin(min, max);
        }
        else {
            return origin.isWithin(min, max) || offset.isWithin(min, max);
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
            Vector min = Vector.getMinimum(cube.getOrigin(), cube.getOffset());
            Vector max = Vector.getMaximum(cube.getOrigin(), cube.getOffset());

            if (complete) {
                return origin.isWithin(min, max) && offset.isWithin(min, max);
            }
            else {
                return origin.isWithin(min, max) || offset.isWithin(min, max);
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
    public boolean isWithin(CLocation v) {
        if (!equalsWorld(v.getWorld(), v.getDimension())) {
            return false;
        }
        Vector min = Vector.getMinimum(origin, offset);
        Vector max = Vector.getMaximum(origin, offset);
        return v.isWithin(min, max);
    }

    /**
     * Calculate and get the volume size of this region
     *
     * @return
     */
    public int getSize() {
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

    /**
     * Add a commands to the list of tabu commands.
     *
     * @param command String
     */
    public void addRestrictedCommand(String command) {
        if (!command.equalsIgnoreCase("no_commands")) {
            if (command.indexOf(",") >= 0) {
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
        for (int i = 0; i < commands.length; i++) {
            if (!commands[i].equalsIgnoreCase("no_commands")) {
                restrictedCommands.add(commands[i]);
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
     * Add items to regions restricted items list
     *
     * @param id
     * @deprecated Use addRestrictedItem with string argument(s) instead.
     */
    @Deprecated
    public void addRestrictedItem(int id) {
        throw new UnsupportedOperationException("Deprecated. Use strings instead of ints!");
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
                restrictedItems.add(item);
            }
        }
        restrictedItems.add(items);
    }

    public void addRestrictedItem(String[] items) {
        if (items == null) {
            return;
        }
        for (String item : items) {
            restrictedItems.add(item);
        }
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
    @Deprecated
    public boolean isItemRestricted(int id) {
        throw new UnsupportedOperationException("Deprecated. Use strings instead of ints!");
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
    public boolean playerIsAllowed(String player, String[] group) {
        if (playerIsOwner(player)) {
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
    public Vector getOrigin() {
        return origin;
    }


    /* ************************************************
     *
     * GETTER / SETTER STUFF
     *
     * ************************************************/

    /**
     * @param origin the origin to set
     */
    public void setOrigin(Vector origin) {
        this.origin = origin;
//        ToolBox.adjustWorldPosition(origin);
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
//        ToolBox.adjustWorldPosition(offset);
    }

    /**
     * Get the center point of this region.
     *
     * @return the center point
     */
    public Vector getRegionCenter() {
        return Vector.getCenterPoint(getOrigin(), getOffset());
    }

    /**
     * Sets the bounding box of this region and manages re-sorting
     * if the new size causes any inconsistency with parent relations
     *
     * @param origin the new origin
     * @param offset the new offset
     */
    public void setBoundingBox(Vector origin, Vector offset) {
        this.origin = origin;
        this.offset = offset;
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
            if (count <= 3) {
                builder.append(ColorManager.Rose).append(key).append(": ").append(ColorManager.LightGreen).append(properties.get(key).name()).append(", ");
            }
            else {
                count = 0;
                builder.append(";");

            }
        }
        return builder.toString();
    }

    public ArrayList<Region> parentsToList(Region r, ArrayList<Region> parents) {
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
            return (c.name.equals(name)) && (c.getSize() == getSize()) && (c.world.equals(world)) && (c.dimension == dimension);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getSize() + priority + dimension;
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
