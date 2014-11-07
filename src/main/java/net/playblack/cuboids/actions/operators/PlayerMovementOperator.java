package net.playblack.cuboids.actions.operators;

import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.PlayerWalkEvent;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.CuboidInterface;

public class PlayerMovementOperator implements ActionListener {

    @ActionHandler
    public void onPlayerMove(PlayerWalkEvent event) {

        CPlayer p = CServer.getServer().getPlayer(event.getPlayer().getName());
        if (!p.canMoveTo(event.getTo())) {
            p.teleportTo(event.getFrom());
        }

        CuboidInterface.get().handleRegionsForPlayer(event.getPlayer(), event.getFrom(), event.getTo());
    }

    static {
        ActionManager.registerActionListener("Cuboids", new PlayerMovementOperator());
    }
}
