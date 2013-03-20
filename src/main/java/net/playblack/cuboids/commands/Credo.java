package net.playblack.cuboids.commands;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.blockoperators.GenericGenerator;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.ToolBox;

/**
 * Redo things
 * 
 * @author Chris
 * 
 */
public class Credo extends CBaseCommand {

    public Credo() {
        super("Redo block operations:" + ColorManager.Yellow + " /credo [steps] [player]", 1, 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!player.hasPermission("cWorldMod")) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }
        if (!Config.get().isAllowUndo()) {
            MessageSystem.failMessage(player, "undoDisabled");
            return; // from a morality standpoint, this should never be disabled
                    // but there you go.
        }
        int steps = 1;
        String subject = player.getName();

        if (command.length == 3) {
            subject = command[2];
            steps = ToolBox.parseInt(command[1]);
            if (steps == -1) {
                steps = 1;
            }
        } else if (command.length == 2) {
            steps = ToolBox.parseInt(command[1]);
            if (steps < 1) {
                steps = 1;
            }
        }

        for (int i = 0; i < steps; i++) {
            CuboidSelection sel = SessionManager.getInstance()
                    .getPlayerHistory(subject).redo();
            if (sel == null) {
                MessageSystem.yellowNote(player, "allRedone");
                // ms.successMessage(player, "redoDone");
                return;
            }
            GenericGenerator gen = new GenericGenerator(sel, player.getWorld());
            try {
                gen.execute(player, false);
            } catch (BlockEditLimitExceededException e) {
                Debug.logWarning(e.getMessage());
                MessageSystem.customFailMessage(player, e.getMessage());
                e.printStackTrace();
            } catch (SelectionIncompleteException e) {
                MessageSystem.failMessage(player, "selectionIncomplete");
            }
        }

        MessageSystem.successMessage(player, "redoDone");
    }
}
