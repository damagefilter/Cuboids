package net.playblack.cuboids.commands;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.blockoperators.GenericGenerator;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.ToolBox;


/**
 * Redo things
 * @author Chris
 *
 */
public class Credo extends BaseCommand {

    public Credo() {
        super("Redo block operations: /credo <steps>", 1,2);
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
        if(!Config.getInstance().isAllowUndo()) {
            ms.failMessage(player, "undoDisabled");
            return; //from a morality standpoint, this should never be disabled but there you go.
        }
        int steps;
        if(command.length == 2) {
            steps = ToolBox.parseInt(command[1]);
            if(steps == -1) {
                steps = 1;
            }
        }
        else {
            steps = 1;
        }
        
        for(int i = 0; i<steps;i++) {
            CuboidSelection sel = SessionManager.getInstance().getPlayerHistory(player.getName()).redo();
            if(sel == null) {
                ms.notification(player, "Nothing more to redo recorded!");
                ms.successMessage(player, "redoDone");
                return;
            }
            GenericGenerator gen = new GenericGenerator(sel, player.getWorld());
            gen.execute(player, false);
        }
        
        ms.successMessage(player, "redoDone");
    }
}
