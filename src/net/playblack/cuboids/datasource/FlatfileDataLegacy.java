package net.playblack.cuboids.datasource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.regions.CuboidE;
import net.playblack.cuboids.regions.CuboidNode;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.EventLogger;
import net.playblack.mcutils.ToolBox;
import net.playblack.mcutils.Vector;

/**
 * FlatFileData extends BaseData and represents the data layer for retrieving
 * Cuboids from text files.
 * 
 * @author Chris
 * 
 */
public class FlatfileDataLegacy implements BaseData {

    private Object lock = new Object();
    private EventLogger log;

    public FlatfileDataLegacy(EventLogger log) {
        this.log = log;
    }

    @Override
    public void saveCuboid(CuboidNode node) {
        // CuboidNode node = getCuboidByName(cube, world);
        String path = "plugins/cuboids2/cuboids/";
        try {
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            CWorld world = CServer.getServer().getWorld(node.getWorld(),
                    node.getDimension());
            String dimName = world.dimensionFromId(node.getDimension());
            BufferedWriter out = new BufferedWriter(new FileWriter(path
                    + dimName + "_" + node.getCuboid().getName() + ".node"));
            out.write(cuboidToCsv(node));
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serialize the data trees and sort stuff out
     */
    @Override
    public void saveAll(ArrayList<CuboidNode> treeList, boolean silent,
            boolean force) {
        if (!silent) {
            // System.out.println("Cuboids2: Saving Cuboid Trees to files...");
            log.logMessage("Cuboids2: Saving Cuboid Nodes (your areas)", "INFO");
            log.logMessage("Saving to fatfile backend ...", "INFO");
        }
        // cleanTreeFiles();
        // FileOutputStream fos;
        String path = "plugins/cuboids2/cuboids/";
        try {
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            CWorld world;// = CServer.getServer().getWorld(node.getWorld(),
                         // node.getDimension());
            String dimName;// = world.dimensionFromId(node.getDimension());
            for (CuboidNode root : treeList) {
                for (CuboidNode saveNode : root.toList()) {
                    if (saveNode.getCuboid().hasChanged || force == true) {
                        world = CServer.getServer().getWorld(
                                saveNode.getWorld(), saveNode.getDimension());
                        dimName = world
                                .dimensionFromId(saveNode.getDimension());
                        BufferedWriter out = new BufferedWriter(new FileWriter(
                                path + dimName + "_" + saveNode.getName()
                                        + ".node"));
                        out.write(cuboidToCsv(saveNode));
                        out.close();
                        saveNode.getCuboid().hasChanged = false;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadCuboid(RegionManager handler, String name, String world) {
        try {
            String path = "plugins/cuboids2/cuboids/";
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            for (File files : new File(path).listFiles()) {
                if (files.getName().toLowerCase().endsWith("node")) {
                    BufferedReader reader = new BufferedReader(new FileReader(
                            path + files.getName()));
                    String props = reader.readLine();
                    CuboidE cube = csvToCuboid(props);
                    if (cube != null) {
                        if (handler.cuboidExists(cube.getName(),
                                cube.getWorld(), cube.getDimension())) {
                            handler.updateCuboidNode(cube);
                        } else {
                            handler.addCuboid(cube);
                        }
                        return;
                    }
                    log.logMessage(
                            "Cuboids2: Failed to load a Cuboid Area from file. It does not exist!",
                            "WARNING");
                    return;
                }

            }

            log.logMessage(
                    "Cuboids2: Failed to load a Cuboid Area from file. It does not exist!",
                    "WARNING");
            return;
        } catch (IOException e) {
            log.logMessage(
                    "Cuboids2: Failed to load a Cuboid Area from file. (IOException - Read/Write issue!!) "
                            + e.getMessage(), "WARNING");
            e.printStackTrace();
        }
        return;
    }

    @Override
    public void loadAll(RegionManager handler) {
        // log.logMessage("Cuboids2: Loading Cuboid Data", "INFO");
        synchronized (lock) {
            ArrayList<CuboidNode> nodelist = new ArrayList<CuboidNode>();
            try {
                // Load node files into a big big (probably) node list
                // ObjectInputStream ois;
                BufferedReader reader;
                // StringBuilder props = new StringBuilder();
                String path = "plugins/cuboids2/cuboids/";
                File folder = new File(path);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                // log.logMessage("Cuboids according to java: "+new
                // File(path).listFiles().length, "INFO");
                String props = "";
                for (File files : new File(path).listFiles()) {
                    // log.logMessage("Running "+i, "INFO");
                    if (files.getName().toLowerCase().endsWith("node")) {
                        reader = new BufferedReader(new FileReader(path
                                + files.getName()));
                        props = reader.readLine();
                        // log.logMessage("Processing", "INFO");
                        CuboidE c = csvToCuboid(props);
                        int dimension = CServer.getServer().getDimensionId(
                                c.getWorld());
                        String world = CServer.getServer().getDefaultWorld()
                                .getName();
                        c.setWorld(world);
                        c.setDimension(dimension);
                        nodelist.add(handler.createNode(c));

                        reader.close();
                    }
                }
                reader = null;
            } catch (ArrayIndexOutOfBoundsException e) {
                log.logMessage(
                        "Cuboids2: Failed to load Cuboid Data files!(AIOOBE) "
                                + e.getMessage(), "SEVERE");
                // e.printStackTrace();
            } catch (IOException f) {
                log.logMessage(
                        "Cuboids2: Failed to load Cuboid Data files!(IOE) "
                                + f.getMessage(), "SEVERE");
                // f.printStackTrace();
            }

            // Create root nodes
            // System.out.println("Cuboids2: Processing Node Files ...");
            for (int i = 0; i < nodelist.size(); i++) {
                // System.out.println("Running: "+i);
                if (nodelist.get(i).getCuboid().getParentDeprecated() == null) {
                    if (handler.cuboidExists(nodelist.get(i).getName(),
                            nodelist.get(i).getWorld(), nodelist.get(i)
                                    .getDimension())) {
                        nodelist.remove(i);
                        i = -1;
                    } else {
                        // System.out.println("Cuboids2: Root Node: "+nodelist.get(i).getCuboid().getName());
                        if (nodelist.get(i) != null
                                && !handler.cuboidExists(nodelist.get(i)
                                        .getName(), nodelist.get(i).getWorld(),
                                        nodelist.get(i).getDimension())) {
                            // System.out.println("Adding root node now.");
                            handler.addRoot(nodelist.get(i));
                            // rootNodes.add(nodelist.get(i));
                            nodelist.remove(i);
                            i = -1;
                        }
                    }
                }
            }
            // Sort parents
            // System.out.println("Cuboids2: Parenting Child Nodes");
            for (int i = 0; i < nodelist.size(); i++) {
                if (nodelist.get(i).getCuboid().getParentDeprecated() != null) {
                    CuboidNode parent = handler.getCuboidNodeByName(nodelist
                            .get(i).getParent(), nodelist.get(i).getWorld(),
                            nodelist.get(i).getDimension());
                    if (parent != null
                            && !handler.cuboidExists(nodelist.get(i).getName(),
                                    nodelist.get(i).getWorld(), nodelist.get(i)
                                            .getDimension())) {
                        // System.out.println("Cuboids2: Add child: "+nodelist.get(i).getCuboid().getName());
                        // System.out.println("to parent: "+parent.getCuboid().getName());
                        parent.addChild(nodelist.get(i));
                        nodelist.remove(i);
                        i = -1;
                    }
                }
            }
            handler.cleanParentRelations();
            log.logMessage("Cuboids2: Cuboids loaded successfully", "INFO");
        }
    }

    public static void cleanupFiles() {
        String path = "plugins/cuboids2/cuboids/";
        String bpath = "plugins/cuboids2/cuboids/backup_e_you_may_delete_this/";
        File folder = new File(path);
        if (!folder.exists()) {
            return;
        }
        File test = new File(bpath);
        if (!test.exists()) {
            test.mkdirs();
        }
        test = null;
        for (File file : new File(path).listFiles()) {
            // log.logMessage("Running "+i, "INFO");
            if (file.getName().toLowerCase().endsWith("node")) {
                File backup = new File(bpath + file.getName());
                boolean del = file.renameTo(backup);
                System.out.println("Moved node: " + del);
            }
        }
    }

    /**
     * Deserialize a string to a CuboidE object
     * 
     * @param str
     * @return
     */
    private CuboidE csvToCuboid(String str) {
        String[] csv = str.split(",");
        log.logMessage(
                "Cuboids split amount: " + csv.length + " for " + csv[0],
                "INFO");
        if (csv.length >= 23) {
            CuboidE cube = new CuboidE();
            cube.setName(csv[0]);

            if (!csv[1].equalsIgnoreCase("null")) {
                cube.setParent(csv[1]);
            }
            cube.setPriority(Integer.parseInt(csv[2]));

            cube.setWorld(csv[3]);

            Vector v1 = new Vector(Double.parseDouble(csv[4]),
                    Double.parseDouble(csv[5]), Double.parseDouble(csv[6]));

            Vector v2 = new Vector(Double.parseDouble(csv[7]),
                    Double.parseDouble(csv[8]), Double.parseDouble(csv[9]));
            cube.setPoints(v1, v2);
            cube.setCreeperSecure(ToolBox.stringToBoolean(csv[10]));
            cube.setHealing(ToolBox.stringToBoolean(csv[11]));
            cube.setProtection(ToolBox.stringToBoolean(csv[12]));
            cube.setSanctuary(ToolBox.stringToBoolean(csv[13]));
            cube.setSanctuarySpawnAnimals(ToolBox.stringToBoolean(csv[14]));
            cube.setAllowPvp(ToolBox.stringToBoolean(csv[15]));
            cube.setFreeBuild(ToolBox.stringToBoolean(csv[16]));
            cube.setBlockFireSpread(ToolBox.stringToBoolean(csv[17]));
            cube.setWelcome(ToolBox.stringToNull(csv[18]));
            cube.setFarewell(ToolBox.stringToNull(csv[19]));
            csv[20] = csv[20].replace("{COMMA}", ",");
            csv[21] = csv[21].replace("{COMMA}", ",");
            csv[22] = csv[22].replace("{COMMA}", ",");
            cube.addPlayer(csv[20]);
            cube.addGroup(csv[21]);
            cube.addTabuCommand(csv[22]);
            // V 1.2.0 stuff current max lenght:27
            cube.setLavaControl(ToolBox.stringToBoolean(csv[23]));
            cube.setWaterControl(ToolBox.stringToBoolean(csv[24]));
            cube.setTntSecure(ToolBox.stringToBoolean(csv[25]));
            cube.setFarmland(ToolBox.stringToBoolean(csv[26]));
            cube.setRestriction(ToolBox.stringToBoolean(csv[27]));
            // V 1.4.0 stuff current max lenght:28
            if (csv.length >= 29) {
                cube.sethMob(ToolBox.stringToBoolean(csv[28]));
            }
            // log.logMessage("Returning Cube while loading area."+csv[0],
            // "INFO");
            return cube;
        }
        // log.logMessage("Returning Null while loading area."+csv[0], "INFO");
        return null;
    }

    private String cuboidToCsv(CuboidNode node) {
        /*
         * Save order: name, parentName, priority, world, p1-x, p1-y, p1-z,
         * p2-x, p2-y, p2-z, creeper, healing, protection, sanctuary,
         * sanctuarySpawnAnimals, pvp, freebuild, firespread, welcome, farewell,
         * playerlist, grouplist, tabuCommands
         */
        StringBuilder csv = new StringBuilder();
        csv.append(node.getCuboid().getName());
        csv.append(",");
        csv.append(node.getCuboid().getParentDeprecated());
        csv.append(",");
        csv.append(node.getCuboid().getPriority());
        csv.append(",");
        csv.append(node.getCuboid().getDimension());
        csv.append(",");

        csv.append(node.getCuboid().getMinorPoint().getX());
        csv.append(",");
        csv.append(node.getCuboid().getMinorPoint().getY());
        csv.append(",");
        csv.append(node.getCuboid().getMinorPoint().getZ());
        csv.append(",");

        csv.append(node.getCuboid().getMajorPoint().getX());
        csv.append(",");
        csv.append(node.getCuboid().getMajorPoint().getY());
        csv.append(",");
        csv.append(node.getCuboid().getMajorPoint().getZ());
        csv.append(",");
        csv.append(node.getCuboid().isCreeperSecure());
        csv.append(",");
        csv.append(node.getCuboid().isHealingArea());
        csv.append(",");
        csv.append(node.getCuboid().isProtected());
        csv.append(",");
        csv.append(node.getCuboid().isSanctuary());
        csv.append(",");
        csv.append(node.getCuboid().sanctuarySpawnAnimals());
        csv.append(",");
        csv.append(node.getCuboid().isAllowedPvp());
        csv.append(",");
        csv.append(node.getCuboid().isFreeBuild());
        csv.append(",");
        csv.append(node.getCuboid().isBlockFireSpread());
        csv.append(",");
        if (node.getCuboid().getWelcome() != null) {
            csv.append(node.getCuboid().getWelcome().replace(",", "{COMMA}"));
            csv.append(",");
        } else {
            csv.append(node.getCuboid().getWelcome());
            csv.append(",");
        }
        if (node.getCuboid().getFarewell() != null) {
            csv.append(node.getCuboid().getFarewell().replace(",", "{COMMA}"));
            csv.append(",");
        } else {
            csv.append(node.getCuboid().getFarewell());
            csv.append(",");
        }

        // PLAYERS
        csv.append(node.getCuboid().getPlayerList().replace(",", "{COMMA}"));
        csv.append(",");

        // GROUPS
        csv.append(node.getCuboid().getGroupList().replace(",", "{COMMA}"));
        csv.append(",");

        // COMMANDS
        StringBuilder commands = new StringBuilder();
        String clist = "";
        ArrayList<String> commandsList = node.getCuboid().getTabuCommands();
        if (commandsList.size() > 0) {
            for (int i = 0; i < commandsList.size(); i++) {
                commands.append(commandsList.get(i) + ",");
            }

            clist = commands.toString();
            clist = clist.substring(0, (clist.length() - 1));
            clist = clist.replace(",", "{COMMA}");
        }
        if (clist.length() == 0 || clist.equalsIgnoreCase("")) {
            clist = "no_commands";
        }
        csv.append(clist);
        csv.append(",");

        // V 1.2.0 stuff
        csv.append(node.getCuboid().isLavaControl());
        csv.append(",");
        csv.append(node.getCuboid().isWaterControl());
        csv.append(",");
        csv.append(node.getCuboid().isTntSecure());
        csv.append(",");
        csv.append(node.getCuboid().isFarmland());
        csv.append(",");
        // 1.4.0 stuff
        csv.append(node.getCuboid().isRestricted());
        csv.append(",");
        // 1.8
        csv.append(node.getCuboid().ishMob());

        return csv.toString();
    }

    @Override
    public void removeNode(CuboidNode node) {
        File file = new File("plugins/cuboids2/cuboids/"
                + node.getCuboid().getDimension() + "_"
                + node.getCuboid().getName() + ".node");
        if (file.exists()) {
            file.delete();
        }

    }
}
