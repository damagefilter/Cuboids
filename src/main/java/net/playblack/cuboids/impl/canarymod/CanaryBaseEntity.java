package net.playblack.cuboids.impl.canarymod;

import net.canarymod.api.entity.Entity;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.gameinterface.IBaseEntity;
import net.playblack.mcutils.Location;
import net.playblack.mcutils.Vector;

public class CanaryBaseEntity implements IBaseEntity {

    Entity entity;
    CanaryWorld world;

    public CanaryBaseEntity(Entity entity) {
        this.entity = entity;
        world = new CanaryWorld(entity.getWorld());
    }

    @Override
    public float getHealth() {
        return 0;
    }

    @Override
    public void setHealth(float health) {
    }

    @Override
    public String getName() {
        return entity.getName();
    }

    @Override
    public CWorld getWorld() {
        if (world.getHandle() != entity.getWorld()) {
            world = new CanaryWorld(entity.getWorld());
        }
        return world;
    }

    @Override
    public Vector getPosition() {
        return new Vector(entity.getX(), entity.getY(), entity.getZ());
    }

    @Override
    public Location getLocation() {
        return new Location(entity.getX(), entity.getY(), entity.getZ(), world.getDimension(), world.getName());
    }

    @Override
    public void setPosition(Vector v) {
        entity.setX(v.getX());
        entity.setY(v.getY());
        entity.setZ(v.getZ());
    }

    @Override
    public double getX() {
        return entity.getX();
    }

    @Override
    public double getY() {
        return entity.getY();
    }

    @Override
    public double getZ() {
        return entity.getZ();
    }

    @Override
    public double getPitch() {
        return entity.getPitch();
    }

    @Override
    public double getRotation() {
        return entity.getRotation();
    }

    @Override
    public boolean isPlayer() {
        if (entity.isLiving()) {
            return entity.isPlayer();
        }
        return false;
    }

    @Override
    public boolean isMob() {
        if (entity.isLiving()) {
            return entity.isMob();
        }
        return false;
    }

    @Override
    public boolean isAnimal() {
        if (entity.isLiving()) {
            return entity.isAnimal();
        }
        return false;
    }
}
