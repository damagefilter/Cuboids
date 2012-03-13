import java.util.HashMap;

import net.playblack.cuboid.tree.CuboidNode;
import net.playblack.cuboid.tree.CuboidTreeHandler;


public class CuboidHookAreaFlags extends CuboidHookBase {
	
	private Boolean getFlagValue(World world, String key, String area) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;
		if(handler == null) {
			return null;
		}
		CuboidNode node = handler.getCuboidByName(area, world.getType().name());
		if(node == null) {
		  return null;
		}
		if(node.getCuboid().getFlagListArray().containsKey(key)) {
			return node.getCuboid().getFlagListArray().get(key);
		}
		return null;
	}
	
	private Boolean setFlagValue(World world, String key, Boolean value, String area) {
		CuboidTreeHandler handler = Cuboids2.treeHandler;
		if(handler == null) {
			return null;
		}
		CuboidNode node = handler.getCuboidByName(area, world.getType().name());
		if(node == null) {
		  return null;
		}
		if(node.getCuboid().getFlagListArray().containsKey(key)) {
			HashMap<String, Boolean> newList = node.getCuboid().getFlagListArray();
			newList.put(key, value);
			node.getCuboid().overwriteProperties(newList);
			return true;
		}
		return null;
	}
	public Object run(Object[] args) {
		String areaName = (String)args[0];
		World world = (World)args[1];
		String flag = (String)args[2];
		//May be boolean in between, see area_set_flag
		String mode = (String)args[args.length-1];
		
		if(mode.equalsIgnoreCase("AREA_GET_FLAG")) {
			return getFlagValue(world, flag, areaName);
		}
		else if(mode.equalsIgnoreCase("AREA_SET_FLAG")) {
			Boolean value = (Boolean)args[3];
			return setFlagValue(world, flag, value, areaName);
		}
		else {
			return null;
		}
		
	}

}
