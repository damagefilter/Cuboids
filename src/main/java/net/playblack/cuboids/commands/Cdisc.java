package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blockoperators.DiscGenerator;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.ToolBox;

/**
 * Create discs or circles around a point
 *
 * @author Chris
 */
public class Cdisc extends CBaseCommand {

    private boolean fill;

    public Cdisc(String variant) {
        super("Create disc/circle: " + ColorManager.Yellow + variant + " <radius> <block>:[data] [height]", 3, 4);
        fill = !variant.equalsIgnoreCase("/ccircle");
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        // Check for the proper permissions
        if (!player.hasPermission("cuboids.super.admin")) {
            if (!player.hasPermission("cWorldMod")) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }

        // create a new template block
        CBlock material = CBlock.parseBlock(command[2]);
        int height;
        int radius;
        if (material == null) {
            MessageSystem.failMessage(player, "invalidBlock");
            return;
        }
        if (command.length == 4) {
            height = ToolBox.parseInt(command[3]);
            radius = ToolBox.parseInt(command[1]);
            if (height == -1) {
                MessageSystem.failMessage(player, "invalidHeight");
                return;
            }
            if (radius == -1) {
                MessageSystem.failMessage(player, "invalidRadius");
                return;
            }
        }
        else {
            height = 1;
            radius = ToolBox.parseInt(command[1]);
            if (radius == -1) {
                MessageSystem.failMessage(player, "invalidRadius");
                return;
            }
        }

        // prepare the selection
        CuboidSelection template = SelectionManager.get().getPlayerSelection(player.getName());
        if (template.getOrigin() == null) {
            MessageSystem.failMessage(player, "originNotSet");
            return;
        }
        if (!template.getBlockList().isEmpty()) {
            template.clearBlocks();
        }

        // Create the block generator
        DiscGenerator gen = new DiscGenerator(template, player.getWorld());
        gen.setMaterial(material);
        gen.setHeight(height);
        gen.setRadius(radius);
        gen.setHollow(fill);

        try {
            if (gen.execute(player, true)) {
                if (fill) {
                    MessageSystem.successMessage(player, "discCreated");
                }
                else {
                    MessageSystem.successMessage(player, "circleCreated");
                }
            }
            else {
                MessageSystem.failMessage(player, "selectionIncomplete");
                MessageSystem.failMessage(player, "discNotCreated");
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
