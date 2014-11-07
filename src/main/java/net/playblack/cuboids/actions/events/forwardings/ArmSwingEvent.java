package net.playblack.cuboids.actions.events.forwardings;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.actions.events.CuboidEvent;

/**
 * Fired when cuboids catches an arm-swing event
 *
 * @author chris
 */
public class ArmSwingEvent extends CuboidEvent {
    private Player player;

    public ArmSwingEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
