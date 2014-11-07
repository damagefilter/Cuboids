package net.playblack.cuboids.loaders.cuboidf;

import net.playblack.cuboids.loaders.CuboidShell;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.mcutils.Vector;
import net.visualillusionsent.utils.PropertiesFile;

import java.util.ArrayList;
import java.util.Collections;

public class CuboidFShell implements CuboidShell {

    private PropertiesFile file;

    public CuboidFShell(PropertiesFile file2) {
        this.file = file2;
    }

    @Override
    public boolean getProtection() {
        return file.getBoolean("protection", true);
    }

    @Override
    public boolean getRestricted() {
        return file.getBoolean("restricted", false);
    }

    @Override
    public boolean getPvp() {
        return file.getBoolean("PvP", false);
    }

    @Override
    public boolean getHealing() {
        return file.getBoolean("heal", false);
    }

    @Override
    public boolean getCreeper() {
        return file.getBoolean("creeper", true);
    }

    @Override
    public boolean getSanctuary() {
        return file.getBoolean("sanctuary", false);
    }

    @Override
    public boolean getWaterControl() {
        return file.getBoolean("water", false);
    }

    @Override
    public boolean getLavaControl() {
        return file.getBoolean("lava", false);
    }

    @Override
    public boolean getCreative() {
        return file.getBoolean("creative", false);
    }

    @Override
    public boolean getFireProof() {
        return file.getBoolean("fire", true);
    }

    @Override
    public boolean getTntSecure() {
        return file.getBoolean("tnt", true);
    }

    @Override
    public boolean getAnimalSpawn() {
        return file.getBoolean("animals", true);
    }

    @Override
    public int getDimension() {
        return file.getInt("dimension", 0);
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String getWorld() {
        return CServer.getServer().getDefaultWorld().getName();
    }

    @Override
    public String getName() {
        return file.getString("name", "world");
    }

    @Override
    public String getFarewell() {
        String msg = file.getString("farewellMessage", null);
        if (msg != null && msg.length() == 0) {
            return null;
        }
        return msg;
    }

    @Override
    public String getWelcome() {
        String msg = file.getString("welcomeMessage", null);
        if (msg != null && msg.length() == 0) {
            return null;
        }
        return msg;
    }

    @Override
    public java.util.List<String> tabuCommands() {
        String[] cmds = file.getString("disallowedCommands", "").split(",");
        ArrayList<String> ret = new ArrayList<String>(cmds.length);
        Collections.addAll(ret, cmds);
        return ret;
    }

    @Override
    public java.util.List<String> getPlayerlist() {
        ArrayList<String> players = new ArrayList<String>(2);
        String[] a = file.getString("allowedPlayers", "").split(",");
        for (String name : a) {
            if (name.startsWith("g:")) {
                continue;
            }
            players.add(name);
        }
        return players;
    }

    @Override
    public java.util.List<String> getGrouplist() {
        ArrayList<String> groups = new ArrayList<String>(2);
        String[] a = file.getString("allowedPlayers", "").split(",");
        for (String name : a) {
            if (!name.startsWith("g:")) {
                continue;
            }
            groups.add(name);
        }
        return groups;
    }

    @Override
    public Vector getOrigin() {
        return new Vector(file.getInt("X1", 0), file.getInt("Y1", 0), file.getInt("Z1", 0));
    }

    @Override
    public Vector getOffset() {
        return new Vector(file.getInt("X2", 0), file.getInt("Y2", 0), file.getInt("Z2", 0));
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
        return file.getBoolean("enderman", false);
    }

    @Override
    public boolean getPhysics() {
        return file.getBoolean("physics", false);
    }

}
