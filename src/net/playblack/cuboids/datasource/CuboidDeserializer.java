//package net.playblack.cuboids.datasource;
//
//import java.io.BufferedReader;
//import java.io.DataInputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import net.playblack.cuboids.blocks.CBlock;
//import net.playblack.cuboids.selections.CuboidSelection;
//import net.playblack.mcutils.Vector;
//
///**
// * The deserializer. Reads from data source and creates CuboidSelection objects
// * @author Chris
// *
// */
//public class CuboidDeserializer {
//    
//    protected ArrayList<String>blockData = new ArrayList<String>();
//    protected HashMap<Integer, String>extraData = new HashMap<Integer, String>();
//    protected CuboidSelection cuboid;
//    /**
//     * Prepared stuff for deserializing. Please check for file_exists before calling this
//     * and send the according world name along.
//     * @param name
//     * @param world
//     */
//    public CuboidDeserializer(String name, String world) {
//        String blockLocation = "plugins/cuboids2/backups/blocks_"+world.toUpperCase()+"_"+name;
//        String extrasLocation = "plugins/cuboids2/backups/contents_"+world.toUpperCase()+"_"+name;
//        cuboid = new CuboidSelection();
//        try {
//            //get file input streams
//            FileInputStream fstreamBlocks = new FileInputStream(blockLocation);
//            FileInputStream fstreamExtras = new FileInputStream(extrasLocation);
//            //Get data input streams
//            DataInputStream inBlocks = new DataInputStream(fstreamBlocks);
//            DataInputStream inExtras = new DataInputStream(fstreamExtras);
//            //Finally get the damn readers!
//
//            BufferedReader blockInput = new BufferedReader(new InputStreamReader(inBlocks));
//            BufferedReader extrasInput = new BufferedReader(new InputStreamReader(inExtras));
//            
//            //Now read
//            String line;
//            try {
//                //First off, get the data from extras so we can already close the extrasInput
//                while((line = extrasInput.readLine()) != null) {
//                    String[] split = line.split("=");
//                    extraData.put(Integer.parseInt(split[0]), split[1]); //This will be converted to useful stuff later
//                }
//                line = null;
//                
//                while((line = blockInput.readLine()) != null) {
//                    blockData.add(line); //this will be converted after this crazy file loading is done
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    extrasInput.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        // -----------------------------------------------
//        
//    }
//    
//    private void generateFromLine(String line, int lineNumber, CuboidSelection selection) throws DeserializeException {
//        String[]split = line.split("\\|"); //results in 0=block,1=vector
//        CBlock block = null;
//        Vector key = null;
//        try {
//            block = BaseBlock.deserialize(split[0]);
//            key = Vector.deserialize(split[1]);
//        } catch (DeserializeException e) {
//            e.printStackTrace();
//        }
//        
//        if((key != null) && (block != null)) {
//            selection.setBlockAt(key, block);
//            if(block instanceof ChestBlock) {
//                generateChestContents(lineNumber, (ChestBlock)block);
//            }
//            if(block instanceof SignBlock) {
//                generateSignData(lineNumber, (SignBlock)block);
//            }
//        }
//        else {
//            throw new DeserializeException("Could not deserialize a Vector-Block pair!", line);
//        }
//
//    }
//    
//    private void generateSignData(int index, SignBlock sign) {
//        String signText = extraData.get(Integer.valueOf(index));
//        if(signText != null) {
//            sign.setText(signText.split("\\|"));
//        }
//    }
//    
//    private void generateChestContents(int index, ChestBlock block) throws DeserializeException {
//        String chestContents = extraData.get(Integer.valueOf(index));
//        if(chestContents != null) {
//            String[]itemSplit = chestContents.split("\\|");
//            for(String item : itemSplit) {
//                block.putItem(BaseItem.deserialize(item));
//            }
//        }
//    }
//    public CuboidSelection convert() {
//        for(int index = 0; index < blockData.size(); index++) {
//            try {
//                generateFromLine(blockData.get(index), index, this.cuboid);
//            } catch (DeserializeException e) {
//                e.printStackTrace();
//            }
//        }
//        return this.cuboid;
//    }
//
//}
