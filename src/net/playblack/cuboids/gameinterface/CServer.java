package net.playblack.cuboids.gameinterface;

import java.util.ArrayList;

public abstract class CServer {
    
    private static CServer instance = null;
    
    /**
     * Get a world by its name
     * @param name
     * @return
     */
    public abstract CWorld getWorld(String name, String dimension);
    
    /**
     * Get a world by its id
     * @param id
     * @return
     */
    public abstract CWorld getWorld(int id);
    
    /**
     * Get a list of all players in a world
     * @param world
     * @return
     */
    public abstract ArrayList<CPlayer> getPlayers(CWorld world);
    
    /**
     * Schedule a task in the server queue
     * @param delay
     * @param task
     */
    public abstract void scheduleTask(long delay, Runnable task);
    
    /**
     * Schedule a periodic task in the server queue
     * @param delay
     * @param task
     */
    public abstract void scheduleTask(long delay, long intervall, Runnable task);
    
    /**
     * Get a new Mob instance with a mob with the given name.
     * @param name
     * @param world
     * @return
     */
    public abstract CMob getMob(String name, CWorld world);
    
    /**
     * Give me an instance of your server implementation!
     * @param server
     */
    public static void setServer(CServer server) {
        instance = server;
    }
    /**
     * Return the current server instance
     * @return
     */
    public static CServer getServer() {
        return instance;
    }
    
    /**
     * Retrieve the id of an item by its name
     * @param itemName
     * @return
     */
    public abstract int getItemId(String itemName);
    
    /**
     * Retrieve the name of an item by its id
     * @param id
     * @return
     */
    public abstract String getItemName(int id);
    
    /**
     * Get the number of online players
     * @return
     */
    public abstract int getPlayersOnline();
    
    /**
     * Get the number of max. allowed players on the server
     * @return
     */
    public abstract int getMaxPlayers();
}
