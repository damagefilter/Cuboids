package net.playblack.cuboids.actions.operators;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.ActionHandler.Priority;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.events.forwardings.ArmSwingEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockRightClickEvent;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.LineBlockTracer;
import net.playblack.mcutils.Location;

/**
 * Handles native selection operations
 * @author chris
 *
 */
public class SelectionOperator implements ActionListener {
    
    /**
     * Explain the current region
     * @param player
     * @param p
     * @return
     */
    private boolean explainRegion(CPlayer player, Location p) {
        if(player.getItemInHand().getId() != Config.get().getInspectorItem()) {
            return false;
        }
        
        CuboidInterface.get().explainRegion(player, p);
        return true;
    }
    
    /**
     * Handle the selection of points of a selection.
     * @param player
     * @param location
     * @param rightclick this must be set=true on rightclick events for doubleAction mode.
     * Has no effect on classic selection mode
     */
    private boolean setSelectionPoint(CPlayer player, Location location, boolean rightclick) {
        if(player.getItemInHand().getId() != Config.get().getRegionItem()) {
            return false;
        }
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!player.hasPermission("cselect")) {
                return false;
            }
        }
        if(Config.get().isUseDoubleAction()) {
            setPointNormalStyle(player, location, rightclick);
        }
        else {
            setPointClassicStyle(player, location);
        }
        return true;
    }
    
    private void setPointClassicStyle(CPlayer player, Location point) {
        CuboidSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
        if(selection.isComplete()) {
            selection.reset();
        }
        if(!selection.hasOrigin() && !selection.hasOffset()) {
            selection.setOrigin(point);
            MessageSystem.successMessage(player, "originSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.explain());
        }
        else if(selection.hasOrigin() && !selection.hasOffset()) {
            selection.setOffset(point);
            MessageSystem.successMessage(player, "offsetSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.explain());
        }
        else if(!selection.hasOrigin() && selection.hasOffset()) {
            selection.reset();
            selection.setOrigin(point);
            MessageSystem.successMessage(player, "originSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.explain());
        }
        else {
            //and this is so unlikely to happen - but just in case some derp manages it
            selection.reset();
            selection.setOrigin(point);
            MessageSystem.successMessage(player, "originSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.explain());
        }
        
    }
    
    private void setPointNormalStyle(CPlayer player, Location point, boolean rightClick) {
        CuboidSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
        if(rightClick) {
            selection.setOffset(point);
            MessageSystem.successMessage(player, "offsetSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.explain());
        }
        else {
            selection.setOrigin(point);
            MessageSystem.successMessage(player, "originSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.explain());
        }
    }
    
    // *******************************
    // Listener creation stuff
    // *******************************
    
    @ActionHandler(priority=Priority.MEDIUM)
    public void onBlockRightClick(BlockRightClickEvent event) {
        //Set seletion?
        if(setSelectionPoint(event.getPlayer(), event.getLocation(), true)) {
            event.cancel();
        }
        //Explain the region?
        if(explainRegion(event.getPlayer(), event.getLocation())) {
            event.cancel();
        }
    }
    
    @ActionHandler(priority = Priority.MEDIUM)
    public void onArmSwing(ArmSwingEvent event) {
        Location loc = new Location(new LineBlockTracer(event.getPlayer()).getTargetVector());
        setSelectionPoint(event.getPlayer(), loc, false);
    }
    
    //Register that thing
    static {
        ActionManager.registerActionListener("Cuboids2", new SelectionOperator());
    }
}
