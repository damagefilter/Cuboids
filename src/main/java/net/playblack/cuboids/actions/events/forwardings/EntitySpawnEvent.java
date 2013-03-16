package net.playblack.cuboids.actions.events.forwardings;

import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.gameinterface.IBaseEntity;

public class EntitySpawnEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled;
    private IBaseEntity entity;
    
    public EntitySpawnEvent(IBaseEntity entity) {
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

    public IBaseEntity getEntity() {
        return entity;
    }
}
