package net.playblack.cuboids.hookapi;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.CuboidNode;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Vector;
import net.playblack.mcutils.Location;

public class AreaChecksHook implements CuboidHook {

    @Override
    public Object run(Object[] args) {
        String mode = (String)args[0];
        CPlayer player = (CPlayer) args[1];
        if(mode.equalsIgnoreCase("CAN_MODIFY")) {
            Location loc = new Location((Vector)args[2]);
            loc.setWorld(player.getWorld().getName());
            loc.setDimension(player.getWorld().getDimension());
            return canModify(player, loc);
        }
        else if(mode.equalsIgnoreCase("AREA_GET_NAME_LOCAL")) {
            return getAreaName(player);
        }
        else if(mode.equalsIgnoreCase("AREA_GET_NAME_REMOTE")) {
            Location loc = new Location((Vector)args[2]);
            loc.setWorld(player.getWorld().getName());
            loc.setDimension(player.getWorld().getDimension());
            return getAreaName(player, loc);
        }
        return null;
    }

    private Object getAreaName(CPlayer player, Location position) {
        CuboidNode node = RegionManager.get().getActiveCuboidNode(position, true);
        if(node == null) {
            return "NO_CUBOID";
        }
        return node.getName();
    }
    
    private Object getAreaName(CPlayer player) {
        CuboidNode node = RegionManager.get().getActiveCuboidNode(player.getLocation(), true);
        if(node == null) {
            return "NO_CUBOID";
        }
        return node.getName();
    }

    private Object canModify(CPlayer player, Location position) {
        return Boolean.valueOf(CuboidInterface.get().canModifyBlock(player, position));
    }

}
