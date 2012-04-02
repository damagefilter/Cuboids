package net.playblack.cuboids.gameinterface;

import net.playblack.mcutils.Vector;

public interface IBaseEntity {
    
    /**
     * Get the Entity health
     * @return
     */
    public int getHealth();
    
    /**
     * Set health for this Entity
     * @param health
     */
    public void setHealth(int health);
    
    /**
     * Get name for this entity
     * @return
     */
    public String getName();
    
    /**
     * Get the world this entity resides in
     * @return
     */
    public CWorld getWorld();
    
    /**
     * Get the position of this entity
     * @return
     */
    public Vector getPosition();
    
    /**
     * Set the position of this entity
     * @param v
     */
    public void setPosition(Vector v);
    
    /**
     * Get X position
     * @return
     */
    public double getX();
    
    /**
     * Get Y Position
     * @return
     */
    public double getY();
    
    /**
     * Get Z Position
     * @return
     */
    public double getZ();
}
