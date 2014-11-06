package net.playblack.cuboids.impl.canarymod;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.living.monster.EntityMob;
import net.playblack.cuboids.gameinterface.CMob;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.mcutils.Location;
import net.playblack.mcutils.Vector;

public class CanaryMob implements CMob {

    EntityMob mob;
    CanaryWorld world;

    public CanaryMob(Entity mob) {
        this.mob = (EntityMob) mob;
        world = new CanaryWorld(mob.getWorld());
    }

    @Override
    public float getHealth() {
        return mob.getHealth();
    }

    @Override
    public void setHealth(float health) {
        mob.setHealth(health);
    }

    @Override
    public String getName() {
        return mob.getName();
    }

    @Override
    public CWorld getWorld() {
        // Player has switched worlds
        if (!mob.getWorld().getName().equals(mob.getWorld().getName())) {
            this.world = new CanaryWorld(mob.getWorld());
        }
        else if (world.getDimension() != mob.getWorld().getType().getId()) {
            this.world = new CanaryWorld(mob.getWorld());
        }
        return world;
    }

    @Override
    public Vector getPosition() {
        return new Vector(mob.getX(), mob.getY(), mob.getZ());
    }

    @Override
    public void setPosition(Vector v) {
        mob.setX(v.getX());
        mob.setY(v.getY());
        mob.setZ(v.getZ());
    }

    @Override
    public Location getLocation() {
        return new Location(mob.getX(), mob.getY(), mob.getZ(), world.getDimension(), world.getName());
    }

    @Override
    public double getX() {
        return mob.getX();
    }

    @Override
    public void setX(double x) {
        mob.setX(x);
    }

    @Override
    public double getY() {
        return mob.getY();
    }

    @Override
    public void setY(double y) {
        mob.setY(y);
    }

    @Override
    public double getZ() {
        return mob.getZ();
    }

    @Override
    public void setZ(double z) {
        mob.setZ(z);
    }

    @Override
    public double getPitch() {
        return mob.getPitch();
    }

    @Override
    public double getRotation() {
        return mob.getRotation();
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public void kill() {
        mob.kill();
    }

    @Override
    public void spawn() {
        mob.spawn();
    }

    @Override
    public boolean isMob() {
        return true;
    }

    @Override
    public boolean isAnimal() {
        return false;
    }
}
