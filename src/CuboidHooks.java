import com.playblack.ToolBox;
import com.playblack.cuboid.tree.CuboidNode;
import com.playblack.cuboid.tree.CuboidTreeHandler;
import com.playblack.vector.Vector;

public class CuboidHooks implements PluginInterface {

	/**
	 * Will return true if the area is protected by a cuboid area.<br>
	 * Params: Player player
	 * @author chris
	 *
	 */
	ToolBox tools = new ToolBox();
	
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
        return node.getCuboid().playerIsOwner(player.getName());
	}
		
	/**
	 * Get my name, dude!
	 */
	public String getName() {
		return "CuboidAPI";
	}
	
	/**
	 * Get the amout of my parameters, dude!
	 */
	public int getNumParameters() {
		return 2;
	}
	
	public String checkParameters(Object[] os) {
		if(os.length != 3) {
			return "Invalid amount of parameters";
		}
		return null;
	}
	
	public Object run(Object[] os) {
		Player player = (Player)os[0];
		Block block = (Block)os[1];
		Vector v = new Vector(block.getX(), block.getY(), block.getZ());
		v = tools.adjustWorldBlock(v);
        String mode = (String)os[2];
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
        	if(isProtected(player, tools.adjustWorldBlock(v))) {
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
        else {
        	return true;
        }
	}	
}
