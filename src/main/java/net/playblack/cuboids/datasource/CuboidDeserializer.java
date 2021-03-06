package net.playblack.cuboids.datasource;

import net.canarymod.Canary;
import net.canarymod.CanaryDeserializeException;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Vector3D;
import net.canarymod.database.DataAccess;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.playblack.cuboids.datasource.da.RegionDataAccess;
import net.playblack.cuboids.datasource.da.RegionExtraDataAccess;
import net.playblack.cuboids.exceptions.DeserializeException;
import net.playblack.cuboids.selections.CuboidSelection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The deserializer. Reads from data source and creates CuboidSelection objects
 *
 * @author Chris
 */
public class CuboidDeserializer {

    protected RegionDataAccess blocks;
    protected List<DataAccess> extraData;
    protected CuboidSelection cuboid;
    protected World world;
    protected String regionName;

    private Map<Vector3D, Item[]> chestContents;
    private Map<Vector3D, String[]> signContents;

    /**
     * Prepared stuff for deserializing. Please check for file_exists before
     * calling this and send the according world name along.
     *
     * @param name
     * @param world
     */
    public CuboidDeserializer(String name, World world) {
        cuboid = new CuboidSelection();
        this.world = world;
        this.blocks = new RegionDataAccess(name);
        this.regionName = name;
        chestContents = new HashMap<Vector3D, Item[]>();
        signContents = new HashMap<Vector3D, String[]>();

        try {
            // Load data from database
            Database.get().load(this.blocks, new HashMap<String, Object>());
            extraData = new ArrayList<DataAccess>();
            HashMap<String, Object> filter = new HashMap<String, Object>();
            filter.put("region", name);
            Database.get().loadAll(new RegionExtraDataAccess(), extraData, filter);

            // Apply
            for (int i = 0; i < blocks.blockData.size(); ++i) {
                generateFromLine(blocks.blockData.get(i), i);
            }

        }
        catch (DatabaseReadException e) {
            e.printStackTrace();
        }
        catch (DeserializeException f) {
            f.printStackTrace();
        }

        // -----------------------------------------------

    }

    private void generateFromLine(String line, int lineNumber) throws DeserializeException {
        String[] split = line.split("\\|"); // results in 0=block,1=vector
        BlockType block;
        Vector3D key;
        try {
            block = BlockType.fromString(split[0]);
            key = Vector3D.fromString(split[1]);
        }
        catch (ArrayIndexOutOfBoundsException f) {
            throw new DeserializeException("Bad line format for region " + regionName + "Line:\n", line);
        }

        catch (CanaryDeserializeException f) {
            throw new DeserializeException("Bad line format for region " + regionName + "Line:\n", line);
        }

        if ((key != null) && (block != null)) {
            cuboid.setBlock(key, block);
            if (BlockType.Chest.equals(block)) {
                generateChestContents(lineNumber, key);
            }
            if (block.getMachineName().endsWith("sign")) {
                generateSignData(lineNumber, key);
            }
        }
        else {
            throw new DeserializeException("Could not deserialize a Vector-Block pair!", line);
        }

    }

    private void generateSignData(int index, Vector3D key) {
        String signText = ((RegionExtraDataAccess)extraData.get(index)).data;
        if (signText != null) {
            signContents.put(key, signText.split("\\|"));
        }
    }

    private void generateChestContents(int index, Vector3D key) throws CanaryDeserializeException {
        String chestContents = ((RegionExtraDataAccess)extraData.get(index)).data;
        if (chestContents != null) {
            String[] itemSplit = chestContents.split("\\|");
            Item[] parsedItems = new Item[itemSplit.length];
            for (int i = 0; i < itemSplit.length; ++i) {
                parsedItems[i] = Canary.deserialize(itemSplit[i], Item.class);
            }
            this.chestContents.put(key, parsedItems);
        }
    }

    public CuboidSelection getSelection() {
        return cuboid;
    }

    public Map<Vector3D, String[]> getSignContents() {
        return this.signContents;
    }

    public Map<Vector3D, Item[]> getChestContents() {
        return this.chestContents;
    }

}
