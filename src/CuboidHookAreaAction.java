import java.util.ArrayList;

import com.playblack.cuboid.tree.CuboidNode;
import com.playblack.cuboid.tree.CuboidTreeHandler;


public class CuboidHookAreaAction extends CuboidHookBase {

	/**
	 * Get ArrayList of all people allowed in the area flagged as owners
	 * @param player
	 * @param area
	 * @return
	 */
	private ArrayList<String> getAreaOwners(World world, String area) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;
		if(handler == null) {
			   return null;
		}
		CuboidNode node = handler.getCuboidByName(area, world.getType().name());
		if(node == null) {
		  return null;
		}
		ArrayList<String> owners = new ArrayList<String>();
		ArrayList<String> playerList = node.getCuboid().getPlayerListRaw();
		for(String p : playerList) {
			if(p.startsWith("o:")) {
				owners.add(p.substring(2));
			}
		}
		return owners;
	}
	
	/**
	 * Get the complete list of players in the area as ArrayList
	 * @param player
	 * @param area
	 * @return
	 */
	private ArrayList<String> getAreaPlayerList(World world, String area) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;
		if(handler == null) {
			   return null;
		}
		CuboidNode node = handler.getCuboidByName(area, world.getType().name());
		if(node == null) {
		  return null;
		}
		return node.getCuboid().getPlayerListRaw();
	}
	
	/**
	 * Get the complete group list of this area as ArrayList
	 * @param player
	 * @param area
	 * @return
	 */
	private ArrayList<String> getAreaGroupList(World world, String area) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;
		if(handler == null) {
			   return null;
		}
		CuboidNode node = handler.getCuboidByName(area, world.getType().name());
		if(node == null) {
		  return null;
		}
		return node.getCuboid().getGroupListRaw();
	}
	
	/**
	 * add player(s) to area
	 * @param player
	 * @param area
	 * @param playerName
	 * @return
	 */
	private Boolean addPlayerToArea(World world, String area, String playerName) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;
		if(handler == null) {
			   return false;
		}
		CuboidNode node = handler.getCuboidByName(area, world.getType().name());
		if(node == null) {
		  return false;
		}
		
		return node.getCuboid().addPlayer(playerName);
	}
	
	/**
	 * Add group(s) to area
	 * @param player
	 * @param area
	 * @param groupName
	 * @return
	 */
	private Boolean addGroupToArea(World world, String area, String groupName) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;
		if(handler == null) {
			   return false;
		}
		CuboidNode node = handler.getCuboidByName(area, world.getType().name());
		if(node == null) {
		  return false;
		}
		
		return node.getCuboid().addGroup(groupName);
	}
	
	/**
	 * Remove player/s from area
	 * @param player
	 * @param area
	 * @param playerName
	 * @return
	 */
	private Boolean removePlayerFromArea(World world, String area, String playerName) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;
		if(handler == null) {
			   return false;
		}
		CuboidNode node = handler.getCuboidByName(area, world.getType().name());
		if(node == null) {
		  return false;
		}
		return node.getCuboid().removePlayer(playerName);
	}
	
	/**
	 * Remove group/s from area
	 * @param player
	 * @param area
	 * @param groupName
	 * @return
	 */
	private Boolean removeGroupFromArea(World world, String area, String groupName) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;
		if(handler == null) {
			   return false;
		}
		CuboidNode node = handler.getCuboidByName(area, world.getType().name());
		if(node == null) {
		  return false;
		}
		return node.getCuboid().removeGroup(groupName);
	}
	
	public Object run(Object[] args) {
		String areaName = (String)args[0];
		World world = (World)args[1];	
		String mode = (String)args[args.length-1];
		
		if(mode.equalsIgnoreCase("AREA_GET_OWNERS")) {

        	return getAreaOwners(world, areaName);
        }
        else if(mode.equalsIgnoreCase("AREA_GET_PLAYERLIST")) {
        	return getAreaPlayerList(world, areaName);
        }
        else if(mode.equalsIgnoreCase("AREA_GET_GROUPLIST")) {
        	return getAreaGroupList(world, areaName);
        }
        else if(mode.equalsIgnoreCase("AREA_ADD_PLAYER")) {
        	String entity = (String)args[2];
        	return addPlayerToArea(world, areaName, entity);
        }
        else if(mode.equalsIgnoreCase("AREA_ADD_GROUP")) {
        	String entity = (String)args[2];
        	return addGroupToArea(world, areaName, entity);
        }
        
        else if(mode.equalsIgnoreCase("AREA_REMOVE_PLAYER")) {
        	String entity = (String)args[2];
        	return removePlayerFromArea(world, areaName, entity);
        }
        else if(mode.equalsIgnoreCase("AREA_REMOVE_GROUP")) {
        	String entity = (String)args[2];
        	return removeGroupFromArea(world, areaName, entity);
        }
		
		return null;
	}
}
