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
 * 
 */
public class CmodPriority extends CBaseCommand {

    public CmodPriority() {
        super("Set cuboid priority:" + ColorManager.Yellow
                + " /cmod <area> priority <level>", 4);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        // player cube prio
        int prio = ToolBox.parseInt(command[3]);
        if (prio < 0) {
            MessageSystem.getInstance().failMessage(player, "invalidPriority");
            return;
        }
        CuboidInterface.getInstance().setPriority(player, command[1], prio);
    }
}
