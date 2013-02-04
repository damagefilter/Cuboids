package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blockoperators.CuboidGenerator;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.EventLogger;

/**
 * Remove blocks in a cuboid selection
 * 
 * @author Chris
 * 
 */
public class Cdel extends CBaseCommand {

    public Cdel() {
        super("Remove contents of a selection:" + ColorManager.Yellow
                + " /cdel", 1);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        // Check for the proper permissions
        MessageSystem ms = MessageSystem.getInstance();
        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!player.hasPermission("cWorldMod")) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }

        // create a new template block
        CBlock b = new CBlock(0, 0);
        // prepare the selection
        CuboidSelection template = SelectionManager.getInstance()
                .getPlayerSelection(player.getName());
        if (!template.getBlockList().isEmpty()) {
            template.clearBlocks();
        }

        // Create the block generator
        CuboidGenerator gen = new CuboidGenerator(template, player.getWorld());
        gen.setBlock(b);
        try {
            if (gen.execute(player, false)) {
                ms.successMessage(player, "selectionDeleted");
            } else {
                ms.failMessage(player, "selectionIncomplete");
                ms.failMessage(player, "selectionNotDeleted");
            }
        } catch (BlockEditLimitExceededException e) {
            EventLogger.getInstance().logMessage(e.getMessage(), "WARNING");
            ms.customFailMessage(player, e.getMessage());
            e.printStackTrace();
        } catch (SelectionIncompleteException e) {
            MessageSystem.getInstance().failMessage(player,
                    "selectionIncomplete");
        }
        return;
    }
}
