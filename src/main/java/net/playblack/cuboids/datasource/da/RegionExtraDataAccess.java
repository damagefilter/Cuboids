package net.playblack.cuboids.datasource.da;

import net.canarymod.database.Column;
import net.canarymod.database.DataAccess;

import java.util.ArrayList;

/**
 * Represents the contents of a region (that's chest contents etc)
 */
public class RegionExtraDataAccess extends DataAccess {

    public RegionExtraDataAccess() {
        super("cuboids_regionextras");
    }

    /**
     * Maps this data to a list index from RegionDataAccess.
     */
    @Column(columnName = "index", dataType = Column.DataType.INTEGER)
    public int index;

    @Column(columnName = "region", dataType = Column.DataType.STRING)
    public String region;

    @Column(columnName = "data", dataType = Column.DataType.STRING, isList = true)
    public String data;

    @Override
    public DataAccess getInstance() {
        return new RegionExtraDataAccess();
    }
}
