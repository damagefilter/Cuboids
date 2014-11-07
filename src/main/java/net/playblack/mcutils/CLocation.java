package net.playblack.mcutils;

import net.canarymod.api.world.World;

public class CLocation extends Vector {
    private String world;
    private int dimension;

    public CLocation(int x, int y, int z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
//        ToolBox.adjustWorldPosition(this);
    }

    public CLocation(double x, double y, double z, int dimension, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.world = world;
//        ToolBox.adjustWorldPosition(this);
    }

    public CLocation(int x, int y, int z, int dimension, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.world = world;
//        ToolBox.adjustWorldPosition(this);
    }

    public CLocation(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = world.getType().getId();
        this.world = world.getName();
//        ToolBox.adjustWorldPosition(this);
    }

    public CLocation(Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        world = "world"; //assume a common default
        dimension = 0; //assume default dimension (Overworld)
//        ToolBox.adjustWorldPosition(this);
    }

    public CLocation(net.canarymod.api.world.position.Location v) {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
        world = v.getWorld().getName();
        dimension = v.getType().getId();
    }

    public CLocation(double x, double y, double z, String name, int dim) {
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
