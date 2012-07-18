package net.playblack.cuboids.datasource;

import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.blocks.CItem;
import net.playblack.cuboids.blocks.ChestBlock;
import net.playblack.cuboids.blocks.SignBlock;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.Vector;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class takes care of serializing a CuboidSelection to text
 * 
 * @author chris
 */
public abstract class CuboidSerializer {
    /**
     * This arrayList contains serialized Blocks as String.
     */
    protected ArrayList<String> baseData;

    /**
     * This is a mapping that contains reference for serialized chest contens
     * and sign text to the block indes in the arraylist for baseData
     */
    protected HashMap<Integer, String> contents;

    /**
     * Load and serialize a cuboid selection into pieces of text
     * 
     * @param cuboid
     */
    public CuboidSerializer(CuboidSelection cuboid) {
        baseData = new ArrayList<String>(cuboid.getBlockList().size());
        contents = new HashMap<Integer, String>(); // cannot predict this size
        serializeBlockList(cuboid);
    }

    /**
     * Turn an ArrayList of BaseItems into a String for saving
     * 
     * @param items
     * @return
     */
    private String serializeItemList(ArrayList<CItem> items) {
        StringBuilder sb = new StringBuilder();
        for (CItem i : items) {
            sb.append(i.serialize()).append("|");
        }
        return sb.toString();
    }

    /**
     * Turn a Sign Text array into a string for saving
     * 
     * @param text
     * @return
     */
    private String serializeSignText(String[] text) {
        StringBuilder sb = new StringBuilder();
        // sb.append("[");
        for (String s : text) {
            sb.append(s).append("|");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        // sb.append("]");
        return sb.toString();
    }

    /**
     * Serialize the cuboid into pieces of Strings.
     * 
     * @param cuboid
     */
    private void serializeBlockList(CuboidSelection cuboid) {
        // LinkedHashMap<Vector,BaseBlock> toSerialize = cuboid.getBlockList();
        for (Vector key : cuboid.getBlockList().keySet()) {
            CBlock b = cuboid.getBlock(key);
            // System.out.println(b.toString());
            baseData.add(b.serialize().append("|").append(key.serialize())
                    .toString());

            // Do chest block serializing
            if ((Short) b.getType() == 54) {
                contents.put(baseData.size() - 1,
                        serializeItemList(((ChestBlock) b).getItemList()));

            }
            // Do SignBlock serializing
            else if ((Short) b.getType() == 63) {
                contents.put(baseData.size() - 1,
                        serializeSignText(((SignBlock) b).getSignTextArray()));
            }
        }
    }

    /**
     * Save the loaded and serialized CuboidSelection to the selected Backend
     */
    public abstract void save(String name, String world);

}
