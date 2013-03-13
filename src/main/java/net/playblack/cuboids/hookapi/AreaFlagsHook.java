package net.playblack.cuboids.hookapi;

import net.playblack.cuboids.gameinterface.CWorld;


public class AreaFlagsHook implements CuboidHook {

    @Override
    public Object run(Object[] args) {
        String mode = (String) args[0];
        if (mode.equalsIgnoreCase("AREA_GET_FLAG")) {

            return getFlagValue((CWorld) args[1], (String) args[2],
                    (String) args[3]);
        } else if (mode.equalsIgnoreCase("AREA_SET_FLAG")) {
            return setFlagValue((CWorld) args[1], (String) args[2],
                    (String) args[3], (Boolean) args[4]);
        }
        return null;
    }

    private Object setFlagValue(CWorld cWorld, String areaName, String flag, Boolean newValue) {
//        Cuboid cube = RegionManager.getInstance().getCuboidByName(areaName,
//                cWorld.getName(), cWorld.getDimension());
//        if (cube == null) {
//            return Boolean.valueOf(false);
//        }
//        if (cube.getFlagListArray().containsKey(flag)) {
//            HashMap<String, Boolean> newList = cube.getFlagListArray();
//            newList.put(flag, newValue);
//            cube.overwriteProperties(newList);
//            cube.hasChanged = true;
//            RegionManager.getInstance().updateCuboidNode(cube);
//            return Boolean.valueOf(true);
//        }
//        return Boolean.valueOf(false);
        //TODO: This needs to be redone for the new list stuff
        return null;
    }

    private Object getFlagValue(CWorld cWorld, String areaName, String flag) {
//        Cuboid cube = RegionManager.getInstance().getCuboidByName(areaName,
//                cWorld.getName(), cWorld.getDimension());
//        if (cube == null) {
//            return Boolean.valueOf(false);
//        }
//        return cube.getFlagListArray().get(flag);
        //TODO: This needs to be redone for the new list stuff
        return null;
    }

}
