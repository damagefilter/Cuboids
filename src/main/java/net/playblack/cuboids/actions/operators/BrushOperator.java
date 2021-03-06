package net.playblack.cuboids.actions.operators;

import net.canarymod.LineTracer;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.Permissions;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.generators.SphereGenerator;
import net.playblack.cuboids.selections.PlayerSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.Debug;

public class BrushOperator {
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
            if ((player.hasPermission(Permissions.EDIT$WORLD) && player.hasPermission(Permissions.BRUSH$USE)) || player.hasPermission(Permissions.ADMIN)) {
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

    public void onArmSwing(Player player) {
        Block target = new LineTracer(player).getTargetBlock();
        if (target != null) {
            handleBrush(player, target.getLocation());
        }
    }
}
