package net.playblack.cuboid;

import net.playblack.blocks.BaseBlock;
import net.playblack.blocks.BaseItem;
import net.playblack.mcutils.Vector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * This class takes care of serializing a CuboidSelection to text
 * @author chris
 */
public abstract class CuboidSerializer {
    /**
     * This arrayList contains serialized Blocks as String.
     */
    private ArrayList<String> baseData;
    
    /**
     * This is a mapping that contains reference for serialized
     * chest contens and sign text to the block indes in the arraylist for baseData
     */
    private HashMap<Integer, String> dataToExtraMapping;
    
    private CuboidSelection cuboid;
    
    public CuboidSerializer(CuboidSelection cuboid) {
        baseData = new ArrayList<String>(cuboid.getBlockList().size());
        dataToExtraMapping = new HashMap<Integer, String>(); //cannot predict this size
        this.cuboid = cuboid;
    }
    
    private String serializeItemList(ArrayList<BaseItem> items) {
        StringBuilder sb = new StringBuilder();
        for(BaseItem i : items) {
            sb.append(i.serialize());
        }
        return sb.toString();
    }
    
    private void serializeBlockList() {
      LinkedHashMap<Vector,BaseBlock> toSerialize = cuboid.getBlockList(); 
      StringBuilder sb = new StringBuilder();
      for(Vector key : toSerialize.keySet()) {
          BaseBlock b = toSerialize.get(key);
          if(b.getType() == 54) {
              //TODO: Serialize Chest Content
          }
          else if(b.getType == 63) {
              //TODO serialize sign contents
          }
          else {
              //simple block
              sb.append(key.serialize()).append(b.serialize());
          }
      }
    }
    
    
}
