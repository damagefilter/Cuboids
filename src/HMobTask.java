import java.util.ArrayList;
import java.util.Random;

import net.playblack.cuboid.tree.CuboidNode;
import net.playblack.cuboid.tree.CuboidTree;
import net.playblack.mcutils.Vector;


/**
 * This mob-spawner is started at Cuboids2 startup and will constantly spawn mobs in areas 
 * that are having players and have the hmob flag set.
 * @author Chris
 *
 */
public class HMobTask implements Runnable {
    ArrayList<CuboidTree> nodes;
    Random rnd;
    public HMobTask(ArrayList<CuboidTree> nodes) {
        this.nodes = nodes;
        rnd = new Random();
    }
    
    private OEntity getRandomNotchMob(World world, int index) {
        switch(index) {
            case 0:
                return new OEntityCaveSpider(world.getWorld());
            case 1:
                return new OEntityCreeper(world.getWorld());
            case 2:
                return new OEntityEnderman(world.getWorld());
            case 3:
                return new OEntitySkeleton(world.getWorld());
            case 4:
                return new OEntitySpider(world.getWorld());
            case 5:
                return new OEntityZombie(world.getWorld());
            default:
                return new OEntityZombie(world.getWorld());
        }
    }

    private int worldToId(String world) {
        if(world.equalsIgnoreCase("NORMAL")) {
            return 0;
        }
        else if(world.equalsIgnoreCase("NETHER")) {
            return -1;
        }
        else {
            return 1;
        }
    }
    
    public synchronized void run() {
        for(CuboidTree tree : nodes) {
            for(CuboidNode node : tree.toList()) {
                if(node.getCuboid().isSanctuary()) {
                    //Sanctuary wins over hmobs
                    continue;
                }
                if((node.getCuboid().ishMob()) && (node.getCuboid().getPlayersWithin().size() > 0)) {
                    World w = etc.getServer().getWorld(worldToId(node.getCuboid().getWorld()));
                    if(w.getRelativeTime() < 13000) {
                        //It's not night, don't bother spawning things
                        continue;
                    }
                    int maxMobs = rnd.nextInt(10);
                    int mobIndex = rnd.nextInt(6);
                    Vector random = Vector.randomVector(node.getCuboid().getFirstPoint(), node.getCuboid().getSecondPoint());
                    for(int i = 0; i < maxMobs; i++) {
                        Mob mob = new Mob((OEntityLiving) getRandomNotchMob(w, mobIndex));
                        mob.setX(random.getX());
                        mob.setY(w.getHighestBlockY(random.getBlockX(), random.getBlockZ()));
                        mob.setZ(random.getZ());
                        mob.spawn();
                    }
                }
            }
        }
    }
}
