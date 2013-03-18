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
import net.playblack.mcutils.Debug;

/**
 * Replace blocks in a cuboid selection
 * 
 * @author Chris
 * 
 */
public class Creplace extends CBaseCommand {

    public Creplace() {
        super("Replace blocks in a selection:" + ColorManager.Yellow
                + " /creplace <block>:[data] <substitute>:[data]", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        // Check for the proper permissions
        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!player.hasPermission("cWorldMod")) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }

        // create a new template block
        CBlock b = CBlock.parseBlock(command[1]);
        CBlock sub = CBlock.parseBlock(command[2]);
        if ((b == null) || (sub == null)) {
            MessageSystem.failMessage(player, "invalidBlock");
            return;
        }

        // prepare the selection
        CuboidSelection template = SelectionManager.get()
                .getPlayerSelection(player.getName());
        if (!template.getBlockList().isEmpty()) {
            template.clearBlocks();
        }

        // Create the block generator
        CuboidGenerator gen = new CuboidGenerator(template, player.getWorld());
        gen.setReplace(true);
        gen.setBlock(sub);
        gen.setBlockToReplace(b);
        try {
            if (gen.execute(player, true)) {
                MessageSystem.successMessage(player, "selectionReplaced");
            } else {
                MessageSystem.failMessage(player, "selectionIncomplete");
                MessageSystem.failMessage(player, "selectionNotReplaced");
            }
        } catch (BlockEditLimitExceededException e) {
            Debug.logWarning(e.getMessage());
            MessageSystem.customFailMessage(player, e.getMessage());
            e.printStackTrace();
        } catch (SelectionIncompleteException e) {
            MessageSystem.failMessage(player, "selectionIncomplete");
        }
        return;
    }
}
