package net.playblack.cuboids.actions.deprecated;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blockoperators.SphereGenerator;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.PlayerSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.Vector;

public class BrushHandler {
    /**
     * Handle brush action, make undo etc and execute in world
     * 
     * @param player
     * @param point
     */
    public static void handleBrush(CPlayer player, Vector point) {
        if (player.getItemInHand().getId() == Config.get()
                .getSculptItem()) {
            if ((player.hasPermission("cWorldMod") && player
                    .hasPermission("cbrush"))
                    || player.hasPermission("cIgnoreRestrictions")) {
                PlayerSelection selection = SelectionManager.getInstance()
                        .getPlayerSelection(player.getName());
                selection.setOrigin(point);
                SphereGenerator gen = new SphereGenerator(selection,
                        player.getWorld());
                gen.setRadius(selection.getBrushRadius());
                gen.setMaterial(new CBlock(selection.getBrushType(), selection
                        .getBrushData()));
                gen.setHollow(true);
                try {
                    gen.execute(player, true);
                } catch (BlockEditLimitExceededException e) {
                    EventLogger.getInstance().logMessage(e.getMessage(),
                            "WARNING");
                    MessageSystem.getInstance().customFailMessage(player,
                            e.getMessage());
                    e.printStackTrace();
                } catch (SelectionIncompleteException e) {
                    MessageSystem.getInstance().failMessage(player,
                            "selectionIncomplete");
                }
            }
        }
    }
}
