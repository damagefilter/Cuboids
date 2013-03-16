package net.playblack.cuboids.gameinterface;

import net.playblack.mcutils.Vector;
import net.playblack.mcutils.Location;

public interface IBaseEntity {

    /**
     * Get the Entity health
     * 
     * @return
     */
    public int getHealth();

    /**
     * Set health for this Entity
     * 
     * @param health
     */
    public void setHealth(int health);

    /**
     * Get name for this entity
     * 
     * @return
     */
    public String getName();

    /**
     * Get the world this entity resides in
     * 
     * @return
     */
    public CWorld getWorld();

    /**
     * Get the position of this entity
     * 
     * @return
     */
    public Vector getPosition();

    /**
     * get this Entities world location
     * 
     * @return
     */
    public Location getLocation();

    /**
     * Set the position of this entity
     * 
     * @param v
     */
    public void setPosition(Vector v);

    /**
     * Get X position
     * 
     * @return
     */
    public double getX();

    /**
     * Get Y Position
     * 
     * @return
     */
    public double getY();

    /**
     * Get Z Position
     * 
     * @return
     */
    public double getZ();
    
    /**
     * Get rotation around X-Axis (Up/Down looking)
     * @return
     */
    public double getPitch();
    
    /**
     * Get rotation around the Y-Axis (look rotation)
     * @return
     */
    public double getRotation();
    
    public boolean isPlayer();
    
    public boolean isMob();
    
    public boolean isAnimal();
}
