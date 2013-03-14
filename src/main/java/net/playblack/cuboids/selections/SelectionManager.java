package net.playblack.cuboids.selections;

import java.util.HashMap;

/**
 * Cuboid Selection Dispatcher.
 * 
 * @author Chris
 * 
 */
public class SelectionManager {
    private HashMap<String, ISelection> selections;

    private static SelectionManager instance = null;

    private SelectionManager() {
        selections = new HashMap<String, ISelection>();
    }

    public static SelectionManager get() {
        if (instance == null) {
            instance = new SelectionManager();
        }
        return instance;
    }

    /**
     * Returns the current player selection data.
     * This will never return null
     * @param player
     * @return
     */
    public PlayerSelection getPlayerSelection(String player) {
        if (selections.get(player) == null) {
            setPlayerSelection(player);
        }
        return (PlayerSelection) selections.get(player);
    }

    public void setPlayerSelection(String player) {
        selections.put(player, new PlayerSelection());
    }
}
