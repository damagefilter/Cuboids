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
}
