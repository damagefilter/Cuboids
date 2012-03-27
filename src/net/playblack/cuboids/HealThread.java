package net.playblack.cuboids;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidE;

/**
 * This runs every time a player runs into a healing area and has hearts left to fill
 * @author Chris
 *
 */
public class HealThread implements Runnable {

    CPlayer player;
    ScheduledExecutorService threadManager;
    CuboidE cube;
    int healPower;
    long healDelay;

    public HealThread(CPlayer player, CuboidE cube, ScheduledExecutorService threadManager, int healPower, long healDelay) {
        this.player = player;
        this.healPower = healPower;
        this.healDelay = healDelay;
        this.cube = cube;
        this.threadManager = threadManager;
    }
    @Override
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
