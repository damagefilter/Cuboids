import net.playblack.ToolBox;
import net.playblack.cuboid.tree.CuboidNode;
import net.playblack.cuboid.tree.CuboidTreeHandler;
import net.playblack.mcutils.Vector;


public class CuboidHookChecks extends CuboidHookBase {

	private ToolBox tools = new ToolBox();
	
	/**
	 * Checks if area is protected
	 * @param player
	 * @param v
	 * @return
	 */
	private boolean isProtected(Player player, Vector v) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;		
		//Player player = (Player)os[0];
       // Vector v = new Vector(player.getX(),player.getY(),player.getZ());
		if(handler == null) {
	    	   return false;
	    }
       // ArrayList<CuboidNode> nodes = handler.getNodesContaining(v, player.getWorld().getType().name());
        CuboidNode node = handler.getActiveCuboid(v, player.getWorld().getType().name());
        if(node == null) {
      	   return false;
        }
        
        return node.getCuboid().isProtected();
	}
	
	/**
	 * Get name of active area
	 * @param player
	 * @param v
	 * @return
	 */
	private String getAreaName(Player player, Vector v) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;		
		//Player player = (Player)os[0];
       // Vector v = new Vector(player.getX(),player.getY(),player.getZ());
		if(handler == null) {
	    	   return null;
	    }
       // ArrayList<CuboidNode> nodes = handler.getNodesContaining(v, player.getWorld().getType().name());
        CuboidNode node = handler.getActiveCuboid(v, player.getWorld().getType().name());
        if(node == null) {
      	   return null;
        }
        
        return node.getCuboid().getName();
	}
	
	/**
	 * Checks if player is in areas allowance list
	 * @param player
	 * @param v
	 * @return
	 */
	private boolean playerIsAllowed(Player player, Vector v) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;
		if(handler == null) {
			//actually, that shouldn't happen
			Cuboids2.eventLog.logMessage("The tree data was not accessible while serving a plugin with CuboidsAPI!!!", "SEVERE");
	    	   return false;
	    }
       // Vector v = new Vector(player.getX(),player.getY(),player.getZ());
        
       // ArrayList<CuboidNode> nodes = handler.getNodesContaining(v, player.getWorld().getType().name());
		CuboidNode node = handler.getActiveCuboid(v, player.getWorld().getType().name());
        if(node == null) {
     	   return false;
        }
        return node.getCuboid().playerIsAllowed(player.getName(), player.getGroups());
	}
	
	/**
	 * Checks if player is owner in the area
	 * @param player
	 * @param v
	 * @return
	 */
	private boolean playerIsOwner(Player player, Vector v) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;
		if(handler == null) {
			Cuboids2.eventLog.logMessage("The tree data was not accessible while serving a plugin with CuboidsAPI!!!", "SEVERE");
	    	   return false;
	    }
		CuboidNode node = handler.getActiveCuboid(v, player.getWorld().getType().name());
        if(node == null) {
      	   return false;
         }
        return node.getCuboid().playerIsOwner(player.getName());
	}
	
	public Object run(Object[] args) {
		Player player;
		Block block;
		String mode;
		Vector v;
		if(args.length == 2) {
			player = (Player)args[0];
			block = (Block)args[1];
			mode = (String)args[2];
			v = new Vector(player.getX(), player.getY(), player.getZ());
		}
		else {
			player = (Player)args[0];
			block = (Block)args[1];
			mode = (String)args[2];
			v = new Vector(block.getX(), block.getY(), block.getZ());
			v = tools.adjustWorldPosition(v);
		}

        //String mode = (String)os[2];
        if(mode.equalsIgnoreCase("IS_PROTECTED"))  {
        	return isProtected(player, v);
        }
        else if(mode.equalsIgnoreCase("PLAYER_ALLOWED")) {
        	return playerIsAllowed(player, v);
        }
        else if(mode.equalsIgnoreCase("PLAYER_OWNER")) {
        	return playerIsOwner(player, v);
        }
        else if(mode.equalsIgnoreCase("CAN_MODIFY")) {
        	if(isProtected(player, v)) {
        		if(playerIsAllowed(player, v) || playerIsOwner(player,v)) {
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
        else if(mode.equalsIgnoreCase("AREA_GET_NAME")) {
        	return getAreaName(player, v);
        }
        else {
        	return false;
        }
	}
}
