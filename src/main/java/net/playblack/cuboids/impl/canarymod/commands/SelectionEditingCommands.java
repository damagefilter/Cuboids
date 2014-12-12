package net.playblack.cuboids.impl.canarymod.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;
import net.playblack.cuboids.commands.Cceiling;
import net.playblack.cuboids.commands.Cdiag;
import net.playblack.cuboids.commands.Cexpand;
import net.playblack.cuboids.commands.Cfloor;
import net.playblack.cuboids.exceptions.InvalidPlayerException;
import net.playblack.mcutils.Debug;

public class SelectionEditingCommands implements CommandListener {
    @Command(aliases = {"cceiling"},
            description = "Set the ceiling level of a selection. Use -r to set relative from your current location (player + <height>)",
            permissions = {"cuboids.selection.cceiling", "cuboids.super.admin"},
            toolTip = "/cceiling <height> [-r]",
            min = 2,
            max = 3)
    public void cceiling(MessageReceiver caller, String[] args) {
        try {
            new Cceiling().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This is not a console command.");
        }
    }

    @Command(aliases = {"cdiag"},
            description = "Cuboid diagnostics tool",
            permissions = {"cuboids.debug.cdiag", "cuboids.super.admin"},
            toolTip = "/cdiag")
    public void cdiag(MessageReceiver caller, String[] args) {
        try {
            new Cdiag().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This is not a console command.");
        }
    }

    @Command(aliases = {"cexpand"},
            description = "vertically expand your selection so it stretches from top to bottom",
            permissions = {"cuboids.selection.cexpand", "cuboids.super.admin"},
            toolTip = "/cexpand")
    public void cexpand(MessageReceiver caller, String[] args) {
        try {
            new Cexpand().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This is not a console command.");
        }
    }

    @Command(aliases = {"cfloor"},
            description = "Set the floor level of a selection. Use -r to set relative from your current location (player - <height>)",
            permissions = {"cuboids.selection.cceiling", "cuboids.super.admin"},
            toolTip = "/cceiling <height> [-r]",
            min = 2,
            max = 3)
    public void cfloor(MessageReceiver caller, String[] args) {
        try {
            new Cfloor().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This is not a console command.");
        }
    }
}
