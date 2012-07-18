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
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.ToolBox;

/**
 * Create discs or circles around a point
 * 
 * @author Chris
 * 
 */
public class Cdisc extends CBaseCommand {

    private boolean fill;

    public Cdisc(String variant) {
        super("Create disc/circle: " + ColorManager.Yellow + variant
                + " <radius> <block>:[data] [height]", 3, 4);
        if (variant.equalsIgnoreCase("/ccircle")) {
            fill = false;
        } else {
            fill = true;
        }
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
        CBlock material = CBlock.parseBlock(command[2]);
        int height;
        int radius;
        if (material == null) {
            ms.failMessage(player, "invalidBlock");
            return;
        }
        if (command.length == 4) {
            height = ToolBox.parseInt(command[3]);
            radius = ToolBox.parseInt(command[1]);
            if (height == -1) {
                ms.failMessage(player, "invalidHeight");
                return;
            }
            if (radius == -1) {
                ms.failMessage(player, "invalidRadius");
                return;
            }
        } else {
            height = 1;
            radius = ToolBox.parseInt(command[1]);
            if (radius == -1) {
                ms.failMessage(player, "invalidRadius");
                return;
            }
        }

        // prepare the selection
        CuboidSelection template = SelectionManager.getInstance()
                .getPlayerSelection(player.getName());
        if (template.getOrigin() == null) {
            ms.failMessage(player, "originNotSet");
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
                    ms.successMessage(player, "discCreated");
                } else {
                    ms.successMessage(player, "circleCreated");
                }
            } else {
                ms.failMessage(player, "selectionIncomplete");
                ms.failMessage(player, "discNotCreated");
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
