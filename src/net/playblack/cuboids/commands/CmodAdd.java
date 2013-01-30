package net.playblack.cuboids.commands;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Cuboid;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Add a new Cuboid
 * 
 * @author Chris
 * 
 */
public class CmodAdd extends CBaseCommand {

    public CmodAdd() {
        super("Add a new Cuboid: " + ColorManager.Yellow
                + "/cmod <area> add/create", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        MessageSystem ms = MessageSystem.getInstance();
        if (!player.hasPermission("cIgnoreRestrictions")) {
            if (!player.hasPermission("ccreate")) {
                ms.failMessage(player, "permissionDenied");
                return;
            }
        }
        if (command[1].matches("[,:]")) {
            ms.failMessage(player, "invalidCharacters");
            ms.failMessage(player, "cuboidNotCreated");
            return;
        }
        Cuboid defaultC = Config.getInstance().getDefaultCuboidSetting(player);
        CuboidSelection selection = SelectionManager.getInstance()
                .getPlayerSelection(player.getName());
        if (!selection.isComplete()) {
            ms.failMessage(player, "selectionIncomplete");
            return;
        }
        defaultC.setBoundingBox(selection.getOrigin(), selection.getOffset());
        defaultC.setName(command[1]);
        defaultC.setWorld(player.getWorld().getName());
        defaultC.setDimension(player.getWorld().getDimension());
        defaultC.addPlayer("o:" + player.getName());
        if (CuboidInterface.get().addCuboid(defaultC)) {
            ms.successMessage(player, "cuboidCreated");
        } else {
            ms.failMessage(player, "cuboidExists");
            ms.failMessage(player, "cuboidNotCreated");
        }
    }
}
