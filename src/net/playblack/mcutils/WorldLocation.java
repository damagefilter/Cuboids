package net.playblack.mcutils;

public class WorldLocation extends Vector {
    private String world;
    private int dimension;
    public WorldLocation(int x, int y, int z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }
    
    public WorldLocation(int x, int y, int z, int dimension, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.world = world;
    }
    
    public WorldLocation(Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        world = "world"; //assume a common default
        dimension = 0; //assume default dimension (Overworld)
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
}
