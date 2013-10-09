package net.playblack.cuboids.datasource;

import net.playblack.cuboids.selections.CuboidSelection;

import java.io.FileWriter;
import java.io.IOException;

public class FlatFileSerializer extends CuboidSerializer {

    public FlatFileSerializer(CuboidSelection cuboid) {
        super(cuboid);
    }

    @Override
    public void save(String name, String world) {
        String blockLocation = "plugins/cuboids2/backups/blocks_"
                + world.toUpperCase() + "_" + name;
        String extrasLocation = "plugins/cuboids2/backups/contents_"
                + world.toUpperCase() + "_" + name;
        FileWriter blockWriter = null;
        FileWriter extraWriter = null;
        try {
            blockWriter = new FileWriter(blockLocation);
            extraWriter = new FileWriter(extrasLocation);
            for (String b : this.baseData) {
                blockWriter.write(b + System.getProperty("line.separator"));
            }
            for (Integer index : this.contents.keySet()) {
                extraWriter.write(index + "=" + this.contents.get(index)
                        + System.getProperty("line.separator"));
            }

        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                if (blockWriter != null) {
                    blockWriter.close();
                }
                if (extraWriter != null) {
                    extraWriter.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
