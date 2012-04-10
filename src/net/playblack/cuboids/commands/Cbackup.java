package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.blockoperators.GenericGenerator;
import net.playblack.cuboids.datasource.CuboidSerializer;
import net.playblack.cuboids.datasource.FlatFileSerializer;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.cuboids.selections.CuboidSelection;


/**
 * Backup an area
 * @author Chris
 *
 */
public class Cbackup extends CBaseCommand {

    public Cbackup() {
        super("Backup a cuboid area: /cbackup <area>", 2);
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
        CuboidE node = RegionManager.getInstance().getCuboidNodeByName(command[1], player.getWorld().getName(), player.getWorld().getDimension()).getCuboid();
        if(node.playerIsOwner(player.getName()) || player.hasPermission("cAreaMod")) {
            GenericGenerator gen = new GenericGenerator(new CuboidSelection(node.getFirstPoint(), node.getSecondPoint()), player.getWorld());
            CuboidSelection tmp = new CuboidSelection(node.getFirstPoint(), node.getSecondPoint()); 
            tmp = gen.getWorldContent(tmp);
            CuboidSerializer ser = new FlatFileSerializer(tmp);
            ser.save(command[1], player.getWorld().getFilePrefix());
            ms.successMessage(player, "backupSuccess");
        }
        else {
            ms.failMessage(player, "playerNotOwner");
        }
    }
}
