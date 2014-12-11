package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.actions.operators.ExplosionType;

import java.util.List;
import java.util.Map;

public class ExplosionEvent extends CuboidEvent implements Cancellable {

    private boolean isCancelled = false;
    private ExplosionType explosionType;
    private Entity entity;
    private Location location;
    private Map<Location, BlockType> affectedBlocks;
    private List<Location> protectedBlocks = null;

    public ExplosionEvent(Entity entity, Location location, ExplosionType explosiontype, Map<Location, BlockType> affectedBlocks) {
        this.explosionType = explosiontype;
        this.entity = entity;
        this.location = location;
        this.affectedBlocks = affectedBlocks;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    public ExplosionType getExplosionType() {
        return explosionType;
    }

    public Entity getEntity() {
        return entity;
    }

    public Location getLocation() {
        return location;
    }

    public Map<Location, BlockType> getAffectedBlocks() {
        return affectedBlocks;
    }

    public List<Location> getProtectedBlocks() {
        return protectedBlocks;
    }

    public void setProtectedBlocks(List<Location> list) {
        this.protectedBlocks = list;
    }

}
