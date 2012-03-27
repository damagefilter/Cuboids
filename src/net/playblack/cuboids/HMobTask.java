package net.playblack.cuboids;
import java.util.ArrayList;
import java.util.Random;

import net.playblack.cuboids.gameinterface.CMob;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.CuboidNode;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Vector;


/**
 * This mob-spawner is started at Cuboids2 startup and will constantly spawn mobs in areas 
 * that are having players and have the hmob flag set.
 * @author Chris
 *
 */
public class HMobTask implements Runnable {
    ArrayList<CuboidNode> nodes;
    Random rnd;
    public HMobTask() {
        this.nodes = RegionManager.getInstance().getRootNodeList();
        rnd = new Random();
    }
    
    private CMob getRandomNotchMob(CWorld world, int index) {
        switch(index) {
            case 0:
                
                return CServer.getInstance().getMob("CaveSpider", world);
            case 1:
                return CServer.getInstance().getMob("Creeper", world);
            case 2:
                return CServer.getInstance().getMob("Enderman", world);
            case 3:
                return CServer.getInstance().getMob("Skeleton", world);
            case 4:
                return CServer.getInstance().getMob("Spider", world);
            case 5:
                return CServer.getInstance().getMob("Zombie", world);
            default:
                return CServer.getInstance().getMob("Zombie", world);
        }
    }

    
    public synchronized void run() {
        for(CuboidNode tree : nodes) {
            for(CuboidNode node : tree.toList()) {
                if(node.getCuboid().isSanctuary()) {
                    //Sanctuary wins over hmobs
                    continue;
                }
                if((node.getCuboid().ishMob()) && (node.getCuboid().getPlayersWithin().size() > 0)) {
                    CWorld w = CServer.getInstance().getWorld(node.getWorld());
                    if(w.getTime() < 13000) {
                        //It's not night, don't bother spawning things
                        continue;
                    }
                    
                    int maxMobs = rnd.nextInt(10);
                    int mobIndex = rnd.nextInt(6);
                    Vector random = Vector.randomVector(node.getCuboid().getFirstPoint(), node.getCuboid().getSecondPoint());
                    for(int i = 0; i < maxMobs; i++) {
                        CMob mob = getRandomNotchMob(w, mobIndex);
                        mob.setX(random.getX());
                        mob.setY(w.getHighestBlock(random.getBlockX(), random.getBlockZ()));
                        mob.setZ(random.getZ());
                        mob.spawn();
                    }
                }
            }
        }
    }
}
