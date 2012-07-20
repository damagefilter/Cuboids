package net.playblack.cuboids;

//import java.util.concurrent.TimeUnit;

import net.playblack.cuboids.commands.*;
import net.playblack.cuboids.converters.Converter;
import net.playblack.cuboids.converters.Loader;
import net.playblack.cuboids.datasource.FlatfileDataLegacy;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.help.CommandHelper;
//import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.EventLogger;

/**
 * The Bootstrapper takes care of loading all the required components to run the
 * cuboidlib and also receives the server implementation.
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
    public Bootstrapper(CServer server, Loader[] loaders) {
        EventLogger log = EventLogger.getInstance();
        log.cacheMessage("Loading Cuboids2 ...", true);

        // ------------------------------------------------------
        CServer.setServer(server);
        log.logCachedMessage("INFO");

        // ------------------------------------------------------
        Config.getInstance(); // init this thing for a first time
        log.cacheMessage("Version ... " + Config.getInstance().getVersion(),
                true);
        log.logCachedMessage("INFO");

        // ------------------------------------------------------
        log.cacheMessage("Tasks ...", false);
        log.cacheMessage("done!", false);
        log.logCachedMessage("INFO");

        // ------------------------------------------------------
        log.cacheMessage("Foreign Cuboid files... ", true);
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
            log.cacheMessage("done", false);
        } else {
            log.cacheMessage("Nothing foreign to load found", false);
        }
        log.logCachedMessage("INFO");

        // ------------------------------------------------------
        log.cacheMessage("Native Cuboid Nodes...", true);
        RegionManager.getInstance().load();
        RegionManager.getInstance().save(true, true); // Save back
        log.cacheMessage("done!", false);
        log.logCachedMessage("INFO");
        FlatfileDataLegacy.cleanupFiles();

        log.cacheMessage("Init Help System", false);
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
        CommandHelper.get().addHelp(new Cload().getToolTip(),
                new String[] { "cload", "load", "cuboid" });
        CommandHelper.get().addHelp(
                new CloadFrom().getToolTip(),
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
        CommandHelper.get().addHelp(new CmodLoad().getToolTip(),
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
        CommandHelper.get()
                .addHelp(
                        new Csave(false).getToolTip(),
                        new String[] { "csave", "save", "selection", "edit",
                                "cuboid" });
        CommandHelper.get().addHelp(new Csave(true).getToolTip(),
                new String[] { "csave-all", "saveall", "edit", "cuboid" });
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
        CommandHelper.get()
                .addHelp(
                        new ToggleAreaProperty().getToolTip(),
                        new String[] { "cmod", "toggle", "property", "flag",
                                "cuboid" });
        CommandHelper.get().addHelp(
                new ToggleGlobalProperty().getToolTip(),
                new String[] { "cmod", "toggle", "property", "flag", "cuboid",
                        "global" });
    }
}
