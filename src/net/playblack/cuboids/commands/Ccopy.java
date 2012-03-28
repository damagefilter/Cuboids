package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.blockoperators.GenericGenerator;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;


/**
 * Copy a selection into the clipboard
 * @author Chris
 *
 */
public class Ccopy extends BaseCommand {

    public Ccopy() {
        super("Copy a selection into your clipboard: /ccopy", 1);
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
        
        SelectionManager selectionManager = SelectionManager.getInstance();
        CuboidSelection sel = selectionManager.getPlayerSelection(player.getName());
        GenericGenerator gen = new GenericGenerator(sel, player.getWorld());
        gen.getWorldContent(sel);
        sel.setOrigin(player.getPosition());
        SessionManager.getInstance().setClipboard(player.getName(), sel);
        ms.successMessage(player, "copiedToClipboard");
    }
}
