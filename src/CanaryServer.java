import java.util.ArrayList;
import java.util.HashMap;

import net.playblack.cuboids.gameinterface.CMob;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.gameinterface.CWorld;


public class CanaryServer extends CServer {

    protected HashMap<String, CWorld> worlds = new HashMap<String, CWorld>(5);
    private HashMap<String,CPlayer> playerList;
    
    /**
     * DO <b>NOT</b> initialize the server at ANY OTHER place than in the Bootstrapper Constructor!!!!!
     * If you do, you will <b>DIE!</b>
     */
    public CanaryServer() {
        playerList = new HashMap<String, CPlayer>(etc.getInstance().getPlayerLimit());
    }
    
    @Override
    public CWorld getWorld(String name, int dimension) {
        if(worlds.containsKey(name+dimension)) {
            return worlds.get(name+dimension);
        }
        CanaryWorld world = null;
        if(dimension == 0) {
            world = new CanaryWorld(etc.getServer().getWorld(name)[0]);
        }
        if(dimension == 1) {
            world = new CanaryWorld(etc.getServer().getWorld(name)[2]);
        }
        if(dimension == -1) {
            world = new CanaryWorld(etc.getServer().getWorld(name)[1]);
        }
        worlds.put(name+dimension, world);
        return world;
    }

    @Override
    public CWorld getWorld(int id) {
        return getWorld(etc.getServer().getDefaultWorld().getName(), id);
    }

    @Override
    public ArrayList<CPlayer> getPlayers(CWorld world) {
        return null;
    }
    @Override
    public CPlayer getPlayer(String name) {
        if(!playerList.containsKey(name)) {
            Player p = etc.getServer().matchPlayer(name);
            playerList.put(name, new CanaryPlayer(p));
        }
        return playerList.get(name);
    }
    
    @Override
    public void removePlayer(String player) {
        playerList.remove(player);
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
        int id = 0;
        id = etc.getDataSource().getItem(itemName);
        if(id != 0 && id >= 0) {
            return id;
        }
        else if(id == 0 && itemName.equalsIgnoreCase("air")) {
            return id;
        }
        else {
            try {
                return Integer.valueOf(itemName);
            }
            catch(NumberFormatException e) {
                return -1;
            }
        }
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

    @Override
    public int getDimensionId(String name) {
        return World.Dimension.valueOf(name).getId();
    }

    @Override
    public CWorld getDefaultWorld() {
        World def = etc.getServer().getDefaultWorld();
        return getWorld(def.getName(), def.getType().getId());
    }
}
