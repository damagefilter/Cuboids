package net.playblack.cuboids.regions;

import java.util.ArrayList;
import java.util.HashMap;

import net.playblack.mcutils.Vector;


/**
 * CuboidE is a complete re-write of Cuboid Storage and 
 * is derived from the old CuboidD class and format.<br>
 * This is the smallest unit in a cuboidTree
 * @author Chris Ksoll
 */
public class CuboidE {
    /*
     * Some setting things
     */
    
    /**
     * The name of this cuboid
     */
    private String name;
    
    /**
     * My parent cuboidE's name
     */
    private String parent = null;
    
    /**
     * My Priority in relation to my _parent
     */
    private int priority;
    
    /**
     * Allowed players, flag with o: to make player owner
     */
    private ArrayList<String> playerList = new ArrayList<String>();
    
    /**
     * Players that are within this cuboid
     */
    private ArrayList<String> playersWithin = new ArrayList<String>();
    
    /**
     * Allowed groups - that is everything flagged with g:
     */
    private ArrayList<String> groupList = new ArrayList<String>();
    
    private ArrayList<Integer> restrictedItems = new ArrayList<Integer>();
    
    /**
     * List of commands that should be denied in this area.
     */
    public ArrayList<String> tabuCommands = new ArrayList<String>();
    
    public boolean hasChanged=false;
    
    /*
     * Cuboids Points
     */
    private Vector point1;
    private Vector point2;
    
    /*
     * Some flags for a cuboidE region
     */
    private boolean creeperSecure = false;
    private boolean healing = false;
    private boolean protection = false;
    private boolean sanctuary = false;
    private boolean sanctuarySpawnAnimals = false;
    private boolean allowPvp = false;
    private boolean freeBuild = false;
    private boolean blockFireSpread = false;
    
    //1.2.0
    private boolean lavaControl = false; //this is also valid for buckets
    private boolean waterControl = false; //this is also valid for buckets
    private boolean farmland = false; //stop wheat trampling
    private boolean tntSecure = false;
    
    //1.4.0
    private boolean restriction = false;
    private String welcome=null;
    private String farewell=null;
    
    private boolean hMob=false;
    /**
     * Name of the world this cuboidE is valid for
     */
    private String world; 
    
    private int dimension;

    public CuboidE(Vector origin, Vector offset) {
        point1 = origin;
        point2 = offset;
    }
    
    public CuboidE() {
        
    }
    /*
     * ***********************************************************
     * BASIC PROPERTIES MANAGEMENT
     * ***********************************************************
     */
    
    /**
     * Get the Name of this Cuboid
     * @return String Name
     */
    public String getName() {
        return name;
    }

    /**
     * Set this cuboids name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    

    /**
     * Set the parent of this cuboid. Parenting cuboids will affect storage mechanics.<br>
     * A system of good parented cuboids is faster to search.
     * @param _parent
     */
    public void setParent(String _parent) {
        this.parent = _parent;
    }

    /**
     * Set this cuboids priority level. 1 - x where 10 has higher priority than 1 
     * @param priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
    

    public void setWorld(String world) {
        this.world = world;
    }
    
    public String getWorld() {
        return world;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
    
    public int getDimension() {
        return dimension;
    }

    /**
     * Get Farewell Message
     * @return
     */
    public String getFarewell() {
        return farewell;
    }

    /**
     * Set Farewell Message
     * @return
     */
    public void setFarewell(String farewell) {
        this.farewell = farewell;
    } 
    
    /**
     * Set if Cuboids is supposed to control the handling of lava
     * @param lava
     */
    public void setLavaControl(boolean lava) {
        lavaControl = lava;
    }
    
    /**
     * Set if Cuboids is supposed to control water handling
     * @param water
     */
    public void setWaterControl(boolean water) {
        waterControl = water;
    }
    
    /**
     * Sets if Cuboids is supposed to control tnt explosions
     * @param tnt
     */
    public void setTntSecure(boolean tnt) {
        tntSecure = tnt;
    }
    
    /**
     * Sets if Cuboids is supposed to control tnt explosions
     * @param tnt
     */
    public void setFarmland(boolean farmland) {
        this.farmland = farmland;
    }
    
    public void setRestriction(boolean restriction) {
        this.restriction = restriction;
    }
    //public void set 
    /*
     * ***********************************************************
     * LISTS MANAGEMENT
     * ***********************************************************
     */
    
    /**
     * Check if the Cuboid area is under lava control
     * @return
     */
    public boolean isLavaControl() {
        return lavaControl;
    }
    
    /**
     * Check if the Cuboid area is under water control
     * @return
     */
    public boolean isWaterControl() {
        return waterControl;
    }
    
    /**
     * Check if the Cuboid is under wheat trampling control
     * @return
     */
    public boolean isFarmland() {
        return farmland;
    }
    
    public boolean isTntSecure() {
        return tntSecure;
    }
    
    
    /**
     * Get this Cuboids Priority.
     * @return int prio
     */
    public int getPriority() {
        return priority;
    }
    
    /**
     * Get parent of this Cuboid
     * @return
     */
    public String getParent() {
        return parent;
    }
    
    /**
     * Get Welcome Message
     * @return
     */
    public String getWelcome() {
        return welcome;
    }

    /**
     * Set Welcome Message
     * @return
     */
    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }
    
    public boolean isCreeperSecure() {
        return creeperSecure;
    }

    public void setCreeperSecure(boolean creeperSecure) {
        this.creeperSecure = creeperSecure;
    }

    public boolean isHealingArea() {
        return healing;
    }

    public void setHealing(boolean healing) {
        this.healing = healing;
    }

    public boolean isProtected() {
        return protection;
    }

    public void setProtection(boolean protection) {
        this.protection = protection;
    }

    public boolean isFreeBuild() {
        return freeBuild;
    }

    public void setFreeBuild(boolean freeBuild) {
        this.freeBuild = freeBuild;
    }

    public boolean isSanctuary() {
        return sanctuary;
    }

    public void setSanctuary(boolean sanctuary) {
        this.sanctuary = sanctuary;
    }
    
    public boolean ishMob() {
        return hMob;
    }

    public void sethMob(boolean hMob) {
        this.hMob = hMob;
    }

    /**
     * Returns true if animals are allowed to spawn, false otherwise
     * @return
     */
    public boolean sanctuarySpawnAnimals() {
        return sanctuarySpawnAnimals;
    }

    public void setSanctuarySpawnAnimals(boolean sanctuarySpawnAnimals) {
        this.sanctuarySpawnAnimals = sanctuarySpawnAnimals;
    }

    public boolean isAllowedPvp() {
        return allowPvp;
    }

    public void setAllowPvp(boolean allowPvp) {
        this.allowPvp = allowPvp;
    }

    /**
     * True when we block fire spread
     * @return
     */
    public boolean isBlockFireSpread() {
        return blockFireSpread;
    }

    public void setBlockFireSpread(boolean blockFireSpread) {
        this.blockFireSpread = blockFireSpread;
    }

    /**
     * If this is true, a playder is not allowed to enter if not in area list
     * @return
     */
    public boolean isRestricted() {
        return restriction;
    }
    
    /**
     * Adds a player that is in this area
     * @param playerName
     */
    public void addPlayerWithin(String playerName) {
        if(!playerName.equalsIgnoreCase("no_players")) {
            if(!playerName.substring(2).isEmpty()) {
                playersWithin.add(playerName);
            }
        }
    }
    
    /**
     * Remove player from within list
     * @param playerName
     * @return
     */
    public boolean removePlayerWithin(String playerName) {
        if(playersWithin.contains(playerName)) {
            playersWithin.remove(playerName);
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Get all players that are within this cuboid as arrayList<string>
     * @return
     */
    public ArrayList<String> getPlayersWithin() {
        return playersWithin;
    }
    
    public boolean playerIsWithin(String playerName) {
        if(playersWithin.contains(playerName)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    
    /**
     * Add a group to the list of allowed groups. This will transform group names to lower case
     * @param group String
     */
    public boolean addGroup(String group) {
        if(groupIsAllowed(group)) {
            return false;
        }
        //group = group.substring(2);
        if(group.indexOf(",") >= 0) {
            addGroup(group.split(","));
            return true;
        }
        else {
            if(group.indexOf("g:") >= 0) {
                if(!group.equalsIgnoreCase("no_groups")) {
                    if(!group.equalsIgnoreCase("g:")) {
                        groupList.add(group.substring(2).replace(" ", "").toLowerCase());
                        return true;
                    }
                }
            }
            else {
                //Could use some smuck up but heck, it's beeing used only so-many times
                if(!group.equalsIgnoreCase("no_groups")) {
                    if(!group.replace(" ", "").isEmpty()) {
                        groupList.add(group.replace(" ", "").toLowerCase());
                        return true;
                    }
                }
            }
            return false;
        }
    }
    
    /**
     * Add a couple of groups to the list of allowed groups.
     * @param groups Array
     */
    public void addGroup(String[] groups) {
        for(int i=0; i<groups.length;i++) {
            addGroup(groups[i]);
        }
        
    }
    
    /**
     * Remove a group name to the list of allowed group names.
     * @param groupName String
     */
    public boolean removeGroup(String groupName) {
        if(groupList.contains(groupName)) {
            groupList.remove(groupName);
            return true;
        }
        else if(groupList.contains("g:"+groupName)) {
            groupList.remove("g:"+groupName);
            return true;
        }
        else if(groupList.contains(groupName.substring(2))) {
            groupList.remove(groupName.substring(2));
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Remove a couple of groups to the list of allowed groups names.
     * @param groupNames Array
     */
    public void removeGroup(String[] groupNames) {
        for(int i = 0; i < groupNames.length; i++) {
            removeGroup(groupNames[i]);
        }
    }
    
    /**
     * Add items to regions restricted items list
     * @param id
     */
    public void addRestrictedItem(int id) {
        restrictedItems.add(Integer.valueOf(id));
    }
    
    /**
     * Remove items to regions restricted items list
     * @param id
     */
    public void removeRestrictedItem(int id) {
        restrictedItems.remove(Integer.valueOf(id));
    }
    
    public boolean isItemRestricted(int id) {
        if(restrictedItems.contains(Integer.valueOf(id))) {
            return true;
        }
        return false;
    }
    /**
     * Add a player name to the list of allowed player names.
     * @param playerName String
     */
    public boolean addPlayer(String playerName) {
        if(playerIsAllowed(playerName)) {
            return false;
        }
        if(playerName.indexOf(",") >= 0) {
            addPlayer(playerName.split(","));
            return true;
        }
        else {
            if(!playerName.equalsIgnoreCase("no_players")) {
                if(!playerName.equalsIgnoreCase("o:")) {
                    playerList.add(playerName.replace(" ", ""));
                    return true;
                }
                return false;
            }
            return false;
        }
    }
    
    /**
     * Add a couple of players to the list of allowed player names.
     * @param playerNames Array
     */
    public void addPlayer(String[] playerNames) {
        for(int i=0; i<playerNames.length;i++) {
            addPlayer(playerNames[i]);
        }
        
    }
    
    /**
     * Remove a player name to the list of allowed player names.
     * @param playerName String
     */
    public boolean removePlayer(String playerName) {

        if(playerList.contains(playerName)) {
            playerList.remove(playerName);
            return true;
        }
        else if(playerList.contains("o:"+playerName)) {
            playerList.remove("o:"+playerName);
            return true;
        }
        else {
            return false;
        }
        
    }
    
    /**
     * Remove a couple of players to the list of allowed player names.
     * @param playerNames Array
     */
    public void removePlayer(String[] playerNames) {
        for(int i = 0; i < playerNames.length; i++) {
            removePlayer(playerNames[i]);
        }
    }
    
    /**
     * Add a commands to the list of tabu commands.
     * @param command String
     */
    public void addTabuCommand(String command) {
        if(!command.equalsIgnoreCase("no_commands")) {
            if(command.indexOf(",") >= 0) {
                addTabuCommand(command.split(","));
            }
            else {
                tabuCommands.add(command);
            }
        }
    }
    
    /**
     * Add a couple of commands to the list of tabu commands.
     * @param commands Array
     */
    public void addTabuCommand(String[] commands) {
        for(int i=0; i<commands.length;i++) {
            if(!commands[i].equalsIgnoreCase("no_commands")) {
                tabuCommands.add(commands[i]);
            }
        }
        
    }
    
    /**
     * Remove a commands to the list of tabu commands.
     * @param command String
     */
    public void removeTabuCommand(String command) {
        if(tabuCommands.contains(command)) {
            tabuCommands.remove(command);
        }
    }
    
    public boolean commandIsRestricted(String command) {
        if(tabuCommands.contains(command) || tabuCommands.contains(command.substring(1))) {
            return true;
        }
        else {
            return false;
        }
    }
    /*
     * ***********************************************************
     * AREA MANAGEMENT AND AREA DATA PROCESSING
     * ***********************************************************
     */
    
    /**
     * Get the point of this cuboid that is closer to 0,0,0
     * @return new Vector
     */
    public Vector getMinorPoint() {
        return Vector.getMinor(this.point1, this.point2);
    }
    
    public Vector getFirstPoint() {
        return point1;
    }
    /**
     * Get the point of this cuboid that is farther away from 0,0,0
     * @return new Vector
     */
    public Vector getMajorPoint() {
        return Vector.getMajor(this.point1, this.point2);
    }
    
    public Vector getSecondPoint() {
        return point2;
    }
    /**
     * Set the first Cuboid Point
     * @param p
     */
    public void setFirstPoint(Vector p) {
        this.point1 = p;
    }
    
    /**
     * Set the second Cuboid point
     * @param p
     */
    public void setSecondPoint(Vector p) {
        this.point2 = p;
    }
    
    /**
     * Set both definition points of this cuboid in one go.
     * @param p1
     * @param p2
     */
    public void setPoints(Vector p1, Vector p2) {
        this.point1 = p1;
        this.point2 = p2;
    }
    
    /**
     * Check if this cuboid is inside another
     * @param v1 Other Cuboid Point 1
     * @param v2 Other Cuboid Point 2
     * @param complete true: Cuboid must be completely inside, false: Cuboid can be inside only partially
     * @return true if cuboid is inside another, false otherwise
     */
    public boolean cuboidIsWithin(Vector v1, Vector v2, boolean complete) {
        Vector min = Vector.getMinor(v1, v2);
        Vector max = Vector.getMajor(v1, v2);
        if(complete == true) {
            if(point1.isWithin(min, max) && point2.isWithin(min, max)) {
                return true;
            }
            else { 
                return false;
            }
        }
        else {
            if(point1.isWithin(min, max) || point2.isWithin(min, max)) {
                return true;
            }
            else { 
                return false;
            }
        }
    }
    
    /**
     * Check if this cuboid is inside another, considering the current world
     * @param v1 Other Cuboid Point 1
     * @param v2 Other Cuboid Point 2
     * @param complete true: Cuboid must be completely inside, false: Cuboid can be inside only partially
     * @param world The World Name to check against
     * @return true if cuboid is inside another, false otherwise
     */
    public boolean cuboidIsWithin(Vector v1, Vector v2, String world, int dimension, boolean complete) {
        if(this.equalWorlds(world, dimension)) {
            Vector min = Vector.getMinimum(v1, v2);
            Vector max = Vector.getMaximum(v1, v2);
            
            if(complete == true) {
                if(point1.isWithin(min, max) && point2.isWithin(min, max)) {
                    return true;
                }
                else { 
                    return false;
                }
            }
            else {
                if(point1.isWithin(min, max) || point2.isWithin(min, max)) {
                    return true;
                }
                else { 
                    return false;
                }
            }
        }
        else {
            return false;
        }
    }
    
    /**
     * Check if this cuboid is inside the given one
     * @param cube
     * @param complete true to check if it is inside with both edges
     * @return
     */
    public boolean cuboidIsWithin(CuboidE cube, boolean complete) {
        if(this.equalWorlds(cube)) {
            Vector min = Vector.getMinimum(cube.getFirstPoint(), cube.getSecondPoint());
            Vector max = Vector.getMaximum(cube.getFirstPoint(), cube.getSecondPoint());
            
            if(complete == true) {
                if(point1.isWithin(min, max) && point2.isWithin(min, max)) {
                    return true;
                }
                else { 
                    return false;
                }
            }
            else {
                if(point1.isWithin(min, max) || point2.isWithin(min, max)) {
                    return true;
                }
                else { 
                    return false;
                }
            }
        }
        else {
            return false;
        }
    }
    
    /**
     * Check if this cuboidE is in the same world and dimension as the given one
     * @param test
     * @return
     */
    public boolean equalWorlds(CuboidE test) {
        if((this.getDimension() == test.getDimension()) && (this.getWorld().equalsIgnoreCase(test.getWorld()))) {
            return true;
        }
        return false;
    }
    
    /**
     * Check if this cuboidE matches the given world and dimension (is in it)
     * @param world
     * @param dimension
     * @return
     */
    public boolean equalWorlds(String world, int dimension) {
        if((this.getDimension() == dimension) && (this.getWorld().equalsIgnoreCase(world))) {
            return true;
        }
        return false;
    }
    
    /**
     * Checks if a given vector v is within this cuboid.
     * @param v
     * @return True: Point is within this cuboid, false otherwise
     */
    public boolean isWithin(Vector v) {
        Vector min = Vector.getMinimum(point1, point2);
        Vector max = Vector.getMaximum(point1, point2);
        if(v.isWithin(min, max)) {
            //System.out.println("Within found and all.");
            return true; 
        }
        else {
            //System.out.println("Not within found and all.");
            return false;
        }
    }
    
    /*
     * ***********************************************************
     * PERMISSION MANAGEMENT
     * ***********************************************************
     */
    
    /**
     * Check if a given player is allowed in this cuboid. That is to say, if his name is inside the list of players.
     * @param player
     * @return True if player is allowed, false otherwise
     */
    private boolean playerIsAllowed(String player) {
        for(String listPlayer : playerList) {
            if(listPlayer.equalsIgnoreCase(player) || listPlayer.equalsIgnoreCase("o:" + player)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if a given player is allowed in this cuboid.
     * This also checks for the player group and the player name
     * @param player
     * @return True if player is allowed, false otherwise
     */
    public boolean playerIsAllowed(String player, String[] group) {
        for(String groupname : group) {
            if(groupList.contains(groupname)) {
                return true;
            }
        }
        for(int i = 0; i < group.length; i++) {
            if(groupList.contains(group[i].toLowerCase())) {
                return true;
            }
            else if(groupList.contains("g:"+group[i].toLowerCase())) {
                return true;
            }
        }
        for(String listPlayer : playerList) {
            if(listPlayer.equalsIgnoreCase(player) || listPlayer.equalsIgnoreCase("o:" + player)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if a given player is the owner of this cuboid. That is to say, if his name is inside the list of players as o:.
     * @param player
     * @return True if player is allowed, false otherwise
     */
    public boolean playerIsOwner(String player) {
        for(String listPlayer : playerList) {
            if(listPlayer.equalsIgnoreCase("o:" + player)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if a given group is allowed in this cuboid. That is to say, if his name is inside the list of groups.
     * @param group
     * @return
     */
    private boolean groupIsAllowed(String group) {
        for(String listGroup : groupList) {
            if(listGroup.equalsIgnoreCase(group)) {
                return true;
            }
        }
        return false;
    }
    
    /*
     * ***********************************************************
     * INFORMATION RETRIEVAL 
     * Please note: This only goes as far as it gets with information
     * that can be retrieved without Canary. Reason:
     * We don't want the cuboid objects to have direct dependencies to anything
     * related to Canary. For that there is an interface.
     * ***********************************************************
     */
    
    public String getFlagList() {
        StringBuilder flaglist = new StringBuilder();
        flaglist.append("This is a ");
        if(isAllowedPvp()) {
            flaglist.append("PvP") ;
        }
        else {
            flaglist.append("No PvP");
        }
        if(isCreeperSecure()) {
            flaglist.append(", Creeper Secure");
        }
        if(isTntSecure()) {
            flaglist.append(", TNT Secure");
        }
        if(isHealingArea()) {
            flaglist.append(", Healing");
        }
        if(isProtected()) {
            flaglist.append(", Protected");
        }
        if(isRestricted()) {
            flaglist.append(", Restricted");
        }
        if(isSanctuary()) {
            flaglist.append(", Sanctuary");
        }
        if(isFreeBuild()) {
            flaglist.append(", Freebuild");
        }
        if(isBlockFireSpread()) {
            flaglist.append(", Fireproof");
        }
        if(isLavaControl()) {
            flaglist.append(", Lava secure");
        }
        if(isWaterControl()) {
            flaglist.append(", Water secure");
        }
        if(isFarmland()) {
            flaglist.append(", Farmland");
        }
        if(ishMob()) {
            flaglist.append(", hMob");
        }
        
        flaglist.append(" area");
        
        if(welcome != null && farewell != null) {
            flaglist.append(" with Welcome and Farewell message.");
            return flaglist.toString();
        }
        if(welcome != null) {
            flaglist.append(" with a Welcome message.");
        }
        if(farewell != null) {
            flaglist.append(" with a Farewell message.");
        }
        return flaglist.toString();
        
    }
    
    public String getFlagListSimple() {
        StringBuilder flaglist = new StringBuilder();
        if(isAllowedPvp()) {
            flaglist.append("PvP");
        }
        else {
            flaglist.append("No PvP");
        }
        if(isCreeperSecure()) {
            flaglist.append(", no-Creeper");
        }
        if(isTntSecure()) {
            flaglist.append(", no-Tnt");
        }
        if(isHealingArea()) {
            flaglist.append(", Healing");
        }
        if(isProtected()) {
            flaglist.append(", Protected");
        }
        if(isRestricted()) {
            flaglist.append(", Restricted");
        }
        if(isSanctuary()) {
            flaglist.append(", Sanctuary");
        }
        if(isFreeBuild()) {
            flaglist.append(", Freebuild");
        }
        if(isBlockFireSpread()) {
            flaglist.append(", Fireproof");
        }
        if(isLavaControl()) {
            flaglist.append(", lava-secure");
        }
        if(isWaterControl()) {
            flaglist.append(", water-secure");
        }
        if(isFarmland()) {
            flaglist.append(", farmland");
        }
        if(ishMob()) {
            flaglist.append(", hmob");
        }
        
        if(welcome != null && farewell != null) {
            flaglist.append(", Welcome + Farewell");
            return flaglist.toString();
        }
        if(welcome != null) {
            flaglist.append(", Welcome");
        }
        if(farewell != null) {
            flaglist.append(", Farewell");
        }
        return flaglist.toString();
        
    }
    
    public String getPlayerList() {
        StringBuilder players = new StringBuilder();
        for(int i = 0; i < playerList.size(); i++) {
            if(i > 0) {
                players.append(",");
            }
            players.append(playerList.get(i));
        }
        String out = players.toString();
        if(out.length() == 0) {
            return "no_players";
        }
        else {
            return out;
        }
    }
    
    public ArrayList<String> getPlayerListRaw() {
        return playerList;
    }
    
    public String getGroupList() {
        StringBuilder groups = new StringBuilder();
        for(int i = 0; i < groupList.size(); i++) {
            if(i > 0) {
                groups.append(",");
            }
            groups.append(groupList.get(i));
        }
        String out = groups.toString();
        if(out.length() == 0) {
            return "no_groups";
        }
        else {
            return out;
        }
    }
    
    public ArrayList<String> getGroupListRaw() {
        return groupList;
    }
    
    /**
     * Retrieve the area settings as boolean HashMap
     * Used for copying properties to child cuboids when adding new ones.
     * @return
     */
    public HashMap<String,Boolean> getFlagListArray() {
        HashMap<String,Boolean> flags = new HashMap<String, Boolean>();
        flags.put("allowPvp",this.allowPvp);
        flags.put("creeperSecure",this.creeperSecure);
        flags.put("healing",this.healing);
        flags.put("protection",this.protection);
        flags.put("sanctuary",this.sanctuary);
        flags.put("sanctuarySpawnAnimals", this.sanctuarySpawnAnimals);
        flags.put("freeBuild", this.freeBuild);
        flags.put("blockFireSpread", this.blockFireSpread);
        //1.2.0
        flags.put("lavaControl", this.lavaControl);
        flags.put("waterControl", this.waterControl);
        flags.put("farmland", this.farmland);
        flags.put("tntSecure", this.tntSecure);
        //1.4.0
        flags.put("restriction", this.isRestricted());
        
        flags.put("hmob", this.ishMob());
        return flags;
        }
    
    /**
     * Calculate the size of this cuboid based on the points
     * @return
     */
    public int getSize() {
        return  (int)Vector.getDistance(point1.getBlockX(), point2.getBlockX()) * 
                (int)Vector.getDistance(point1.getBlockY(), point2.getBlockY()) * 
                (int)Vector.getDistance(point1.getBlockZ(), point2.getBlockZ());
    }
    
    /**
     * This takes a cuboid and overrides the local properties<br>
     * with the ones given inside cube
     * @param cube
     */
    public void overwriteProperties(CuboidE cube) {
        overwriteProperties(cube.getFlagListArray());
    }
    
    /**
     * This takes a cuboid and overrides the local properties<br>
     * with the ones given inside cube
     * @param cube
     */
    public void overwriteProperties(HashMap<String, Boolean> props) {
        setAllowPvp(((Boolean)props.get("allowPvp")).booleanValue());
        setCreeperSecure(((Boolean)props.get("creeperSecure")).booleanValue());
        setHealing(((Boolean)props.get("healing")).booleanValue());
        setProtection(((Boolean)props.get("protection")).booleanValue());
        setSanctuary(((Boolean)props.get("sanctuary")).booleanValue());
        setSanctuarySpawnAnimals(((Boolean)props.get("sanctuarySpawnAnimals")).booleanValue());
        setFreeBuild(((Boolean)props.get("freeBuild")).booleanValue());
        setBlockFireSpread(((Boolean)props.get("blockFireSpread")).booleanValue());

        setLavaControl(((Boolean)props.get("lavaControl")).booleanValue());
        setWaterControl(((Boolean)props.get("waterControl")).booleanValue());
        setFarmland(((Boolean)props.get("farmland")).booleanValue());
        setTntSecure(((Boolean)props.get("tntSecure")).booleanValue());
        setRestriction(((Boolean)props.get("restriction")).booleanValue());
        sethMob(((Boolean)props.get("hmob")).booleanValue());
    }
}
