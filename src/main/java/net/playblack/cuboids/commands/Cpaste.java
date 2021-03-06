package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.Permissions;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.generators.VectorOffsetGenerator;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Debug;

/**
 * Paste a selection from clipboard into the world
 *
 * @author Chris
 */
public class Cpaste extends CBaseCommand {

    public Cpaste() {
        super("Paste a selection from clipboard relative to your position: " + ColorManager.Yellow + "/cpaste", 1);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        if (!player.hasPermission(Permissions.ADMIN)) {
            if (!player.hasPermission(Permissions.EDIT$WORLD)) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }

        CuboidSelection sel = SessionManager.get().getClipboard(player.getName());
        if (sel == null || !sel.isComplete()) {
            MessageSystem.failMessage(player, "clipboardEmpty");
            return;
        }
        VectorOffsetGenerator gen = new VectorOffsetGenerator(sel, player.getWorld());
        gen.setOffsetVector(new Vector3D(player.getPosition()));
        try {
            try {
                if (gen.execute(player, true)) {
                    MessageSystem.successMessage(player, "selectionPasted");
                }
                else {
                    MessageSystem.failMessage(player, "selectionNotPasted");
                }
            }
            catch (SelectionIncompleteException e) {
                MessageSystem.failMessage(player, "selectionIncomplete");
            }
        }
        catch (BlockEditLimitExceededException e) {
            Debug.logWarning(e.getMessage());
            MessageSystem.customFailMessage(player, e.getMessage());
            e.printStackTrace();
        }
    }
}
