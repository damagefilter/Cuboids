package net.playblack.cuboids.hookapi;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.cuboids.regions.CuboidNode;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.WorldLocation;

public class AreaChecksHook implements CuboidHook {

    @Override
    public Object run(Object[] args) {
        String mode = (String)args[0];
        if(mode.equalsIgnoreCase("CAN_MODIFY")) {
            return canModify((CPlayer)args[1], (WorldLocation)args[2]);
        }
        else if(mode.equalsIgnoreCase("AREA_GET_NAME_LOCAL")) {
            return getAreaName((CPlayer)args[1]);
        }
        else if(mode.equalsIgnoreCase("AREA_GET_NAME_REMOTE")) {
            return getAreaName((CPlayer)args[1],(WorldLocation)args[2]);
        }
        return null;
    }

    private Object getAreaName(CPlayer player, WorldLocation position) {
        CuboidNode node = RegionManager.getInstance().getActiveCuboid(position, true);
        if(node == null) {
            return "NO_CUBOID";
        }
        return node.getName();
    }
    
    private Object getAreaName(CPlayer player) {
        CuboidNode node = RegionManager.getInstance().getActiveCuboid(player.getLocation(), true);
        if(node == null) {
            return "NO_CUBOID";
        }
        return node.getName();
    }

    private Object canModify(CPlayer player, WorldLocation position) {
        return Boolean.valueOf(CuboidInterface.getInstance().canModifyBlock(player, position));
    }

}
