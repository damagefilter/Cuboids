package net.playblack.cuboids.datasource.da;

import net.canarymod.database.Column;
import net.canarymod.database.DataAccess;

import java.util.ArrayList;

/**
 * Represents the contents of a region (that's blocks)
 */
public class RegionDataAccess extends DataAccess {

    private String regionName;

    public RegionDataAccess(String regionName) {
        super("cuboids_regionbackups", regionName);
        this.regionName = regionName;
    }

    @Column(columnName = "block_data", dataType = Column.DataType.STRING, isList = true)
    public ArrayList<String> blockData;

    @Override
    public DataAccess getInstance() {
        return new RegionDataAccess(this.regionName);
    }
}
