package net.playblack.cuboids.actions.operators;

import net.canarymod.LineTracer;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.Permissions;
import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.ArmSwingEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockLeftClickEvent;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
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
    private boolean explainRegion(Player player, Location p) {
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
     * @param location
     * @param rightclick this must be set=true on rightclick events for doubleAction mode.
     */
    private boolean setSelectionPoint(Player player, Location location, boolean rightclick, boolean remote) {
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

        if (!player.hasPermission(Permissions.ADMIN)) {
            if (!player.hasPermission(Permissions.SELECTION$CREATE)) {
                return false;
            }
        }
        if (Config.get().isUseDoubleAction()) {
//            System.out.println("selection in normal mode");
            setPointNormalStyle(player, location, rightclick);
        }
        else {
            System.out.println("selection in classic mode");
            setPointClassicStyle(player, location);
        }
        return true;
    }

    private void setPointClassicStyle(Player player, Location point) {
        CuboidSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
        if (selection.isComplete()) {
            selection.reset();
        }
        if (!selection.hasOrigin() && !selection.hasOffset()) {
            selection.setOrigin(point);
            MessageSystem.successMessage(player, "originSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.toString());
        }
        else if (selection.hasOrigin() && !selection.hasOffset()) {
            selection.setOffset(point);
            MessageSystem.successMessage(player, "offsetSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.toString());
        }
        else if (!selection.hasOrigin() && selection.hasOffset()) {
            selection.reset();
            selection.setOrigin(point);
            MessageSystem.successMessage(player, "originSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.toString());
        }
        else {
            //and this is so unlikely to happen - but just in case some derp manages it
            selection.reset();
            selection.setOrigin(point);
            MessageSystem.successMessage(player, "originSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.toString());
        }

    }

    private void setPointNormalStyle(Player player, Location point, boolean rightClick) {
        CuboidSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
        if (rightClick) {
            selection.setOffset(point);
            MessageSystem.successMessage(player, "offsetSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.toString());
        }
        else {
            selection.setOrigin(point);
            MessageSystem.successMessage(player, "originSet");
            MessageSystem.customMessage(player, ColorManager.Gray, point.toString());
        }
    }

    // *******************************
    // Listener creation stuff
    // *******************************

    /**
     * Returns true if the event should be canceled.
     * Does some things depending on item in hand
     *
     * @param player
     * @param location
     * @return
     */
    public boolean onBlockRightClick(Player player, Location location) {
//        System.out.println("Rightclick selection");
        Item i = player.getItemHeld();
        if (i != null) {
            if (Config.get().getRegionItem().equals(i.getType().getMachineName())) {
                if (setSelectionPoint(player, location, true, false)) {
                    return true;
                }
            }
            if (Config.get().getInspectorItem().equals(i.getType().getMachineName())) {
                explainRegion(player, location);
                return true;
            }
        }
        return false;
    }

    public void onArmSwing(Player player) {
        Block v = new LineTracer(player).getTargetBlock();
        if (v == null) {
            return;
        }
        setSelectionPoint(player, v.getLocation(), false, true);
    }

    @ActionHandler
    public void onBlockLeftClick(BlockLeftClickEvent event) {
        if (setSelectionPoint(event.getPlayer(), event.getLocation(), false, false)) {
            event.cancel();
        }
    }

    public boolean onBlockLeftClick(Player player, Location location) {
        if (setSelectionPoint(player, location, false, false)) {
            return true;
        }
        return false;
    }

    //Register that thing
    static {
        ActionManager.registerActionListener("Cuboids", new SelectionOperator());
    }
}
