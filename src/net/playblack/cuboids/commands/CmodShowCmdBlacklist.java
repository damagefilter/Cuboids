package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * Show the list of blacklisted commands
 * @author Chris
 *
 */
public class CmodShowCmdBlacklist extends BaseCommand {

    public CmodShowCmdBlacklist() {
        super("Show blacklisted commands: /cmod <area> cmdblacklist", 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if(!parseCommand(player, command)) {
            return;
        }
       CuboidInterface.getInstance().showCommandBlacklist(player, command[1]);
    }
}
