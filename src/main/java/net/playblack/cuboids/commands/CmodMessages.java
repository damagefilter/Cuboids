package net.playblack.cuboids.commands;

import net.playblack.cuboids.MessageSystem;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.ColorManager;

/**
 * Set welcome and / or farewell messages
 *
 * @author Chris
 */
public class CmodMessages extends CBaseCommand {

    private boolean setFarewell = false;

    public CmodMessages(String action) {
        super("Set area message: " + ColorManager.Yellow + "/cmod" + action + "<area> [message]", 2);
        if (action.equalsIgnoreCase("farewell") || action.equalsIgnoreCase("goodbye")) {
            setFarewell = true;
        }
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        if (!parseCommand(player, command)) {
            return;
        }
        String message = null;
        StringBuilder toMessage = new StringBuilder();
        if (command.length > 2) {
            for (int i = 2; i < command.length; i++) {
                if (i > 2) {
                    toMessage.append(" ");
                }
                toMessage.append(command[i]);
            }
            message = toMessage.toString();
        }

        if (setFarewell) {
            CuboidInterface.get().setFarewell(player, command[1], message);
            MessageSystem.successMessage(player, "farewellSet");
        }
        else {
            CuboidInterface.get().setWelcome(player, command[1], message);
            MessageSystem.successMessage(player, "welcomeSet");
        }
    }
}
