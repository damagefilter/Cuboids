package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.Entity;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;

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

    public enum ExplosionType {
        TNT(1),
        CREEPER(2),
        GHAST_FIREBALL(3),
        WITHER_SKULL(4);

        private int myId;

        ExplosionType(int id) {
            myId = id;
        }

        public static ExplosionType fromId(int id) {
            switch (id) {
                case 1:
                    return TNT;
                case 2:
                    return CREEPER;
                case 3:
                    return GHAST_FIREBALL;
                case 4:
                    return WITHER_SKULL;
                default:
                    return TNT;
            }
        }

        public static int toId(ExplosionType t) {
            switch (t) {
                case CREEPER:
                    return 2;
                case GHAST_FIREBALL:
                    return 3;
                case TNT:
                    return 1;
                case WITHER_SKULL:
                    return 4;
                default:
                    return -1;
            }
        }

        public int getId() {
            return myId;
        }
    }

}
