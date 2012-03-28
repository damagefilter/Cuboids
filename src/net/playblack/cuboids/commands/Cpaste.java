package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.blockoperators.VectorOffsetGenerator;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;


/**
 * Paste a selection from clipboard into the world
 * @author Chris
 *
 */
public class Cpaste extends BaseCommand {

    public Cpaste() {
        super("Paste a selection from clipboard relative to your position: /cpaste", 1);
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
        
        CuboidSelection sel = SessionManager.getInstance().getClipboard(player.getName());
        VectorOffsetGenerator gen = new VectorOffsetGenerator(sel, player.getWorld());
        gen.setOffsetVector(player.getPosition());
        if(gen.execute(player, true)) {
            ms.successMessage(player, "selectionPasted");
        }
        else {
            ms.failMessage(player, "selectionNotPasted");
        }
    }
}
