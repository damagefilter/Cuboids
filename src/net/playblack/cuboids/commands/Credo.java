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
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.ToolBox;

/**
 * Redo things
 * 
 * @author Chris
 * 
 */
public class Credo extends CBaseCommand {

    public Credo() {
        super("Redo block operations:" + ColorManager.Yellow
                + " /credo [steps] [player]", 1, 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        MessageSystem ms = MessageSystem.getInstance();
        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!player.hasPermission("cWorldMod")) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }
        if (!Config.getInstance().isAllowUndo()) {
            ms.failMessage(player, "undoDisabled");
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
                ms.notification(player, "Nothing lef to redo!");
                // ms.successMessage(player, "redoDone");
                return;
            }
            GenericGenerator gen = new GenericGenerator(sel, player.getWorld());
            try {
                gen.execute(player, false);
            } catch (BlockEditLimitExceededException e) {
                EventLogger.getInstance().logMessage(e.getMessage(), "WARNING");
                ms.customFailMessage(player, e.getMessage());
                e.printStackTrace();
            } catch (SelectionIncompleteException e) {
                MessageSystem.getInstance().failMessage(player,
                        "selectionIncomplete");
            }
        }

        ms.successMessage(player, "redoDone");
    }
}
