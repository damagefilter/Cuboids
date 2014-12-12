package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.datasource.CuboidDeserializer;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.generators.RestoreRegionGenerator;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Debug;

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
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        CuboidDeserializer des = new CuboidDeserializer(command[1], player.getWorld());
        RestoreRegionGenerator gen = new RestoreRegionGenerator(des.getSelection(), des.getSignContents(), des.getChestContents(), player.getWorld());

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
}
