package net.playblack.mcutils;

import net.playblack.cuboids.gameinterface.CWorld;

public class Location extends Vector {
    private String world;
    private int dimension;

    public Location(int x, int y, int z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
//        ToolBox.adjustWorldPosition(this);
    }

    public Location(double x, double y, double z, int dimension, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.world = world;
//        ToolBox.adjustWorldPosition(this);
    }

    public Location(int x, int y, int z, int dimension, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.world = world;
//        ToolBox.adjustWorldPosition(this);
    }

    public Location(int x, int y, int z, CWorld world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = world.getDimension();
        this.world = world.getName();
//        ToolBox.adjustWorldPosition(this);
    }

    public Location(Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        world = "world"; //assume a common default
        dimension = 0; //assume default dimension (Overworld)
//        ToolBox.adjustWorldPosition(this);
    }

    public Location(double x, double y, double z, String name, int dim) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = name;
        this.dimension = dim;
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
