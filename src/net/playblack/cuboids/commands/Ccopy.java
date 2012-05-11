package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.blockoperators.GenericGenerator;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.EventLogger;


/**
 * Copy a selection into the clipboard
 * @author Chris
 *
 */
public class Ccopy extends CBaseCommand {

    public Ccopy() {
        super("Copy a selection into your clipboard: "+ColorManager.Yellow+" /ccopy", 1);
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
        try {
            sel = gen.getWorldContent(sel);
        } catch (BlockEditLimitExceededException e) {
            EventLogger.getInstance().logMessage(e.getMessage(), "WARNING");
            ms.customFailMessage(player, e.getMessage());
            e.printStackTrace();
        } catch (SelectionIncompleteException e) {
            MessageSystem.getInstance().failMessage(player, "selectionIncomplete");
        }
        sel.setOrigin(player.getPosition());
        //gen.
        SessionManager.getInstance().setClipboard(player.getName(), sel);
        ms.successMessage(player, "copiedToClipboard");
    }
}
