package net.playblack.cuboids.datasource;

import java.util.ArrayList;

import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.EventLogger;

/**
 * FlatFileData extends BaseData and represents the data layer for retrieving
 * Cuboids from text files.
 * 
 * @author Chris
 * 
 */
public class XmlData implements BaseData {

    private Object lock = new Object();
    private EventLogger log;

    public XmlData(EventLogger log) {
        this.log = log;
    }

    @Override
    public void saveRegion(Region node) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveAll(ArrayList<Region> treeList, boolean silent,
            boolean force) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void loadAll(RegionManager handler) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void loadCuboid(RegionManager handler, String name, String world) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeNode(Region node) {
        // TODO Auto-generated method stub
        
    }
}
