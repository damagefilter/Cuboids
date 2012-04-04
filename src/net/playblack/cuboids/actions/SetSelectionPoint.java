package net.playblack.cuboids.actions;

import java.util.HashMap;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.Vector;

public class SetSelectionPoint {

    private static HashMap<String, Boolean> setOffsetList = new HashMap<String, Boolean>();
    /**
     * Set points for a player selection
     * @param player
     * @param point
     * @param setOffset True to set offset instead of origin
     */
    public static void setFixedPoint(CPlayer player, Vector point) {
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!player.hasPermission("cselect")) {
               // MessageSystem.getInstance().failMessage(player, "permissionDenied");
                return;
            }
        }
        CuboidSelection selection = SelectionManager.getInstance().getPlayerSelection(player.getName());
        if(!setOffsetList.containsKey(player.getName())) {
            setOffsetList.put(player.getName(), Boolean.valueOf(false));
        }
        if(setOffsetList.get(player.getName())) {
            selection.setOffset(point);
            MessageSystem.getInstance().yellowNote(player, "secondPointSet");
            setOffsetList.put(player.getName(), Boolean.valueOf(false));
        }
        else {
            selection.setOrigin(point);
            MessageSystem.getInstance().yellowNote(player, "firstPointSet");
            setOffsetList.put(player.getName(), Boolean.valueOf(true));
        }
    }
}
