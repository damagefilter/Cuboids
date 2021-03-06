package net.playblack.cuboids.loaders;

import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Debug;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert foreign Cuboid file formats to native Cuboid.
 *
 * @author Chris
 */
public class Converter {
    protected List<CuboidShell> shells;

    protected Region toCuboid(CuboidShell shell) {
        Region cube = new Region();

        cube.setName(shell.getName());
        cube.setBoundingBox(shell.getOrigin(), shell.getOffset(), false);
        cube.setWorld(shell.getWorld());
        cube.setFarewell(shell.getFarewell());
        cube.setWelcome(shell.getWelcome());
        cube.setPriority(0);

        cube.setProperty("pvp-damage", Status.fromBoolean(shell.getPvp()));
        cube.setProperty("firespread", Status.softFromBoolean(!shell.getFireProof()));
        cube.setProperty("creeper-explosion", Status.fromBoolean(!shell.getCreeper()));
        cube.setProperty("crops-trampling", Status.fromBoolean(!shell.getFarmland()));
        cube.setProperty("creative", Status.fromBoolean(shell.getCreative()));
        cube.setProperty("healing", Status.fromBoolean(shell.getHealing()));
        cube.setProperty("lava-flow", Status.softFromBoolean(!shell.getLavaControl()));
        cube.setProperty("water-flow", Status.softFromBoolean(!shell.getWaterControl()));
        cube.setProperty("protection", Status.softFromBoolean(shell.getProtection()));
        cube.setProperty("enter-cuboid", Status.softFromBoolean(!shell.getProtection()));
        cube.setProperty("mob-damage", Status.softFromBoolean(!shell.getSanctuary()));
        cube.setProperty("mob-spawn", Status.softFromBoolean(!shell.getSanctuary())); //new and by default same as mob damage
        cube.setProperty("animal-spawn", Status.softFromBoolean(!shell.getAnimalSpawn()));
        cube.setProperty("tnt-explosion", Status.softFromBoolean(!shell.getTntSecure()));
        cube.setProperty("physics", Status.softFromBoolean(shell.getPhysics()));
        cube.setProperty("enderman-pickup", Status.softFromBoolean(!shell.getEnderControl()));
        return cube;
    }

    private void convert() {
        RegionManager regions = RegionManager.get();
        for (CuboidShell shell : shells) {
            if (shell.canConvertDirectly()) {
                regions.addRoot(shell.getRegion());
            }
            else {
                regions.addRoot(toCuboid(shell));
            }
        }
        // now that we have all the fancy and unsorted roots added, lets sort
        // them.
        regions.autoSortRegions();
    }

    /**
     * This will execute a load with the given cuboid loader and then write the
     * results to the CuboidSehll arraylist. Returns false if there were no
     * files to getSelection
     *
     * @param loader
     */
    public boolean convertFiles(Loader loader) {
        if (shells == null || !shells.isEmpty()) {
            shells = new ArrayList<CuboidShell>();
        }
        shells = loader.load();
        if (shells.size() > 0) {
            Debug.cacheMessage("Legacy loader " + loader.getImplementationVersion() + " loaded " + shells.size() + " regions. Adding to tree ...", true);
            convert();
            return true;
        }
        return false;
    }
}
