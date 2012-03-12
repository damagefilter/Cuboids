import java.util.ArrayList;

import com.playblack.cuboid.tree.CuboidNode;
import com.playblack.cuboid.tree.CuboidTree;
import com.playblack.cuboid.tree.CuboidTreeHandler;
import com.playblack.mcutils.Vector;


/**
 * This mob-spawner is started at Cuboids2 startup and will constantly spawn mobs in areas 
 * that are having players and have the hmob flag set.
 * @author Chris
 *
 */
public class HMobTask implements Runnable {
    ArrayList<CuboidTree> nodes;

    public HMobTask(ArrayList<CuboidTree> nodes) {
        this.nodes = nodes;
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
                    World w = etc.getServer().getWorld(worldToId(node.getCuboid().getWorld()));
                    Mob mob = new Mob(new OEntityCreeper(w.getWorld()));
                    Vector random = Vector.randomVector(node.getCuboid().getFirstPoint(), node.getCuboid().getSecondPoint());
                    mob.setX(random.getX());
                }
            }
        }
    }
}
