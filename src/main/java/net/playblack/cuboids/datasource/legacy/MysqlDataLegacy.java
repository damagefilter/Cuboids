package net.playblack.cuboids.datasource.legacy;

import net.playblack.cuboids.datasource.BaseData;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.Debug;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MysqlData extends BaseData and represents the data layer for retrieving
 * Regions from a MySQL database.
 *
 * @author Chris
 */
public class MysqlDataLegacy implements BaseData {

    HashMap<String, ArrayList<Region>> loadedRegions = new HashMap<String, ArrayList<Region>>();
    private HashMap<String, String> cfg;
    private boolean connected = false;
    private Connection connection;

    public MysqlDataLegacy(HashMap<String, String> conf) {
        cfg = conf;
    }

    /**
     * Get a mysql connection to work with
     *
     * @return
     */
    private Connection getConnection() {
        try {
            if (!connected) {
                // log.logMessage("Logging with this info: "+cfg.get("url") +
                // "?autoReconnect=true&user=" + cfg.get("user") + "&password="
                // + cfg.get("passwd"), "INFO");
                connection = DriverManager.getConnection(cfg.get("url") + "?autoReconnect=true&user=" + cfg.get("user") + "&password=" + cfg.get("passwd"));
                connected = true;
                return connection;
            }
            else {
                return connection;
            }
        }
        catch (SQLException e) {
            Debug.logWarning("SQL Connection Problem: " + e.getMessage());
            // log.logMessage("URL: "+cfg.get("url"), "INFO");
            return null;
        }
        // return false;
    }

    /**
     * Check if the given Node has a save in the database already
     *
     * @param node
     * @return true if node exists, false otherwise
     * @throws SQLException
     */
    public boolean mysqlCheckRegion(Region node) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("SELECT id FROM nodes" + " WHERE name=? AND world=? AND dimension=?");
        ps.setString(1, node.getName());
        ps.setString(2, node.getWorld());
        ps.setInt(3, node.getDimension());
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    /**
     * This updates the given node in the database, use only if node exists!
     *
     * @param node
     * @throws SQLException
     */
    public void mysqlUpdateNode(Region node) throws SQLException {
        throw new UnsupportedOperationException("Cannot update files in legacy MySQL backend. Please use the new backend implementation.");
    }

    public HashMap<String, ArrayList<Region>> resultSetToRegion(ResultSet rs) throws SQLException {
        HashMap<String, ArrayList<Region>> regionList = new HashMap<String, ArrayList<Region>>();
        regionList.put("root", new ArrayList<Region>());
//        ArrayList<Region> list = new ArrayList<Region>();
        while (rs.next()) {
            Region cube = new Region();
            cube.setProperty("pvp-damage", Status.softFromBoolean(ToolBox.stringToBoolean(rs.getString("allowPvp"))));
//            cube.setAllowPvp(ToolBox.stringToBoolean(rs.getString("allowPvp")));

            cube.setProperty("firespread", Status.softFromBoolean(!ToolBox.stringToBoolean(rs.getString("blockFireSpread"))));
//            cube.setBlockFireSpread(ToolBox.stringToBoolean(rs.getString("blockFireSpread")));
            cube.setProperty("creeper-explosion", Status.softFromBoolean(ToolBox.stringToBoolean(rs.getString("creeperSecure"))));
//            cube.setCreeperSecure(ToolBox.stringToBoolean(rs.getString("creeperSecure")));

            if (ToolBox.stringToNull(rs.getString("farewell")) != null) {
                cube.setFarewell(rs.getString("farewell"));
            }
            cube.setProperty("crops-trampling", Status.fromBoolean(!ToolBox.stringToBoolean(rs.getString("wheatControl"))));
//            cube.setFarmland();
            cube.setProperty("creative", Status.fromBoolean(ToolBox.stringToBoolean(rs.getString("freeBuild"))));
//            cube.setFreeBuild(ToolBox.stringToBoolean(rs.getString("freeBuild")));
            cube.setProperty("healing", Status.fromBoolean(ToolBox.stringToBoolean(rs.getString("healing"))));
//            cube.setHealing(ToolBox.stringToBoolean(rs.getString("healing")));
            cube.setProperty("lava-flow", Status.softFromBoolean(!ToolBox.stringToBoolean(rs.getString("lavaControl"))));
//            cube.setLavaControl(ToolBox.stringToBoolean(rs.getString("lavaControl")));
            cube.setName(rs.getString("name"));
            cube.setPriority(rs.getInt("priority"));

            cube.setProperty("protection", Status.softFromBoolean(ToolBox.stringToBoolean(rs.getString("protection"))));
//            cube.setProtection(ToolBox.stringToBoolean(rs.getString("protection")));
            cube.setProperty("enter-cuboid", Status.softFromBoolean(!ToolBox.stringToBoolean(rs.getString("restriction"))));
//            cube.setRestriction(ToolBox.stringToBoolean(rs.getString("restriction")));
            cube.setProperty("mob-damage", Status.softFromBoolean(!ToolBox.stringToBoolean(rs.getString("sanctuary"))));
            cube.setProperty("mob-spawn", Status.softFromBoolean(!ToolBox.stringToBoolean(rs.getString("sanctuary"))));
            cube.setProperty("animal-spawn", Status.softFromBoolean(!ToolBox.stringToBoolean(rs.getString("sanctuarySpawnAnimals"))));
//            cube.setSanctuary(ToolBox.stringToBoolean(rs.getString("sanctuary")));
//            cube.setSanctuarySpawnAnimals(ToolBox.stringToBoolean(rs.getString("sanctuarySpawnAnimals")));
            cube.setProperty("tnt-explosion", Status.softFromBoolean(!ToolBox.stringToBoolean(rs.getString("tntSecure"))));
//            cube.setTntSecure(ToolBox.stringToBoolean(rs.getString("tntSecure")));
            cube.setProperty("water-flow", Status.softFromBoolean(!ToolBox.stringToBoolean(rs.getString("waterControl"))));
//            cube.setWaterControl(ToolBox.stringToBoolean(rs.getString("waterControl")));

            if (ToolBox.stringToNull(rs.getString("welcome")) != null) {
                cube.setWelcome(ToolBox.stringToNull(rs.getString("welcome")));
            }

            // ---------------------- LEGACY LOADING ------------------------
            String world = rs.getString("world");
            if (world.equalsIgnoreCase("NORMAL")) {
                cube.setDimension(0);
                cube.setWorld(CServer.getServer().getDefaultWorld().getName());
                cube.hasChanged = true;
            }
            else if (world.equalsIgnoreCase("NETHER")) {
                cube.setDimension(-1);
                cube.setWorld(CServer.getServer().getDefaultWorld().getName());
                cube.hasChanged = true;
            }
            else if (world.equalsIgnoreCase("END")) {
                cube.setDimension(1);
                cube.setWorld(CServer.getServer().getDefaultWorld().getName());
                cube.hasChanged = true;
            }
            else {
                cube.setDimension(rs.getInt("dimension"));
                cube.setWorld(world);
            }
            // ---------------------- LEGACY LOADING END --------------------
            cube.setProperty("more-mobs", Status.softFromBoolean(ToolBox.stringToBoolean(rs.getString("hmob"))));
//            cube.sethMob(ToolBox.stringToBoolean(rs.getString("hmob")));

            /* ***********************************************************
             * GIVE ME POINTS
             * **********************************************************
             */
            try {
                String[] point1 = rs.getString("point1").trim().split(",");
                String[] point2 = rs.getString("point2").trim().split(",");
                Vector p1 = new Vector(Double.parseDouble(point1[0]), Double.parseDouble(point1[1]), Double.parseDouble(point1[2]));
                Vector p2 = new Vector(Double.parseDouble(point2[0]), Double.parseDouble(point2[1]), Double.parseDouble(point2[2]));
                cube.setBoundingBox(p1, p2);
            }
            catch (NumberFormatException e) {
                Debug.logWarning("Failed to parse points for Region " + cube.getName() + ", stopping!");
                e.printStackTrace();
                return null;
            }
            if (!rs.getString("playerList").equalsIgnoreCase("no_players")) {
                cube.addPlayer(rs.getString("playerList"));
            }
            if (!rs.getString("groupList").equalsIgnoreCase("no_groups")) {
                cube.addGroup(rs.getString("groupList"));
            }
            cube.addRestrictedItem(rs.getString("tabuItems"));
            cube.addRestrictedCommand(rs.getString("tabuCommands"));
            cube.setProperty("physics", Status.softFromBoolean(ToolBox.stringToBoolean(rs.getString("physics"))));
//            cube.setPhysics(ToolBox.stringToBoolean(rs.getString("physics")));
            cube.setProperty("enderman-pickup", Status.softFromBoolean(ToolBox.stringToBoolean(rs.getString("enderControl"))));
//            cube.setEnderControl(ToolBox.stringToBoolean(rs.getString("enderControl")));
            String parent = ToolBox.stringToNull(rs.getString("_parent"));
            //We have a prent
            if (parent != null) {
                if (!regionList.containsKey(parent)) {
                    regionList.put(parent, new ArrayList<Region>());
                }
                regionList.get(parent).add(cube);
            }
            //No parent, put in root
            else {
                regionList.get("root").add(cube);
            }
        }
        return regionList;
    }

    @Override
    public void saveRegion(Region node) {
        throw new UnsupportedOperationException("Cannot save files in legacy MySQL backend. Please use the new backend implementation.");
    }

    @Override
    public void saveAll(ArrayList<Region> treeList, boolean silent, boolean force) {
        throw new UnsupportedOperationException("Cannot save files in legacy MySQL backend. Please use the new backend implementation.");
    }

    @Override
    public int loadAll() {
        RegionManager regionMan = RegionManager.get();

        if (getConnection() == null) {
            Debug.logError("Failed to establish Database Connection, cannot load Region data! (legacy)");
            return 0; // uh oh ...
        }

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM nodes");
            loadedRegions = resultSetToRegion(ps.executeQuery());
        }
        catch (SQLException e) {
            Debug.logError("Failed to load Region Nodes (legacy). Reason: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }

        //Sort out parents and stuff.
        int numRegions = 0;
        for (String key : loadedRegions.keySet()) {
            //Root has no parents to sort out
            if (!key.equals("root")) {
                Region parent = findByName(key);
                for (Region r : loadedRegions.get(key)) {
                    if (parent == null) {
                        Debug.logWarning("Cannot find parent " + key + ". Dropping regions with this parent.");
                        break;
                    }
                    numRegions++;
                    r.setParent(parent);
                }
            }
        }

        //Now that we have all the parents sorted out, we can just add all nodes under "root" to the regionmanager
        for (Region root : loadedRegions.get("root")) {
            numRegions++;
            regionMan.addRoot(root);
        }
        return numRegions;
    }

    /**
     * Loads all regions but doesn't add them to the region manager.
     * Returned list contains root regions all others are parented down
     * @return
     */
    public List<Region> loadAllRaw() {
        ArrayList<Region> regions = new ArrayList<Region>();
        if (getConnection() == null) {
            Debug.logError("Failed to establish Database Connection, cannot load Region data! (legacy)");
            return regions; // uh oh ...
        }

        try {
            PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM nodes");
            loadedRegions = resultSetToRegion(ps.executeQuery());
        }
        catch (SQLException e) {
            Debug.logError("Failed to load Region Nodes (legacy). Reason: " + e.getMessage());
            e.printStackTrace();
            return regions;
        }

        //Sort out parents and stuff.
        for (String key : loadedRegions.keySet()) {
            //Root has no parents to sort out
            if (!key.equals("root")) {
                Region parent = findByName(key);
                for (Region r : loadedRegions.get(key)) {
                    if (parent == null) {
                        Debug.logWarning("Cannot find parent " + key + ". Dropping regions with this parent.");
                        break;
                    }
                    r.setParent(parent);
                }
            }
        }

        //Now that we have all the parents sorted out, we can just add all nodes under "root" to the regionmanager
        for (Region root : loadedRegions.get("root")) {
            regions.add(root);
        }
        return regions;
    }

    /**
     * Get a region from the given list with the given name
     *
     * @param name
     * @return
     */
    private Region findByName(String name) {
        for (String key : loadedRegions.keySet()) {
            for (Region r : loadedRegions.get(key)) {
                if (r.getName().equals(name)) {
                    return r;
                }
            }
        }
        return null;
    }

    @Override
    public void loadRegion(String name, String world, int dimension) {
        throw new UnsupportedOperationException("Loading single Regions in legacy mode is not supported!");
    }

    @Override
    public void deleteRegion(Region node) {
        throw new UnsupportedOperationException("Cannot delete from legacy MySQL backen. Please use the new backend implementation.");
    }
}
