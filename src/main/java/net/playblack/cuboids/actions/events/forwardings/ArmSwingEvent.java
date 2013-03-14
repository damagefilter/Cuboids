package net.playblack.cuboids.actions.events.forwardings;

import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.gameinterface.CPlayer;

/**
 * Fired when cuboids catches an arm-swing event
 * @author chris
 *
 */
public class ArmSwingEvent extends CuboidEvent {
    private CPlayer player;
    
    public ArmSwingEvent(CPlayer player) {
        this.player = player;
    }
    
    public CPlayer getPlayer() {
        return player;
    }
}
