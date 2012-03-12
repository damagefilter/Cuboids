import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.playblack.EventLogger;
import com.playblack.cuboid.CuboidE;
import com.playblack.mcutils.Vector;


/**
 * Converts CuboidD into CuboidE Trees
 * @author Chris
 *
 */
public class CuboidsConverter {

	private ArrayList<CuboidD> listOfCuboids;
	private EventLogger log;
	
	public CuboidsConverter(EventLogger log) {
		this.log = log;
	}
	public boolean loadCuboids() {
        listOfCuboids = new ArrayList<CuboidD>();
        if (new File("cuboids/areas/").exists() && new File("cuboids/areas/").listFiles().length > 0) {
        	log.logMessage("Cuboids2: Converting CuboidD FS to CuboidE FS.", "INFO");
            return loadCuboidAreasD();
        } 
 
        else {
        	return false;
        }
    }
	
	private boolean loadCuboidAreasD() {
		log.logMessage("Cuboids2: Loading CuboidD FS.", "INFO");
        try {
            ObjectInputStream ois;
            for (File files : new File("cuboids/areas/").listFiles()) {
                if (files.getName().toLowerCase().endsWith(".area")) {
                    ois = new ObjectInputStream(
                            new BufferedInputStream(
                            new FileInputStream(
                            new File("cuboids/areas/" + files.getName()))));
                    listOfCuboids.add((CuboidD) (ois.readObject()));
                    ois.close();
                }
            }
            return true;
        } catch (Exception e) {
            //CuboidPlugin.log.severe("CuboidPlugin : severe error while loading cuboids");
        	log.logMessage("Cuboids2: Something crashed while loading the CuboidD areas. Sry!", "WARNING");
        	return false;
        }
    }
	
	public ArrayList<CuboidE> convertFS() {
		log.logMessage("Cuboids2: Converting Cuboid areas; Puny humans are instructed to wait!", "INFO");
		ArrayList<CuboidE> cubes = new ArrayList<CuboidE>();
		CuboidE tmp;
		for(CuboidD cd : listOfCuboids) {
			tmp = new CuboidE();
			tmp.setAllowPvp(cd.PvP);
			
			tmp.setCreeperSecure(cd.creeper);
			
			tmp.setFarewell(cd.farewellMessage);
				
			tmp.setHealing(cd.heal);
			tmp.setName(cd.name);
			
			Vector p1 = new Vector(cd.coords[0],cd.coords[1],cd.coords[2]);
			Vector p2 = new Vector(cd.coords[3],cd.coords[4],cd.coords[5]);
			tmp.setPoints(p1, p2);
			
			tmp.setProtection(cd.protection);
			
			tmp.setSanctuary(cd.sanctuary);
			
			tmp.setSanctuarySpawnAnimals(cd.sanctuary); //quickFix
			
			tmp.setWelcome(cd.welcomeMessage);

			if(cd.world.equalsIgnoreCase("normal")) {
				tmp.setWorld("NORMAL");
			}
			else {
				tmp.setWorld("NETHER");
			}
			
			tmp.setRestriction(cd.restricted);
			
			/*
			 * players and groups
			 */
//			for(String playerName : cd.allowedPlayers) {
//				//log.logMessage(cd.allowedPlayers.toString(), "INFO");
//				if(playerName.startsWith("g:")) {
//					if(playerName.equalsIgnoreCase("g:")) {
//						playerName = playerName+cd.allowedPlayers.
//					}
//					tmp.addGroup(playerName);
//				}
//				else {
//					playerName = playerName.replace(" ", "");
//					tmp.addPlayer(playerName);
//				}
//			}
			for(int i = 0; i < cd.allowedPlayers.size(); i++) {
				//log.logMessage("Players for "+cd.name, "INFO");
				//log.logMessage(cd.allowedPlayers.toString(), "INFO");
				if(cd.allowedPlayers.get(i).startsWith("g:")) {
					if(cd.allowedPlayers.get(i).equalsIgnoreCase("g:")) {
						//playerName = playerName+cd.allowedPlayers.
						tmp.addGroup(cd.allowedPlayers.get(i)+cd.allowedPlayers.get(i+1));
					}
					else {
						tmp.addGroup(cd.allowedPlayers.get(i));
					}
				}
				else if(cd.allowedPlayers.get(i).startsWith("o:")){
					if(cd.allowedPlayers.get(i).equalsIgnoreCase("o:")) {
						//playerName = playerName+cd.allowedPlayers.
						tmp.addPlayer(cd.allowedPlayers.get(i)+cd.allowedPlayers.get(i+1));
					}
					else {
						tmp.addPlayer(cd.allowedPlayers.get(i));
					}
				}
				else {
					tmp.addPlayer(cd.allowedPlayers.get(i));
				}
			}
			
			/*
			 * restricted commands
			 */
			for(String command : cd.disallowedCommands) {
				tmp.addTabuCommand(command);
			}
			cubes.add(tmp);
			//tmp.setPriority(cd.) //meh -.-
		} //end object
		log.logMessage("Cuboids2: Converting finished without errors.", "INFO");
		return cubes;
	}
	
	/**
	 * Removes old CubvoidD files from FS.
	 */
	public void cleanFS() {
		log.logMessage("Cuboids2: Removing CuboidD areas from disk", "INFO");
		log.logMessage("... hopefully you made a backup.", "INFO");
		
		//System.out.println("Cuboids2:  --- Cleaning up old tree files");
		//log.logMessage("Cuboids2: --- Cleaning up old ", "INFO");
		for(File file : new File("cuboids/areas/").listFiles()) {
			file.delete();
		}
	}
}
