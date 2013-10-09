package net.playblack.cuboids.actions.events.forwardings;

import net.playblack.cuboids.actions.events.Cancellable;
import net.playblack.cuboids.actions.events.CuboidEvent;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.mcutils.Location;

public class IgniteEvent extends CuboidEvent implements Cancellable {

    public enum FireSource {
        LAVA,
        LIGHTER,
        SPREAD,
        FIRE_DAMAGE,
        LIGHTING;

        public static FireSource fromInt(int in) {
            switch (in) {
                case 1:
                    return LAVA;
                case 2:
                    return LIGHTER;
                case 3:
                    return SPREAD;
                case 4:
                    return FIRE_DAMAGE;
                case 5:
                    return LIGHTING;
                default:
                    return SPREAD;
            }
        }
    }

    private boolean isCancelled;

    private FireSource source;
    private Location location;
    private CBlock block;
    private CPlayer player;

    public IgniteEvent(FireSource source, Location location, CBlock block, CPlayer player) {
        this.source = source;
        this.location = location;
        this.block = block;
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

    public FireSource getSource() {
        return source;
    }

    public Location getLocation() {
        return location;
    }

    public CBlock getBlock() {
        return block;
    }

    public CPlayer getPlayer() {
        return player;
    }
}
