package net.playblack.cuboids.actions.operators;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.PlayerWalkEvent;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.CuboidInterface;

public class PlayerMovementOperator implements ActionListener {

    @ActionHandler
    public void onPlayerMove(PlayerWalkEvent event) {

        Player p = CServer.getServer().getPlayer(event.getPlayer().getName());
        Location from = new Location(event.getPlayer().getWorld(), event.getFrom());
        Location to = new Location(event.getPlayer().getWorld(), event.getTo());
        if (!p.canMoveTo(to)) {
            p.teleportTo(event.getFrom());
        }

        CuboidInterface.get().handleRegionsForPlayer(event.getPlayer(), from, to);
    }

    static {
        ActionManager.registerActionListener("Cuboids", new PlayerMovementOperator());
    }
}
