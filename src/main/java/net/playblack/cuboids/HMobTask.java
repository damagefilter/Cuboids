package net.playblack.cuboids;

import net.canarymod.api.entity.living.monster.EntityMob;
import net.canarymod.api.world.World;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Vector;

import java.util.ArrayList;
import java.util.Random;

/**
 * This mob-spawner is started at Cuboids2 startup and will constantly spawn
 * mobs in areas that are having players and have the hmob flag set.
 *
 * @author Chris
 */
public class HMobTask implements Runnable {
    ArrayList<Region> nodes;
    Random rnd;

    public HMobTask() {
        this.nodes = RegionManager.get().getRootNodeList();
        rnd = new Random();
    }

    private EntityMob getRandomhMob(World world, int index) {
        switch (index) {
            case 0:

                return CServer.getServer().getMob("CaveSpider", world);
            case 1:
                return CServer.getServer().getMob("Creeper", world);
            case 2:
                return CServer.getServer().getMob("Enderman", world);
            case 3:
                return CServer.getServer().getMob("Skeleton", world);
            case 4:
                return CServer.getServer().getMob("Spider", world);
            case 5:
                return CServer.getServer().getMob("Zombie", world);
            default:
                return CServer.getServer().getMob("Zombie", world);
        }
    }

    public synchronized void run() {
        for (Region tree : nodes) {
            for (Region node : tree.getChildsDeep(new ArrayList<Region>())) {
                if (node.getProperty("mob-spawn") == Status.DENY) {
                    // if mobs are not allowed to spawn, more-mobs flag is ignored
                    continue;
                }
                if ((node.getProperty("more-mobs") == Status.ALLOW) && (CServer.getServer().isPlayerInRegion(node))) {
                    World w = CServer.getServer().getWorld(node.getWorld(), node.getDimension());
                    if (w.getRawTime() < 13000) {
                        // It's not night, don't bother spawning things
                        continue;
                    }

                    int maxMobs = rnd.nextInt(10);
                    int mobIndex = rnd.nextInt(6);
                    Vector random = Vector.randomVector(node.getOrigin(), node.getOffset());
                    for (int i = 0; i < maxMobs; i++) {
                        EntityMob mob = getRandomhMob(w, mobIndex);
                        mob.setX(random.getX());
                        mob.setY(w.getHighestBlockAt(random.getBlockX(), random.getBlockZ()));
                        mob.setZ(random.getBlockZ());
                        mob.spawn();
                    }
                }
            }
        }
    }
}
