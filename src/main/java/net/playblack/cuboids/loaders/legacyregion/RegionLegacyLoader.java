package net.playblack.cuboids.loaders.legacyregion;

import net.playblack.cuboids.loaders.CuboidShell;
import net.playblack.cuboids.loaders.Loader;
import net.playblack.mcutils.Debug;
import net.visualillusionsent.utils.PropertiesFile;

import java.io.File;
import java.util.ArrayList;

public class RegionLegacyLoader implements Loader {

    @Override
    public java.util.List<CuboidShell> load() {
        ArrayList<CuboidShell> shells = new ArrayList<CuboidShell>(20);

        return shells;
    }

    @Override
    public String getImplementationVersion() {
        return "Region(Cuboids 2/3 legacy)";
    }

}
