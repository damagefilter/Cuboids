package net.playblack.cuboids;

import java.util.HashMap;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.mcutils.ColorManager;

/**
 * Message System.
 * To send messages to a player or global chat. depending
 * @author Chris
 *
 */
public class MessageSystem {
    private HashMap<String,String> messages = new HashMap<String,String>();
    private HashMap<String,String> errors = new HashMap<String,String>();
    private static MessageSystem ms = null;
    private MessageSystem() {
        messages.put("pvpEnabled", "PVP is now enabled!");
        messages.put("pvpDisabled", "PVP is now disabled!");
        
        messages.put("blockFireSpreadEnabled", "Firespread is now blocked!");
        messages.put("blockFireSpreadDisabled", "Firespread is now enabled! (it spreads)");
        
        messages.put("creeperSecureEnabled", "Creeper Security is now enabled!");
        messages.put("creeperSecureDisabled", "Creeper Security is now disabled! (they go boom)");
        
        messages.put("creativeEnabled", "Freebuild is now enabled!");
        messages.put("creativeDisabled", "Freebuild is now disabled!");
        
        messages.put("farmlandEnabled", "Farmland is now enabled!");
        messages.put("farmlandDisabled", "Farmland is now disabled!");
        
        messages.put("healingEnabled", "Healing is now enabled!");
        messages.put("healingDisabled", "Healing is now disabled!");
        
        messages.put("hmobEnabled", "HMobs are now enabled!");
        messages.put("hmobDisabled", "HMobs are now disabled!");
        
        messages.put("lavaFlowControlEnabled", "Lava Flow Control is now enabled!");
        messages.put("lavaFlowControlDisabled", "Lava Flow Control is now disabled!");
        
        messages.put("waterFlowControlEnabled", "Water Flow Control is now enabled!");
        messages.put("waterFlowControlDisabled", "Water Flow Control is now disabled!");
        
        messages.put("protectionEnabled", "Protection is now enabled!");
        messages.put("protectionDisabled", "Protection is now disabled!");
        
        messages.put("restrictionEnabled", "Restriction is now enabled!");
        messages.put("restrictionDisabled", "Restriction is now disabled!");
        
        messages.put("sanctuaryEnabled", "Sanctuary is now enabled!");
        messages.put("sanctuaryDisabled", "Sanctuary is now disabled!");
        
        messages.put("sanctuaryAnimalSpawnEnabled", "Sanctuary now spawns animals!");
        messages.put("sanctuaryAnimalSpawnDisabled", "Sanctuary does not spawn animals now!");
        
        messages.put("tntSecureSpawnEnabled", "Tnt Security is now enabled!");
        messages.put("tntSecureDisabled", "Tnt Security is now disabled!");
        
        messages.put("cuboidRemoved", "Cuboid was successfully removed");
        messages.put("cuboidCreated", "Your Cuboid has been created!");
        messages.put("cuboidMoved", "Your Cuboid has been moved!");
        
        messages.put("selectionFilled", "Your selection was filled!");
        messages.put("selectionReplaced", "Your selection was replaced!");
        messages.put("selectionDeleted", "The blocks in your selection have been removed!");
        
        messages.put("brushSet", "Your brush properties have been set");
        messages.put("copiedToClipboard", "Blocks have been copied to your personal clipboard");
        messages.put("selectionPasted", "Your selection has been pasted into the world!");
        messages.put("selectionMoved", "Your selection has been moved!");
        messages.put("wallsCreated", "The walls have been build!");
        messages.put("facesCreated", "Selection faces have been outlined!");
        messages.put("sphereCreated", "A sphere has been created!");
        messages.put("pyramidCreated", "A pyramid has been created! Egyptian spirits!");
        messages.put("discCreated", "A cylinder has been created!");
        messages.put("circleCreated", "A circle has been created!");
        messages.put("undoDone", "Undo successful!");
        messages.put("redoDone", "Redo successful!");
        messages.put("ceilingSet", "Ceiling level has been set!");
        messages.put("floorSet", "Floor level has been set!");
        messages.put("selectionExpanded", "Selection has been expanded! (Bottom-to-Top)");
        messages.put("backupSuccess", "Cuboid backup stored!"); 
        messages.put("restoreSuccess", "Cuboid has been restored!");
        messages.put("cuboidUpdated", "Cuboid has been updated!");
        messages.put("prioritySet", "Cuboid priority has been set");
        messages.put("parentSet", "Cuboids parent has been set"); 
        
        messages.put("cuboidSaved", "Cuboid has been saved!");
        messages.put("cuboidSavedAll", "Cuboid has been saved!");
        messages.put("cuboidLoaded", "Cuboid has been loaded!");
        messages.put("cuboidLoadedAll", "Cuboids have been loaded!");
        
        messages.put("globalPvpOn", "Global pvp enabled!"); 
        messages.put("globalPvpOff", "Global pvp disabled!");
        messages.put("globalCreeperOn", "Global Creeper protection enabled!"); 
        messages.put("globalCreeperOff", "Global Creeper protection disabled!");
        messages.put("globalSanctuaryOn", "Global Sanctuary enabled!"); 
        messages.put("globalSanctuaryOff", "Global Sanctuary disabled!");
        messages.put("globalSanctuaryASOn", "Global Sanctuary animal spawn enabled!"); 
        messages.put("globalSanctuaryASOff", "Global Sanctuary animal spawn disabled!");
        messages.put("globalTntOn", "Global TNT protection enabled!"); 
        messages.put("globalTntOff", "Global TNT protection disabled!");
        messages.put("globalProtectionOn", "Global protection enabled!"); 
        messages.put("globalProtectionOff", "Global protection disabled!");
        messages.put("globalLavaOn", "Global Lavaflow control enabled!"); 
        messages.put("globalLavaOff", "Global Lavaflow control disabled!");
        messages.put("globalWaterOn", "Global Waterflow control enabled!"); 
        messages.put("globalWaterOff", "Global Waterflow control disabled!");
        // *************************************************
        //
        // *************************************************
        errors.put("permissionDenied", "You don't have permission to do this!");
        errors.put("invalidCharacters", "Name contains invalid characters!");
        errors.put("playerNotOwner", "You don't own this area!");
        errors.put("optionDisabled", "This option is disabled!");
        errors.put("cuboidNotFoundOnCommand", "There was no Cuboid with the provided name!");
        errors.put("cuboidNotRemovedHasChilds", "Cuboid was not removed. It has child elements!");
        errors.put("cuboidNotRemovedNotFound", "Cuboid was not removed. It was not found!");
        errors.put("cuboidNotRemovedError", "Cuboid was not removed. Bugmonster ate the code :S");
        errors.put("cuboidExists", "A cuboid with this name already exists!");
        errors.put("invalidBlock", "The provided block was invalid!");
        errors.put("selectionIncomplete", "The selection is not completed. Please set start and end point!");
        errors.put("selectionNotFilled", "Your selection was not filled!");
        errors.put("selectionNotReplaced", "Your selection was not replaced!");
        errors.put("selectionNotDeleted", "The blocks in your selection have not been removed!");
        errors.put("cuboidNotCreated", "Your Cuboid has not been created. Choose a different name!");
        errors.put("invalidRadius", "The selected radius is invalid. What did you do, put a char instead of a number? Dude!");
        errors.put("invalidCardinalDirection", "You specified an invalid cardinal direction! Use SOUTH/WEST/NORTH/EAST/UP/DOWN");
        errors.put("selectionNotMoved", "Your selection has not been moved! Blocks seem to be move resistant. Try again.");
        errors.put("selectionNotPasted", "Your selection has not been pasted. The bugmonster ate it!");
        errors.put("originNotSet", "You must specify the first point for this operation!");
        errors.put("wallsNotCreated", "Walls were not created!");
        errors.put("sphereNotCreated", "Your sphere has not been created!");
        errors.put("pyramidNotCreated", "Your pyramid has not been created!");
        errors.put("discNotCreated", "Cylinder/circle/disk not created!");
        errors.put("invalidHeight", "The height you specified was invalid! (How can you fail at this?)");
        errors.put("invalidPriority", "You specified an invalid priority!");
        errors.put("undoDisabled", "Undo is disabled!");
        errors.put("restoreFail", "Cuboid restore has failed!");
        errors.put("priorityNotSet", "Cuboid Priority has not been set");
        errors.put("parentNotSet", "Cuboids parent has not been set");
        errors.put("notWithinSpecifiedParent", "Cuboid is not within the specified parent node.");
    }
    public static MessageSystem getInstance() {
        if(ms == null) {
            ms = new MessageSystem();
        }
        return ms;
    }
    
    /**
     * Send a red message to the player from the error message pool
     * @param player
     * @param messageKey
     */
    public void failMessage(CPlayer player, String messageKey) {
        player.notify(errors.get(messageKey));
    }
    
    /**
     * Send a green message to the player from the messages pool
     * @param player
     * @param messageKey
     */
    public void successMessage(CPlayer player, String messageKey) {
        player.sendMessage(ColorManager.Green + messages.get(messageKey));
    }
    
    /**
     * Send a yellow custom message to the player
     * @param player
     * @param messageKey
     */
    public void notification(CPlayer player, String message) {
        player.sendMessage(ColorManager.Yellow + message);
    }
    
    /**
     * Send a red custom message
     * @param player
     * @param message
     */
    public void customFailMessage(CPlayer player, String message) {
        player.notify(message);
    }
    
    public static void customMessage(CPlayer player, String color, String message) {
        player.sendMessage(color+message);
    }
}
