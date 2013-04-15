package net.playblack.cuboids.impl.canarymod.commands;

import net.canarymod.Canary;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;
import net.playblack.cuboids.InvalidPlayerException;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.mcutils.Debug;

public class CmodCommands implements CommandListener {

    @Command(aliases = { "cmod" },
            description = "Manage Cuboids and Cuboid properties",
            permissions = { "cuboids.cmod.add", "cuboids.super.admin" },
            toolTip = "/cmod <add | remove | rename | allowcommand | restrictcommand | allowentity | allowitem " +
                      "| restrictitem | disallowentity | info | list | listflags | load | loadpoints | welcome | farewell " +
                      "| parent | priority | setflag | removefalg | tpto",
            max = 1)
    public void cmodBase(MessageReceiver caller, String[] args) {
        Canary.help().getHelp(caller, "cmod");
    }

    @Command(aliases = { "add" },
            parent = "cmod",
            helpLookup = "cmod add",
            description = "Add a new Cuboid to the world",
            permissions = { "cuboids.cmod.add","cuboids.super.admin" },
            toolTip = "/cmod add <area>",
            min=2)
    public void cmodAdd(MessageReceiver caller, String[] args) {
        Canary.println("fiooo");
        try {
            new net.playblack.cuboids.commands.CmodAdd().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "allowcommand" },
            parent = "cmod",
            helpLookup = "cmod allowcommand",
            description = "Allow a command in a cuboid",
            permissions = { "cuboids.cmod.allowcommand", "cuboids.super.admin" },
            toolTip = "/cmod allowcommand <area> <commands...>",
            min=3)
    public void cmodAllowCommand(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodAllowCommand().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "allow" },
            parent = "cmod",
            helpLookup = "cmod allowentity",
            description = "Allow a player into your area, this only works if the enter-cuboid flag is set",
            permissions = { "cuboids.cmod.allowentity", "cuboids.super.admin" },
            toolTip = "/cmod allow <area> <player g:group o:owner ...>",
            min=3)
    public void cmodAllowEntity(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodAllowEntity().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "allowitem" },
            parent = "cmod",
            helpLookup = "cmod allowitem",
            description = "Allow an item to be used in a Cuboid.",
            permissions = { "cuboids.cmod.allowitem","cuboids.super.admin" },
            toolTip = "/cmod allowitem <area> <item name or item id>",
            min=3)
    public void cmodAllowItem(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodAllowItem().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "disallow" },
            parent = "cmod",
            helpLookup = "cmod disallow",
            description = "Disallow a player to enter a Cuboid. This only works if the enter-cuboid flag is set",
            permissions = { "cuboids.cmod.disallow", "cuboids.super.admin" },
            toolTip = "/cmod disallow <area> <player g:group o:owner ...>",
            min=3)
    public void cmodDisallowEntity(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodDisallowEntity().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }


    @Command(aliases = { "info" },
            parent = "cmod",
            helpLookup = "cmod info",
            description = "Explains a Cuboid and your relation to it",
            permissions = { "cuboids.cmod.info", "cuboids.misc.cinfo", "cuboids.super.admin" },
            toolTip = "/cmod info <area>",
            min = 2)
    public void cmodInfo(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodInfo().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "list" },
            parent = "cmod",
            helpLookup = "cmod list",
            description = "View all available Cuboids in your current world",
            permissions = { "cuboids.cmod.list", "cuboids.super.admin" },
            toolTip = "/cmod list [page]",
            max = 2)
    public void cmodList(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodList().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "flags" },
            parent = "cmod",
            helpLookup = "cmod flags",
            description = "Manage Cuboid Flags",
            permissions = { "cuboids.cmod.flags", "cuboids.super.admin" },
            toolTip = "/cmod flags <list|add|remove>",
            max = 2)
    public void cmodFlags(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodListFlags().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "list" },
            parent = "cmod.flags",
            helpLookup = "cmod listflags",
            description = "View all available Cuboidflags or the currently set ones of a specified Cuboid",
            permissions = { "cuboids.cmod.flags.list", "cuboids.super.admin" },
            toolTip = "/cmod flags list [area]",
            max = 2)
    public void cmodListFlags(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodListFlags().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "remove" },
            parent = "cmod.flags",
            helpLookup = "cmod removeflags",
            description = "Remove a flag from a Cuboid or from the global settings (by not specifiying an area name)",
            permissions = { "cuboids.cmod.flags.list", "cuboids.super.admin" },
            toolTip = "/cmod flags remove [area] <flag>",
            min = 2)
    public void cmodRemoveFlags(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodRemoveFlag().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "set" },
            parent = "cmod.flags",
            helpLookup = "cmod setflags",
            description = "Set a flag on a Cuboid or from the global settings (by not specifiying an area name)",
            permissions = { "cuboids.cmod.flags.list", "cuboids.super.admin" },
            toolTip = "/cmod flags set [area] <flag> <ALLOW|DENY|DEFAULT>",
            min = 3,
            max = 4)
    public void cmodSetFlags(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodSetFlag().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "loadpoints" },
            parent = "cmod",
            helpLookup = "cmod loadpoints",
            description = "Load the area bounding rectangle as selection",
            permissions = { "cuboids.cmod.loadpoints", "cuboids.super.admin" },
            toolTip = "/cmod loadpoints <area>",
            min = 2)
    public void cmodLoadPoints(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodLoadPoints().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "load" },
            parent = "cmod",
            helpLookup = "cmod load",
            description = "Load the data of the specified Cuboid from database",
            permissions = { "cuboids.cmod.load", "cuboids.super.admin" },
            toolTip = "/cmod load <area>",
            min = 2)
    public void cmodLoad(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodLoad().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "loadfrom" },
            parent = "cmod",
            helpLookup = "cmod loadfrom",
            description = "Load all cuboids from a specified datasource. Useful if you switch datasource types.",
            permissions = { "cuboids.cmod.loadfrom", "cuboids.super.admin" },
            toolTip = "/cmod loadfrom <mysql|flatfile|xml>",
            min = 2)
    public void cmodLoadFrom(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodLoadFrom().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "save" },
            parent = "cmod",
            helpLookup = "cmod save",
            description = "Save the data of the specified Cuboid (or all if no Cuboid specified) to database",
            permissions = { "cuboids.cmod.save", "cuboids.super.admin" },
            toolTip = "/cmod save [area]",
            min = 1,
            max = 2)
    public void cmodSave(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodSave().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "welcome" },
            parent = "cmod",
            helpLookup = "cmod welcome",
            description = "Set Welcome message of a given Cuboid. Leave the message blank to remove the old one.",
            permissions = { "cuboids.cmod.welcome", "cuboids.super.admin" },
            toolTip = "/cmod welcome <area> [message]",
            min = 2)
    public void cmodWelcome(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodMessages("welcome").execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "farewell", "goodbye", "leaving" },
            parent = "cmod",
            helpLookup = "cmod farewell",
            description = "Set Farewell message of a given Cuboid. Leave the message blank to remove the old one.",
            permissions = { "cuboids.cmod.farewell", "cuboids.super.admin" },
            toolTip = "/cmod farewell <area> [message]",
            min = 2)
    public void cmodFarewell(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodMessages("farewell").execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "move", "resize" },
            parent = "cmod",
            helpLookup = "cmod move",
            description = "Move the Cuboid bounding rectangle to your current selection",
            permissions = { "cuboids.cmod.move", "cuboids.super.admin" },
            toolTip = "/cmod move <area>",
            min = 2)
    public void cmodMove(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodMove().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "parent" },
            parent = "cmod",
            helpLookup = "cmod parent",
            description = "Set an areas parent manually.",
            permissions = { "cuboids.cmod.parent", "cuboids.super.admin" },
            toolTip = "/cmod parent <area> <parent>",
            min = 3)
    public void cmodParent(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodParent().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "priority", "prio" },
            parent = "cmod",
            helpLookup = "cmod parent",
            description = "Set the areas priority.",
            permissions = { "cuboids.cmod.priority", "cuboids.super.admin" },
            toolTip = "/cmod parent <area> <priority>",
            min = 3)
    public void cmodPriority(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodPriority().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "remove", "delete" },
            parent = "cmod",
            helpLookup = "cmod remove",
            description = "Remove a Cuboid",
            permissions = { "cuboids.cmod.remove", "cuboids.super.admin" },
            toolTip = "/cmod remove <area>",
            min = 3)
    public void cmodRemove(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodRemove().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "rename" },
            parent = "cmod",
            helpLookup = "cmod rename",
            description = "Rename a Cuboid",
            permissions = { "cuboids.cmod.rename", "cuboids.super.admin" },
            toolTip = "/cmod rename <area> <newname>",
            min = 3)
    public void cmodRemoveFlag(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodRename().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "restrictcommand", "restrictcmd" },
            parent = "cmod",
            helpLookup = "cmod restrictcommand",
            description = "Restrict a command in this area",
            permissions = { "cuboids.cmod.restrictcommand", "cuboids.super.admin" },
            toolTip = "/cmod restrictcommand <area> <command,command ...>",
            min = 3)
    public void cmodRestrictCommand(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodRestrictCommand().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "restrictitem" },
            parent = "cmod",
            helpLookup = "cmod restrictitem",
            description = "Restrict an item in this area",
            permissions = { "cuboids.cmod.restrictitem", "cuboids.super.admin" },
            toolTip = "/cmod restrictitem <area> <command,command ...>",
            min = 3)
    public void cmodRestrictItem(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodRestrictItem().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

    @Command(aliases = { "tpto", "tp" },
            parent = "cmod",
            helpLookup = "cmod tpto",
            description = "Teleport to the center of the specified area",
            permissions = { "cuboids.cmod.tpto", "cuboids.super.admin" },
            toolTip = "/cmod tpto <area>",
            min = 2)
    public void cmodTpTo(MessageReceiver caller, String[] args) {
        try {
            new net.playblack.cuboids.commands.CmodTpTo().execute(CServer.getServer().getPlayer(caller.getName()), args);
        } catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
    }

}
