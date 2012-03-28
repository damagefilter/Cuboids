package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blockoperators.OffsetGenerator;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;


/**
 * Move the contents of a selection, leaving empty space.
 * @author Chris
 *
 */
public class Cmove extends BaseCommand {

    public Cmove() {
        super("Move the contents of a selection: /cmove <distance> <NORTH/EAST/SOUTH/WEST/UP/DOWN>", 1);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        MessageSystem ms = MessageSystem.getInstance();
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!player.hasPermission("cWorldMod")) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }
        CuboidSelection origin = SelectionManager.getInstance().getPlayerSelection(player.getName());
        CuboidSelection offset = new CuboidSelection(origin.getOrigin(), origin.getOffset(), origin.getBlockList());
        OffsetGenerator gen = new OffsetGenerator(offset, player.getWorld());
        if(!gen.setDirection(command[2])) {
            ms.failMessage(player, "invalidCardinalDirection");
            return;
        }
        boolean result = gen.execute(player, true);
        if(result) {
            ms.successMessage(player, "selectionMoved");
        }
        else {
            ms.failMessage(player, "selectionIncomplete");
            ms.failMessage(player, "selectionNotMoved");
        }
    }
}
