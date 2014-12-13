package net.playblack.cuboids;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.monster.EntityMob;
import net.canarymod.api.factory.EntityFactory;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Vector3D;
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
        this.nodes = RegionManager.get().getRegionsWithProperty("more-mobs");
        rnd = new Random();
    }

    private EntityMob getRandomhMob(World world, int index) {
        EntityFactory factory = Canary.factory().getEntityFactory();

        switch (index) {
            case 0:
                return factory.newEntityMob("CaveSpider", world);
            case 1:
                return factory.newEntityMob("Creeper", world);
            case 2:
                return factory.newEntityMob("Enderman", world);
            case 3:
                return factory.newEntityMob("Skeleton", world);
            case 4:
                return factory.newEntityMob("Spider", world);
            case 5:
                return factory.newEntityMob("Zombie", world);
            default:
                return factory.newEntityMob("Zombie", world);
        }
    }

    public synchronized void run() {
        for (Region tree : nodes) {
            for (Region node : tree.getChildsDeep(new ArrayList<Region>())) {
                if (node.getProperty("mob-spawn") == Status.DENY) {
                    // if mobs are not allowed to spawn, more-mobs flag is ignored
                    continue;
                }
                if ((node.getProperty("more-mobs") == Status.ALLOW) && (SessionManager.get().isRegionPopulated(node))) {
                    World w = Canary.getServer().getWorld(node.getWorld());
                    if (w.getRawTime() < 13000) {
                        // It's not night, don't bother spawning things
                        continue;
                    }

                    int maxMobs = rnd.nextInt(10);
                    int mobIndex = rnd.nextInt(6);
                    Vector3D random = Vector.randomVector(node.getOrigin(), node.getOffset());
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
