package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blockoperators.GenericGenerator;
import net.playblack.cuboids.datasource.CuboidSerializer;
import net.playblack.cuboids.datasource.FlatFileSerializer;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.Debug;

/**
 * Backup an area
 * 
 * @author Chris
 * 
 */
public class Cbackup extends CBaseCommand {

    public Cbackup() {
        super("Backup a cuboid area: " + ColorManager.Yellow
                + "/cbackup <area>", 2);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!player.hasPermission("cbackup")) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }
        Region node = RegionManager.get().getRegionByName(command[1], player.getWorld().getName(), player.getWorld().getDimension());
        if (node.playerIsOwner(player.getName()) || player.hasPermission("cAreaMod")) {
            GenericGenerator gen = new GenericGenerator(new CuboidSelection(node.getOrigin(), node.getOffset()), player.getWorld());
            
            CuboidSelection tmp = new CuboidSelection(node.getOrigin(), node.getOffset());
            
            try {
                tmp = gen.getWorldContent(tmp);
            } catch (BlockEditLimitExceededException e) {
                Debug.logWarning(e.getMessage());
                MessageSystem.customFailMessage(player, e.getMessage());
                e.printStackTrace();
                return;
            } catch (SelectionIncompleteException e) {
                MessageSystem.failMessage(player,
                        "selectionIncomplete");
                return;
            }
            CuboidSerializer ser = new FlatFileSerializer(tmp);
            ser.save(command[1], player.getWorld().getFilePrefix());
            MessageSystem.successMessage(player, "backupSuccess");
        } else {
            MessageSystem.failMessage(player, "playerNotOwner");
        }
    }
}
