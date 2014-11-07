package net.playblack.cuboids.actions.operators;

import net.canarymod.LineTracer;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.blocks.Block;
import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.ArmSwingEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockLeftClickEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockRightClickEvent;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.CLocation;
import net.playblack.mcutils.ColorManager;

/**
 * Handles native selection operations
 *
 * @author chris
 */
public class SelectionOperator implements ActionListener {

    /**
     * Explain the current region
     *
     * @param player
     * @param p
     * @return
     */
    private boolean explainRegion(Player player, CLocation p) {
        Item item = player.getItemHeld();
        if (item == null) {
            return false;
        }
        if (!Config.get().getInspectorItem().equals(item.getType().getMachineName())) {
            return false;
        }

        CuboidInterface.get().explainRegion(player, p, true);
        return true;
    }

    /**
     * Handle the selection of points of a selection.
     *
     * @param player
     * @param CLocation
     * @param rightclick this must be set=true on rightclick events for doubleAction mode.
     */
    private boolean setSelectionPoint(Player player, CLocation CLocation, boolean rightclick, boolean remote) {
        Item item = player.getItemHeld();
        if (item == null) {
            return false;
        }
        if (remote) {

            if (!item.getType().getMachineName().equals(Config.get().getRemoteRegionItem())) {
                return false;
            }
//            System.out.println("Remote selection");
        }
        else {
            if (!item.getType().getMachineName().equals(Config.get().getRegionItem())) {
                return false;
            }
        }

        if (!player.hasPermission("cuboids.super.admin")) {
            if (!player.hasPermission("cselect")) {
                return false;
            }
        }
        if (Config.get().isUseDoubleAction()) {
//            System.out.println("selection in normal mode");
            setPointNormalStyle(player, CLocation, rightclick);
        }
        else {
            System.out.println("selection in classic mode");
            setPointClassicStyle(player, CLocation);
        }
        return true;
    }

    private void setPointClassicStyle(Player player, CLocation point) {
        CuboidSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
        if (selection.isComplete()) {
            selection.reset();
        }
        if (!selection.hasOrigin() && !selection.hasOffset()) {
            selection.setOrigin(point);
            MessageSystem.successMessage(player, "originSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.explain());
        }
        else if (selection.hasOrigin() && !selection.hasOffset()) {
            selection.setOffset(point);
            MessageSystem.successMessage(player, "offsetSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.explain());
        }
        else if (!selection.hasOrigin() && selection.hasOffset()) {
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

    private void setPointNormalStyle(Player player, CLocation point, boolean rightClick) {
        CuboidSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
        if (rightClick) {
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

    @ActionHandler
    public void onBlockRightClick(BlockRightClickEvent event) {
//        System.out.println("Rightclick selection");
        Item i = event.getPlayer().getItemHeld();
        if (i != null) {
            if (Config.get().getRegionItem().equals(i.getType().getMachineName())) {
                if (setSelectionPoint(event.getPlayer(), event.getLocation(), true, false)) {
                    event.cancel();
                }
            }
        }
        explainRegion(event.getPlayer(), event.getLocation());
    }

    @ActionHandler
    public void onArmSwing(ArmSwingEvent event) {
        Block v = new LineTracer(event.getPlayer()).getTargetBlock();
        if (v == null) {
            return;
        }
        CLocation loc = new CLocation(v.getLocation());
        setSelectionPoint(event.getPlayer(), loc, false, true);
    }

    @ActionHandler
    public void onBlockLeftClick(BlockLeftClickEvent event) {
        if (setSelectionPoint(event.getPlayer(), event.getLocation(), false, false)) {
            event.cancel();
        }
    }

    //Register that thing
    static {
        ActionManager.registerActionListener("Cuboids", new SelectionOperator());
    }
}
