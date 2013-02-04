import java.util.ArrayList;

import net.playblack.cuboids.converters.CuboidShell;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.mcutils.Vector;

public class CuboidFShell implements CuboidShell {

    private PropertiesFile file;

    public CuboidFShell(PropertiesFile prop) {
        this.file = prop;
    }

    @Override
    public boolean getProtection() {
        return file.getBoolean("protection");
    }

    @Override
    public boolean getRestricted() {
        return file.getBoolean("restricted");
    }

    @Override
    public boolean getPvp() {
        return file.getBoolean("PvP");
    }

    @Override
    public boolean getHealing() {
        return file.getBoolean("heal");
    }

    @Override
    public boolean getCreeper() {
        return file.getBoolean("creeper");
    }

    @Override
    public boolean getSanctuary() {
        return file.getBoolean("sanctuary");
    }

    @Override
    public boolean getWaterControl() {
        return file.getBoolean("water");
    }

    @Override
    public boolean getLavaControl() {
        return file.getBoolean("lava");
    }

    @Override
    public boolean getCreative() {
        return file.getBoolean("creative");
    }

    @Override
    public boolean getFireProof() {
        return file.getBoolean("fire");
    }

    @Override
    public boolean getTntSecure() {
        return file.getBoolean("tnt");
    }

    @Override
    public boolean getAnimalSpawn() {
        return file.getBoolean("animals");
    }

    @Override
    public int getDimension() {
        return file.getInt("dimension");
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
        return file.getString("name");
    }

    @Override
    public String getFarewell() {
        String msg = file.getString("farewellMessage");
        if (msg != null && msg.length() == 0) {
            return null;
        }
        return msg;
    }

    @Override
    public String getWelcome() {
        String msg = file.getString("welcomeMessage");
        if (msg != null && msg.length() == 0) {
            return null;
        }
        return msg;
    }

    @Override
    public ArrayList<String> tabuCommands() {
        String[] cmds = file.getString("disallowedCommands").split(",");
        ArrayList<String> ret = new ArrayList<String>(cmds.length);
        for (String name : cmds) {
            ret.add(name);
        }
        return ret;
    }

    @Override
    public ArrayList<String> getPlayerlist() {
        ArrayList<String> players = new ArrayList<String>(2);
        String[] a = file.getString("allowedPlayers").split(",");
        for (String name : a) {
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
        String[] a = file.getString("allowedPlayers").split(",");
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
        return new Vector(file.getInt("X1"), file.getInt("Y1"),
                file.getInt("Z1"));
    }

    @Override
    public Vector getOffset() {
        return new Vector(file.getInt("X2"), file.getInt("Y2"),
                file.getInt("Z2"));
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
        return file.getBoolean("enderman");
    }

    @Override
    public boolean getPhysics() {
        return file.getBoolean("physics");
    }

}
