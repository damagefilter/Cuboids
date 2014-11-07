package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.BlockType;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.selections.PlayerSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.ToolBox;

/**
 * Set the brush properties
 *
 * @author Chris
 */
public class Cbrush extends CBaseCommand {

    public Cbrush() {
        super("Set the property of a brush:" + ColorManager.Yellow + " /cbrush <radius> <block>:[data]", 3);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        if (!player.hasPermission("cuboids.super.admin")) {
            if (!(player.hasPermission("WorldMod") && player.hasPermission("cbrush"))) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }

        SelectionManager selectionManager = SelectionManager.get();
        // command, radius, type
        int radius = ToolBox.parseInt(command[1]);
        if (radius < 0) {
            MessageSystem.failMessage(player, "invalidRadius");
            return;
        }
        BlockType block = ToolBox.parseBlock(command[2]);
        PlayerSelection selection = selectionManager.getPlayerSelection(player.getName());
        selection.setBrushRadius(radius);
        selection.setBrushType(block);
        MessageSystem.successMessage(player, "brushSet");
    }
}
