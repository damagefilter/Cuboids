import java.util.ArrayList;

import net.playblack.cuboids.gameinterface.CMob;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.gameinterface.CWorld;


public class CanaryServer extends CServer {

    @Override
    public CWorld getWorld(String name, int dimension) {
        return new CanaryWorld(etc.getServer().getWorld(name)[dimension]);
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
    public void scheduleTask(long delay, Runnable task) {
        etc.getServer().addToServerQueue(task);
    }

    @Override
    public void scheduleTask(long delay, long intervall, Runnable task) {
        // TODO Auto-generated method stub
    }

    @Override
    public CMob getMob(String name, CWorld world) {
        //TODO implement some mobs!
        return null;
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
