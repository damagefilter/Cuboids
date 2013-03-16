package net.playblack.cuboids.actions.operators;

import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.PlayerWalkEvent;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.EventLogger;

public class PlayerMovementOperator implements ActionListener {
    
    @ActionHandler
    public void onPlayerMove(PlayerWalkEvent event) {
        
        if(!event.getPlayer().canMoveTo(event.getTo())) {
            EventLogger.getInstance().logMessage(event.getPlayer().getName() + " will not move and be tp'd back!", "INFO");
            event.getPlayer().teleportTo(event.getFrom());
        }
        
        CuboidInterface.get().handleRegionsForPlayer(event.getPlayer(), event.getFrom(), event.getTo());
    }
    
    static {
        ActionManager.registerActionListener("Cuboids2", new PlayerMovementOperator());
    }
}
