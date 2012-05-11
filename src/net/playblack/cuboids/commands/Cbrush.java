package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.PlayerSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.ToolBox;

/**
 * Set the brush properties
 * @author Chris
 *
 */
public class Cbrush extends CBaseCommand {

    public Cbrush() {
        super("Set the property of a brush:"+ColorManager.Yellow+" /cbrush <radius> <block>:[data]", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        MessageSystem ms = MessageSystem.getInstance();
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!(player.hasPermission("cWorldMod") && player.hasPermission("cbrush"))) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }
        
        SelectionManager selectionManager = SelectionManager.getInstance();
        //command, radius, type
        int radius = ToolBox.parseInt(command[1]);
        if(radius < 0) {
            ms.failMessage(player, "invalidRadius");
            return;
        }
        CBlock block = CBlock.parseBlock(command[2]);
        PlayerSelection selection = selectionManager.getPlayerSelection(player.getName());
        selection.setBrushData(block.getData());
        selection.setBrushRadius(radius);
        selection.setBrushType(block.getType());
        ms.successMessage(player, "brushSet");
    }
}
