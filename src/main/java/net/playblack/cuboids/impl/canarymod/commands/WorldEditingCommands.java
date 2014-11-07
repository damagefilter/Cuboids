package net.playblack.cuboids.impl.canarymod.commands;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;
import net.playblack.cuboids.InvalidPlayerException;
import net.playblack.cuboids.commands.Cbackup;
import net.playblack.cuboids.commands.Cbrush;
import net.playblack.cuboids.commands.Ccopy;
import net.playblack.cuboids.commands.Cdel;
import net.playblack.cuboids.commands.Cdisc;
import net.playblack.cuboids.commands.Cfill;
import net.playblack.cuboids.commands.Cmove;
import net.playblack.cuboids.commands.Cpaste;
import net.playblack.cuboids.commands.Cpyramid;
import net.playblack.cuboids.commands.Credo;
import net.playblack.cuboids.commands.Creplace;
import net.playblack.cuboids.commands.Crestore;
import net.playblack.cuboids.commands.Csphere;
import net.playblack.cuboids.commands.Cundo;
import net.playblack.cuboids.commands.Cwalls;
import net.playblack.mcutils.Debug;

public class WorldEditingCommands implements CommandListener {

    @Command(aliases = {"cbackup"},
            description = "Backup a Cuboid area",
            permissions = {"cuboids.editing.cbackup", "cuboids.super.admin"},
            toolTip = "/cbackup <area>",
            min = 2)
    public void cbackup(MessageReceiver caller, String[] args) {
        try {
            new Cbackup().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"cbrush"},
            description = "Change Brush Settings for world editing",
            permissions = {"cuboids.editing.cbackup", "cuboids.super.admin"},
            toolTip = "/cbrush <radius> <block>:[data]",
            min = 3)
    public void cbrush(MessageReceiver caller, String[] args) {
        try {
            new Cbrush().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"ccopy"},
            description = "Copy the contents of your selection into the clipboard",
            permissions = {"cuboids.editing.ccopy", "cuboids.super.admin"},
            toolTip = "/cceopy")
    public void ccopy(MessageReceiver caller, String[] args) {
        try {
            new Ccopy().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"cdel"},
            description = "Replaces all blocks inside your selection with air",
            permissions = {"cuboids.editing.cdel", "cuboids.super.admin"},
            toolTip = "/cdel")
    public void cdel(MessageReceiver caller, String[] args) {
        try {
            new Cdel().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"cdisc"},
            description = "Draws a disc around the selected start point",
            permissions = {"cuboids.editing.cdisc", "cuboids.super.admin"},
            toolTip = "/cdisc <radius> <block>:[data] [height]",
            min = 3,
            max = 4)
    public void cdisc(MessageReceiver caller, String[] args) {
        try {
            new Cdisc("/cdisc").execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"ccircle"},
            description = "Draws a circle around the selected start point",
            permissions = {"cuboids.editing.ccircle", "cuboids.super.admin"},
            toolTip = "/ccircle <radius> <block>:[data] [height]",
            min = 3,
            max = 4)
    public void ccircle(MessageReceiver caller, String[] args) {
        try {
            new Cdisc("/ccircle").execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"cfill"},
            description = "Fills the current selection",
            permissions = {"cuboids.editing.cfill", "cuboids.super.admin"},
            toolTip = "/cfill <block>:[data]",
            min = 2,
            max = 3)
    public void cfill(MessageReceiver caller, String[] args) {
        try {
            new Cfill().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"cmove"},
            description = "Moves the contents of a selection <distance> blocks into a direction",
            permissions = {"cuboids.editing.cmove", "cuboids.super.admin"},
            toolTip = "/cmove <distance> <NORTH/EAST/SOUTH/WEST/UP/DOWN>",
            min = 3)
    public void cmove(MessageReceiver caller, String[] args) {
        try {
            new Cmove().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"cpaste"},
            description = "Paste your clipboard relative to your current position",
            permissions = {"cuboids.editing.cpaste", "cuboids.super.admin"},
            toolTip = "/cpaste")
    public void cpaste(MessageReceiver caller, String[] args) {
        try {
            new Cpaste().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"cpyramid"},
            description = "Draw a pyramid from the selected start point, add the word hollow to create a hollow pyramid",
            permissions = {"cuboids.editing.cpyramid", "cuboids.super.admin"},
            toolTip = "/cpyramid <radius> <block>:[data] [hollow]",
            min = 3,
            max = 4)
    public void cpyramid(MessageReceiver caller, String[] args) {
        try {
            new Cpyramid().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"credo"},
            description = "Redo your or others last action",
            permissions = {"cuboids.editing.redo", "cuboids.super.admin"},
            toolTip = "/credo [steps] [player]",
            min = 1,
            max = 3)
    public void credo(MessageReceiver caller, String[] args) {
        try {
            new Credo().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"creplace"},
            description = "Replace <block> with <substitute> inside your current selection",
            permissions = {"cuboids.editing.replace", "cuboids.super.admin"},
            toolTip = "/creplace <block>:[data] <substitute>:[data]",
            min = 3)
    public void creplace(MessageReceiver caller, String[] args) {
        try {
            new Creplace().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"crestore"},
            description = "Restore the contents of a backed up Cuboid",
            permissions = {"cuboids.editing.restore", "cuboids.super.admin"},
            toolTip = "/crestore <area>",
            min = 2)
    public void crestore(MessageReceiver caller, String[] args) {
        try {
            new Crestore().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"csphere"},
            description = "Draw a sphere around the set start point",
            permissions = {"cuboids.editing.csphere", "cuboids.super.admin"},
            toolTip = "/csphere <radius> <block>:[data] [hollow]",
            min = 3,
            max = 4)
    public void csphere(MessageReceiver caller, String[] args) {
        try {
            new Csphere().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"cundo"},
            description = "Undo your or others last block operation",
            permissions = {"cuboids.editing.cundo", "cuboids.super.admin"},
            toolTip = "/cundo [steps] [player]",
            min = 1,
            max = 3)
    public void cundo(MessageReceiver caller, String[] args) {
        try {
            new Cundo().execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"cwalls"},
            description = "Create the walls or your selection",
            permissions = {"cuboids.editing.cwalls", "cuboids.super.admin"},
            toolTip = "/cwalls <wallblock>:[data]",
            min = 2,
            max = 4)
    public void cwalls(MessageReceiver caller, String[] args) {
        try {
            new Cwalls("/cwalls").execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }

    @Command(aliases = {"cfaces"},
            description = "Create the walls, floor and ceiling or your selection",
            permissions = {"cuboids.editing.cwalls", "cuboids.super.admin"},
            toolTip = "/cfaces <wall>:[data] [floor]:[data] [ceiling]:[data]",
            min = 3,
            max = 4)
    public void cfaces(MessageReceiver caller, String[] args) {
        try {
            new Cwalls("/cfaces").execute((Player) caller, args);
        }
        catch (InvalidPlayerException e) {
            Debug.logError(e.getMessage());
        }
        catch (ClassCastException f) {
            Debug.logError("This command works only on players!");
        }
    }
}
