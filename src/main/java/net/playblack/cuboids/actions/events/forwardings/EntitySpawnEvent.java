package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.monster.EntityMob;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;

public class EntitySpawnEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled;
    private EntityMob entity;

    public EntitySpawnEvent(EntityMob entity) {
        this.entity = entity;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    public EntityMob getEntity() {
        return entity;
    }
}
