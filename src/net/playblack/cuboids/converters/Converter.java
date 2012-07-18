package net.playblack.cuboids.converters;

import java.util.ArrayList;

import net.playblack.cuboids.regions.CuboidE;
import net.playblack.cuboids.regions.RegionManager;

/**
 * Convert foreign Cuboid file formats to native CuboidE.
 * 
 * @author Chris
 * 
 */
public class Converter {
    protected ArrayList<CuboidShell> shells;

    protected CuboidE toCuboidE(CuboidShell shell) {
        CuboidE cube = new CuboidE();
        cube.setAllowPvp(shell.getPvp());
        cube.setBlockFireSpread(shell.getFireProof());
        cube.setCreeperSecure(shell.getCreeper());
        cube.setDimension(shell.getDimension());
        cube.setFarewell(shell.getFarewell());
        cube.setFarmland(shell.getFarmland());
        cube.setFirstPoint(shell.getOrigin());
        cube.setFreeBuild(shell.getCreative());
        cube.setHealing(shell.getHealing());
        cube.sethMob(false); // c2 property
        cube.setLavaControl(shell.getLavaControl());
        cube.setName(shell.getName());
        cube.setParent(null); // there is no parenting in CuboidPlugin ... lol
        cube.setPriority(0); // There is no priority in CuboidPlugin ... lol
        cube.setProtection(shell.getProtection());
        cube.setRestriction(shell.getRestricted());
        cube.setSanctuary(shell.getSanctuary());
        cube.setSanctuarySpawnAnimals(shell.getAnimalSpawn());
        cube.setSecondPoint(shell.getOffset());
        cube.setTntSecure(shell.getTntSecure());
        cube.setWaterControl(shell.getWaterControl());
        cube.setWelcome(shell.getWelcome());
        cube.setWorld(shell.getWorld());
        cube.setPhysics(shell.getPhysics());
        cube.setEnderControl(shell.getEnderControl());
        return cube;
    }

    private void convert() {
        RegionManager regions = RegionManager.getInstance();
        for (CuboidShell shell : shells) {
            regions.addRoot(toCuboidE(shell));
        }
        // now that we have all the fancy and unsorted roots added, lets sort
        // them.
        regions.autoSortCuboidAreas();
    }

    /**
     * This will execute a load with the given cuboid loader and then write the
     * results to the CuboidSehll arraylist. Returns false if there were no
     * files to convert
     * 
     * @param loader
     */
    public boolean convertFiles(Loader loader) {
        if (shells == null || !shells.isEmpty()) {
            shells = new ArrayList<CuboidShell>();
        }
        shells = loader.load();
        if (shells.size() > 0) {
            convert();
            return true;
        }
        return false;
    }
}
