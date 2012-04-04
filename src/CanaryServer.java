import java.util.ArrayList;
import java.util.HashMap;

import net.playblack.cuboids.gameinterface.CMob;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.gameinterface.CWorld;


public class CanaryServer extends CServer {

    protected HashMap<String, CWorld> worlds = new HashMap<String, CWorld>(5);
    @Override
    public CWorld getWorld(String name, int dimension) {
        if(worlds.containsKey(name+dimension)) {
            return worlds.get(name+dimension);
        }
        CanaryWorld world = new CanaryWorld(etc.getServer().getWorld(name)[dimension]);
        worlds.put(name+dimension, world);
        return world;
    }

    @Override
    public CWorld getWorld(int id) {
        return new CanaryWorld(etc.getServer().getWorld(id));
    }

    @Override
    public ArrayList<CPlayer> getPlayers(CWorld world) {
        return null;
    }
    @Override
    public CPlayer getPlayer(String name) {
        Player p = etc.getServer().matchPlayer(name);
        if(p == null) {
            return null;
        }
        return new CanaryPlayer(p);
    }

    @Override
    public void scheduleTask(long delay, Runnable task) {
        etc.getServer().addToServerQueue(task);
    }

    @Override
    public void scheduleTask(long delay, long intervall, Runnable task) {
        etc.getServer().addToServerQueue(task, delay);
    }

    @Override
    public CMob getMob(String name, CWorld world) {
        return new CanaryMob(new Mob(name, etc.getServer().getWorld(world.getName())[world.getDimension()]));
    }

    @Override
    public int getItemId(String itemName) {
        return etc.getDataSource().getItem(itemName);
    }

    @Override
    public String getItemName(int id) {
        return etc.getDataSource().getItem(id);
    }

    @Override
    public int getPlayersOnline() {
        return etc.getServer().getPlayerList().size();
    }

    @Override
    public int getMaxPlayers() {
        return etc.getInstance().getPlayerLimit();
    }
}
