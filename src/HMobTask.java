import java.util.ArrayList;
import java.util.HashMap;
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
    HashMap<Short, Class<?>> mobs = new HashMap<Short, Class<?>>();
    Random rnd;
    
    
    public HMobTask(ArrayList<CuboidTree> nodes, World world) {
        this.nodes = nodes;
        mobs.put((short) 0, OEntityCaveSpider.class);
        mobs.put((short) 1, OEntityCreeper.class);
        mobs.put((short) 2, OEntityEnderman.class);
        mobs.put((short) 3, OEntitySkeleton.class);
        mobs.put((short) 4, OEntitySpider.class);
        mobs.put((short) 5, OEntityZombie.class);
        rnd = new Random();
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
                if(node.getCuboid().ishMob() && node.getCuboid().getPlayersWithin().size() > 0) {
                    
                    short nr = (short) rnd.nextInt(mobs.size());
                    Mob mob = null;
                    try {
                        mob = new Mob((OEntityLiving) mobs.get(nr).newInstance());
                    } catch (InstantiationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    Vector random = WorldObserver.getValidSpawnPosition(
                            Vector.randomVector(node.getCuboid().getFirstPoint(), 
                            node.getCuboid().getSecondPoint()), 
                            worldToId(node.getCuboid().getWorld()));
                    mob.setX(random.getX());
                    mob.setY(random.getY());
                    mob.setZ(random.getZ());
                    mob.spawn();
                }
            }
        }
    }
}
