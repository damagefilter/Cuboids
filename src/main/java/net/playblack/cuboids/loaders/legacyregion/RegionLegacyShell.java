package net.playblack.cuboids.loaders.legacyregion;

import net.canarymod.Canary;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.loaders.CuboidShell;
import net.playblack.cuboids.regions.Region;

import java.util.ArrayList;

/**
 * Loads regions from old places that are from old hmod / canary classic days
 */
public class RegionLegacyShell implements CuboidShell {

    private Region file;

    public RegionLegacyShell(Region file2) {
        this.file = file2;
    }

    @Override
    public boolean getProtection() {
//        return file.getBoolean("protection", true);
        return true;
    }

    @Override
    public boolean getRestricted() {
//        return file.getBoolean("restricted", false);
        return true;
    }

    @Override
    public boolean getPvp() {
        return true;
    }

    @Override
    public boolean getHealing() {
        return true;
    }

    @Override
    public boolean getCreeper() {
        return true;
    }

    @Override
    public boolean getSanctuary() {
        return true;
    }

    @Override
    public boolean getWaterControl() {
        return true;
    }

    @Override
    public boolean getLavaControl() {
        return true;
    }

    @Override
    public boolean getCreative() {
        return true;
    }

    @Override
    public boolean getFireProof() {
        return true;
    }

    @Override
    public boolean getTntSecure() {
        return true;
    }

    @Override
    public boolean getAnimalSpawn() {
        return true;
    }

    @Override
    public int getDimension() {
        return 0;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String getWorld() {
        return Canary.getServer().getDefaultWorldName();
    }

    @Override
    public String getName() {
        return "invalid";
    }

    @Override
    public String getFarewell() {
        return "bye_invalid";
    }

    @Override
    public String getWelcome() {
        return "welcome_invalid";
    }

    @Override
    public java.util.List<String> tabuCommands() {
        return new ArrayList<String>();
    }

    @Override
    public java.util.List<String> getPlayerlist() {
        return new ArrayList<String>();
    }

    @Override
    public java.util.List<String> getGrouplist() {
        return new ArrayList<String>();
    }

    @Override
    public Vector3D getOrigin() {
        return new Vector3D();
    }

    @Override
    public Vector3D getOffset() {
        return new Vector3D();
    }

    @Override
    public boolean getFarmland() {
        return false;
    }

    @Override
    public boolean getHmob() {
        return false;
    }

    @Override
    public boolean getEnderControl() {
        return true;
    }

    @Override
    public boolean getPhysics() {
        return true;
    }

    @Override
    public boolean canConvertDirectly() {
        return true;
    }

    @Override
    public Region getRegion() {
        return this.file;
    }

}
