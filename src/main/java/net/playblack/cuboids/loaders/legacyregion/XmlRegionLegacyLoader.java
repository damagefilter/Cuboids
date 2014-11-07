package net.playblack.cuboids.loaders.legacyregion;

import net.playblack.cuboids.datasource.legacy.XmlDataLegacy;
import net.playblack.cuboids.loaders.CuboidShell;
import net.playblack.cuboids.loaders.Loader;
import net.playblack.cuboids.regions.Region;

import java.util.ArrayList;

public class XmlRegionLegacyLoader implements Loader {

    @Override
    public java.util.List<CuboidShell> load() {
        ArrayList<CuboidShell> shells = new ArrayList<CuboidShell>(20);
        XmlDataLegacy backend = new XmlDataLegacy();
        for (Region r : backend.loadAllRaw()) {
            shells.add(new RegionLegacyShell(r));
        }

        return shells;
    }

    @Override
    public String getImplementationVersion() {
        return "Region(Cuboids 2/3 xml legacy)";
    }

}
