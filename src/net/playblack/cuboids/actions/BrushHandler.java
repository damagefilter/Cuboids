package net.playblack.cuboids.actions;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.blockoperators.SphereGenerator;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.PlayerSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.Vector;

public class BrushHandler {
    /**
     * Handle brush action, make undo etc and execute in world
     * @param player
     * @param point
     */
    public static void handleBrush(CPlayer player, Vector point) {
        if(player.getItemInHand().getId() == Config.getInstance().getSculptItem()) {
            if((player.hasPermission("cWorldMod") && player.hasPermission("cbrush")) || player.hasPermission("cIgnoreRestrictions")) {
                PlayerSelection selection = SelectionManager.getInstance().getPlayerSelection(player.getName());
                selection.setOrigin(point);
                SphereGenerator gen = new SphereGenerator(selection, player.getWorld());
                gen.setRadius(selection.getBrushRadius());
                gen.setMaterial(new CBlock(selection.getBrushType(), selection.getBrushData()));
                gen.setHollow(true);
                gen.execute(player, true);
            }
        }
    }
}
