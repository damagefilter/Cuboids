package net.playblack.cuboids.commands;

import java.io.File;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blockoperators.GenericGenerator;
import net.playblack.cuboids.datasource.CuboidDeserializer;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;


/**
 * Re an area
 * @author Chris
 *
 */
public class Crestore extends BaseCommand {

    public Crestore() {
        super("Restore a cuboid area from backup: /crestore <area>", 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
        MessageSystem ms = MessageSystem.getInstance();
        if(!player.hasPermission("cIgnoreRestrictions")) {
            if(!player.hasPermission("cbackup")) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }
        String world = player.getWorld().getFqName();
        File f = new File("plugins/cuboids2/backups/blocks_"+world+"_"+command[1]);
        if(f.exists()) {
            CuboidDeserializer des = new CuboidDeserializer(command[1], world);
            CuboidSelection restore = des.convert();
            GenericGenerator gen = new GenericGenerator(restore, player.getWorld());
            
            boolean success = gen.execute(player, true);
            if(success) {
                ms.successMessage(player, "restoreSuccess");
                return;
            }
            else {
                ms.failMessage(player, "restoreFail");
                return;
            }
        }
        else {
            ms.failMessage(player, "restoreFail");
            ms.failMessage(player, "cuboidNotFoundOnCommand");
            return;
        }
    }
}
