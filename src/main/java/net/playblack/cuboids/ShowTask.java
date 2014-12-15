package net.playblack.cuboids;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.packet.Packet;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.tasks.ServerTask;
import net.canarymod.tasks.TaskOwner;

import java.util.List;

/**
 * A Task to show a Cuboid
 *
 * @author Ekranos
 */
public class ShowTask extends ServerTask {

    private Player player;
    private List<Block> blocks;
    private int amount;

    public ShowTask(TaskOwner owner, long delay, Player player, List<Block> blocks, int amount) {
        super(owner, delay, true);
        this.player = player;
        this.blocks = blocks;
        this.amount = amount;
    }

    @Override
    public void run() {
        for (Block block : blocks) {
            Packet packet = Canary.factory().getPacketFactory().particles(
                    "reddust",
                    block.getX(),
                    block.getY(),
                    block.getZ(),
                    0,
                    1,
                    0,
                    1,
                    3
            );
            player.sendPacket(packet);
        }

        if (amount-- == 0)
            Canary.getServer().removeSynchronousTask(this);
    }
}