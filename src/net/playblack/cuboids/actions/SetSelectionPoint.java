package net.playblack.cuboids.actions;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.Vector;

public class SetSelectionPoint {

    /**
     * Set points for a player selection
     * @param player
     * @param point
     * @param setOffset True to set offset instead of origin
     */
    public void execute(CPlayer player, Vector point, boolean setOffset) {
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!player.hasPermission("cselect")) {
                MessageSystem.getInstance().failMessage(player, "permissionDenied");
                return;
            }
        }
        CuboidSelection selection = SelectionManager.getInstance().getPlayerSelection(player.getName());
        if(setOffset) {
            selection.setOffset(point);
            MessageSystem.getInstance().yellowNote(player, "secondPointSet");
        }
        else {
            selection.setOrigin(point);
            MessageSystem.getInstance().yellowNote(player, "firstPointSet");
        }
    }
}
