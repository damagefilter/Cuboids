package net.playblack.cuboids.actions.events.forwardings;

import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.blocks.CItem;
import net.playblack.cuboids.gameinterface.CPlayer;

public class ItemDropEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled = false;
    private CItem item;
    private CPlayer player;

    public ItemDropEvent(CItem item, CPlayer player) {
        this.item = item;
        this.player = player;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    public CItem getItem() {
        return item;
    }

    public CPlayer getPlayer() {
        return player;
    }
}
