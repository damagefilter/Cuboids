package net.playblack.cuboids;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.regions.Region;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This runs every time a player runs into a healing area and has hearts left to
 * fill
 *
 * @author Chris
 */
public class HealThread implements Runnable {

    Player player;
    ScheduledExecutorService threadManager;
    Region cube;
    int healPower;
    long healDelay;

    public HealThread(Player player, Region cube, ScheduledExecutorService threadManager, int healPower, long healDelay) {
        this.player = player;
        this.healPower = healPower;
        this.healDelay = healDelay;
        this.cube = cube;
        this.threadManager = threadManager;
    }

    @Override
    public void run() {

        if (SessionManager.get().playerIsInRegion(player.getName(), cube)) {

            if (player.getHealth() > 0) {
                player.setHealth(player.getHealth() + this.healPower);
            }
            if (player.getHealth() < 20) {
                threadManager.schedule(this, healDelay, TimeUnit.SECONDS);
            }
        }
    }

}
