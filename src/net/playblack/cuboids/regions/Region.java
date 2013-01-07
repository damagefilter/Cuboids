package net.playblack.cuboids.regions;

import net.playblack.cuboids.gameinterface.CWorld;

public abstract class Region {
    /** Reference to this Cuboids parent*/ //This'll get ugly when reading from datasource
    protected Cuboid parent;
    
    /** The name of this cuboid*/
    protected String name;
    
    /** The name of the world this cuboid sits in*/
    protected String world;
    
    /** The ID of the dimension for this cuboid. This may always be 0 in some implementations */
    protected int dimension;
    
    /** Cuboids priority. Cuboids with higher priority will be considered when areas clash*/
    protected int priority;
    
    /**
     * Set the parent cuboid for this guy
     * @param cube
     */
    public void setParent(Cuboid cube) {
        parent = cube;
    }
    
    /**
     * Get the parent cuboid for this guy
     * @return
     */
    public Cuboid getParent() {
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
    
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int prio) {
        this.priority = prio;
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
}
