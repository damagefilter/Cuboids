package net.playblack.cuboids.hookapi;

import java.util.ArrayList;

import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;

public class AreaActionHook implements CuboidHook {

    @Override
    public Object run(Object[] args) {
        String mode = (String) args[0];

        if (mode.equalsIgnoreCase("AREA_GET_OWNERS")) {

            return getAreaOwners((CWorld) args[1], (String) args[2]);
        } else if (mode.equalsIgnoreCase("AREA_GET_PLAYERLIST")) {
            return getAreaPlayerList((CWorld) args[1], (String) args[2]);
        }

        else if (mode.equalsIgnoreCase("AREA_GET_GROUPLIST")) {
            return getAreaGroupList((CWorld) args[1], (String) args[2]);
        }

        else if (mode.equalsIgnoreCase("AREA_ADD_PLAYER")) {
            return addPlayerToArea((CWorld) args[1], (String) args[2],
                    (String) args[3]);
        }

        else if (mode.equalsIgnoreCase("AREA_ADD_GROUP")) {
            return addGroupToArea((CWorld) args[1], (String) args[2],
                    (String) args[3]);
        }

        else if (mode.equalsIgnoreCase("AREA_REMOVE_PLAYER")) {
            return removePlayerFromArea((CWorld) args[1], (String) args[2],
                    (String) args[3]);
        }

        else if (mode.equalsIgnoreCase("AREA_REMOVE_GROUP")) {
            return removeGroupFromArea((CWorld) args[1], (String) args[2],
                    (String) args[3]);
        }
        return null;
    }

    private Object removeGroupFromArea(CWorld cWorld, String areaName,
            String groupName) {
        Region cube = RegionManager.get().getCuboidByName(areaName,
                cWorld.getName(), cWorld.getDimension());
        if (cube == null) {
            return Boolean.valueOf(false);
        }
        cube.removeGroup(groupName);
        cube.hasChanged = true;
        RegionManager.get().updateRegion(cube);
        return Boolean.valueOf(true);
    }

    /**
     * 
     * @param cWorld
     * @param areaName
     * @param playerName
     * @return
     */
    private Object removePlayerFromArea(CWorld cWorld, String areaName,
            String playerName) {
        Region cube = RegionManager.get().getCuboidByName(areaName,
                cWorld.getName(), cWorld.getDimension());
        if (cube == null) {
            return Boolean.valueOf(false);
        }
        cube.removePlayer(playerName);
        cube.hasChanged = true;
        RegionManager.get().updateRegion(cube);
        return Boolean.valueOf(true);
    }

    /**
     * 
     * @param cWorld
     * @param string
     * @param string2
     * @return
     */
    private Object addGroupToArea(CWorld cWorld, String areaName,
            String groupName) {
        Region cube = RegionManager.get().getCuboidByName(areaName,
                cWorld.getName(), cWorld.getDimension());
        if (cube == null) {
            return Boolean.valueOf(false);
        }
        cube.addGroup(groupName);
        cube.hasChanged = true;
        RegionManager.get().updateRegion(cube);
        return Boolean.valueOf(true);
    }

    /**
     * 
     * @param cWorld
     * @param areaName
     * @param playerName
     * @return
     */
    private Object addPlayerToArea(CWorld cWorld, String areaName,
            String playerName) {
        Region cube = RegionManager.get().getCuboidByName(areaName,
                cWorld.getName(), cWorld.getDimension());
        if (cube == null) {
            return Boolean.valueOf(false);
        }
        cube.addPlayer(playerName);
        cube.hasChanged = true;
        RegionManager.get().updateRegion(cube);
        return Boolean.valueOf(true);
    }

    /**
     * 
     * @param cWorld
     * @param areaName
     * @return
     */
    private Object getAreaGroupList(CWorld cWorld, String areaName) {
        Region cube = RegionManager.get().getCuboidByName(areaName,
                cWorld.getName(), cWorld.getDimension());
        if (cube == null) {
            return null;
        }
        return cube.getGroupList(); //TODO: send raw list!
    }

    /**
     * 
     * @param cWorld
     * @param areaName
     * @return
     */
    private Object getAreaPlayerList(CWorld cWorld, String areaName) {
        Region cube = RegionManager.get().getCuboidByName(areaName,
                cWorld.getName(), cWorld.getDimension());
        if (cube == null) {
            return null;
        }
        return cube.getPlayerList(); //TODO: send raw list!
    }

    /**
     * 
     * @param cWorld
     * @param areaName
     * @return
     */
    private Object getAreaOwners(CWorld cWorld, String areaName) {
        Region cube = RegionManager.get().getCuboidByName(areaName,
                cWorld.getName(), cWorld.getDimension());
        if (cube == null) {
            return null;
        }
        ArrayList<String> owners = new ArrayList<String>(3);
        String[] players = cube.getPlayerList().split(",");
        for (String player : players) {
            if (player.startsWith("o:")) {
                owners.add(player.substring(2));
            }
        }
        return owners;
    }

}
