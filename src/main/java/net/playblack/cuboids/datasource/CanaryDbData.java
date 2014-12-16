package net.playblack.cuboids.datasource;

import net.canarymod.Canary;
import net.canarymod.ToolBox;
import net.canarymod.database.DataAccess;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;
import net.canarymod.tasks.ServerTask;
import net.playblack.cuboids.datasource.da.RegionInformationDataAccess;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Uses the CanaryMod database to load and save region files
 */
public class CanaryDbData implements BaseData {

    private HashMap<String, ArrayList<Region>> loadedRegions = new HashMap<String, ArrayList<Region>>();

    @Override
    public void saveRegion(Region node) {
        DataAccess da = RegionInformationDataAccess.toDataAccess(node);
        try {
            HashMap<String, Object> filter = new HashMap<String, Object>();
            filter.put("region_name", node.getName());
            filter.put("world_name", node.getWorld());
            Database.get().update(da, filter);
        }
        catch (DatabaseWriteException e) {
            Debug.logStack("Could not save region.", e);
        }
    }

    @Override
    public void saveAll(HashMap<String, List<Region>> treeList) {
        Map<DataAccess, Map<String, Object>> toInsert = new HashMap<DataAccess, Map<String, Object>>();
        for (List<Region> roots : treeList.values()) {
            for (Region r : roots) {
                List<Region> all = r.getChildsDeep(new ArrayList<Region>());
                for (Region reg : all) {
                    HashMap<String, Object> filter = new HashMap<String, Object>();
                    filter.put("region_name", reg.getName());
                    filter.put("world_name", reg.getWorld());
                    toInsert.put(RegionInformationDataAccess.toDataAccess(reg), filter);
                }
            }
            try {
                Database.get().updateAll(new RegionInformationDataAccess(), toInsert);
            }
            catch (DatabaseWriteException e) {
                Debug.logStack("Could not save region tree.", e);
            }
        }

    }

    @Override
    public int loadAll() {
        List<DataAccess> loadedDas = new ArrayList<DataAccess>();
        this.loadedRegions.put("root", new ArrayList<Region>());
        int loaded = 0;
        try {
            Database.get().loadAll(new RegionInformationDataAccess(), loadedDas, new HashMap<String, Object>());
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
    public void loadRegion(String name, String world) {
        HashMap<String, Object> filter = new HashMap<String, Object>();
        filter.put("region_name", name);
        filter.put("world_name", world);
        RegionInformationDataAccess da = new RegionInformationDataAccess();
        try {
            Database.get().load(da, filter);
            if (da.hasData()) {
                Region old = RegionManager.get().getRegionByName(name, world);
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
            filter.put("region_name", node.getName());
            filter.put("world_name", node.getWorld());
            Database.get().remove(da, filter);
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
