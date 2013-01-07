package net.playblack.cuboids.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.cuboids.regions.CuboidNode;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.ToolBox;

import net.playblack.mcutils.Vector;

/**
 * MysqlData extends BaseData and represents the data layer for retrieving
 * Cuboids from a MySQL database.
 * 
 * @author Chris
 * 
 */
public class MysqlData implements BaseData {

    private HashMap<String, String> cfg;
    private EventLogger log;
    private boolean connected = false;
    private Connection connection;

    public MysqlData(HashMap<String, String> conf, EventLogger log) {
        cfg = conf;
        this.log = log;
        if (getConnection() != null) {
            try {
                PreparedStatement ps = getConnection()
                        .prepareStatement(
                                "CREATE TABLE IF NOT EXISTS `nodes` ("
                                        + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                                        + "`_parent` varchar(45) DEFAULT NULL,"
                                        + "`allowPvp` varchar(5) DEFAULT NULL,"
                                        + "`blockFireSpread` varchar(5) DEFAULT NULL,"
                                        + "`creeperSecure` varchar(5) DEFAULT NULL,"
                                        + "`farewell` varchar(120) DEFAULT NULL,"
                                        + "`freeBuild` varchar(5) DEFAULT NULL,"
                                        + "`groupList` varchar(500) DEFAULT NULL,"
                                        + "`healing` varchar(5) DEFAULT NULL,"
                                        + "`lavaControl` varchar(5) DEFAULT NULL,"
                                        + "`name` varchar(45) NOT NULL,"
                                        + "`playerList` varchar(500) DEFAULT NULL,"
                                        + "`point1` varchar(120) DEFAULT NULL,"
                                        + "`point2` varchar(120) DEFAULT NULL,"
                                        + "`priority` int(11) DEFAULT NULL,"
                                        + "`protection` varchar(5) DEFAULT NULL,"
                                        + "`restriction` varchar(5) DEFAULT NULL,"
                                        + "`sanctuary` varchar(5) DEFAULT NULL,"
                                        + "`sanctuarySpawnAnimals` varchar(5) DEFAULT NULL,"
                                        + "`tabuCommands` varchar(500) DEFAULT NULL,"
                                        + "`tntSecure` varchar(5) DEFAULT NULL,"
                                        + "`waterControl` varchar(5) DEFAULT NULL,"
                                        + "`welcome` varchar(120) DEFAULT NULL,"
                                        + "`wheatControl` varchar(5) DEFAULT NULL,"
                                        + "`world` varchar(45) DEFAULT NULL,"
                                        + "`dimension` int(1) DEFAULT NULL,"
                                        + "`hmob` varchar(5) DEFAULT NULL, "
                                        + "`tabuItems` varchar(500) DEFAULT NULL, "
                                        + "`physics` varchar(5) DEFAULT NULL, "
                                        + "`enderControl` varchar(5) DEFAULT NULL, "
                                        + "PRIMARY KEY (`id`)"
                                        + ") ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1");
                ps.execute();
            } catch (SQLException e) {
                log.logMessage(
                        "Cuboids2: Problem with setting up the database table! "
                                + e.getMessage(), "SEVERE");
                e.printStackTrace();
            }
        } else {
            log.logMessage(
                    "Cuboids2: No DB Connection! Please revise your database settings!",
                    "SEVERE");
        }
    }

    /**
     * Get a mysql connection to work with
     * 
     * @return
     */
    private Connection getConnection() {
        try {
            if (connected == false) {
                // log.logMessage("Logging with this info: "+cfg.get("url") +
                // "?autoReconnect=true&user=" + cfg.get("user") + "&password="
                // + cfg.get("passwd"), "INFO");
                connection = DriverManager.getConnection(cfg.get("url")
                        + "?autoReconnect=true&user=" + cfg.get("user")
                        + "&password=" + cfg.get("passwd"));
                connected = true;
                return connection;
            } else {
                return connection;
            }
        } catch (SQLException e) {
            log.logMessage(
                    "Cuboids2: SQL Connection Problem: " + e.getMessage(),
                    "SEVERE");
            // log.logMessage("URL: "+cfg.get("url"), "INFO");
            return null;
        }
        // return false;
    }

    /**
     * Used to convert command or user lists to string.
     * 
     * @param list
     * @return
     */
    private String listToCsv(ArrayList<String> list) {
        StringBuilder csv = new StringBuilder();
        if (!list.isEmpty() && list != null) {
            for (String entry : list) {
                csv.append(entry).append(",");
            }
        }
        if (csv.length() > 0) {
            csv.deleteCharAt(csv.length() - 1);
        }
        return csv.toString();
    }

    private String integerListToCsv(ArrayList<Integer> list) {
        StringBuilder csv = new StringBuilder();
        if (!list.isEmpty() && list != null) {
            for (Integer entry : list) {
                csv.append(entry).append(",");
            }
        }
        if (csv.length() > 0) {
            csv.deleteCharAt(csv.length() - 1);
        }
        return csv.toString();
    }

    /**
     * Check if the given Node has a save in the database already
     * 
     * @param node
     * @return true if node exists, false otherwise
     * @throws SQLException
     */
    public boolean mysqlCheckCuboid(CuboidNode node) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement(
                "SELECT id FROM nodes"
                        + " WHERE name=? AND world=? AND dimension=?");
        ps.setString(1, node.getCuboid().getName());
        ps.setString(2, node.getCuboid().getWorld());
        ps.setInt(3, node.getCuboid().getDimension());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;
    }

    /**
     * This updates the given node in the database, use only if node exists!
     * 
     * @param node
     * @throws SQLException
     */
    public void mysqlUpdateNode(CuboidNode node) throws SQLException {
        CuboidE cube = node.getCuboid();
        if (cube.getName().equals(cube.getParentDeprecated())) {
            cube.setParent(ToolBox.stringToNull("null"));
        }
        PreparedStatement ps = getConnection().prepareStatement(
                "UPDATE nodes" + " SET" + " _parent=?," + " allowPvp=?,"
                        + " blockFireSpread=?," + " creeperSecure=?,"
                        + " farewell=?," + " freeBuild=?," + " groupList=?,"
                        + " healing=?," + " lavaControl=?," + " name=?,"
                        + " playerList=?," + " point1=?," + " point2=?,"
                        + " priority=?," + " protection=?," + " restriction=?,"
                        + " sanctuary=?," + " sanctuarySpawnAnimals=?,"
                        + " tabuCommands=?," + " tntSecure=?,"
                        + " waterControl=?," + " welcome=?,"
                        + " wheatControl=?," + " world=?," + " dimension=?,"
                        + " hmob=?," + " tabuItems=?," + " physics=?,"
                        + " enderControl=?"
                        + " WHERE name=? AND world=? AND dimension=?");
        ps.setInt(32, cube.getDimension());
        ps.setString(31, "" + cube.getWorld());
        ps.setString(30, "" + cube.getName());
        ps.setString(29, "" + cube.hasEnderControl());
        ps.setString(28, "" + cube.isPhysicsDisabled());
        ps.setString(27, integerListToCsv(cube.getRestrictedItems()));
        ps.setString(26, "" + cube.ishMob());
        ps.setInt(25, cube.getDimension());
        ps.setString(24, "" + cube.getWorld());
        ps.setString(23, "" + cube.isFarmland());
        ps.setString(22, "" + cube.getWelcome());
        ps.setString(21, "" + cube.isWaterControl());
        ps.setString(20, "" + cube.isTntSecure());
        ps.setString(19, "" + listToCsv(cube.getTabuCommands()));
        ps.setString(18, "" + cube.sanctuarySpawnAnimals());
        ps.setString(17, "" + cube.isSanctuary());
        ps.setString(16, "" + cube.isRestricted());
        ps.setString(15, "" + cube.isProtected());
        ps.setString(14, "" + cube.getPriority());
        ps.setString(13, cube.getMinorPoint().getX() + ","
                + cube.getMinorPoint().getY() + ","
                + cube.getMinorPoint().getZ());
        ps.setString(12, cube.getMajorPoint().getX() + ","
                + cube.getMajorPoint().getY() + ","
                + cube.getMajorPoint().getZ());
        ps.setString(11, cube.getPlayerList());
        ps.setString(10, cube.getName());
        ps.setString(9, "" + cube.isLavaControl());
        ps.setString(8, "" + cube.isHealingArea());
        ps.setString(7, cube.getGroupList());
        ps.setString(6, "" + cube.isFreeBuild());
        ps.setString(5, cube.getFarewell());
        ps.setString(4, "" + cube.isCreeperSecure());
        ps.setString(3, "" + cube.isBlockFireSpread());
        ps.setString(2, "" + cube.isAllowedPvp());
        ps.setString(1, cube.getParentDeprecated());
        ps.executeUpdate();
    }

    /**
     * Insert a new node into the database
     * 
     * @param node
     * @throws SQLException
     */
    public void mysqlInsertNode(CuboidNode node) throws SQLException {
        CuboidE cube = node.getCuboid();
        PreparedStatement ps = getConnection().prepareStatement(
                "INSERT INTO nodes" + "(_parent, " + "allowPvp, "
                        + "blockFireSpread, " + "creeperSecure," + "farewell,"
                        + "freeBuild," + "groupList," + "healing,"
                        + "lavaControl," + "name," + "playerList," + "point1,"
                        + "point2," + "priority," + "protection,"
                        + "restriction," + "sanctuary,"
                        + "sanctuarySpawnAnimals," + "tabuCommands,"
                        + "tntSecure," + "waterControl," + "welcome,"
                        + "wheatControl," + "world, " + "dimension, " + "hmob,"
                        + "tabuItems)" +

                        "VALUES(" + "?," + "?," + "?," + "?," + "?," + "?,"
                        + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?,"
                        + "?," + "?," + "?," + "?," + "?," + "?," + "?," + "?,"
                        + "?," + "?," + "?," + "?," + "?)");
        ps.setString(27, this.integerListToCsv(cube.getRestrictedItems()));
        ps.setString(26, "" + cube.ishMob());
        ps.setInt(25, cube.getDimension());
        ps.setString(24, "" + cube.getWorld());
        ps.setString(23, "" + cube.isFarmland());
        ps.setString(22, "" + cube.getWelcome());
        ps.setString(21, "" + cube.isWaterControl());
        ps.setString(20, "" + cube.isTntSecure());
        ps.setString(19, "" + listToCsv(cube.getTabuCommands()));
        ps.setString(18, "" + cube.sanctuarySpawnAnimals());
        ps.setString(17, "" + cube.isSanctuary());
        ps.setString(16, "" + cube.isRestricted());
        ps.setString(15, "" + cube.isProtected());
        ps.setInt(14, cube.getPriority());
        ps.setString(13, cube.getMinorPoint().getX() + ","
                + cube.getMinorPoint().getY() + ","
                + cube.getMinorPoint().getZ());
        ps.setString(12, cube.getMajorPoint().getX() + ","
                + cube.getMajorPoint().getY() + ","
                + cube.getMajorPoint().getZ());
        ps.setString(11, cube.getPlayerList());
        ps.setString(10, cube.getName());
        ps.setString(9, "" + cube.isLavaControl());
        ps.setString(8, "" + cube.isHealingArea());
        ps.setString(7, cube.getGroupList());
        ps.setString(6, "" + cube.isFreeBuild());
        ps.setString(5, "" + cube.getFarewell());
        ps.setString(4, "" + cube.isCreeperSecure());
        ps.setString(3, "" + cube.isBlockFireSpread());
        ps.setString(2, "" + cube.isAllowedPvp());
        ps.setString(1, cube.getParentDeprecated());
        ps.executeUpdate();
    }

    public ArrayList<CuboidE> resultSetToCuboid(ResultSet rs)
            throws SQLException {
        ArrayList<CuboidE> list = new ArrayList<CuboidE>();
        while (rs.next()) {
            CuboidE cube = new CuboidE();
            cube.setAllowPvp(ToolBox.stringToBoolean(rs.getString("allowPvp")));
            cube.setBlockFireSpread(ToolBox.stringToBoolean(rs
                    .getString("blockFireSpread")));
            cube.setCreeperSecure(ToolBox.stringToBoolean(rs
                    .getString("creeperSecure")));
            if (ToolBox.stringToNull(rs.getString("farewell")) != null) {
                cube.setFarewell(rs.getString("farewell"));
            }
            cube.setFarmland(ToolBox.stringToBoolean(rs
                    .getString("wheatControl")));
            cube.setFreeBuild(ToolBox.stringToBoolean(rs.getString("freeBuild")));
            cube.setHealing(ToolBox.stringToBoolean(rs.getString("healing")));
            cube.setLavaControl(ToolBox.stringToBoolean(rs
                    .getString("lavaControl")));
            cube.setName(rs.getString("name"));
            if (ToolBox.stringToNull(rs.getString("_parent")) != null) {
                cube.setParent(ToolBox.stringToNull(rs.getString("_parent")));
            }
            cube.setPriority(rs.getInt("priority"));
            cube.setProtection(ToolBox.stringToBoolean(rs
                    .getString("protection")));
            cube.setRestriction(ToolBox.stringToBoolean(rs
                    .getString("restriction")));
            cube.setSanctuary(ToolBox.stringToBoolean(rs.getString("sanctuary")));
            cube.setSanctuarySpawnAnimals(ToolBox.stringToBoolean(rs
                    .getString("sanctuarySpawnAnimals")));
            cube.setTntSecure(ToolBox.stringToBoolean(rs.getString("tntSecure")));
            cube.setWaterControl(ToolBox.stringToBoolean(rs
                    .getString("waterControl")));
            if (ToolBox.stringToNull(rs.getString("welcome")) != null) {
                cube.setWelcome(ToolBox.stringToNull(rs.getString("welcome")));
            }

            // ---------------------- LEGACY LOADING ------------------------
            String world = rs.getString("world");
            if (world.equalsIgnoreCase("NORMAL")) {
                cube.setDimension(0);
                cube.setWorld(CServer.getServer().getDefaultWorld().getName());
                cube.hasChanged = true;
            } else if (world.equalsIgnoreCase("NETHER")) {
                cube.setDimension(-1);
                cube.setWorld(CServer.getServer().getDefaultWorld().getName());
                cube.hasChanged = true;
            } else if (world.equalsIgnoreCase("END")) {
                cube.setDimension(1);
                cube.setWorld(CServer.getServer().getDefaultWorld().getName());
                cube.hasChanged = true;
            } else {
                cube.setDimension(rs.getInt("dimension"));
                cube.setWorld(world);
            }
            // ---------------------- LEGACY LOADING END --------------------

            cube.sethMob(ToolBox.stringToBoolean(rs.getString("hmob")));

            /* ***********************************************************
             * GIVE ME POINTS
             * **********************************************************
             */
            try {
                String[] point1 = rs.getString("point1").trim().split(",");
                String[] point2 = rs.getString("point2").trim().split(",");
                Vector p1 = new Vector(Double.parseDouble(point1[0]),
                        Double.parseDouble(point1[1]),
                        Double.parseDouble(point1[2]));
                Vector p2 = new Vector(Double.parseDouble(point2[0]),
                        Double.parseDouble(point2[1]),
                        Double.parseDouble(point2[2]));
                cube.setPoints(p1, p2);
            } catch (NumberFormatException e) {
                log.logMessage("Cuboids2: Failed to parse points for Cuboid "
                        + cube.getName() + ", stopping!", "SEVERE");
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
            cube.addTabuCommand(rs.getString("tabuCommands"));
            cube.setPhysics(ToolBox.stringToBoolean(rs.getString("physics")));
            cube.setEnderControl(ToolBox.stringToBoolean(rs
                    .getString("enderControl")));
            list.add(cube);
        }
        return list;
    }

    @Override
    public void saveCuboid(CuboidNode node) {
        if (getConnection() == null) {
            log.logMessage(
                    "Cuboids2: Failed to establish Database Connection, cannot SAVE Cuboid Nodes!",
                    "SEVERE");
            return; // uh oh ...
        }
        try {
            if (mysqlCheckCuboid(node)) {
                mysqlUpdateNode(node);
            } else {
                mysqlInsertNode(node);
            }
        } catch (SQLException e) {
            log.logMessage("Exception while saving node: " + e.getMessage(),
                    "INFO");
        }
    }

    @Override
    public void saveAll(ArrayList<CuboidNode> treeList, boolean silent,
            boolean force) {
        if (!silent) {
            log.logMessage("Cuboids2: Saving Cuboid Nodes (your areas)", "INFO");
            log.logMessage("Saving to MySQL backend ...", "INFO");
        }
        for (CuboidNode tree : treeList) {
            for (CuboidNode node : tree.toList()) {
                if (node.getCuboid().hasChanged || force) {
                    saveCuboid(node);
                }
            }
        }
    }

    @Override
    public void loadAll(RegionManager handler) {
        if (getConnection() == null) {
            log.logMessage(
                    "Cuboids2: Failed to establish Database Connection, cannot LOAD Cuboid Nodes!",
                    "SEVERE");
            return; // uh oh ...
        }
        ArrayList<CuboidE> list = new ArrayList<CuboidE>();
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT * FROM nodes");
            list = resultSetToCuboid(ps.executeQuery());
        } catch (SQLException e) {
            log.logMessage("Cuboids2: Failed to load Cuboid Nodes. Reason: "
                    + e.getMessage(), "SEVERE");
            e.printStackTrace();
            return;
        }
        // turning into a node list

        ArrayList<CuboidNode> nodelist = new ArrayList<CuboidNode>(list.size());
        ArrayList<CuboidNode> visited = new ArrayList<CuboidNode>(list.size());
        for (CuboidE cube : list) {
            nodelist.add(handler.createNode(cube));
        }

        // Creating Root nodes here
        for (int i = 0; i < nodelist.size(); i++) {

            if (visited.contains(nodelist.get(i))) {
                continue;
            }
            if (nodelist.get(i).getCuboid().getParentDeprecated() == null) {
                if (handler.cuboidExists(nodelist.get(i).getName(), nodelist
                        .get(i).getWorld(), nodelist.get(i).getDimension())) {
                    visited.add(nodelist.get(i));
                    continue;
                } else {
                    if (nodelist.get(i) != null
                            && !handler.cuboidExists(nodelist.get(i).getName(),
                                    nodelist.get(i).getWorld(), nodelist.get(i)
                                            .getDimension())) {
                        handler.addRoot(nodelist.get(i));
                        visited.add(nodelist.get(i));
                        continue;
                    }
                }
            }
        }
        visited.clear();
        // Sorting parents here:
        for (int i = 0; i < nodelist.size(); i++) {
            if (visited.contains(nodelist.get(i))) {
                continue;
            }
            if (nodelist.get(i).getCuboid().getParentDeprecated() != null) {
                CuboidNode parent = handler.getCuboidNodeByName(nodelist.get(i)
                        .getParent(), nodelist.get(i).getWorld(),
                        nodelist.get(i).getDimension());
                if (parent != null
                        && !handler.cuboidExists(nodelist.get(i).getName(),
                                nodelist.get(i).getWorld(), nodelist.get(i)
                                        .getDimension())) {
                    parent.addChild(nodelist.get(i));
                    visited.add(nodelist.get(i));
                    i = -1; // Count from the beginning again as we might have
                            // missed a couple of nodes with a missing parent
                            // that now exists
                    continue;
                }
            }
        }
        handler.cleanParentRelations();
        handler.log.logMessage("Cuboids2: Cuboids loaded successfully", "INFO");
        return;
    }

    @Override
    public void loadCuboid(RegionManager handler, String name, String world) {
        if (getConnection() == null) {
            log.logMessage(
                    "Cuboids2: Failed to establish Database Connection, cannot LOAD Cuboid Nodes!",
                    "SEVERE");
            return; // uh oh ...
        }
        try {
            PreparedStatement ps = getConnection().prepareStatement(
                    "SELECT ? FROM nodes WHERE world=?");
            ps.setString(1, name);
            ps.setString(2, world);
            ArrayList<CuboidE> list = resultSetToCuboid(ps.executeQuery());
            for (CuboidE cube : list) {
                if (cube != null) {
                    if (handler.cuboidExists(cube.getName(), cube.getWorld(),
                            cube.getDimension())) {
                        handler.updateCuboidNode(cube);
                    } else {
                        handler.addCuboid(cube);
                    }
                    return;
                }
            }

        } catch (SQLException e) {
            log.logMessage("Cuboids2: Failed to load Cuboid Nodes. Reason: "
                    + e.getMessage(), "SEVERE");
        }

    }

    @Override
    public void removeNode(CuboidNode node) {
        if (getConnection() == null) {
            log.logMessage(
                    "Cuboids2: Failed to establish Database Connection, cannot REMOVE Cuboid Nodes!",
                    "SEVERE");
            return; // uh oh ...
        }
        try {
            PreparedStatement ps = getConnection()
                    .prepareStatement(
                            "DELETE FROM nodes WHERE name=? AND world=? AND dimension=?");
            ps.setString(1, node.getName());
            ps.setString(2, node.getWorld());
            ps.setInt(3, node.getDimension());
            ps.execute();
        } catch (SQLException e) {
            log.logMessage("Cuboids2: Failed to remove a Cuboid Node. Reason: "
                    + e.getMessage(), "SEVERE");
            e.printStackTrace();
        }

    }
}
