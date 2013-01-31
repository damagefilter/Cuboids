package net.playblack.cuboids.regions;

import java.util.ArrayList;
import java.util.Collections;

import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.mcutils.Location;

public abstract class Region {
    
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
    
    /** Players that are currently within this area*/
    private ArrayList<String> playersWithin;
    
    public boolean hasChanged = false;
    
    
    /**
     * Checks if a given location is within this region.
     * 
     * @param v
     * @return True: Point is within this region, false otherwise
     */
    public abstract boolean isWithin(Location location);
    
    /**
     * Check if this cuboid is inside the given one
     * 
     * @param cube
     * @param complete
     *            true to check if it is inside with both edges
     * @return
     */
    public abstract boolean cuboidIsWithin(Region r, boolean complete);
    
    /**
     * Get the volume in blocks of this region
     * @return
     */
    public abstract int getSize();
    
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

    /**
     * Check if a player is within this region
     * @param playerName
     * @return
     */
    public boolean playerIsWithin(String playerName) {
        if (playersWithin.contains(playerName)) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Set the parent region for the this region
     * @param cube
     */
    public void setParent(Region cube) {
        parent = cube;
        if(parent != null) {
            parent.childs.add(this);
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
    public ArrayList<Region> getChildsDeep() {
        ArrayList<Region> collection = new ArrayList<Region>();
        collection.addAll(childs);
        for(Region r : childs) {
            collection.addAll(r.getChildsDeep());
        }
        return (ArrayList<Region>) Collections.unmodifiableList(collection);
    }
}
