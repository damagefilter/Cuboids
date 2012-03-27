package net.playblack.cuboids.commands;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

/**
 * cmod for areas
 * @author Chris
 *
 */
public class Cmod extends BaseCommand {

    public Cmod(String action) {
        super("Cmod: /cmod <area> "+action, 3);
    }

    @Override
    public void execute(CPlayer player, String[] command) {
        //cmod AREA toggle PROPERTY
        if(!parseCommand(player, command)) {
            return;
        }
        //now, mega über super if monster!!! :D
        if(command[3].equalsIgnoreCase("creeper")) {
            CuboidInterface.getInstance().toggleCreeperSecure(player, command[1]);
            return;
        }
    }
}
