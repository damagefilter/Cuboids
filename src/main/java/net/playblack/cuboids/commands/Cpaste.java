package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.blockoperators.VectorOffsetGenerator;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Debug;

/**
 * Paste a selection from clipboard into the world
 * 
 * @author Chris
 * 
 */
public class Cpaste extends CBaseCommand {

    public Cpaste() {
        super("Paste a selection from clipboard relative to your position: " + ColorManager.Yellow + "/cpaste", 1);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        if (!player.hasPermission("cuboids.super.admin")) {
            if (!player.hasPermission("cWorldMod")) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }

        CuboidSelection sel = SessionManager.get().getClipboard(player.getName());
        if (sel == null || !sel.isComplete()) {
            MessageSystem.failMessage(player, "clipboardEmpty");
            return;
        }
        VectorOffsetGenerator gen = new VectorOffsetGenerator(sel,
                player.getWorld());
        gen.setOffsetVector(player.getPosition());
        try {
            try {
                if (gen.execute(player, true)) {
                    MessageSystem.successMessage(player, "selectionPasted");
                } else {
                    MessageSystem.failMessage(player, "selectionNotPasted");
                }
            } catch (SelectionIncompleteException e) {
                MessageSystem.failMessage(player, "selectionIncomplete");
            }
        } catch (BlockEditLimitExceededException e) {
            Debug.logWarning(e.getMessage());
            MessageSystem.customFailMessage(player, e.getMessage());
            e.printStackTrace();
        }
    }
}
