package net.playblack.cuboids.actions.operators;

import net.canarymod.LineTracer;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.ArmSwingEvent;
import net.playblack.cuboids.generators.SphereGenerator;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.selections.PlayerSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.Debug;

public class BrushOperator implements ActionListener {
    /**
     * Handle brush action, make undo etc and execute in world
     *
     * @param player
     * @param point
     */
    public void handleBrush(Player player, Vector3D point) {
        Item i = player.getItemHeld();
        if (i == null) {
            return;
        }

        if (Config.get().getSculptItem().equals(i.getType().getMachineName())) {
            if ((player.hasPermission("cuboids.worldmod") && player.hasPermission("cuboids.cbrush")) || player.hasPermission("cuboids.super.admin")) {
                PlayerSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
                selection.setOrigin(point);
                SphereGenerator gen = new SphereGenerator(selection, player.getWorld());
                gen.setRadius(selection.getBrushRadius());
                gen.setMaterial(selection.getBrushType());
                gen.setHollow(true);
                try {
                    gen.execute(player, true);
                }
                catch (BlockEditLimitExceededException e) {
                    Debug.logWarning(e.getMessage());
                    MessageSystem.customFailMessage(player, e.getMessage());
                    e.printStackTrace();
                }
                catch (SelectionIncompleteException e) {
                    MessageSystem.failMessage(player, "selectionIncomplete");
                }
            }
        }
    }

    @ActionHandler
    public void onArmSwing(ArmSwingEvent event) {
        Block target = new LineTracer(event.getPlayer()).getTargetBlock();
        if (target != null) {
            Vector3D v = new Vector3D(target.getPosition());
            handleBrush(event.getPlayer(), v);
        }
    }

    static {
        ActionManager.registerActionListener("Cuboids", new BrushOperator());
    }
}
