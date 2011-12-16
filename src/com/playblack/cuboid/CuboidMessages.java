package com.playblack.cuboid;

import java.util.HashMap;

public class CuboidMessages {
	
	public HashMap<String,String> messages = new HashMap<String,String>();
	
	public HashMap<String,String> help = new HashMap<String,String>();
	
	public CuboidMessages() {
		//COMMAND MANAGEMENT AND ALL
		messages.put("firstPointSet", "First Point set.");
		messages.put("secondPointSet", "Second Point set.");
		messages.put("selectionExpanded", "Expanded selection vertically!");
		messages.put("onDisable", "Cuboids2 disabled");
		messages.put("onEnable", "Cuboids2 enabled");
		
		messages.put("selectionFilled", "The selection has been filled");
		messages.put("selectionReplaced", "The selection has been replaced!");
		messages.put("selectionIncomplete", "Selection is not completed. Please specify start- and end point");
		
		messages.put("missingArgumentsBlockId", "Please specify a valid block for Cuboids to work with.");
		messages.put("missingArgumentsUndo", "Please specify how many steps to revert.");
		messages.put("missingArguments", "Not enough information to work with, try more arguments");
		
		messages.put("invalidBlock", "The Block you specified is invalid. Try again.");
		messages.put("invalidCharacters", "Your Cuboid name contains invalid characters! (, and/or : )");
		messages.put("invalidParameterTypeInteger", "This commands expects a number as parameter. Try again!");
		messages.put("invalidParameterTypeString", "This commands expects a String/Word as parameter. Try again!");
		
		messages.put("actionsUndone", "Your action has been reverted.");
		messages.put("UndoDisabled", "The undo function is disabled.");
		
		messages.put("actionsRedone", "Your action has been re-done.");
		messages.put("RedoDisabled", "The redo function is disabled (because undo is disabled).");
		
		//MANAGEMENT / TREE HANDLING
		messages.put("cuboidAdded", "Cuboid created and saved");
		messages.put("cuboidNotAdded", "Cuboid could not be created.");
		messages.put("cuboidNotRemovedError", "Cuboid could not be removed because of an internal error.");
		messages.put("cuboidRemoved", "Cuboid was removed.");
		messages.put("cuboidNotRemovedHasChilds", "Cuboid not removed because it has Child-Cuboids.");
		messages.put("cuboidNotRemovedNotFound", "Cuboid was not removed because it wasn't found.");
		messages.put("cuboidUpdated", "Cuboid was updated successfully.");
		messages.put("cuboidExist", "A Cuboid with this name already exist. Please specify a different name");
		messages.put("noCuboidFound", "There is no Cuboid.");
		messages.put("noCuboidFoundOnCommand", "There is no Cuboid with the name given.");
		messages.put("cuboidBackupSuccess", "Your Cuboid has been backed up.");
		messages.put("cuboidBackupFail", "Your Cuboid has not been backed up.");
		messages.put("cuboidRestoreSuccess", "Your Cuboid has been restored!");
		messages.put("cuboidRestoreFail", "Your Cuboid has not been restored!");
		
		messages.put("prioritySet", "Priority was updated.");
		messages.put("priorityNotSet", "Priority was not updated. Something went wrong.");
		
		messages.put("parentSet", "Parent was updated.");
		messages.put("parentNotSet", "Parent was not updated. (Perhaps it does not exist)");
		
		messages.put("cuboidSaved", "Cuboid has been saved to disk!");
	    messages.put("cuboidNotSaved", "Cuboid has not been saved to disk. It probably does not exist yet!");

	    messages.put("cuboidLoaded", "Cuboid has been loaded from disk!");
	    messages.put("cuboidNotLoaded", "Cuboid has not been loaded from disk. It probably does not exist!");
		
		messages.put("globalCreeperDisabled", "Global Creeper protection is now disabled!");
		messages.put("globalCreeperEnabled", "Global Creeper protection is now enabled!");
		
		messages.put("globalSanctuaryDisabled", "Global Sanctuary is now disabled!");
		messages.put("globalSanctuaryEnabled", "Global Sanctuary is now enabled!");
		
		messages.put("globalSanctuaryAnimalsDisabled", "Global Sanctuary now spawns animals!");
		messages.put("globalSanctuaryAnimalsEnabled", "Global Sanctuary now does not spawn animals!");
		
		messages.put("globalPvpDisabled", "Global PVP is now ENABLED");
		messages.put("globalPvpEnabled", "Global PVP is now DISABLED");
		
		messages.put("globalTntDisabled", "Global TNT protection is now disabled");
		messages.put("globalTntEnabled", "Global TNT protection is now enabled");
		
		messages.put("globalLavaFlowDisabled", "Global Lava Flow Control is disabled");
		messages.put("globalLavaFlowEnabled", "Global Lava Flow Control is enabled");
		
		messages.put("globalWaterFlowDisabled", "Global Water Flow Control is disabled");
		messages.put("globalWaterFlowEnabled", "Global Water Flow Control is enabled");
		
		messages.put("globalFirespreadEnabled", "Global Firespread is now blocked.");
		messages.put("globalFirespreadDisabled", "Global Firespread is not blocked anymore.");
		
		messages.put("globalProtectionDisabled", "Global protection is now disabled");
		messages.put("globalProtectionEnabled", "Global protection is now enabled");
		
		messages.put("cuboidCommandBlacklisted", "The command is now restricted!");
		messages.put("cuboidCommandAllowed", "The command is no longer restricted!");
		
		//PLAYER FAULTS >:D
		messages.put("playerNotOwner", "You are not the owner of this cuboid.");
		messages.put("permissionDenied", "You don't have the permission to do that!");
		messages.put("commandDenied", "This command is not allowed around here!");
		messages.put("optionDisabled", "This option is disabled and can't be changed.");
		messages.put("areaTooLarge", "Cannot backup area. It is too large, sorry");
		messages.put("noCopy", "You must use /ccopy before pasting!");
		messages.put("pasteFailed", "Pasting failed. Selection is incomplete!");
		messages.put("invalidRadius", "The radius specified is invalid!(Not a number)");
		messages.put("negativeNumber", "A number you entered was smaller than 0! Try again with 0 or greater.");
		messages.put("invalidHeight", "The height specified is invalid!(Not a number)");
		messages.put("cannotParentChild", "Can't parent - Child would not be inside the desired parent.");
		
		//Ceiling/Flooring/Copy/paste etc
		messages.put("cuboidCeiled", "Ceiling height has been set!");
		messages.put("cuboidFloored", "Floor level has been set");
		messages.put("cuboidCopied", "Selection copied!");
		messages.put("cuboidPasted", "Selection pasted!");
		messages.put("cuboidMoved", "Selection has been moved!");
		
		messages.put("sphereCreated", "The sphere has been build!");
		messages.put("pyramidCreated", "The pyramid has been build!");
		messages.put("wallsCreated", "The walls have been build!");
		
		//Brush Tool
		messages.put("brushSet", "Updated Brush settings!");
		
		//help.put("", "");
		help.put("cmodHelp", "/cmod Basic Help(For more RTFM plx :) )");
		
		help.put("cmodCreate", "Create area: /cmod <areaname> create");
		help.put("cmodDelete", "Delete area: /cmod <areaname> delete");
		
		help.put("cmodAllow", "Allow players/groups: /cmod <areaname> allow <list of players/groups>");
		help.put("cmodDisallow", "Disallow players/groups: /cmod <areaname> disallow <list of players/groups>");
		
		help.put("cmodAllowCommand", "Allow commands: /cmod <areaname> restrictcommand <list of players/groups>");
		help.put("cmodDisallowCommand", "Disallow commands: /cmod <areaname> allowcommand <list of players/groups>");
		
		help.put("cmodToggle", "Toggle area flags: /cmod <areaname> toggle protection/pvp/creeper/sanctuary/heal/creative/fire");
		
		help.put("cmodList", "List all Cuboids: /cmod list");
		
		help.put("cmodResize", "Move/Resize and area: /cmod <areaname> resize");
		
		help.put("cmodPrio", "Set area priority: /cmod <areaname> prio <number>");
		
		help.put("cmodGlobalToggle", "Toggle global flags: /cmod toggle pvp/creeper");
		
		help.put("cmove_base", "Move Blocks: /cmove <direction> <distance>");
		help.put("cmove_directions", "Directions: Up, Down, North, East, South, West");
		help.put("cmodWelcome", "/cmod <areaname> welcome [message] Set Welcome message");
	    help.put("cmodFarewell", "/cmod <areaname> farewell [message] Set Farewell message");

	    help.put("cbrush", "/cbrush <radius> <block> [data] - Set brush for sculpt tool");
	    help.put("cfill", "/cfill <block> [data] - Fill area with blocks");
	    help.put("creplace", "/creplace <block> [data] <block> [data] - Replace blocks");
	    help.put("csphere", "/csphere <radius> <block> [data] [hollow] - Create a sphere");
	    help.put("cdisc", "/cdisc <radius> <block> [data] [height] - Create a disc around a selected point");
	    help.put("ccircle", "/ccircle <radius> <block> [data] [height] - Create a circle around a selected point");
	    help.put("cwalls", "/cwalls <block> [data] - Create the walls of a selection (Cuboid)");
	    help.put("cfaces", "/cfaces <block> [data] - Create all faces of a selection (Cuboid)");
	    help.put("cpyramid", "/cpyramid <radius> <block> [data] [hollow] - Create a pyramid");
	    help.put("cundo", "/cundo <steps> - Undo the last X operations");
	    help.put("cceiling", "/cceiling <steps> - Set top level of Selection manually");
	    help.put("cfloor", "/cfloor <steps> - Set bottom level of Selection manually");
	    help.put("highprotect", "/highprotect <players/groups> <area name> - Highprotect the area");
	    help.put("protect", "/protect <players/groups> <area name> - Protect the area");
	    help.put("csave", "/csave <area name> - Save the area file");
	    help.put("csaveall", "/csaveall - Save all areas also unchanged ones");
	    help.put("cload", "/cload <area name> - Load the area file");
	    help.put("cloadfrom", "/cloadfrom <flatfile | mysql> - Load all areas from the specified data source");	
	}
}
