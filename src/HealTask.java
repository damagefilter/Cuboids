import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.playblack.cuboid.CuboidE;


/**
 * This heal job is called from protection interface and
 * re-schedules itself until the player is done healing.
 * @author Chris
 *
 */
public class HealTask implements Runnable {
    Player player;
    ScheduledExecutorService threadManager;
    CuboidE cube;
    int healPower;
    long healDelay;

    public HealTask(Player player, CuboidE cube, ScheduledExecutorService threadManager, int healPower, long healDelay) {
        this.player = player;
        this.healPower = healPower;
        this.healDelay = healDelay;
        this.cube = cube;
        this.threadManager = threadManager;
    }

    public void run() {
        if (this.cube.playerIsWithin(player.getName())) {
        	
            if (player.getHealth() > 0) {
            	
                player.setHealth(player.getHealth() + this.healPower);
            }
            if (player.getHealth() < 20) {
                threadManager.schedule(this, healDelay, TimeUnit.SECONDS);
            }
        }
    }
}
