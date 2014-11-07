package net.playblack.cuboids.loaders.legacyregion;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.datasource.legacy.MysqlDataLegacy;
import net.playblack.cuboids.loaders.CuboidShell;
import net.playblack.cuboids.loaders.Loader;
import net.playblack.cuboids.regions.Region;

import java.util.ArrayList;

public class MysqlRegionLegacyLoader implements Loader {

    @Override
    public java.util.List<CuboidShell> load() {
        ArrayList<CuboidShell> shells = new ArrayList<CuboidShell>(20);
        MysqlDataLegacy backend = new MysqlDataLegacy(Config.get().getSqlConfig());
        for (Region r : backend.loadAllRaw()) {
            shells.add(new RegionLegacyShell(r));
        }

        return shells;
    }

    @Override
    public String getImplementationVersion() {
        return "Region(Cuboids 2/3 mysql legacy)";
    }

}
