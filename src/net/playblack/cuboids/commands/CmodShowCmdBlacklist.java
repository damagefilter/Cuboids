package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Show the list of blacklisted commands
 * 
 * @author Chris
 * 
 */
public class CmodShowCmdBlacklist extends CBaseCommand {

    public CmodShowCmdBlacklist() {
        super("Show blacklisted commands:" + ColorManager.Yellow
                + " /cmod <area> cmdblacklist", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        CuboidInterface.get().showCommandBlacklist(player, command[1]);
    }
}
