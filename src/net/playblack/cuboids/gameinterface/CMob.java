package net.playblack.cuboids.gameinterface;

public abstract class CMob implements IBaseEntity {
    
    /**
     * Kill this mob
     */
    public abstract void kill();
    
    /**
     * Spawn this mob
     */
    public abstract void spawn();
    
    /**
     * Set X
     */
    public abstract void setX(double x);
    
    /**
     * Set Y
     */
    public abstract void setY(double y);
    
    /**
     * Set Z
     */
    public abstract void setZ(double z);
}
