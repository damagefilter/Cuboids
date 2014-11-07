package net.playblack.cuboids.datasource;

import net.canarymod.Canary;
import net.canarymod.api.chat.ChatComponent;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.Chest;
import net.canarymod.api.world.blocks.Sign;
import net.canarymod.api.world.blocks.TileEntity;
import net.canarymod.database.DataAccess;
import net.canarymod.database.exceptions.DatabaseWriteException;
import net.playblack.cuboids.datasource.da.RegionDataAccess;
import net.playblack.cuboids.datasource.da.RegionExtraDataAccess;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * This class takes care of serializing a CuboidSelection to something storable
 *
 * @author chris
 */
public class CuboidSerializer {
    /**
     * This arrayList contains serialized Blocks as String.
     */
    protected RegionDataAccess baseData;

    /**
     * This is a mapping that contains reference for serialized chest contents
     * and sign text to the block indexes in the arraylist for baseData
     */
    protected List<DataAccess> contents;

    protected World world;
    protected String regionName;

    /**
     * Load and serialize a cuboid selection into pieces of text
     *
     * @param cuboid
     */
    public CuboidSerializer(CuboidSelection cuboid, World targetWorld, String regionName) {
        this.regionName = regionName;
        baseData = new RegionDataAccess(regionName);
        baseData.blockData = new ArrayList<String>();
        contents = new ArrayList<DataAccess>();
        this.world = targetWorld;

        serializeBlockList(cuboid);
    }

    /**
     * Turn an ArrayList of BaseItems into a String for saving
     *
     * @param items
     * @return
     */
    private String serializeItemList(Item[] items) {
        StringBuilder sb = new StringBuilder();
        for (Item i : items) {
            // Canary provides an item serializer
            sb.append(Canary.serialize(i)).append("|");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Turn a Sign Text array into a string for saving
     *
     * @param text
     * @return
     */
    private String serializeSignText(ChatComponent[] text) {
        StringBuilder sb = new StringBuilder();
        for (ChatComponent s : text) {
            sb.append(s.getFullText()).append("|");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Serialize the cuboid into pieces of Strings.
     *
     * @param cuboid
     */
    private void serializeBlockList(CuboidSelection cuboid) {
        // LinkedHashMap<Vector,BaseBlock> toSerialize = cuboid.getBlockList();
        int i = 0;
        for (Vector key : cuboid.getBlockList().keySet()) {
            BlockType b = cuboid.getBlock(key);
            // System.out.println(b.toString());
            baseData.blockData.add(b.getMachineName().concat("|").concat(key.serialize().toString()));

            // Do chest block serializing
            if (b.getMachineName().equals("minecraft:chest")) {

                Block block = this.world.getBlockAt(key.getBlockX(), key.getBlockY(), key.getBlockZ());
                TileEntity te = block.getTileEntity();
                if (te instanceof Chest) {
                    RegionExtraDataAccess da = new RegionExtraDataAccess();
                    da.index = i;
                    da.region = this.regionName;
                    Chest c = (Chest)te;
                    da.data = this.serializeItemList(c.getContents());
                    contents.add(da);
                }

            }
            // Do SignBlock serializing
            else if (b.getMachineName().endsWith("sign")) { // wall mounted signs, standing signs and normal signs (whatever)
                Block block = this.world.getBlockAt(key.getBlockX(), key.getBlockY(), key.getBlockZ());
                TileEntity te = block.getTileEntity();
                if (te instanceof Sign) {
                    RegionExtraDataAccess da = new RegionExtraDataAccess();
                    da.index = i;
                    da.region = this.regionName;
                    Sign sign = (Sign)te;
                    da.data = serializeSignText(sign.getLines());
                    contents.add(da);
                }
            }
            ++i;
        }
    }

    /**
     * Save the loaded and serialized CuboidSelection to the selected Backend
     */
    public void save() {
        try {
            Canary.db().insert(baseData);
            Canary.db().insertAll(contents);
        }
        catch (DatabaseWriteException e) {
            e.printStackTrace();
        }
    }

}
