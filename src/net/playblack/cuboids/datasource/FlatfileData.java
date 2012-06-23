package net.playblack.cuboids.datasource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
public class FlatfileData implements BaseData {

    private Object lock = new Object();
    private EventLogger log;

    public FlatfileData(EventLogger log) {
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
            BufferedWriter out = new BufferedWriter(new FileWriter(path
                    + node.getWorld() + "_" + node.getDimension() + "_"
                    + node.getCuboid().getName() + ".c2n"));
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
            // System.out.println("  Saving Cuboid Trees to files...");
            log.logMessage("Saving Cuboid Nodes (your areas)", "INFO");
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
            for (CuboidNode root : treeList) {

                for (CuboidNode saveNode : root.toList()) {
                    if (saveNode.getCuboid().hasChanged || force == true) {
                        BufferedWriter out = new BufferedWriter(new FileWriter(
                                path + root.getWorld() + "_"
                                        + root.getDimension() + "_"
                                        + saveNode.getCuboid().getName()
                                        + ".c2n"));
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
                if (files.getName().toLowerCase().endsWith("c2n")) {
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
                            "Failed to load a Cuboid Area from file. It does not exist!",
                            "WARNING");
                    return;
                }

            }

            log.logMessage(
                    "Failed to load a Cuboid Area from file. It does not exist!",
                    "WARNING");
            return;
        } catch (IOException e) {
            log.logMessage(
                    "Failed to load a Cuboid Area from file. (IOException - Read/Write issue!!) "
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
            ArrayList<CuboidNode> visited = new ArrayList<CuboidNode>();
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
                    if (files.getName().toLowerCase().endsWith("c2n")) {
                        reader = new BufferedReader(new FileReader(path
                                + files.getName()));
                        props = reader.readLine();
                        // log.logMessage("Processing", "INFO");
                        nodelist.add(handler.createNode(csvToCuboid(props)));
                    }
                }
            } catch (Exception e) {
                log.logMessage(
                        "Failed to load Cuboid Data files! " + e.getMessage(),
                        "SEVERE");
                e.printStackTrace();
            }

            // Creating Root nodes here
            for (int i = 0; i < nodelist.size(); i++) {
                
                if(visited.contains(nodelist.get(i))) {
                    continue;
                }
                if (nodelist.get(i).getCuboid().getParent() == null) {
                    if (handler.cuboidExists(nodelist.get(i).getName(), nodelist.get(i).getWorld(), nodelist.get(i).getDimension())) {
                        visited.add(nodelist.get(i));
                        continue;
                    } 
                    else {
                        if (nodelist.get(i) != null && !handler.cuboidExists(nodelist.get(i).getName(), nodelist.get(i).getWorld(), nodelist.get(i).getDimension())) {
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
                if(visited.contains(nodelist.get(i))) {
                    continue;
                }
                if (nodelist.get(i).getCuboid().getParent() != null) {
                    CuboidNode parent = handler.getCuboidNodeByName(nodelist.get(i).getParent(), nodelist.get(i).getWorld(), nodelist.get(i).getDimension());
                    if (parent != null && !handler.cuboidExists(nodelist.get(i).getName(), nodelist.get(i).getWorld(), nodelist.get(i).getDimension())) {
                        parent.addChild(nodelist.get(i));
                        visited.add(nodelist.get(i));
                        i = -1; //Count from the beginning again as we might have missed a couple of nodes with a missing parent that now exists
                        continue;
                    }
                }
            }
            handler.cleanParentRelations();
            log.logMessage("Cuboids loaded successfully", "INFO");
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
        if (csv.length >= 23) {
            CuboidE cube = new CuboidE();
            cube.setName(csv[0]);

            if (!csv[1].equalsIgnoreCase("null")) {
                // There is a node that has parented itself
                // Remove this parent relation, if will be saved properly next
                // time a save is issued
                if (csv[1].equals(csv[0])) {
                    cube.setParent(null);
                } else {
                    cube.setParent(csv[1]);
                }

            }

            cube.setPriority(Integer.parseInt(csv[2]));

            cube.setWorld(csv[3]);
            cube.setDimension(Integer.parseInt(csv[4]));

            Vector v1 = new Vector(Double.parseDouble(csv[5]),
                    Double.parseDouble(csv[6]), Double.parseDouble(csv[7]));

            Vector v2 = new Vector(Double.parseDouble(csv[8]),
                    Double.parseDouble(csv[9]), Double.parseDouble(csv[10]));
            cube.setPoints(v1, v2);
            cube.setCreeperSecure(ToolBox.stringToBoolean(csv[11]));
            cube.setHealing(ToolBox.stringToBoolean(csv[12]));
            cube.setProtection(ToolBox.stringToBoolean(csv[13]));
            cube.setSanctuary(ToolBox.stringToBoolean(csv[14]));
            cube.setSanctuarySpawnAnimals(ToolBox.stringToBoolean(csv[15]));
            cube.setAllowPvp(ToolBox.stringToBoolean(csv[16]));
            cube.setFreeBuild(ToolBox.stringToBoolean(csv[17]));
            cube.setBlockFireSpread(ToolBox.stringToBoolean(csv[18]));
            cube.setWelcome(ToolBox.stringToNull(csv[19]));
            cube.setFarewell(ToolBox.stringToNull(csv[20]));
            csv[21] = csv[21].replace("{COMMA}", ",");
            csv[22] = csv[22].replace("{COMMA}", ",");
            csv[23] = csv[23].replace("{COMMA}", ",");
            cube.addPlayer(csv[21]);
            cube.addGroup(csv[22]);
            cube.addTabuCommand(csv[23]);
            cube.setLavaControl(ToolBox.stringToBoolean(csv[24]));
            cube.setWaterControl(ToolBox.stringToBoolean(csv[25]));
            cube.setTntSecure(ToolBox.stringToBoolean(csv[26]));
            cube.setFarmland(ToolBox.stringToBoolean(csv[27]));
            cube.setRestriction(ToolBox.stringToBoolean(csv[28]));
            cube.sethMob(ToolBox.stringToBoolean(csv[29]));
            cube.addRestrictedItem(csv[30].replace("{COMMA}", ","));
            cube.setPhysics(ToolBox.stringToBoolean(csv[31]));
            cube.setEnderControl(ToolBox.stringToBoolean(csv[32]));
            return cube;
        }
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
        csv.append(node.getCuboid().getParent());
        csv.append(",");
        csv.append(node.getCuboid().getPriority());
        csv.append(",");
        csv.append(node.getCuboid().getWorld());
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

        // RESTRICTED ITEMS
        StringBuilder items = new StringBuilder();
        String iList = "";
        ArrayList<Integer> itemList = node.getCuboid().getRestrictedItems();
        if (itemList.size() > 0) {
            for (Integer item : itemList) {
                items.append(item.toString()).append("{COMMA}");
            }

            iList = items.toString();
        }
        if (iList.length() == 0 || iList.equalsIgnoreCase("")) {
            iList = "no_items";
        }
        csv.append(clist);
        csv.append(",");
        csv.append(node.getCuboid().isLavaControl());
        csv.append(",");
        csv.append(node.getCuboid().isWaterControl());
        csv.append(",");
        csv.append(node.getCuboid().isTntSecure());
        csv.append(",");
        csv.append(node.getCuboid().isFarmland());
        csv.append(",");
        csv.append(node.getCuboid().isRestricted());
        csv.append(",");
        csv.append(node.getCuboid().ishMob()).append(",");
        csv.append(iList).append(",");
        csv.append(node.getCuboid().isPhysicsDisabled()).append(",");
        csv.append(node.getCuboid().hasEnderControl()).append(",");

        return csv.toString();
    }

    @Override
    public void removeNode(CuboidNode node) {
        File file = new File("plugins/cuboids2/cuboids/" + node.getWorld()
                + "_" + node.getDimension() + "_" + node.getName() + ".c2n");
        if (file.exists()) {
            file.delete();
        }

    }
}
