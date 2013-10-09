package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;
import net.playblack.mcutils.ToolBox;

/**
 * Set the priority of a cuboid
 *
 * @author Chris
 */
public class CmodPriority extends CBaseCommand {

    public CmodPriority() {
        super("Set cuboid priority:" + ColorManager.Yellow + " /cmod priority <area> <level>", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        // player cube prio
        int prio = ToolBox.parseInt(command[2]);
        if (prio < 0) {
            MessageSystem.failMessage(player, "invalidPriority");
            return;
        }
        CuboidInterface.get().setPriority(player, command[1], prio);
    }
}
