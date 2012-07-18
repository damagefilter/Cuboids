import java.util.ArrayList;

import net.playblack.cuboids.converters.CuboidShell;
import net.playblack.mcutils.Vector;

public class CuboidDShell implements CuboidShell {

    CuboidD cuboid;

    public CuboidDShell(CuboidD cuboid) {
        this.cuboid = cuboid;
    }

    @Override
    public boolean getProtection() {
        return cuboid.protection;
    }

    @Override
    public boolean getRestricted() {
        return cuboid.restricted;
    }

    @Override
    public boolean getPvp() {
        return cuboid.PvP;
    }

    @Override
    public boolean getHealing() {
        return cuboid.heal;
    }

    @Override
    public boolean getCreeper() {
        return cuboid.creeper;
    }

    @Override
    public boolean getSanctuary() {
        return cuboid.sanctuary;
    }

    @Override
    public boolean getWaterControl() {
        return false;
    }

    @Override
    public boolean getLavaControl() {
        return false;
    }

    @Override
    public boolean getCreative() {
        return false;
    }

    @Override
    public boolean getFireProof() {
        return false;
    }

    @Override
    public boolean getTntSecure() {
        return false;
    }

    @Override
    public boolean getAnimalSpawn() {
        return true;
    }

    @Override
    public int getDimension() {
        return 0; // default dimension (overworld)
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String getWorld() {
        return cuboid.world;
    }

    @Override
    public String getName() {
        return cuboid.name;
    }

    @Override
    public String getFarewell() {
        return cuboid.farewellMessage;
    }

    @Override
    public String getWelcome() {
        return cuboid.welcomeMessage;
    }

    @Override
    public ArrayList<String> tabuCommands() {
        return cuboid.disallowedCommands;
    }

    @Override
    public ArrayList<String> getPlayerlist() {
        ArrayList<String> players = new ArrayList<String>(2);
        for (String name : cuboid.allowedPlayers) {
            if (name.startsWith("g:")) {
                continue;
            }
            players.add(name);
        }
        return players;
    }

    @Override
    public ArrayList<String> getGrouplist() {
        ArrayList<String> groups = new ArrayList<String>(2);
        for (String name : cuboid.allowedPlayers) {
            if (!name.startsWith("g:")) {
                continue;
            }
            groups.add(name);
        }
        return groups;
    }

    @Override
    public Vector getOrigin() {
        return new Vector(cuboid.coords[0], cuboid.coords[1], cuboid.coords[2]);
    }

    @Override
    public Vector getOffset() {
        return new Vector(cuboid.coords[3], cuboid.coords[4], cuboid.coords[5]);
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
        return false;
    }

    @Override
    public boolean getPhysics() {
        return false;
    }

}
