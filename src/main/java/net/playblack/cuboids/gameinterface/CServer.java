package net.playblack.cuboids.gameinterface;

import java.util.ArrayList;

import net.playblack.cuboids.InvalidPlayerException;
import net.playblack.cuboids.regions.Region;

public abstract class CServer {

    private static CServer instance = null;

    /**
     * Get a world by its name
     * 
     * @param name
     * @return
     */
    public abstract CWorld getWorld(String name, int dimension);

    /**
     * Get a world by its id
     * 
     * @param id
     * @return
     */
    public abstract CWorld getWorld(int id);

    public abstract CWorld getDefaultWorld();

    /**
     * Get the id of a dimension by its name
     * 
     * @param name
     * @return
     */
    public abstract int getDimensionId(String name);
    
    /**
     * get all players online on this server
     * @return
     */
    public abstract ArrayList<CPlayer> getPlayers();

    /**
     * Get a player by name
     * 
     * @param name
     * @return
     * @throws InvalidPlayerException when given player does not exist (is not online)
     */
    public abstract CPlayer getPlayer(String name) throws InvalidPlayerException;

    /**
     * Force to update the player reference in the player cache and return the
     * new one
     * 
     * @param name
     * @return
     * @throws InvalidPlayerException when given player does not exist (is not online)
     */
    public abstract CPlayer refreshPlayer(String name) throws InvalidPlayerException;

    /**
     * Remove player from the server list
     * 
     * @param player
     */
    public abstract void removePlayer(String player);

    /**
     * Schedule a task in the server queue
     * 
     * @param delay
     * @param task
     */
    public abstract void scheduleTask(long delay, Runnable task);

    /**
     * Schedule a periodic task in the server queue
     * 
     * @param delay
     * @param task
     */
    public abstract void scheduleTask(long delay, long intervall, Runnable task);

    /**
     * Get a new Mob instance with a mob with the given name.
     * 
     * @param name
     * @param world
     * @return
     */
    public abstract CMob getMob(String name, CWorld world);

    /**
     * Give me an instance of your server implementation!
     * 
     * @param server
     */
    public static void setServer(CServer server) {
        instance = server;
    }

    /**
     * Return the current server instance
     * 
     * @return
     */
    public static CServer getServer() {
        return instance;
    }

    /**
     * Retrieve the id of an item by its name
     * 
     * @param itemName
     * @return
     */
    public abstract int getItemId(String itemName);

    /**
     * Retrieve the name of an item by its id
     * 
     * @param id
     * @return
     */
    public abstract String getItemName(int id);

    /**
     * Get the number of online players
     * 
     * @return
     */
    public abstract int getPlayersOnline();

    /**
     * Get the number of max. allowed players on the server
     * 
     * @return
     */
    public abstract int getMaxPlayers();
    
    /**
     * Check if this region contains a player
     * @param r
     * @return true if r contains any players, false otherwise
     */
    public boolean isPlayerInRegion(Region r) {
        for(CPlayer p : getPlayers()) {
            if(p.isInRegion(r)) {
                return true;
            }
        }
        return false;
    }
}
