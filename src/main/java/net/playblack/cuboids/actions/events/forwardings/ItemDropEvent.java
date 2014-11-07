package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;

public class ItemDropEvent extends CuboidEvent implements Cancellable {
    private boolean isCancelled = false;
    private Item item;
    private Player player;
    private Location itemDropLocation;

    public ItemDropEvent(Item item, Player player, Location itemDropLocation) {
        this.item = item;
        this.player = player;
        this.itemDropLocation = itemDropLocation;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    public Item getItem() {
        return item;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getItemDropLocation() {
        return itemDropLocation;
    }
}
