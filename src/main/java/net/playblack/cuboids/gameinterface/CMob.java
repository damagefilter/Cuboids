package net.playblack.cuboids.gameinterface;

public interface CMob extends IBaseEntity {

    /**
     * Kill this mob
     */
    public void kill();

    /**
     * Spawn this mob
     */
    public void spawn();

    /**
     * Set X
     */
    public void setX(double x);

    /**
     * Set Y
     */
    public void setY(double y);

    /**
     * Set Z
     */
    public void setZ(double z);

    /**
     * Check if this mob is a hostile mob
     *
     * @return
     */
    public boolean isMob();

    /**
     * Check if this mob is an animal
     *
     * @return
     */
    public boolean isAnimal();

}
