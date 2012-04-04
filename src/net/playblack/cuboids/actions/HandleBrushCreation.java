package net.playblack.cuboids.actions;

import net.playblack.cuboids.blockoperators.SphereGenerator;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.PlayerSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.Vector;

public class HandleBrushCreation {
    /**
     * Handle brush action, make undo etc and execute in world
     * @param player
     * @param point
     */
    public static void handleBrush(CPlayer player, Vector point) {
        //creates a 0,0 data brush with radius of 3
        if((player.hasPermission("cWorldMod") && player.hasPermission("cbrush")) || player.hasPermission("cIgnoreRestrictions")) {
            PlayerSelection selection = SelectionManager.getInstance().getPlayerSelection(player.getName());
            SphereGenerator gen = new SphereGenerator(selection, player.getWorld());
            gen.setRadius(selection.getBrushRadius());
            gen.setMaterial(new CBlock(selection.getBrushType(), selection.getBrushData()));
            gen.setHollow(false);
            gen.execute(player, true);
        }
    }
}
