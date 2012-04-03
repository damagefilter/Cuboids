package net.playblack.cuboids.converters;

import java.util.ArrayList;

import net.playblack.cuboids.regions.CuboidE;
import net.playblack.cuboids.regions.RegionManager;

public abstract class Converter {
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
        cube.sethMob(false); //c2 property
        cube.setLavaControl(shell.getLavaControl());
        cube.setName(shell.getName());
        cube.setParent(null); //there is no parenting in CuboidPlugin ... lol
        cube.setPriority(0); //There is no priority in CuboidPlugin ... lol
        cube.setProtection(shell.getProtection());
        cube.setRestriction(shell.getRestricted());
        cube.setSanctuary(shell.getSanctuary());
        cube.setSanctuarySpawnAnimals(shell.getAnimalSpawn());
        cube.setSecondPoint(shell.getOffset());
        cube.setTntSecure(shell.getTntSecure());
        cube.setWaterControl(shell.getWaterControl());
        cube.setWelcome(shell.getWelcome());
        cube.setWorld(shell.getWorld());
        return cube;
    }
    public void convert() {
        RegionManager regions = RegionManager.getInstance();
        for(CuboidShell shell : shells) {
            //Sorting
        }
    }
    
    public abstract void loadFiles();
}
