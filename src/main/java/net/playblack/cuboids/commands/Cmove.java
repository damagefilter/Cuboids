package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blockoperators.OffsetGenerator;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.ToolBox;

/**
 * Move the contents of a selection, leaving empty space.
 * 
 * @author Chris
 * 
 */
public class Cmove extends CBaseCommand {

    public Cmove() {
        super("Move the contents of a selection: " + ColorManager.Yellow
                + "/cmove <distance> <NORTH/EAST/SOUTH/WEST/UP/DOWN>", 3);
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
        CuboidSelection origin = SelectionManager.get()
                .getPlayerSelection(player.getName());
        OffsetGenerator gen = new OffsetGenerator(origin, player.getWorld());
        if (!gen.setDirection(command[2])) {
            ms.failMessage(player, "invalidCardinalDirection");
            return;
        }
        int distance = ToolBox.parseInt(command[1]);
        if (distance < 0) {
            ms.failMessage(player, "invalidDistance");
            return;
        }
        gen.setDistance(distance);
        boolean result;
        try {
            result = gen.execute(player, true);
            if (result) {
                ms.successMessage(player, "selectionMoved");
            } else {
                ms.failMessage(player, "selectionIncomplete");
                ms.failMessage(player, "selectionNotMoved");
            }
        } catch (BlockEditLimitExceededException e) {
            EventLogger.getInstance().logMessage(e.getMessage(), "WARNING");
            ms.customFailMessage(player, e.getMessage());
            e.printStackTrace();
        } catch (SelectionIncompleteException e) {
            MessageSystem.getInstance().failMessage(player,
                    "selectionIncomplete");
        }

    }
}
