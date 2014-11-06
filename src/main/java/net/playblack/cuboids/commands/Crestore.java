package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blockoperators.GenericGenerator;
import net.playblack.cuboids.datasource.CuboidDeserializer;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Debug;

import java.io.File;

/**
 * Re an area
 *
 * @author Chris
 */
public class Crestore extends CBaseCommand {

    public Crestore() {
        super("Restore a cuboid area from backup:" + ColorManager.Yellow + " /crestore <area>", 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        String world = player.getWorld().getFilePrefix();
        File f = new File("plugins/cuboids2/backups/blocks_" + world + "_" + command[1]);
        if (f.exists()) {
            CuboidDeserializer des = new CuboidDeserializer(command[1], world);
            CuboidSelection restore = des.convert();
            GenericGenerator gen = new GenericGenerator(restore, player.getWorld());

            boolean success;
            try {
                success = gen.execute(player, true);
                if (success) {
                    MessageSystem.successMessage(player, "restoreSuccess");
                }
                else {
                    MessageSystem.failMessage(player, "restoreFail");
                }
            }
            catch (BlockEditLimitExceededException e) {
                Debug.logWarning(e.getMessage());
                MessageSystem.customFailMessage(player, e.getMessage());
                e.printStackTrace();
            }
            catch (SelectionIncompleteException e) {
                MessageSystem.failMessage(player, "selectionIncomplete");
            }

        }
        else {
            MessageSystem.failMessage(player, "restoreFail");
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
        }
    }
}
