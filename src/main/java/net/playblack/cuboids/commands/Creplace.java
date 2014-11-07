package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.BlockType;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blockoperators.CuboidGenerator;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.ToolBox;

/**
 * Replace blocks in a cuboid selection
 *
 * @author Chris
 */
public class Creplace extends CBaseCommand {

    public Creplace() {
        super("Replace blocks in a selection:" + ColorManager.Yellow + " /creplace <block>:[data] <substitute>:[data]", 3);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        // Check for the proper permissions
        if (!player.hasPermission("cuboids.super.admin")) {
            if (!player.hasPermission("WorldMod")) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }

        // create a new template block
        BlockType b = ToolBox.parseBlock(command[1]);
        BlockType sub = ToolBox.parseBlock(command[2]);
        if ((b == null) || (sub == null)) {
            MessageSystem.failMessage(player, "invalidBlock");
            return;
        }

        // prepare the selection
        CuboidSelection template = SelectionManager.get().getPlayerSelection(player.getName());
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
            }
            else {
                MessageSystem.failMessage(player, "selectionIncomplete");
                MessageSystem.failMessage(player, "selectionNotReplaced");
            }
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
