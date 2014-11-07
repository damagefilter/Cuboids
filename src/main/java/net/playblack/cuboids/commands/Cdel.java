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

/**
 * Remove blocks in a cuboid selection
 *
 * @author Chris
 */
public class Cdel extends CBaseCommand {

    public Cdel() {
        super("Remove contents of a selection:" + ColorManager.Yellow + " /cdel", 1);
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
        BlockType b = BlockType.Air;
        // prepare the selection
        CuboidSelection template = SelectionManager.get().getPlayerSelection(player.getName());
        if (!template.getBlockList().isEmpty()) {
            template.clearBlocks();
        }

        // Create the block generator
        CuboidGenerator gen = new CuboidGenerator(template, player.getWorld());
        gen.setBlock(b);
        try {
            if (gen.execute(player, false)) {
                MessageSystem.successMessage(player, "selectionDeleted");
            }
            else {
                MessageSystem.failMessage(player, "selectionIncomplete");
                MessageSystem.failMessage(player, "selectionNotDeleted");
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
