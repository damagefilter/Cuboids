package net.playblack.cuboids.regions;

import net.canarymod.Canary;
import net.canarymod.Translator;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.*;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.cuboids.selections.SelectionManager;
import net.playblack.mcutils.ColorManager;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Interfaces RegionManager and applies permissions.
 *
 * @author Chris
 */
public class CuboidInterface {
    private static CuboidInterface instance = null;
    private ScheduledExecutorService threadManager = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * Construct a new CuboidInterface
     */
    private CuboidInterface() {
        Config cfg = Config.get();
        threadManager.scheduleAtFixedRate(new HMobTask(), 20, 20, TimeUnit.SECONDS);
        threadManager.scheduleAtFixedRate(new CuboidSaveTask(), cfg.getSaveDelay(), cfg.getSaveDelay(), TimeUnit.MINUTES);
    }

    public static CuboidInterface get() {
        if (instance == null) {
            instance = new CuboidInterface();
        }
        return instance;
    }

    public ScheduledExecutorService getThreadManager() {
        return threadManager;
    }


    /*
     * **************************************************************************************
     * **************************************************************************************
     * AREA SETS FOR WELCOME AND GOODBYE
     * ****************************************
     */

    /**
     * Set the welcome message. Insert null for message to remove the welcome
     * message
     *
     * @param player
     * @param cuboid
     * @param message
     */
    public void setWelcome(Player player, String cuboid, String message) {
        Region cube = RegionManager.get().getRegionByName(cuboid, player.getWorld().getFqName());
        if (cube == null) {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            return;
        }
        cube.setWelcome(message);
    }

    public boolean canModifyBlock(Player p, Location location) {
        Region cube = RegionManager.get().getActiveRegion(location, false);
        return p.hasPermission(Permissions.ADMIN) || cube.playerIsAllowed(p, p.getPlayerGroups()) || cube.getProperty("protection") != Region.Status.ALLOW;

    }
    /**
     * Set the farewell message. Insert null for message to remove the farewell
     * message
     *
     * @param player
     * @param cuboid
     * @param message
     */
    public void setFarewell(Player player, String cuboid, String message) {
        Region cube = RegionManager.get().getRegionByName(cuboid, player.getWorld().getFqName());
        cube.setFarewell(message);

    }

    /*
     * **************************************************************************************
     * **************************************************************************************
     */

    /**
     * Load a cuboid from file
     *
     * @param player
     * @param cube
     */
    public void loadCuboid(Player player, String cube) {
        if ((player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN))) {
            if (!RegionManager.get().cuboidExists(cube, player.getWorld().getFqName())) {
                MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
                return;
            }
            RegionManager.get().loadSingle(cube, player.getWorld().getFqName());
            MessageSystem.successMessage(player, "cuboidLoaded");
        }
        else {
            MessageSystem.failMessage(player, "playerNotOwner");
        }
    }

    /**
     * Save a single cuboid to file
     *
     * @param player
     * @param cube
     */
    public void saveCuboid(Player player, String cube) {
        // getCuboidByName(cube, player.getWorld().getName(),
        // player.getWorld().getDimension())
        Region cubeNode = RegionManager.get().getRegionByName(cube, player.getWorld().getFqName());
        if (cubeNode == null) {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            return;
        }
        if ((cubeNode.playerIsOwner(player.getName())) || (player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN))) {
            if (RegionManager.get().saveSingle(cube, player.getWorld().getFqName())) {
                MessageSystem.successMessage(player, "cuboidSaved");
            }
            else {
                MessageSystem.failMessage(player, "cuboidNotSaved");
            }
        }
        else {
            MessageSystem.failMessage(player, "playerNotOwner");
        }
    }

    /**
     * Save all cuboids in all worlds and dimensions
     *
     * @param player
     */
    public void saveAll(Player player) {
        RegionManager.get().save();
        MessageSystem.successMessage(player, "cuboidSavedAll");
    }

    /**
     * Handle the leaving and entering of RegionManager.get() for a specified player.
     * This will update the currentRegion reference in a player, aswell
     * as send welcome/farewell messages and toggle game modes according to
     * the specifications of a region
     *
     * @param player
     * @param from
     * @param to
     */
    public void handleRegionsForPlayer(Player player, Location from, Location to) {
        Region r = SessionManager.get().getRegionForPlayer(player.getName());
        if (r != null) {
            if (!r.isWithin(player.getLocation()) && !r.isWithin(from)) {
                SessionManager.get().setRegionForPlayer(player, null);
            }
        }
        if (to != null) {
            SessionManager.get().setRegionForPlayer(player, RegionManager.get().getActiveRegion(to, true));
        }
    }


    /**
     * Check if that command is on the restricted commands list
     *
     * @param player
     * @param command
     * @return
     */
    public boolean commandIsRestricted(Player player, String command) {
        // !!!! NOTE: this MUST NOT check for permissions!!!
        // Check that in the implementation!
        // This is to avoid stack overflows!
        // if(player.hasPermission("cuboids.super.admin")) {
        // return false;
        // }
        Region cube = RegionManager.get().getActiveRegion(player.getLocation(), true);
        return cube != null && cube.commandIsRestricted(command);
    }

    /**
     * Add a cuboid to the tree
     *
     * @param cube
     * @return
     */
    public boolean addCuboid(Region cube) {
        return RegionManager.get().addRegion(cube);
    }

    /**
     * Remove a cuboid from tree
     *
     * @param cubeName
     * @return
     */
    public boolean removeCuboid(Player player, String cubeName) {
//        CPlayer player = CServer.getServer().getPlayer(p.getName());
        Region cube = RegionManager.get().getRegionByName(cubeName, player.getWorld().getFqName());
        if (cube == null) {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            return false;
        }

        if (cube.playerIsOwner(player.getName()) || player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN)) {
            RegionManager.get().removeRegion(cube);
            MessageSystem.successMessage(player, "cuboidRemoved");
            return true;

        }
        else {
            MessageSystem.failMessage(player, "permissionDenied");
            return false;
        }
    }

    /**
     * Remove a player or group from the cuboid with given name (index 1)
     *
     * @param player
     * @param command
     * @return
     */
    public boolean disallowEntity(Player player, String[] command) {
        Region cube = RegionManager.get().getRegionByName(command[1], player.getWorld().getFqName());
        if (cube == null) {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            return false;
        }
        if (cube.playerIsOwner(player.getName()) || player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN)) {
            for (int i = 2; i < command.length; i++) {
                if (command[i].startsWith("g:")) {
                    cube.removeGroup(command[i]);
                }
                else {
                    cube.removePlayer(command[i]);
                }
            }

            RegionManager.get().updateRegion(cube);
            MessageSystem.successMessage(player, "cuboidUpdated");
            return true;

        }
        else {
            MessageSystem.failMessage(player, "cuboidNotUpdated");
            return false;
        }
    }

    /**
     * Allow an entity into the area given with index 1 of command array
     *
     * @param player
     * @param command
     * @return
     */
    public boolean allowEntity(Player player, String[] command) {
        Region cube = RegionManager.get().getRegionByName(command[1], player.getWorld().getFqName());
        if (cube != null) {
            if (cube.playerIsOwner(player.getName()) ||player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN)) {

                for (int i = 3; i < command.length; i++) {
                    if (command[i].startsWith("g:")) {
                        cube.addGroup(command[i]);
                    }
                    else {
                        cube.addPlayer(command[i]);
                    }
                }
                RegionManager.get().updateRegion(cube);
                MessageSystem.successMessage(player, "cuboidUpdated");
                return true;
            }
            else {
                MessageSystem.failMessage(player, "playerNotOwner");
                return false;
            }
        }
        else {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            return false;
        }
    }

    /**
     * Remove items from the list of restricted items
     *
     * @param player
     * @param command
     * @return
     */
    public boolean allowItem(Player player, String[] command) {
        Region cube = RegionManager.get().getRegionByName(command[1], player.getWorld().getFqName());
        if (cube != null) {
            if (cube.playerIsOwner(player.getName()) || player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN)) {

                for (int i = 3; i < command.length; i++) {
                    cube.removeRestrictedItem(command[i]);
                }
                RegionManager.get().updateRegion(cube);
                MessageSystem.successMessage(player, "cuboidUpdated");
                return true;
            }
            else {
                MessageSystem.failMessage(player, "playerNotOwner");
                return false;
            }
        }
        else {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            return false;
        }
    }

    public boolean disallowItem(Player player, String[] command) {
        Region cube = RegionManager.get().getRegionByName(command[0], player.getWorld().getFqName());
        if (cube != null) {
            if (cube.playerIsOwner(player.getName()) || player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN)) {

                for (int i = 1; i < command.length; i++) {
                    cube.addRestrictedItem(command[i]);
                }
                RegionManager.get().updateRegion(cube);
                MessageSystem.successMessage(player, "cuboidUpdated");
                return true;
            }
            else {
                MessageSystem.failMessage(player, "playerNotOwner");
                return false;
            }
        }
        else {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            return false;
        }
    }

    /**
     * Put a command on the list of not allowed commands
     *
     * @param player
     * @param command
     * @param cubeName
     * @return
     */
    public boolean restrictCommand(Player player, String[] command, String cubeName) {
        Region cube = RegionManager.get().getRegionByName(cubeName, player.getWorld().getFqName());
        if (cube != null) {
            if (cube.playerIsOwner(player.getName()) || player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN)) {
                for (int i = 3; i < command.length; i++) {
                    cube.addRestrictedCommand(command[i]);
                }
                RegionManager.get().updateRegion(cube);
                MessageSystem.successMessage(player, "cuboidUpdated");
                return true;
            }
        }
        return false;
    }

    /**
     * Remove a command from te command blacklist
     *
     * @param player
     * @param command
     * @param cubeName
     * @return
     */
    public boolean allowCommand(Player player, String[] command, String cubeName) {
        Region cube = RegionManager.get().getRegionByName(cubeName, player.getWorld().getFqName());
        if (cube != null) {
            if (cube.playerIsOwner(player.getName()) || player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN)) {
                for (int i = 3; i < command.length; i++) {
                    cube.removeRestrictedCommand(command[i]);
                }
                RegionManager.get().updateRegion(cube);
                MessageSystem.successMessage(player, "cuboidUpdated");
                return true;
            }
        }
        return false;
    }

    /**
     * Resize or move the cuboid bounding rectangle
     *
     * @param player
     * @param cuboidName
     * @return
     */
    public boolean resize(Player player, String cuboidName) {
        Region cube = RegionManager.get().getRegionByName(cuboidName, player.getWorld().getFqName());
        if (cube != null) {
            if (cube.playerIsOwner(player.getName()) || player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN)) {
                CuboidSelection selection = SelectionManager.get().getPlayerSelection(player.getName());
                if (!selection.isComplete()) {
                    MessageSystem.failMessage(player, "selectionIncomplete");
                    return false;
                }
                cube.setBoundingBox(selection.getOrigin(), selection.getOffset(), false);
                RegionManager.get().updateRegion(cube);
                RegionManager.get().autoSortRegions();

                RegionManager.get().saveSingle(RegionManager.get().getRegionByName(cube.getName(), cube.getWorld()));
                MessageSystem.successMessage(player, "cuboidMoved");
                return true;
            }
            else {
                MessageSystem.failMessage(player, "playerNotOwner");
                return false;
            }
        }
        else {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            return false;
        }
    }

    /**
     * Set the priority of a cuboid
     *
     * @param player
     * @param cubeName
     * @param prio
     */
    public void setPriority(Player player, String cubeName, int prio) {
        Region cube = RegionManager.get().getRegionByName(cubeName, player.getWorld().getFqName());
        if (cube != null) {
            if (cube.playerIsOwner(player.getName()) || player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN)) {
                cube.setPriority(prio);
                RegionManager.get().updateRegion(cube);
                MessageSystem.successMessage(player, "prioritySet");
                return;
            }
            else {
                MessageSystem.failMessage(player, "priorityNotSet");
                MessageSystem.failMessage(player, "playerNotOwner");
                return;
            }
        }
        MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
    }

    public void setParent(Player player, String subject, String parent) {
        Region cube = RegionManager.get().getRegionByName(subject, player.getWorld().getFqName());
        if (cube != null) {
            if (cube.playerIsOwner(player.getName()) || player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN)) {
                Region parentCube = RegionManager.get().getRegionByName(parent, player.getWorld().getFqName());
                if (parentCube == null) {
                    MessageSystem.failMessage(player, "parentNotSet");
                    MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
                    return;
                }

                // Check if the specified parent is a valid parent (parent
                // encloses child 100%)
                if (cube.cuboidIsWithin(parentCube, true)) {
                    cube.setParent(parentCube);
                    if (cube.getPriority() <= parentCube.getPriority()) {
                        cube.setPriority(parentCube.getPriority() + 1);
                    }
                    RegionManager.get().updateRegion(cube);
                    MessageSystem.successMessage(player, "parentSet");
                    return;
                }
                else {
                    MessageSystem.failMessage(player, "notWithinSpecifiedParent");
                    MessageSystem.failMessage(player, "parentNotSet");
                    return;
                }
            }
            else {
                MessageSystem.failMessage(player, "playerNotOwner");
                return;
            }
        }
        MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
    }

    /*
     * **************************************************************************************
     * **************************************************************************************
     * INFORMATION RETRIEVAL
     */

    /**
     * Explain a cuboid. This is called when using the inspector tool
     *
     * @param player
     * @param position
     * @param showBlockInfo
     */
    public void explainRegion(Player player, Location position, boolean showBlockInfo) {
        Region node = RegionManager.get().getActiveRegion(position, true);

        if (node != null) {
            player.message(ColorManager.LightGray + "---- " + node.getName() + " ----");
            if (node.playerIsAllowed(player, player.getPlayerGroups())) {
                if (node.playerIsOwner(player.getName())) {
                    MessageSystem.translateMessage(player, ColorManager.LightGreen, "youOwnThisArea");
                }
                else {
                    MessageSystem.translateMessage(player, ColorManager.LightGray, "youCanBuildHere");
                }
            }
            else if (player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN)) {
                MessageSystem.translateMessage(player, ColorManager.LightGreen, "youCanBuildHere");
            }
            else {
                MessageSystem.translateMessage(player, ColorManager.Rose, "youCanNotBuildHere");
            }
            MessageSystem.translateMessage(player, ColorManager.LightGray, "players");
            player.message(ColorManager.Yellow + node.getPlayerList());

            MessageSystem.translateMessage(player, ColorManager.LightGray, "groups");
            player.message(ColorManager.Yellow + node.getGroupList());

            String commands = node.getRestrictedCommands().toString();
            String items = node.getItemListAsNames();
            if (!commands.isEmpty()) {
                MessageSystem.translateMessage(player, ColorManager.LightGray, "disallowedCommands");
                player.message(ColorManager.Yellow + commands);
            }

            if (!items.isEmpty()) {
                MessageSystem.translateMessage(player, ColorManager.LightGray, "disallowedItems");
                player.message(ColorManager.Yellow + items);
            }

            if (node.getParent() == null) {
                //TODO: Translate parent? hmmm
                player.message(ColorManager.LightGray + "Parent: " + ColorManager.Yellow + "none");
            }
            else {
                player.message(ColorManager.LightGray + "Parent: " + ColorManager.Yellow + node.getParent().getName());
            }
        }
        else {
            MessageSystem.yellowNote(player, "noCuboidFound");
        }
        if (showBlockInfo) {
            Block current = player.getWorld().getBlockAt(position);
            MessageSystem.customMessage(player, ColorManager.Blue, "Block: " + Translator.nativeTranslate(current.getType().getMachineName()));
        }
    }

    public void explainRegion(Player player, String name) {
        Region node = RegionManager.get().getRegionByName(name, player.getWorld().getFqName());

        if (node != null) {
            player.message(ColorManager.LightGray + "---- " + node.getName() + " ----");
            if (node.playerIsAllowed(player, player.getPlayerGroups())) {
                if (node.playerIsOwner(player.getName())) {
                    MessageSystem.translateMessage(player, ColorManager.LightGreen, "youOwnThisArea");
                }
                else {
                    MessageSystem.translateMessage(player, ColorManager.LightGray, "youCanBuildHere");
                }
            }
            else if (player.hasPermission(Permissions.REGION$EDIT$ANY) || player.hasPermission(Permissions.ADMIN)) {
                MessageSystem.translateMessage(player, ColorManager.LightGreen, "youCanBuildHere");
            }
            else {
                MessageSystem.translateMessage(player, ColorManager.Rose, "youCanNotBuildHere");
            }

            MessageSystem.translateMessage(player, ColorManager.LightGray, "players");
            player.message(ColorManager.Yellow + node.getPlayerList());

            MessageSystem.translateMessage(player, ColorManager.LightGray, "groups");
            player.message(ColorManager.Yellow + node.getGroupList());

            String commands = node.getRestrictedCommands().toString();
            String items = node.getItemListAsNames();
            if (!commands.isEmpty()) {
                MessageSystem.translateMessage(player, ColorManager.LightGray, "disallowedCommands");
                player.message(ColorManager.Yellow + commands);
            }

            if (!items.isEmpty()) {
                MessageSystem.translateMessage(player, ColorManager.LightGray, "disallowedItems");
                player.message(ColorManager.Yellow + items);
            }

            if (node.getParent() == null) {
                //TODO: Translate parent? hmmm
                player.message(ColorManager.LightGray + "Parent: " + ColorManager.Yellow + "none");
            }
            else {
                player.message(ColorManager.LightGray + "Parent: " + ColorManager.Yellow + node.getParent());
            }
        }
        else {
            MessageSystem.yellowNote(player, "noCuboidFound");
        }
    }

    /**
     * Display a list of cuboids in a given world and dimension
     *
     * @param player
     * @param page
     */
    public void displayCuboidList(Player player, int page) {
        String world = player.getWorld().getFqName();
        String dimName = player.getWorld().getType().getName();

        int perPage = 10, maxPages, amount;
        ArrayList<Region> cuboids = RegionManager.get().getAllInDimension(world);
        // Following is all taken from CuboidPlugin
        // Because I suck at making paging
        if (cuboids == null || cuboids.isEmpty()) {
            MessageSystem.translateMessage(player, ColorManager.LightGray, "noCuboidsInworldAndDimemsion", world, dimName);
            //(player, ColorManager.LightGray, "No cuboids for world " + world + " in Dimension " + dimName);
            return;
        }
        maxPages = (int) Math.ceil(cuboids.size() / perPage);
        if ((cuboids.size() % perPage) > 0) {
            maxPages++;
        }
        if (page > maxPages) {
            page = 1;
        }
        amount = (page - 1) * perPage;

        MessageSystem.translateMessage(player, ColorManager.Yellow, "cuboidsInWorld", dimName, world, Integer.toString(page), Integer.toString(maxPages));
        for (int i = amount; i < (amount + perPage); i++) {
            if (cuboids.size() <= i) {
                break;
            }
            Region cuboid = cuboids.get(i);
            MessageSystem.customMessage(player, ColorManager.LightGray, cuboid.getName() + " : " + cuboid.getFlagList());
        }
    }

    public void showCommandBlacklist(Player player, String cubeName) {
        Region cube = RegionManager.get().getRegionByName(cubeName, player.getWorld().getFqName());
        if (cube == null) {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            return;
        }
        if (!player.hasPermission(Permissions.ADMIN)) {
            if (!(cube.playerIsOwner(player.getName()) || player.hasPermission(Permissions.REGION$EDIT$ANY))) {
                MessageSystem.failMessage(player, "permissionDenied");
                return;
            }
        }
        MessageSystem.translateMessage(player, ColorManager.LightGray, "restrictedCommandsForCuboid", cube.getName());
        player.message(ColorManager.Rose + cube.getRestrictedCommands().toString());
    }

    public void showCommand(Player player, String cubeName) {
        Region cube = RegionManager.get().getRegionByName(cubeName, player.getWorld().getFqName());
        if (cube == null) {
            MessageSystem.failMessage(player, "cuboidNotFoundOnCommand");
            return;
        }

        Canary.getServer().addSynchronousTask(new ShowTask(Config.getPlugin(), 10, player, cube.getOuterTopBlocks(cube), 10));
    }

    public void killTasks() {
        threadManager.shutdown();

    }
}
