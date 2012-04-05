package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blockoperators.CuboidGenerator;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;

/**
 * Replace blocks in a cuboid selection
 * @author Chris
 *
 */
public class Creplace extends CBaseCommand {

    public Creplace() {
        super("Replace blocks in a selection: /creplace <block>:[data] <substitute>:[data]", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        //Check for the proper permissions
        MessageSystem ms = MessageSystem.getInstance();
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!player.hasPermission("cWorldMod")) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }
        
      //create a new template block
        CBlock b = CBlock.parseBlock(command[1]);
        CBlock sub = CBlock.parseBlock(command[2]);
        if((b == null) || (sub == null)) {
            ms.failMessage(player, "invalidBlock");
            return;
        }
        
        //prepare the selection
        CuboidSelection template = SelectionManager.getInstance().getPlayerSelection(player.getName());
        if(!template.getBlockList().isEmpty()) {
            template.clearBlocks();
        }
        
        //Create the block generator
        CuboidGenerator gen = new CuboidGenerator(template, player.getWorld());
        gen.setReplace(true);
        gen.setBlock(sub);
        gen.setBlockToReplace(b);
        if(gen.execute(player, false)) {
            ms.successMessage(player, "selectionReplaced");
        }
        else {
            ms.failMessage(player, "selectionIncomplete");
            ms.failMessage(player, "selectionNotReplaced");
        }
        return;
    }
}
