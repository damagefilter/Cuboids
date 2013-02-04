package net.playblack.cuboids.datasource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.cuboids.regions.Region.Status;
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
    
    //List of cuboids with a parent that wasn't loaded yet 
    //Parent -> childs
    private HashMap<String, ArrayList<String>> parentUpdateList = new HashMap<String, ArrayList<String>>();
    
    // List of loaded nodes
    private ArrayList<Region> nodelist = new ArrayList<Region>();

    public FlatfileDataLegacy(EventLogger log) {
        this.log = log;
    }

    @Override
    public void saveRegion(Region node) {
        throw new IllegalStateException("CuboidE is deprecated and must not be saved. Use a newer backend implementation!");
    }

    @Override
    public void saveAll(ArrayList<Region> treeList, boolean silent, boolean force) {
        throw new IllegalStateException("CuboidE is deprecated and must not be saved. Use a newer backend implementation!");
    }

    @Override
    public void loadRegion(String name, String world) {
       throw new IllegalStateException("Loading single CuboidE files is not supported anymore!");
    }


    
    @Override
    public void loadAll() {
        log.logMessage("**** ATTEMPTING TO LOAD CUBOID-E FROM FILE **** ", "INFO");
        RegionManager handler = RegionManager.get();
        synchronized (lock) {
            
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
                String props = "";
                //Reads cuboids
                for (File files : new File(path).listFiles()) {
                    // log.logMessage("Running "+i, "INFO");
                    if (files.getName().toLowerCase().endsWith("node")) {
                        reader = new BufferedReader(new FileReader(path
                                + files.getName()));
                        props = reader.readLine();
                        // log.logMessage("Processing", "INFO");
                        Region c = new Region();
                        int dimension = CServer.getServer().getDimensionId(c.getWorld());
                        String world = CServer.getServer().getDefaultWorld().getName();
                        c.setWorld(world);
                        c.setDimension(dimension);
                        c = csvToCuboid(c, props);
                        reader.close();
                    }
                }
                reader = null;
            } catch (ArrayIndexOutOfBoundsException e) {
                log.logMessage("Cuboids2: Failed to load Cuboid Data files!(AIOOBE) " + e.getMessage(), "SEVERE");
                // e.printStackTrace();
            } catch (IOException f) {
                log.logMessage("Cuboids2: Failed to load Cuboid Data files!(IOE) " + f.getMessage(), "SEVERE");
                // f.printStackTrace();
            }
            
            //All nodes collected, lets fix up the parents
            for(String parentName : parentUpdateList.keySet()) {
                Region parent = findByNameFromNodeList(parentName);
                if(parent == null) {
                    continue;
                }
                for(String cubename : parentUpdateList.get(parentName)) {
                    Region cube = findByNameFromNodeList(cubename);
                    if(cube != null) {
                        cube.setParent(parent);
                        
                    }
                }
            }
            
            // Create root nodes
            // System.out.println("Cuboids2: Processing Node Files ...");
            for (int i = 0; i < nodelist.size(); i++) {
                // System.out.println("Running: "+i);
                if (nodelist.get(i).getParent() == null) {
                    if (handler.cuboidExists(nodelist.get(i).getName(),
                            nodelist.get(i).getWorld(), nodelist.get(i)
                                    .getDimension())) {
                        nodelist.remove(i);
                        i = -1;
                    } else {
                        // System.out.println("Cuboids2: Root Node: "+nodelist.get(i).getCuboid().getName());
                        if (nodelist.get(i) != null && !handler.cuboidExists(nodelist.get(i).getName(), nodelist.get(i).getWorld(), nodelist.get(i).getDimension())) {
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
                if (nodelist.get(i).getParent() != null) {
                    Region parent = handler.getCuboidByName(nodelist.get(i).getParent().getName(), nodelist.get(i).getWorld(), nodelist.get(i).getDimension());
                    if (parent != null && !handler.cuboidExists(nodelist.get(i).getName(), nodelist.get(i).getWorld(), nodelist.get(i).getDimension())) {
                        // System.out.println("Cuboids2: Add child: "+nodelist.get(i).getCuboid().getName());
                        // System.out.println("to parent: "+parent.getCuboid().getName());
                        parent.attachRegion(nodelist.get(i));
                        nodelist.remove(i);
                        i = -1;
                    }
                }
            }
            handler.cleanParentRelations();
            log.logMessage("Cuboids2: Cuboids loaded successfully", "INFO");
        }
    }

    private Region findByNameFromNodeList(String name) {
        for(Region node : nodelist) {
            if(node.getName().equals(name)) {
                return node;
            }
        }
        return null;
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
    private Region csvToCuboid(Region cube, String str) {
        String[] csv = str.split(",");
        
        log.logMessage("Cuboids split amount: " + csv.length + " for " + csv[0], "INFO");
        if (csv.length >= 23) {
            cube.setName(csv[0]);
            cube.setPriority(Integer.parseInt(csv[2]));

            cube.setWorld(csv[3]);

            Vector v1 = new Vector(Double.parseDouble(csv[4]),
                    Double.parseDouble(csv[5]), Double.parseDouble(csv[6]));

            Vector v2 = new Vector(Double.parseDouble(csv[7]),
                    Double.parseDouble(csv[8]), Double.parseDouble(csv[9]));
            cube.setBoundingBox(v1, v2);
            cube.setProperty("creeper-explosion", Status.fromBoolean(!ToolBox.stringToBoolean(csv[10])));
            cube.setProperty("healing", Status.fromBoolean(ToolBox.stringToBoolean(csv[11])));
            cube.setProperty("protection", Status.softFromBoolean(!ToolBox.stringToBoolean(csv[12])));
            cube.setProperty("mob-damage", Status.softFromBoolean(!ToolBox.stringToBoolean(csv[13])));
            cube.setProperty("mob-spawn", Status.softFromBoolean(!ToolBox.stringToBoolean(csv[13]))); //new and by default same as mob damage
            cube.setProperty("animal-spawn", Status.softFromBoolean(!ToolBox.stringToBoolean(csv[14])));
            cube.setProperty("pvp-damage", Status.fromBoolean(ToolBox.stringToBoolean(csv[15])));
            cube.setProperty("creative", Status.fromBoolean(ToolBox.stringToBoolean(csv[16])));
            cube.setProperty("firespread", Status.softFromBoolean(!ToolBox.stringToBoolean(csv[17])));
            cube.setWelcome(ToolBox.stringToNull(csv[18]));
            cube.setFarewell(ToolBox.stringToNull(csv[19]));
            csv[20] = csv[20].replace("{COMMA}", ",");
            csv[21] = csv[21].replace("{COMMA}", ",");
            csv[22] = csv[22].replace("{COMMA}", ",");
            cube.addPlayer(csv[20]);
            cube.addGroup(csv[21]);
            cube.addRestrictedCommand(csv[22]);
            // V 1.2.0 stuff current max lenght:27
            cube.setProperty("lava-flow", Status.softFromBoolean(!ToolBox.stringToBoolean(csv[23])));
            cube.setProperty("water-flow", Status.softFromBoolean(!ToolBox.stringToBoolean(csv[24])));
            cube.setProperty("tnt-explosion", Status.softFromBoolean(!ToolBox.stringToBoolean(csv[25])));
            cube.setProperty("crops-trampling", Status.fromBoolean(!ToolBox.stringToBoolean(csv[26])));
            cube.setProperty("enter-cuboid", Status.softFromBoolean(!ToolBox.stringToBoolean(csv[27])));
            // V 1.4.0 stuff current max lenght:28
            if (csv.length >= 29) {
                cube.setProperty("more-mobs", Status.softFromBoolean(ToolBox.stringToBoolean(csv[28])));
            }
            if (!csv[1].equalsIgnoreCase("null")) {
                if(parentUpdateList.get(csv[1]) == null) {
                    parentUpdateList.put(csv[1], new ArrayList<String>());
                }
                parentUpdateList.get(csv[1]).add(cube.getName());
            }
            return cube;
        }
        // log.logMessage("Returning Null while loading area."+csv[0], "INFO");
        return null;
    }

    @Override
    public void deleteRegion(Region node) {
        throw new IllegalStateException("Cannot operate on a deprecated Cuboid format (CuboidE)");
    }
}
