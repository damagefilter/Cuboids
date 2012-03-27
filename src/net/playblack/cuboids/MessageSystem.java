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
        // *************************************************
        //
        // *************************************************
        errors.put("permissionDenied", "You don't have permission to do this!");
        errors.put("playerNotOwner", "You don't own this area!");
        errors.put("optionDisabled", "This option is disabled!");
        errors.put("cuboidNotFoundOnCommand", "There was no Cuboid with the provided name!");
        errors.put("cuboidNotRemovedHasChilds", "Cuboid was not removed. It has child elements!");
        errors.put("cuboidNotRemovedNotFound", "Cuboid was not removed. It was not found!");
        errors.put("cuboidNotRemovedError", "Cuboid was not removed. Unknown error occured :S");
        errors.put("invalidBlock", "The provided block was invalid!");
        errors.put("selectionIncomplete", "The selection is not completed. Please set start and end point!");
        errors.put("selectionNotFilled", "Your selection was not filled!");
        errors.put("selectionNotReplaced", "Your selection was not replaced!");
        errors.put("selectionNotDeleted", "The blocks in your selection have not been removed!");
        errors.put("cuboidNotCreated", "Your Cuboid has not been created. Choose a different name!");
        errors.put("invalidRadius", "The selected radius is invalid. What did you do, put a char instead of a number? Dude!");
        
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
}
