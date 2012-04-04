package net.playblack.cuboids;

import java.util.HashMap;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.history.HistoryTimeline;
import net.playblack.cuboids.selections.CuboidSelection;

/**
 * Contains management stuff for managing edithistories for players, a player factory
 * and player clipboards that contain world data
 * @author Chris
 *
 */
public class SessionManager {
    private HashMap<String, HistoryTimeline> playerHistories = new HashMap<String, HistoryTimeline>(CServer.getServer().getMaxPlayers());
    private HashMap<String,CuboidSelection> playerClipboard = new HashMap<String, CuboidSelection>(CServer.getServer().getMaxPlayers());
    private HashMap<String,CPlayer> playerList = new HashMap<String, CPlayer>(CServer.getServer().getMaxPlayers());
    
    
    private static SessionManager instance = null;
    
    private SessionManager() {
        
    }
    
    public static SessionManager getInstance() {
        if(instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Get a player. If that player is not mapped yet, it will create a mapping.
     * This may return null if a player is really not existent
     * @param name
     * @return
     */
    public CPlayer getPlayer(String name) {
        if(playerList.get(name) == null) {
            playerList.put(name, CServer.getServer().getPlayer(name));
        }
        return playerList.get(name);
    }
    /**
     * Get the history timeline for a player
     * @param name
     * @return
     */
    public HistoryTimeline getPlayerHistory(String name) {
        return playerHistories.get(name);
    }
    
    /**
     * Set a new and empty timeline for this player
     * @param name
     */
    public void setHistoryTimeline(String name) {
        playerHistories.put(name, new HistoryTimeline());
    }
    
    /**
     * Get the current clipboard of the player 
     * @param name the player name
     * @return
     */
    public CuboidSelection getClipboard(String name) {
        return playerClipboard.get(name);
    }
    
    /**
     * Put the clipboard of this player
     * @param name
     */
    public void setClipboard(String name, CuboidSelection sel) {
        playerClipboard.put(name, sel);
    }
}
