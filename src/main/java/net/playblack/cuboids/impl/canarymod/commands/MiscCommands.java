package net.playblack.cuboids.impl.canarymod.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;
import net.playblack.cuboids.commands.Cinfo;
import net.playblack.cuboids.commands.Highprotect;
import net.playblack.cuboids.commands.Protect;
import net.playblack.cuboids.exceptions.InvalidPlayerException;
import net.playblack.mcutils.Debug;

public class MiscCommands implements CommandListener {

    @Command(aliases = {"cinfo"},
            description = "Explains the Cuboid you're currently inside.",
            permissions = {"cuboids.misc.cinfo", "cuboids.cmod.info", "cuboids.super.admin"},
            toolTip = "/cinfo")
    public void cinfo(MessageReceiver caller, String[] args) {
        try {
            new Cinfo().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This is not a console command");
        }
    }

    @Command(aliases = {"highprotect"},
            description = "Create a top-to-bottom protected area from your selection.",
            permissions = {"cuboids.cmod.add.high", "cuboids.super.admin"},
            toolTip = "/highprotect <player/group..> <area name>",
            min = 3)
    public void highprotect(MessageReceiver caller, String[] args) {
        try {
            new Highprotect().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This is not a console command");
        }
    }

    @Command(aliases = {"protect"},
            description = "Create a protected area from your selection.",
            permissions = {"cuboids.cmod.add", "cuboids.super.admin"},
            toolTip = "/protect <player/group..> <area name>",
            min = 3)
    public void protect(MessageReceiver caller, String[] args) {
        try {
            new Protect().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This is not a console command");
        }
    }

}
