package net.playblack.cuboids.datasource;

import net.canarymod.Canary;
import net.canarymod.ToolBox;
import net.canarymod.database.DataAccess;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;
import net.playblack.cuboids.datasource.da.RegionInformationDataAccess;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Uses the CanaryMod database to load and save region files
 */
public class CanaryDbData implements BaseData {

    private HashMap<String, ArrayList<Region>> loadedRegions = new HashMap<String, ArrayList<Region>>();

    @Override
    public void saveRegion(Region node) {
        DataAccess da = RegionInformationDataAccess.toDataAccess(node);
        try {
            Canary.db().insert(da);
        }
        catch (DatabaseWriteException e) {
            Debug.logStack("Could not save region.", e);
        }
    }

    @Override
    public void saveAll(ArrayList<Region> treeList, boolean silent, boolean force) {
        ArrayList<DataAccess> toInsert = new ArrayList<DataAccess>();
        for (Region r : treeList) {
            toInsert.add(RegionInformationDataAccess.toDataAccess(r));
            for (Region reg : r.getChildsDeep(new ArrayList<Region>())) {
                toInsert.add(RegionInformationDataAccess.toDataAccess(reg));
            }
        }
        try {
            Canary.db().insertAll(toInsert);
        }
        catch (DatabaseWriteException e) {
            Debug.logStack("Could not save region tree.", e);
        }
    }

    @Override
    public int loadAll() {
        List<DataAccess> loadedDas = new ArrayList<DataAccess>();
        this.loadedRegions.put("root", new ArrayList<Region>());
        int loaded = 0;
        try {
            Canary.db().loadAll(new RegionInformationDataAccess(), loadedDas, new HashMap<String, Object>());
            for (DataAccess d : loadedDas) {
                RegionInformationDataAccess da = (RegionInformationDataAccess)d;
                if (ToolBox.stringToNull(da.parent) == null) {
                    this.loadedRegions.get("root").add(da.getRegion());
                }
                else {
                    if (loadedRegions.get(da.parent) == null) {
                        loadedRegions.put(da.parent, new ArrayList<Region>());
                    }
                    loadedRegions.get(da.parent).add(da.getRegion());
                }
                ++loaded;
            }

            //Sort out parents and stuff.
            for (String key : loadedRegions.keySet()) {
                //Root has no parents to sort out
                if (!key.equals("root")) {
                    for (Region r : loadedRegions.get(key)) {
                        Region parent = findByName(key);
                        if (parent == null) {
                            Debug.logWarning("Cannot find parent " + key + ". Dropping regions with this parent!");
                            break;
                        }
                        r.setParent(parent);
                    }
                }
            }

            RegionManager instance = RegionManager.get();
            //Now that we have all the parents sorted out, we can just add all nodes under "root" to the regionmanager
            for (Region root : loadedRegions.get("root")) {
                instance.addRegion(root);
            }

        }
        catch (DatabaseReadException e) {
            e.printStackTrace();
        }
        return loaded;
    }

    @Override
    public void loadRegion(String name, String world, int dimension) {
        HashMap<String, Object> filter = new HashMap<String, Object>();
        filter.put("name", name);
        filter.put("world", world);
        filter.put("dimension", dimension);
        RegionInformationDataAccess da = new RegionInformationDataAccess();
        try {
            Canary.db().load(da, filter);
            if (da.hasData()) {
                Region old = RegionManager.get().getRegionByName(name, world, dimension);
                if (old != null) {
                    RegionManager.get().removeRegion(old);
                }
                RegionManager.get().addRegion(da.getRegion());
            }
        }
        catch (DatabaseReadException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteRegion(Region node) {
        try {
            RegionInformationDataAccess da = RegionInformationDataAccess.toDataAccess(node);
            HashMap<String, Object> filter = new HashMap<String, Object>();
            filter.put("name", node.getName());
            filter.put("world", node.getWorld());
            filter.put("dimension", node.getDimension());
            Canary.db().remove(da, filter);
        }
        catch (DatabaseWriteException e) {
            e.printStackTrace();
        }
    }

    private Region findByName(String name) {
        for (String key : loadedRegions.keySet()) {
            for (Region r : loadedRegions.get(key)) {
                if (r.getName().equals(name)) {
                    return r;
                }
            }
        }
        return null;
    }
}
