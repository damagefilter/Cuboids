package net.playblack.cuboids;

//import java.util.concurrent.TimeUnit;

import net.playblack.cuboids.Config.Implementation;
import net.playblack.cuboids.actions.operators.BlockModificationsOperator;
import net.playblack.cuboids.actions.operators.DamageOperator;
import net.playblack.cuboids.actions.operators.MiscOperator;
import net.playblack.cuboids.actions.operators.OperableItemsOperator;
import net.playblack.cuboids.actions.operators.PlayerMovementOperator;
import net.playblack.cuboids.actions.operators.SelectionOperator;
import net.playblack.cuboids.commands.Cbackup;
import net.playblack.cuboids.commands.Cbrush;
import net.playblack.cuboids.commands.Cceiling;
import net.playblack.cuboids.commands.Ccopy;
import net.playblack.cuboids.commands.Cdel;
import net.playblack.cuboids.commands.Cdisc;
import net.playblack.cuboids.commands.Cexpand;
import net.playblack.cuboids.commands.Cfill;
import net.playblack.cuboids.commands.Cfloor;
import net.playblack.cuboids.commands.Chelp;
import net.playblack.cuboids.commands.Cinfo;
import net.playblack.cuboids.commands.CmodAdd;
import net.playblack.cuboids.commands.CmodAllowCommand;
import net.playblack.cuboids.commands.CmodAllowEntity;
import net.playblack.cuboids.commands.CmodAllowItem;
import net.playblack.cuboids.commands.CmodDisallowEntity;
import net.playblack.cuboids.commands.CmodInfo;
import net.playblack.cuboids.commands.CmodList;
import net.playblack.cuboids.commands.CmodLoad;
import net.playblack.cuboids.commands.CmodLoadFrom;
import net.playblack.cuboids.commands.CmodLoadPoints;
import net.playblack.cuboids.commands.CmodMessages;
import net.playblack.cuboids.commands.CmodMove;
import net.playblack.cuboids.commands.CmodParent;
import net.playblack.cuboids.commands.CmodPriority;
import net.playblack.cuboids.commands.CmodRemove;
import net.playblack.cuboids.commands.CmodRename;
import net.playblack.cuboids.commands.CmodRestrictCommand;
import net.playblack.cuboids.commands.CmodRestrictItem;
import net.playblack.cuboids.commands.CmodSave;
import net.playblack.cuboids.commands.CmodShowCmdBlacklist;
import net.playblack.cuboids.commands.CmodTpTo;
import net.playblack.cuboids.commands.Cmove;
import net.playblack.cuboids.commands.Cpaste;
import net.playblack.cuboids.commands.Cpyramid;
import net.playblack.cuboids.commands.Credo;
import net.playblack.cuboids.commands.Creplace;
import net.playblack.cuboids.commands.Crestore;
import net.playblack.cuboids.commands.Csphere;
import net.playblack.cuboids.commands.Cundo;
import net.playblack.cuboids.commands.Cwalls;
import net.playblack.cuboids.commands.Highprotect;
import net.playblack.cuboids.commands.Protect;
import net.playblack.cuboids.converters.Converter;
import net.playblack.cuboids.converters.Loader;
import net.playblack.cuboids.datasource.FlatfileDataLegacy;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.help.CommandHelper;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Debug;
//import net.playblack.cuboids.regions.CuboidInterface;

/**
 * The Bootstrapper takes care of loading all the required components to run the
 * Plugin and also receives the server implementation.
 *
 * @author Chris
 *
 */
public class Bootstrapper {
    /**
     * Expects the server implementation and a list of loaders for foreign
     * cuboids. Leave the list null if nothing should be loaded from foreign
     * sources
     *
     * @param server
     * @param loaders
     */
    public Bootstrapper(CServer server, Loader[] loaders, Implementation impl) {
        Debug.cacheMessage("Loading Cuboids2 ...", true);

        // ------------------------------------------------------
        CServer.setServer(server);
        Debug.logCachedMessage();

        // ------------------------------------------------------
        Config.get().setImplementation(impl); // init this thing for a first time
        Debug.cacheMessage("Version ... " + Config.get().getVersion(),
                true);
        Debug.logCachedMessage();

        // ------------------------------------------------------
        Debug.cacheMessage("Tasks ...", false);
        Debug.cacheMessage("done!", false);
        Debug.logCachedMessage();

        // ------------------------------------------------------
        Debug.cacheMessage("Foreign Cuboid files... ", true);
        boolean hasConverted = false;
        if (loaders != null) {
            Converter c = new Converter();
            for (Loader loader : loaders) {
                if (c.convertFiles(loader)) {
                    if (hasConverted == false) {
                        hasConverted = true;
                    }
                }
            }
        }
        if (hasConverted) {
            Debug.cacheMessage("done", false);
        } else {
            Debug.cacheMessage("Nothing foreign to load found", false);
        }
        Debug.logCachedMessage();

        // ------------------------------------------------------
        Debug.cacheMessage("Native Cuboid Nodes...", true);
        RegionManager.get().load();
        RegionManager.get().save(true, true); // Save back
        Debug.cacheMessage("done!", false);
        Debug.logCachedMessage();
        FlatfileDataLegacy.cleanupFiles();

        Debug.log("Setting up Event Operators");
        new BlockModificationsOperator();
        new DamageOperator();
        new MiscOperator();
        new OperableItemsOperator();
        new PlayerMovementOperator();
        new SelectionOperator();


        Debug.log("Init help System");
        CommandHelper.get().addHelp(
                new Cbackup().getToolTip(),
                new String[] { "backup", "restore", "cbackup", "edit",
                        "selection", "cuboid" });
        CommandHelper.get()
                .addHelp(
                        new Cbrush().getToolTip(),
                        new String[] { "brush", "cbrush", "csphere", "sphere",
                                "edit" });
        CommandHelper.get().addHelp(new Cceiling().getToolTip(),
                new String[] { "ceiling", "cceiling", "selection" });
        CommandHelper.get().addHelp(new Ccopy().getToolTip(),
                new String[] { "ccopy", "copy", "paste", "edit" });
        CommandHelper.get().addHelp(new Cdel().getToolTip(),
                new String[] { "cdel", "cfill", "delete", "edit" });
        CommandHelper.get().addHelp(new Cdisc("/ccircle").getToolTip(),
                new String[] { "circle", "ccircle", "edit" });
        CommandHelper.get().addHelp(new Cdisc("/cdisc").getToolTip(),
                new String[] { "disc", "cdisc", "edit" });
        CommandHelper.get().addHelp(new Cexpand().getToolTip(),
                new String[] { "expand", "cexpand", "stretch" });
        CommandHelper.get().addHelp(new Cfill().getToolTip(),
                new String[] { "cfill", "replace", "fill", "edit" });
        CommandHelper.get().addHelp(new Cfloor().getToolTip(),
                new String[] { "floor", "cfloor", "selection" });
        CommandHelper.get().addHelp(new Chelp().getToolTip(),
                new String[] { "chelp", "help" });
        CommandHelper.get().addHelp(new Cinfo().getToolTip(),
                new String[] { "cinfo", "info", "cuboid" });
        CommandHelper.get().addHelp(new CmodLoad().getToolTip(),
                new String[] { "cload", "load", "cuboid" });
        CommandHelper.get().addHelp(
                new CmodLoadFrom().getToolTip(),
                new String[] { "cloadfrom", "loadfrom", "load", "crossload",
                        "cuboid" });
        CommandHelper.get().addHelp(new CmodAdd().getToolTip(),
                new String[] { "cmod", "add", "create", "cuboid" });
        CommandHelper.get().addHelp(
                new CmodAllowCommand().getToolTip(),
                new String[] { "cmod", "allowcommand", "command", "allow",
                        "cuboid" });
        CommandHelper.get().addHelp(new CmodAllowEntity().getToolTip(),
                new String[] { "cmod", "allow", "entity", "player", "cuboid" });
        CommandHelper.get()
                .addHelp(
                        new CmodAllowItem().getToolTip(),
                        new String[] { "cmod", "allowitem", "item", "allow",
                                "cuboid" });
        CommandHelper.get()
                .addHelp(
                        new CmodDisallowEntity().getToolTip(),
                        new String[] { "cmod", "disallow", "player", "entity",
                                "cuboid" });
        CommandHelper.get().addHelp(new CmodInfo().getToolTip(),
                new String[] { "cmod", "info", "cinfo", "cuboid" });
        CommandHelper.get().addHelp(new CmodList().getToolTip(),
                new String[] { "cmod", "list", "all", "cuboid" });
        CommandHelper.get().addHelp(new CmodLoadPoints().getToolTip(),
                new String[] { "cmod", "loadpoints", "selection", "load" });
        CommandHelper.get().addHelp(
                new CmodMessages("farewell").getToolTip(),
                new String[] { "cmod", "farewell", "goodbye", "message",
                        "notice", "cuboid" });
        CommandHelper.get()
                .addHelp(
                        new CmodMessages("welcome").getToolTip(),
                        new String[] { "cmod", "welcome", "message", "notice",
                                "cuboid" });
        CommandHelper.get().addHelp(
                new CmodMove().getToolTip(),
                new String[] { "cmod", "move", "resize", "change", "selection",
                        "cuboid" });
        CommandHelper.get().addHelp(new CmodParent().getToolTip(),
                new String[] { "cmod", "parent", "child", "cuboid" });
        CommandHelper.get().addHelp(new CmodPriority().getToolTip(),
                new String[] { "cmod", "priority", "prio", "cuboid" });
        CommandHelper.get().addHelp(new CmodRemove().getToolTip(),
                new String[] { "cmod", "remove", "delete", "cuboid" });
        CommandHelper.get().addHelp(new CmodRename().getToolTip(),
                new String[] { "cmod", "rename", "cuboid" });
        CommandHelper.get().addHelp(
                new CmodRestrictCommand().getToolTip(),
                new String[] { "cmod", "restrictcommand", "restrict",
                        "command", "cuboid" });
        CommandHelper.get().addHelp(
                new CmodRestrictItem().getToolTip(),
                new String[] { "cmod", "restrictitem", "item", "restrict",
                        "cuboid" });
        CommandHelper.get()
                .addHelp(
                        new CmodShowCmdBlacklist().getToolTip(),
                        new String[] { "cmod", "command", "blacklist",
                                "cmdblacklist" });
        CommandHelper.get().addHelp(new CmodTpTo().getToolTip(),
                new String[] { "cmod", "tpto", "teleport", "cuboid" });
        CommandHelper.get().addHelp(new Cmove().getToolTip(),
                new String[] { "cmove", "move", "selection", "edit" });
        CommandHelper.get()
                .addHelp(
                        new Cpaste().getToolTip(),
                        new String[] { "cpaste", "paste", "copy", "selection",
                                "edit" });
        CommandHelper.get().addHelp(new Cpyramid().getToolTip(),
                new String[] { "cpyramid", "pyramid", "selection", "edit" });
        CommandHelper.get().addHelp(new Credo().getToolTip(),
                new String[] { "credo", "redo", "undo", "selection", "edit" });
        CommandHelper.get().addHelp(
                new Creplace().getToolTip(),
                new String[] { "creplace", "replace", "selection", "cfill",
                        "edit" });
        CommandHelper.get().addHelp(
                new Crestore().getToolTip(),
                new String[] { "crestore", "restore", "backup", "selection",
                        "edit", "cuboid" });
        CommandHelper.get().addHelp(
                        new CmodSave().getToolTip(),
                        new String[] { "csave", "save", "selection", "edit",
                                "cuboid" });
        CommandHelper.get()
                .addHelp(
                        new Csphere().getToolTip(),
                        new String[] { "csphere", "sphere", "ball",
                                "selection", "edit" });
        CommandHelper.get().addHelp(new Cundo().getToolTip(),
                new String[] { "cundo", "undo", "redo", "selection", "edit" });
        CommandHelper.get().addHelp(new Cwalls("/cwalls").getToolTip(),
                new String[] { "cwalls", "walls", "selection", "edit" });
        CommandHelper.get().addHelp(new Cwalls("/cfaces").getToolTip(),
                new String[] { "cfaces", "faces", "selection", "edit" });
        CommandHelper.get()
                .addHelp(
                        new Highprotect().getToolTip(),
                        new String[] { "protect", "highprotect", "selection",
                                "cuboid" });
        CommandHelper.get()
                .addHelp(
                        new Protect().getToolTip(),
                        new String[] { "protect", "highprotect", "selection",
                                "cuboid" });
    }
}
