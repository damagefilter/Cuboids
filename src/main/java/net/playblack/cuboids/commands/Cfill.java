package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.BlockType;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.Permissions;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.generators.CuboidGenerator;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.ToolBox;

/**
 * Fill a cuboid region
 *
 * @author Chris
 */
public class Cfill extends CBaseCommand {

    public Cfill() {
        super("Fill a selection: " + ColorManager.Yellow + "/cfill <block id>:[data]", 2);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        // Check for the proper permissions
        if (!player.hasPermission(Permissions.ADMIN)) {
            if (!player.hasPermission(Permissions.EDIT$WORLD)) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }

        // create a new template block
        BlockType b = ToolBox.parseBlock(command[1]);
        if (b == null) {
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
        gen.setBlock(b);
        try {
            if (gen.execute(player, true)) {
                MessageSystem.successMessage(player, "selectionFilled");
            }
            else {
                MessageSystem.failMessage(player, "selectionIncomplete");
                MessageSystem.failMessage(player, "selectionNotFilled");
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
