package net.playblack.cuboids.actions.operators;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.ArmSwingEvent;
import net.playblack.cuboids.blockoperators.SphereGenerator;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.PlayerSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.LineBlockTracer;
import net.playblack.mcutils.Vector;

public class BrushOperator implements ActionListener {
    /**
     * Handle brush action, make undo etc and execute in world
     * 
     * @param player
     * @param point
     */
    public void handleBrush(CPlayer player, Vector point) {
        if (player.getItemInHand().getId() == Config.get().getSculptItem()) {
            if ((player.hasPermission("cWorldMod") && player.hasPermission("cbrush")) || player.hasPermission("cuboids.super.admin")) {
                PlayerSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
                selection.setOrigin(point);
                SphereGenerator gen = new SphereGenerator(selection,player.getWorld());
                gen.setRadius(selection.getBrushRadius());
                gen.setMaterial(new CBlock(selection.getBrushType(), selection.getBrushData()));
                gen.setHollow(true);
                try {
                    gen.execute(player, true);
                } catch (BlockEditLimitExceededException e) {
                    Debug.logWarning(e.getMessage());
                    MessageSystem.customFailMessage(player, e.getMessage());
                    e.printStackTrace();
                } catch (SelectionIncompleteException e) {
                    MessageSystem.failMessage(player, "selectionIncomplete");
                }
            }
        }
    }
    
    @ActionHandler
    public void onArmSwing(ArmSwingEvent event) {
        Vector v = new LineBlockTracer(event.getPlayer()).getTargetVector();
        if(v != null) {
            handleBrush(event.getPlayer(), v);
        }
    }
    
    static {
        ActionManager.registerActionListener("Cuboids2", new BrushOperator());
    }
}
