package net.playblack.cuboids.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.Config;
import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;

/**
 * Add a new Cuboid
 *
 * @author Chris
 */
public class CmodAdd extends CBaseCommand {

    public CmodAdd() {
        super("Add a new Cuboid: " + ColorManager.Yellow + "/cmod add <area>", 2);
    }

    @Override
    public void execute(Player player, String[] command) {
        if (parseCommand(player, command)) {
            return;
        }
        if (command[1].matches("[,:]")) {
            MessageSystem.failMessage(player, "invalidCharacters");
            MessageSystem.failMessage(player, "cuboidNotCreated");
            return;
        }
        Region defaultC = Config.get().getDefaultCuboidSetting(player);
        CuboidSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
        if (!selection.isComplete()) {
            MessageSystem.failMessage(player, "selectionIncomplete");
            return;
        }
        defaultC.setBoundingBox(selection.getOrigin(), selection.getOffset());
        defaultC.setName(command[1]);
        defaultC.setWorld(player.getWorld().getFqName());
        defaultC.addPlayer("o:" + player.getName());
        if (CuboidInterface.get().addCuboid(defaultC)) {
            MessageSystem.successMessage(player, "cuboidCreated");
        }
        else {
            MessageSystem.failMessage(player, "cuboidExists");
            MessageSystem.failMessage(player, "cuboidNotCreated");
        }
    }
}
