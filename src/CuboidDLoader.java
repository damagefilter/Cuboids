import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import net.playblack.cuboids.converters.CuboidShell;
import net.playblack.cuboids.converters.Loader;
import net.playblack.mcutils.EventLogger;


public class CuboidDLoader implements Loader {

    @Override
    public ArrayList<CuboidShell> load() {
        EventLogger.getInstance().logMessage("Loading CuboidD", "INFO");
        ArrayList<CuboidShell> shells = new ArrayList<CuboidShell>(20); 
        try {
            File cuboidDPath = new File("cuboids/areas/");
            if(!cuboidDPath.exists()) {
                //Does not exists, return empty shell list
                return shells;
            }
            if(new File("cuboids/areas/").listFiles().length > 0) {
                File test = new File("plugins/cuboids2/backups_cuboidD/");
                if(!test.exists()) {
                    test.mkdirs();
                }
            }
            ObjectInputStream ois;
            for (File files : new File("cuboids/areas/").listFiles()) {
                if (files.getName().toLowerCase().endsWith(".area")) {
                    File cuboid = new File("cuboids/areas/" + files.getName());
                    ois = new ObjectInputStream(
                            new BufferedInputStream(
                            new FileInputStream(
                            cuboid)));
                    shells.add(new CuboidDShell((CuboidD) (ois.readObject())));
                    ois.close();
                    //Move away the files to somewhere else so they won't get converted again.
                    File b = new File("plugins/cuboids2/backups_cuboidD/"+cuboid.getName());
                    cuboid.renameTo(b);
                    
                }
            }
        } catch (IOException e) {
            //CuboidPlugin.log.severe("CuboidPlugin : severe error while loading cuboids");
            EventLogger.getInstance().logMessage("IOException while loading CuboidD files! (File permissions?)", "WARNING");
        } catch(ClassNotFoundException f) {
            EventLogger.getInstance().logMessage("CuboidD class definition could not be found. Implementation failure! Report back to author!", "WARNING");
        }
        return shells;
    }

    @Override
    public String getImplementationVersion() {
        return "CuboidD (by Wire)";
    }

}
