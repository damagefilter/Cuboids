import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import com.playblack.EventLogger;
import com.playblack.ToolBox;
import com.playblack.blocks.WorldBlock;
import com.playblack.cuboid.CuboidE;
import com.playblack.cuboid.CuboidSelection;
import com.playblack.cuboid.tree.CuboidNode;

import com.playblack.vector.Vector;


/**
 * This interfaces Canary Mod with the CuboidE and CuboidTree
 * This interfce handles cuboid regions<br>
 * This is only here because Canary doesn't utilise packages and we connot
 * import Canary methods in custom packages but it works okay alright.
 * @author Chris
 *
 */
public class CuboidProtectionInterface {
	//static CuboidTreeCuboids2.treeHandler Cuboids2.treeHandler;
	CuboidSelection selection;
	Timer healjob = new Timer();
	ToolBox toolBox = new ToolBox();
	EventLogger log;
	Object lock = new Object();
	ArrayList<String> noTeleportList = new ArrayList<String>(0);
	CuboidsConverter conv;
	CuboidSelection myContent;
	HashMap<String,Item[]> playerInventories = new HashMap<String,Item[]>(0);
	
	/**
	 * Default constructor, creates a new Cuboids2.treeHandler for data tree
	 */
	public CuboidProtectionInterface(EventLogger log)
	  {
		this.log = log;
		conv = new CuboidsConverter(log);
	    if (this.conv.loadCuboids()) {
	      //SysteCuboids2.msg.out.println("Cuboids2: Converting CuboidD FS to CuboidE FS.");
	      ArrayList<CuboidE> cubes = this.conv.convertFS();

	      log.logMessage("Cuboids2: Saving Cuboids to file now", "INFO");
	      log.logMessage("Cuboids2: Prepare for log spam. It's better that way so you know what went wrong, if at all!", "INFO");
	      for (int i = 0; i < cubes.size(); i++)
	      {
	        if (cubes.get(i) != null) {
	          ((CuboidE)cubes.get(i)).hasChanged = true;
	          Cuboids2.treeHandler.addCuboid(cubes.get(i));
	        }
	        else { //should never happen
	        	log.logMessage("Cuboids2: Failing! CuboidPanic!", "WARNING");
	        }
	      }
	      log.logMessage("Cuboids2: Done converting. Sorting out parent relations and priorities...", "INFO");
	      Cuboids2.treeHandler.save(false, true);
	      Cuboids2.treeHandler.autoSortCuboidAreas();
	      log.logMessage("Cuboids2: Finished the sorting for you. Please remove/move away the old files now.", "INFO");
	    }
	    Cuboids2.treeHandler.load();
	    Cuboids2.treeHandler.scheduleSave(Cuboids2.cfg.getSaveDelay());
	  }

	/**
	 * Check whether a cuboid with the given name in the given world already exist.
	 * @param name
	 * @param world
	 * @return True if so, false otherwise
	 */
	public boolean cuboidExist(String name, String world) {
		return Cuboids2.treeHandler.cuboidExists(name, world);
	}
	
	public boolean addCuboid(CuboidE cube) {
		if(Cuboids2.cfg.autoParent()) {
			//Override default with parent settings and make parent stuff
				CuboidNode parent = Cuboids2.treeHandler.getPossibleParent(cube);
				if(parent != null) {
					cube.setParent(parent.getCuboid().getName());
					cube.setPriority(parent.getCuboid().getPriority()+1);
					cube.overwriteProperties(parent.getCuboid());
					return Cuboids2.treeHandler.addCuboid(cube);
				}
				else {
					return Cuboids2.treeHandler.addCuboid(cube);
				}
		}
		else {
			return Cuboids2.treeHandler.addCuboid(cube);
		}
	}
	
	public boolean removeCuboid(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") 
					|| player.canUseCommand("/cIgnoreRestrictions") || player.canUseCommand("/cdelete")) {
					String response = Cuboids2.treeHandler.removeCuboid(cube, false);
					
					if(response.equalsIgnoreCase("NOT_REMOVED_HAS_CHILDS")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("cuboidNotRemovedHasChilds"));
						return false;
					}
					
					if(response.equalsIgnoreCase("REMOVED")) {
						player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidRemoved"));
						return true;
					}
					
					if(response.equalsIgnoreCase("NOT_REMOVED_NOT_FOUND")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("cuboidNotRemovedNotFound"));
						return false;
					}
					if(response.equalsIgnoreCase("NOT_REMOVED")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("cuboidNotRemovedError"));
						return false;
					}
					
					else {
						player.sendMessage(Colors.Rose+"Unexpected Exception! Sorry! Try again.");
						return false;
					}
				
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied")); //no permissions
				return false;
			}

		} //cuboid not null
		else {
			player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("cuboidNotRemovedNotFound"));
			return false;
		}
	}
	
	/**
	 * Find a cuboidE in the given World at the given vector, which would be the location (goal)
	 * @param World
	 * @param v
	 * @return CuboidE or null if there was no cuboid
	 */
	private CuboidE findCuboid(String world, Vector v) {
		if(v != null) {
			try {
				return Cuboids2.treeHandler.getActiveCuboid(v, world).getCuboid();
			}
			catch(NullPointerException e) {
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	/**
	 * Find a cuboidE in the given World with the given name.
	 * @param World
	 * @param v
	 * @return CuboidE or null if there was no cuboid
	 */
	private CuboidE findCuboid(String world, String name) {
		try {
			return Cuboids2.treeHandler.getCuboidByName(name, world).getCuboid();
		}
		catch(NullPointerException e) {
			return null;
		}
	}
	
	
	/**
	 * Takes a player and the block that was placed/removed from Hook and checks whether player is allowed to place/remove that block.
	 * @param player
	 * @param block
	 * @return Boolean True if player is allowed, false otherwise
	 */
	public boolean canModifyBlock(Player player, Block block) {
		Vector v;
		if(block == null) {
			v = new Vector(player.getX(), player.getY(), player.getZ());
		}
		else {
			v = toolBox.adjustWorldPosition(new Vector(block.getX(), block.getY(), block.getZ()));
		}
		
		CuboidE cube = findCuboid(player.getWorld().getType().name(), v);
		if(cube != null) {
			if(cube.isProtected()) {
				if(cube.playerIsAllowed(player.getName(), player.getGroups()) 
						|| player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return true;
			}
		}
		else {
			if(Cuboids2.cfg.globalProtection()) {
				if(player.canUseCommand("/cIgnoreRestrictions")) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return true;
			}
		}
	}
	
	/**
	 * Explains a cuboid to the player, printing information about Flags, parent, priority etc
	 * This is called when using the inspector tool
	 * @param player
	 * @param position
	 */
	public void explainCuboid(Player player, Vector position ) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), position);
		if(cube != null) {
			player.sendMessage(Colors.LightGray+"---- "+cube.getName()+" ----");
			if(cube.playerIsAllowed(player.getName(), player.getGroups())) {
				if(cube.playerIsOwner(player.getName())) {
					player.sendMessage(Colors.LightGreen+"You own this areas");
				}
				else {
					player.sendMessage(Colors.LightGray+"You can build in this area");
				}
			}
			else if(player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				player.sendMessage(Colors.LightGreen+"You can build in this area");
			}
			else {
				player.sendMessage(Colors.Rose+"You can't build in this area");
			}
			
			player.sendMessage(Colors.LightGray+"Flags: " + Colors.Yellow+cube.getFlagListSimple());
			player.sendMessage(Colors.LightGray+"Players: " + Colors.Yellow+cube.getPlayerList());
			player.sendMessage(Colors.LightGray+"Groups: " + Colors.Yellow+ cube.getGroupList());
			if(cube.getParent() == null) {
				player.sendMessage(Colors.LightGray+"Parent: " + Colors.Yellow+"none");
			}
			else {
				player.sendMessage(Colors.LightGray+"Parent: " + Colors.Yellow+cube.getParent());
			}
		}
		else {
			player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFound"));
		}
		
		
	}
	
	/**
	 * Explains a cuboid to the player, printing information about Flags, parent, priority etc
	 * This is called when using the info or explain commands
	 * @param player
	 * @param position
	 */
	public void explainCuboid(Player player, String name) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), name);
		if(cube != null) {
			player.sendMessage(Colors.LightGray+"---- "+cube.getName()+" ----");
			player.sendMessage(Colors.Yellow+cube.getFlagList());
			player.sendMessage(Colors.Yellow+"Players: "+Colors.LightGray+cube.getPlayerList());
			player.sendMessage(Colors.Yellow+"Groups: "+Colors.LightGray+cube.getGroupList());
			player.sendMessage(Colors.Yellow+"Priority: "+Colors.LightGray+cube.getPriority());
		//	player.sendMessage(Colors.Yellow+"Parent: "+Colors.LightGray+cube.getParent());
			if(cube.getParent() == null) {
				player.sendMessage(Colors.LightGray+"Parent: " + Colors.Yellow+"none");
			}
			else {
				player.sendMessage(Colors.LightGray+"Parent: " + Colors.Yellow+cube.getParent());
			}
		}
		else {
			player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		}
		
		
	}
	
	/**
	 * Check if defender can take damage from attacker 
	 * @param type
	 * @param attacker
	 * @param defender
	 * @param v
	 * @return False if can take damage, true otherwise (that's becuz canary is strange about that :P )
	 */
	public boolean canTakeDamage(PluginLoader.DamageType type, BaseEntity attacker, BaseEntity defender) {
		if(type == PluginLoader.DamageType.ENTITY && defender.isPlayer()) {
			//So this means an entity is attacking a player
			//Get defender information
			Player target = defender.getPlayer();
			Vector targetPosition = new Vector(target.getLocation().x,target.getLocation().y,target.getLocation().z);
			//targetPosition = toolBox.adjustBlockPosition(targetPosition);
			//find the node we are in
			if(attacker.isPlayer()) {
				if(attacker.getPlayer().canUseCommand("/cIgnoreRestrictions")) {
					//etc.getServer().messageAll("Player can ignore PVP settings!");
					return false;
				}
				else {
					//player attacking player ... hm
					CuboidE cube = findCuboid(target.getWorld().getType().name(), targetPosition);
					if(cube != null) {
						if(cube.isAllowedPvp()) {
							return false;
						}
						else {
							return true;
						}
					}
					else {
						if(Cuboids2.cfg.globalDisablePvp()) {
							//etc.getServer().messageAll("PVP Disabled");
							return true;
						}
						else {
							//etc.getServer().messageAll("PVP Enabled");
							return false;
						}
					}
				}
			}
			//Mob, sanctuary!
			if(attacker.isMob() || attacker.isAnimal()) {
				CuboidE cube = findCuboid(target.getWorld().getType().name(), targetPosition);
				if(cube != null) {
					if(cube.isSanctuary()) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
			}	
		}
		return false;
	}
	
	/**
	 * Check if a mob is allowed to spawn.
	 * @param mob
	 * @return Returns false if mobs can spawn, true if they are not allowed to spawn
	 */
	public boolean sanctuarySpawnsMobs(Mob mob) {
		Vector v = new Vector(mob.getX(), mob.getY(), mob.getZ());
		//v = toolBox.adjustBlockPosition(v);
		CuboidE cube = findCuboid(mob.getWorld().getType().name(), v);
		if(cube != null) {
			//log.logMessage("Cube Found, doing Cube Stuff!", "INFO");
			if(mob.isMob()) {
				return cube.isSanctuary();
			}
			if(mob.isAnimal()) {
				return !cube.sanctuarySpawnAnimals();
			}
			else {
				return false;
			}
		}
		else {
			//log.logMessage("No Cube, doing global stuff", "INFO");
			if(mob.isMob()) {
				return Cuboids2.cfg.globalSanctuary(); 
			}
			if(mob.isAnimal()) {
				if(Cuboids2.cfg.globalSanctuary()) {
					return !Cuboids2.cfg.globalSanctuaryAnimalSpawn();
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
	}
	
	/**
	 * Check if anything can explode in our area
	 * @param block
	 * @return Returns true if creeper Secure
	 */
	public boolean isCreeperSecure(Block block) {
		if(block.getStatus() == 2) {
			//log.info("Receiving Creeper Explosion!");
			//got a creeper explosion!
			Vector v = toolBox.adjustWorldPosition(new Vector(block.getX(), block.getY(), block.getZ()));
			//SysteCuboids2.msg.out.print(v.toString());
			CuboidE cube = findCuboid(block.getWorld().getType().name(), v);
			if(cube != null) {
				//log.info("Creeper stat: "+cube.isCreeperSecure());
				return cube.isCreeperSecure();
			}
			else {
				//log.info("No area found.");
				if(Cuboids2.cfg.globalDisableCreeperSecure()) {
					//log.info("Global creeper protection.");
					return false;
				}
				else {
					//log.info("No Creeper protection found, but cube was there.");
					return true;
				}
			}
		}
		//log.info("NOT a Creeper Explosion!");
		return false;
	}
	
	/**
	 * Check if anything can explode in our area
	 * @param block
	 * @return Returns true if tnt secure
	 */
	public boolean isTntSecure(Block block) {
		if(block.getStatus() == 1) { //that's tnt
			//log.info("Receiving Creeper Explosion!");
			//got a creeper explosion!
			Vector v = toolBox.adjustWorldPosition(new Vector(block.getX(), block.getY(), block.getZ()));
			//SysteCuboids2.msg.out.print(v.toString());
			CuboidE cube = findCuboid(block.getWorld().getType().name(), v);
			if(cube != null) {
				//log.info("Creeper stat: "+cube.isCreeperSecure());
				return cube.isTntSecure();
			}
			else {
				//log.info("No area found.");
				if(Cuboids2.cfg.globalTntSecure()) {
					//log.info("Global creeper protection.");
					return true;
				}
				else {
					//log.info("No Creeper protection found, but cube was there.");
					return false;
				}
			}
		}
		//log.info("NOT a Creeper Explosion!");
		return false;
	}
	
	/**
	 * Wrapper for setting new priority to a cuboid
	 * @param player
	 * @param cubeName
	 * @param prio
	 * @return true if okay, false otherwise
	 */
	public boolean setPriority(Player player, String cubeName, int prio) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				cube.setPriority(prio);
				Cuboids2.treeHandler.updateCuboidNode(cube);
				player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("prioritySet"));
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("priorityNotSet"));
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Wrapper for setting a cuboids parent
	 * @param player
	 * @param subject
	 * @param parent
	 * @return True if okay, false otherwise
	 */
	public boolean setParent(Player player, String subject, String parent) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), subject);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				CuboidE parentNode = findCuboid(player.getWorld().getType().name(), parent);
				if(parentNode == null) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("parentNotSet"));
					return false;
				}
				if(cube.cuboidIsWithin(parentNode.getMajorPoint(), parentNode.getMinorPoint(), true, parentNode.getWorld())) {
					cube.setParent(parent);
					if(cube.getPriority() <= parentNode.getPriority()) {
						cube.setPriority(parentNode.getPriority()+1);
					}
						if(Cuboids2.treeHandler.updateCuboidNode(cube)) {
							player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("parentSet"));
							return true;
						}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("parentNotSet"));
						return false;
					}
				}
				else {
					//TODO: Make message
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("cannotParentChild"));
					return false;
				}
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Toggle the cuboids creeper state
	 * @param player
	 * @param cubeName
	 * @return true if done, false otherwise
	 */
	public boolean toggleCreeper(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				
				if(!player.canUseCommand("/ccreeper")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				if(cube.isCreeperSecure()) {
					if(Cuboids2.cfg.allowCreeperSecure()) {
						cube.setCreeperSecure(false);
						player.sendMessage(Colors.LightGreen+"Creeper explosions enabled! (They do damage)");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				else {
					if(Cuboids2.cfg.allowCreeperSecure()) {
						cube.setCreeperSecure(true);
						player.sendMessage(Colors.LightGreen+"Creeper explosions disabled! (They don't do damage)");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Toggle the cuboids lavaControl state
	 * @param player
	 * @param cubeName
	 * @return true if done, false otherwise
	 */
	public boolean toggleLavaControl(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				
				if(!player.canUseCommand("/cliquids")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				if(cube.isLavaControl()) {
					if(Cuboids2.cfg.allowLavaControl()) {
						cube.setLavaControl(false);
						player.sendMessage(Colors.LightGreen+"Lava control disabled");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				else {
					if(Cuboids2.cfg.allowLavaControl()) {
						cube.setLavaControl(true);
						player.sendMessage(Colors.LightGreen+"Lava Control is enabled");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Toggle the cuboids waterControl state
	 * @param player
	 * @param cubeName
	 * @return true if done, false otherwise
	 */
	public boolean toggleWaterControl(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				
				if(!player.canUseCommand("/cliquids")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				if(cube.isWaterControl()) {
					if(Cuboids2.cfg.allowWaterControl()) {
						cube.setWaterControl(false);
						player.sendMessage(Colors.LightGreen+"Water Flow Control is disabled");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				else {
					if(Cuboids2.cfg.allowWaterControl()) {
						cube.setWaterControl(true);
						player.sendMessage(Colors.LightGreen+"Water Flow Control is enabled");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Toggle the cuboids farmland state
	 * @param player
	 * @param cubeName
	 * @return true if done, false otherwise
	 */
	public boolean toggleFarmland(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				
				if(!player.canUseCommand("/cfarmland")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				if(cube.isFarmland()) {
					if(Cuboids2.cfg.allowFarmland()) {
						cube.setFarmland(false);
						player.sendMessage(Colors.LightGreen+"Farmland control disabled");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				else {
					if(Cuboids2.cfg.allowWaterControl()) {
						cube.setWaterControl(true);
						player.sendMessage(Colors.LightGreen+"Water Control is enabled");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Toggle the cuboids tnt secure state
	 * @param player
	 * @param cubeName
	 * @return true if done, false otherwise
	 */
	public boolean toggleTnt(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				
				if(!player.canUseCommand("/ctnt")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				if(cube.isTntSecure()) {
					if(Cuboids2.cfg.allowTntSecure()) {
						cube.setTntSecure(false);
						player.sendMessage(Colors.LightGreen+"TNT Security is now off (TNT goes boom for real now)");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				else {
					if(Cuboids2.cfg.allowTntSecure()) {
						cube.setTntSecure(true);
						player.sendMessage(Colors.LightGreen+"Area is now TNT secured!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Toggle the restriction flag. With this flag, players that are no owners
	 * or otherwise not on the allow-list in this area are bounced back
	 * @param player
	 * @param string
	 */
	public void toggleRestriction(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				
				if(!player.canUseCommand("/crestrict")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return;
					}
				}
				if(cube.isRestricted()) {
					if(Cuboids2.cfg.allowRestriction()) {
						cube.setRestriction(false);
						player.sendMessage(Colors.LightGreen+"Players may now freely walk here");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return;
					}
				}
				else {
					if(Cuboids2.cfg.allowRestriction()) {
						cube.setRestriction(true);
						player.sendMessage(Colors.LightGreen+"Only allowed players may enter here from now on");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return;
		
	}
	
	/**
	 * Toggle the cuboids pvp state
	 * @param player
	 * @param cubeName
	 * @return true if done, false otherwise
	 */
	public boolean togglePvp(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cpvp")) {
						if(!player.canUseCommand("/cIgnoreRestrictions")) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
							return false;
						}
				}
				if(cube.isAllowedPvp()) {
					if(Cuboids2.cfg.allowPvp()) {
						cube.setAllowPvp(false);
						player.sendMessage(Colors.LightGreen+"PvP disabled!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				else {
					if(Cuboids2.cfg.allowPvp()) {
						cube.setAllowPvp(true);
						player.sendMessage(Colors.LightGreen+"PvP enabled!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Toggle the cuboids protection state
	 * @param player
	 * @param cubeName
	 * @return true if done, false otherwise
	 */
	public boolean toggleProtection(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cprotection")) {
						if(!player.canUseCommand("/cIgnoreRestrictions")) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
							return false;
						}
				}
				if(cube.isProtected()) {
					if(Cuboids2.cfg.allowProtection()) {
						cube.setProtection(false);
						player.sendMessage(Colors.LightGreen+"Protection disabled!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				else {
					if(Cuboids2.cfg.allowProtection()) {
						cube.setProtection(true);
						player.sendMessage(Colors.LightGreen+"Protection enabled!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Toggle a cuboids sanctuary state
	 * @param player
	 * @param cubeName
	 * @return true if done, false otherwise
	 */
	public boolean toggleSanctuary(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/csanctuary")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				if(cube.isSanctuary()) {
					if(Cuboids2.cfg.allowSanctuary()) {
						cube.setSanctuary(false);
						player.sendMessage(Colors.LightGreen+"Sanctuary disabled!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				else {
					if(Cuboids2.cfg.allowSanctuary()) {
						cube.setSanctuary(true);
						player.sendMessage(Colors.LightGreen+"Sanctuary enabled!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Toggle a cuboids sanctuary-spawn-animals state
	 * @param player
	 * @param cubeName
	 * @return true if done, false otherwise
	 */
	public boolean toggleSanctuaryAnimals(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/csanctuary")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				if(cube.sanctuarySpawnAnimals()) {
					if(Cuboids2.cfg.allowSanctuarySpawnAnimals()) {
						cube.setSanctuarySpawnAnimals(false);
						player.sendMessage(Colors.LightGreen+"Sanctuary prevents animal spawning now!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				else {
					if(Cuboids2.cfg.allowSanctuarySpawnAnimals()) {
						cube.setSanctuarySpawnAnimals(true);
						player.sendMessage(Colors.LightGreen+"Sanctuary spawns animals now!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Toggle healing flag
	 */
	public boolean toggleHealing(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod")|| player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cheal")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				if(cube.isHealingArea()) {
					if(Cuboids2.cfg.allowHealing()) {
						cube.setHealing(false);
						player.sendMessage(Colors.LightGreen+"Healing disabled!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				else {
					if(Cuboids2.cfg.allowHealing()) {
						cube.setHealing(true);
						player.sendMessage(Colors.LightGreen+"Healing enabled!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Toggle Freebuild flag
	 */
	public boolean toggleFreebuild(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod")|| player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cfreebuild")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				if(cube.isFreeBuild()) {
					if(Cuboids2.cfg.allowFreebuild()) {
						cube.setFreeBuild(false);
						player.sendMessage(Colors.LightGreen+"Freebuild disabled!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				else {
					if(Cuboids2.cfg.allowFreebuild()) {
						cube.setFreeBuild(true);
						player.sendMessage(Colors.LightGreen+"Freebuild enabled!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * Toggle Firespread
	 */
	public boolean toggleFirespread(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod")|| player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cfirespread")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				if(cube.isBlockFireSpread()) {
					if(Cuboids2.cfg.allowFireSpreadBlock()) {
						cube.setBlockFireSpread(false);
						player.sendMessage(Colors.LightGreen+"Firespread is enabled (fire spreads)!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				else {
					if(Cuboids2.cfg.allowFireSpreadBlock()) {
						cube.setBlockFireSpread(true);
						player.sendMessage(Colors.LightGreen+"Firespread disabled (fire will not spread)!");
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("optionDisabled"));
						return false;
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
		return false;
	}
	
	/**
	 * add Entity (group or player) to the list of allowed things in this cuboid
	 * @param player
	 * @param command This is the command split from listener. Start from index 3 ++ for player/group names
	 * @param cubeName
	 * @return
	 */
	public boolean allowEntity(Player player, String[] command) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), command[1]);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				
				if(!player.canUseCommand("/callow")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				for(int i = 3; i < command.length; i++) {
					if(command[i].startsWith("g:")) {
						cube.addGroup(command[i]);
					}
					else {
						cube.addPlayer(command[i]);
					}
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidUpdated"));
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		else {
			player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
			return false;
		}
	}
	
	/**
	 * Restrict a command in this area
	 * @param player
	 * @param command
	 * @param cubeName
	 * @return
	 */
	public boolean restrictCommand(Player player, String[] command, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/ccommand")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				for(int i = 3; i < command.length; i++) {
					cube.addTabuCommand(command[i]);
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
			}
		}
		return false;
	}
	
	/**
	 * Remove a command form the restriction list
	 * @param player
	 * @param command
	 * @param cubeName
	 * @return
	 */
	public boolean allowCommand(Player player, String[] command, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/ccommand")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				for(int i = 3; i < command.length; i++) {
					cube.removeTabuCommand(command[i]);
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
			}
		}
		return false;
	}
	
	/**
	 * remove Entity (group or player) from the list of allowed things in this cuboid
	 * @param player
	 * @param command This is the command split from listener. Start from index 3 ++ for player/group names
	 * @param cubeName
	 * @return
	 */
	public boolean disallowEntity(Player player, String[] command) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), command[1]);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod")|| player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/callow")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return false;
					}
				}
				for(int i = 2; i < command.length; i++) {
					if(command[i].startsWith("g:")) {
						log.logMessage("Group found", "INFO");
						cube.removeGroup(command[i]);
					}
					else {
						cube.removePlayer(command[i]);
					}
				}
				
				if(Cuboids2.treeHandler.updateCuboidNode(cube)) {
					player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidUpdated"));
					return true;
				}
				else {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("cuboidNotUpdated"));
					return false;
				}
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		else {
			player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
			return false;
		}
	}
	
	/*
	 * Bucket use if prohibited or not per allowance list!
	 * stopWaterFLow and stopLavaFLow shall be global settings to stop flows
	 * make options in config look the same for global and area
	 */
	/**
	 * Adds a player to the list of players inside this area. 
	 * This has nothing to do with the allowance list!
	 * @param player
	 * @param location
	 * @param teleported - this should be false by default, however, it may be true,
	 * if the onMove hook was called automatically via onTeleport, which crazily
	 * auto-calls onMove once more, making the usual procedure run twice
	 */
	public void addPlayerWithin(Player player, Vector location, boolean teleported) {
		ArrayList<CuboidE> all = Cuboids2.treeHandler.getCuboidsContaining(location, player.getWorld().getType().name());
		for(CuboidE c : all) {
			if(!c.playerIsWithin(player.getName())) {
				c.addPlayerWithin(player.getName());
				if(c.getWelcome() != null) {
					player.sendMessage(Colors.Yellow+c.getWelcome());
				}
				if(Cuboids2.cfg.allowHealing()) {
					//log.info("Healing is allowed");
					if(c.isHealingArea()) {
						//log.info("Player Health: "+player.getHealth());
						if(player.getHealth() < 20) {
							//player.sendMessage("Healing Player");
							healjob.schedule( new CuboidHealThread(player, c, Cuboids2.cfg.getHealPower(), Cuboids2.cfg.getHealDelay()), Cuboids2.cfg.getHealDelay());
						}
					}
				}
				if((Cuboids2.cfg.allowFreebuild()) && (teleported == false)) {
					if(c.isFreeBuild()) {
						if(c.playerIsWithin(player.getName())) {
							if(player.getCreativeMode() != 1) {
								if(!playerInventories.containsKey(player.getName())) {
									//log.logMessage("CreativeMode change, saving inventory!", "INFO");
									playerInventories.put(	player.getName(), 
									player.getInventory().getContents());
								}
								player.setCreativeMode(1);
							}
						}	
					}
				}
			}
		}
	}
	
	/**
	 * Removes a player to the list of players inside this area. 
	 * This has nothing to do with the allowance list.
	 * @param player
	 * @param from
	 * @param to
	 */
	public void removePlayerWithin(Player player, Vector vFrom, Vector vTo) {
		CuboidE cubeFrom = findCuboid(player.getWorld().getType().name(), vFrom);
		CuboidE cubeTo = findCuboid(player.getWorld().getType().name(), vTo);
		//Before we remove player, we check if they both have parent relations
		if(cubeFrom != null) {
			//log.logMessage("removing player from cube from...", "INFO");
			if(cubeTo != null) {
				if(cubeFrom.getName().equalsIgnoreCase(cubeTo.getName())) {	
					return;
				}
				//ArrayList<CuboidE> cubesTo = Cuboids2.treeHandler.getCuboidsContaining(vTo, cubeTo.getWorld());
				ArrayList<CuboidE> cubesFrom = Cuboids2.treeHandler.getCuboidsContaining(vFrom, cubeFrom.getWorld());			
				for(CuboidE from : cubesFrom) {
					if(!from.isWithin(vTo)) {
						if(from.getFarewell() != null) {
							player.sendMessage(Colors.Yellow+from.getFarewell());
						}
						if((from.isFreeBuild()) && (!cubeTo.isFreeBuild())) {
							if(player.getCreativeMode() != 0) {
								player.setCreativeMode(0);
								try {
									player.getInventory().setContents(playerInventories.get(player.getName()));
									playerInventories.remove(player.getName());
									//TODO: This is a work around, remove when canary fixes the updateInventory
									try {
										player.updateInventory();
									} catch(ClassCastException e) {
										
									}
								} catch(NullPointerException e) {
									//log.logMessage("Cuboids2: NPE while resetting inventory!", "INFO");
									//whoops
								}
							}
						}
						from.removePlayerWithin(player.getName());
					}
				} 
			} 
			
			else {
				ArrayList<CuboidE> cubesFrom = Cuboids2.treeHandler.getCuboidsContaining(vFrom, cubeFrom.getWorld());				
				for(CuboidE from : cubesFrom) {
					if(from.getFarewell() != null) {
						player.sendMessage(Colors.Yellow + from.getFarewell());
					}
					if(from.isFreeBuild()) {
						player.setCreativeMode(0);
						try {
							player.getInventory().setContents(playerInventories.get(player.getName()));
							playerInventories.remove(player.getName());
							//TODO: This is a work around, remove when canary fixes the updateInventory
							try {
								player.updateInventory();
							}
							catch(ClassCastException e) {
								
							}
						}
						catch(NullPointerException e) {
							//whoops
						}
					}
					from.removePlayerWithin(player.getName());
				}
			}
		}
	}
	
	/**
	 * Check if a player can enter the block he's going to.
	 * @param player
	 * @param v the Vector where the playaer is going to
	 * @return True if player can enter, false otherwise
	 */
	public boolean canEnter(Player player, Vector block) {
		if(player.canUseCommand("/cIgnoreRestrictions")) {
			return true;
		}		
		CuboidE cube = findCuboid(player.getWorld().getType().name(), block);
		if(cube != null) {
			if(cube.isRestricted()) {
				if(!cube.playerIsAllowed(player.getName(), player.getGroups())) {
					return false;
				}
				else {
					return true;
				}
			}
			else {
				return true;
			}
		}
		else {
			return true;
		}
	}
	/**
	 * Checks if player is in a cuboid, does not specify which cuboid
	 * @param world
	 * @param v
	 * @return
	 */
	public boolean isInCuboid(String world, Vector v) {
		if(findCuboid(world, v) != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean resize(Player player, CuboidSelection selection, String cuboidName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cuboidName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cIgnoreRestrictions")) {
					if(!player.canUseCommand("/cmove")) {
						if(!player.canUseCommand("/cAreaMod")) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
							return false;
						}
					}
				}
				cube.setPoints(selection.getOrigin(), selection.getOffset()); //vectors come here corrected already
				Cuboids2.treeHandler.updateCuboidNode(cube);
				player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidUpdated"));
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		else {
			player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
			return false;
		}
	}
	
	/**
	 * Set the welcome message to the cuboid
	 * @param player
	 * @param cubeName
	 * @param message
	 */
	public boolean setWelcomeMessage(Player player, String cubeName, String message) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				
				if(!player.canUseCommand("/cIgnoreRestrictions")) {
					if(!player.canUseCommand("/cmessage")) {
						if(!player.canUseCommand("/cAreaMod")) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
							return false;
						}
					}
				}
				if(message != null) {
					cube.setWelcome(message);
				}
				else {
					cube.setWelcome(null);
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidUpdated"));
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		else {
			player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
			return false;
		}
	}
	
	/**
	 * Set the farewell message to the cuboid
	 * @param player
	 * @param cubeName
	 * @param message
	 */
	public boolean setFarewellMessage(Player player, String cubeName, String message) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube != null) {
			if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cAreaMod") || player.canUseCommand("/cIgnoreRestrictions")) {
				
				if(!player.canUseCommand("/cIgnoreRestrictions")) {
					if(!player.canUseCommand("/cmessage")) {
						if(!player.canUseCommand("/cAreaMod")) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
							return false;
						}
					}
				}
				if(message != null) {
					cube.setFarewell(message);
				}
				else {
					cube.setFarewell(null);
				}
				Cuboids2.treeHandler.updateCuboidNode(cube);
				player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidUpdated"));
				return true;
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("playerNotOwner"));
				return false;
			}
		}
		else {
			player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
			return false;
		}
	}
	
	/**
	 * Display a list of all cuboids in players world
	 * @param player
	 * @param page
	 */
	public void displayCuboidList(Player player, int page) {
        String world = player.getWorld().getType().name();

        
        int perPage = 10, maxPages = 0, amount = 0;
        ArrayList<CuboidNode> cuboids = Cuboids2.treeHandler.getAllInWorld(world);
        //Following is all taken from CuboidPlugin
        //Because I suck at making paging
        if (cuboids == null ||cuboids.isEmpty()) {
            player.sendMessage(Colors.LightGray + "No cuboids in world " + world);
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
        player.sendMessage(Colors.Yellow + "Cuboid Areas (" + world + " world) Page " + page + " from " + maxPages);
        for (int i = amount; i < (amount + perPage); i++) {
            if (cuboids.size() <= i) {
                break;
            }
            CuboidE cuboid = cuboids.get(i).getCuboid();
            player.sendMessage(Colors.LightGray + cuboid.getName() + " : " + Colors.LightGreen + cuboid.getFlagListSimple());
        }
    }
	
	/**
	 * Checks if a certain command is not allowed in the area a player is standing in.
	 * @param player
	 * @param command
	 * @return True if command is restricted, false otherwise
	 */
	public boolean commandIsRestricted(Player player, String command) {
		Vector v = new Vector(player.getX(), player.getY(), player.getZ()); 
		CuboidE cube = findCuboid(player.getWorld().getType().name(), v);
		if(cube != null) {
			//NOTE: There is a check for player.isAdmin in the canPlayerUseCommand hook!!
			return cube.commandIsRestricted(command);
		}
		return false;
	}
	
	/**
	 * Show blacklisted commands for a given area.
	 * @param player
	 * @param cubeName
	 */
	public void showCommandBlacklist(Player player, String cubeName) {
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(!player.canUseCommand("/cIgnoreRestrictions")) {
			if(!cube.playerIsOwner(player.getName())) {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
				return;
			}
		}
		player.sendMessage(Colors.LightGreen+"Restricted Commands for "+cube.getName());
		player.sendMessage(Colors.LightGray+cube.tabuCommands.toString());
	}
	
	/**
	 * Returns true if player can start fire
	 * @param player
	 * @param block
	 * @return
	 */
	public boolean canStartFire(Player player, Block block) {
		Vector v = new Vector(player.getX(), player.getY(), player.getZ());
		CuboidE cube = findCuboid(player.getWorld().getType().name(), v);
		if(cube != null) {
			if(cube.isProtected() && cube.isBlockFireSpread()) {
				if(player.canUseCommand("/cIgnoreRestrictions") || cube.playerIsOwner(player.getName())) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return true;
			}
		}
		else {
			return true;
		}
	}
	
	/**
	 * Checks if a block of liquid can flow or not
	 * @param block
	 * @return true if can not flow, false otherwise
	 */
	public boolean canFlow(Block block) {
		Vector v = toolBox.adjustWorldPosition(new Vector(block.getX(), block.getY(), block.getZ()));
		CuboidE cube = findCuboid(block.getWorld().getType().name(), v);
		if(cube != null) {
			//LAVA
			if(block.getType() == 10 || block.getType() == 11) {
				if(cube.isLavaControl()) {
					return true;
				}
				else {
					return false;
				}
			}
			//WATER
			if(block.getType() == 8 || block.getType() == 9) {
				if(cube.isWaterControl()) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		else {
			
			//WATER
			if(block.getType() == 8 || block.getType() == 9) {
				return Cuboids2.cfg.stopWaterFlowGlobal();
			}
			//LAVA
			else if(block.getType() == 10 || block.getType() == 11) {
				return Cuboids2.cfg.stopLavaFlowGlobal();
			}
			else {
				return false;
			}
		}
	}
	
	/**
	 * Checks if bucket can be placed or not
	 * @param block
	 * @return true if can not place, false otherwise
	 */
	public boolean canPlaceBucket(Player player, Block block) {
		Vector v = toolBox.adjustWorldPosition(new Vector(block.getX(), block.getY(), block.getZ()));
		CuboidE cube = findCuboid(block.getWorld().getType().name(), v);
		if(player.canUseCommand("/cIgnorerestriction")) {
			return false;
		}
		if(cube != null) {
			if(block.getType() == 10 || block.getType() == 11) {
				if(cube.isLavaControl()
						&& (cube.playerIsAllowed(player.getName(), player.getGroups()) || player.canUseCommand("/cAreaMod"))) {
					return false;
				}
				else {
					return true;
				}
			}
			else if(block.getType() == 8 || block.getType() == 9) {
				if(cube.isWaterControl()
						&& (cube.playerIsAllowed(player.getName(), player.getGroups()) || player.canUseCommand("/cAreaMod"))) {
					return false;
				}
				else {
					return true;
				}
			}
			else {
				return !canModifyBlock(player, block);
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * Checks if a block is fireproof, ie. cannot ignite
	 * @param block
	 * @return True if fireproof, false otherwise
	 */
	public boolean isFireProof(Block block) {
		Vector v = toolBox.adjustWorldPosition(new Vector(block.getX(), block.getY(), block.getZ()));
		CuboidE cube = findCuboid(block.getWorld().getType().name(), v);
		if(cube != null) {
			if(cube.isBlockFireSpread()) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if(Cuboids2.cfg.globalFirespreadBlock()) {
				return true;
			}
			return false;
		}
	}
	
	/**
	 * Convert content object to json.
	 * This creates a new file which will be streamed to handle larger files and all.
	 * Lets hope this works out.
	 * @return
	 */
	public boolean saveCuboidBackup(Player player, String cubeName) {
		if(!player.canUseCommand("/cIgnoreRestrictions")) {
			if(!player.canUseCommand("/cbackup")) {
				if(!player.canUseCommand("/cAreaMod")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return false;
				}
			}
		}
		CuboidE cube = findCuboid(player.getWorld().getType().name(), cubeName);
		if(cube == null) {
			return false;
		}
		loadAreaData(cube.getMinorPoint(), cube.getMajorPoint(), player);
		if(myContent.getBlockList().size() >= Cuboids2.cfg.getMaxBlockBagSize()) {
			player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("areaTooLarge"));
			return false;
		}
		
		if(cube.playerIsOwner(player.getName()) || player.canUseCommand("/cIgnoreRestrictions")) {
			 ObjectOutputStream oos;
			 File folder = new File("plugins/cuboids2/backups/");
			 if(!folder.exists()) {
				 folder.mkdirs();
			 }
			 String path = "plugins/cuboids2/backups/"+player.getWorld().getType().name()+"_"+player.getName()+"_"+cubeName+".area";
			 synchronized(lock) {
				 try {
						oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(path))));
						oos.writeObject(myContent);
						oos.close();
						return true;
						//return path;
					} catch (FileNotFoundException e) {
						log.logMessage("Cuboids2: File not Found Exception while serialaizing for area backup", "WARNING");
						//e.printStackTrace();
						return false;
					} catch (IOException e) {
						log.logMessage("Cuboids2: IO Exception while serializing area backup", "WARNING");
						//SysteCuboids2.msg.out.println("Cuboids2: Cannot serialize area for backup, IOException!");
						return false;
					}
			 }
			 //return null;
		}
		return false;
	}
	
	/**
	 * Saves Cuboid File to Disk
	 * @param player
	 * @param cube
	 */
	public void saveCuboid(Player player, String cube)
	  {
	    CuboidNode cubeNode = Cuboids2.treeHandler.getCuboidByName(cube, player.getWorld().getType().name());
	    if ((cubeNode.getCuboid().playerIsOwner(player.getName())) || 
	      ((player.canUseCommand("/cIgnoreRestrictions")) || (player.canUseCommand("/cAreaMod")))) {
	      if (Cuboids2.treeHandler.saveSingle(cube, player.getWorld().getType().name())) {
	        player.sendMessage(Colors.LightGreen + Cuboids2.msg.messages.get("cuboidSaved"));
	      }
	      else {
	        player.sendMessage(Colors.Rose + Cuboids2.msg.messages.get("cuboidNotSaved"));
	      }
	    }
	    else
	      player.sendMessage(Colors.Rose + Cuboids2.msg.messages.get("playerNotOwner"));
	  }
	
	/**
	 * Load Cuboid File from Disk
	 * @param player
	 * @param cube
	 */
	public void loadCuboid(Player player, String cube)
	  {
	    CuboidNode cubeNode = Cuboids2.treeHandler.getCuboidByName(cube, player.getWorld().getType().name());
	    if ((cubeNode.getCuboid().playerIsOwner(player.getName())) || 
	      ((player.canUseCommand("/cIgnoreRestrictions")) || (player.canUseCommand("/cAreaMod")))) {
	      if (Cuboids2.treeHandler.loadSingle(cube, player.getWorld().getType().name())) {
	        player.sendMessage(Colors.LightGreen + Cuboids2.msg.messages.get("cuboidLoaded"));
	      }
	      else {
	        player.sendMessage(Colors.Rose + Cuboids2.msg.messages.get("cuboidNotLoaded"));
	      }
	    }
	    else
	      player.sendMessage(Colors.Rose + Cuboids2.msg.messages.get("playerNotOwner"));
	  }
	
	
	/**
	 * Loads block information into a CuboidSelection object. Used to save area backups
	 * @param v1
	 * @param v2
	 * @param player
	 */
	private void loadAreaData(Vector v1, Vector v2, Player player) {
		int length_x = (int)Vector.getDistance(v1.getX(), v2.getX())+1;
		int length_y = (int)Vector.getDistance(v1.getY(), v2.getY())+1;
		int length_z = (int)Vector.getDistance(v1.getZ(), v2.getZ())+1;
		
		//CuboidData myContent = new CuboidData();
		Vector min = Vector.getMinimum(v1, v2);
		
		Vector size = new Vector();
		
		size.setX(length_x);
		size.setY(length_y);
		size.setZ(length_z);
		myContent = new CuboidSelection();
		//myContent.setSizeVector(size);
		//For instanciating we need this ugly monster to get the correct block positions But that should be alright
		for(int x = 0; x < length_x; ++x) {
			
			for(int y = 0; y < length_y; ++y) {
				
				for(int z = 0; z < length_z; ++z) {
					Vector current = new Vector(min.getBlockX()+x, min.getBlockY()+y, min.getBlockZ()+z);
					current = toolBox.adjustWorldPosition(current);
					//Vector arrayPosition = new Vector(x,y,z);
					Block b = player.getWorld().getBlockAt(current.getBlockX(),current.getBlockY(),current.getBlockZ());
					
					WorldBlock bc = new WorldBlock((byte)b.getData(), (short)b.getType());
					myContent.setBlockAt(current, bc);
				}
			}
		}
		//log.info("Area Volume: "+myContent.getBlockBag().size());
	}
	
	/**
	 * Restore area data for a cuboid from file
	 * @param path
	 * @return CuboidData object or null if somethign went wrong
	 */
	public CuboidSelection restoreFromBackup(Player player, String cubeName) {
		if(!player.canUseCommand("/cbackup")) {
			if(!player.canUseCommand("/cIgnorerestrictions")) {
				if(!player.canUseCommand("/cAreaMod")) {
					return null;
				}
			}
		}
		//log.info("cubeName: "+cubeName);
		String path = "plugins/cuboids2/backups/"+player.getWorld().getType().name()+"_"+player.getName()+"_"+cubeName+".area";
		try {
            ObjectInputStream ois;
            ois = new ObjectInputStream(
                    new BufferedInputStream(
                    new FileInputStream(
                    new File(path))));
            CuboidSelection cube = (CuboidSelection) ois.readObject();
            ois.close();
            return cube;
        }
		catch (Exception e) {
			log.logMessage("Cuboids2: Problem while looking up area backup!", "WARNING");
            //log.severe("Cuboids2: Problemski while recovering area backup file (no such backup?)");
            return null;
        }
    }
}
