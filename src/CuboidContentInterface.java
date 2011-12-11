import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.playblack.ToolBox;
import com.playblack.cuboid.Blockc;
import com.playblack.cuboid.CuboidData;
import com.playblack.vector.Vector;

/**
 * This interfaces Canary Mod with the CuboidContentMatrix methods
 * This interfce handles Block Manipulation<br>
 * This is only here because Canary doesn't utilise packages and we connot
 * import Canary methods in custom packages but it works okay alright.
 * @author Chris
 *
 */
public class CuboidContentInterface {

	/**
	 * This is the content matrix that we can get from selection.
	 * If the content matrix inside a selection wasn't filled with the
	 * actual blocks inside the world region, this can cause undesired results.
	 */
	private CuboidData myContent;
	private ToolBox toolBox = new ToolBox();
	//private static final Logger log = Logger.getLogger("Minecraft");
	private static Object lock = new Object();

	/**
	 * Set a block in the content matrix used by this interface
	 * @param v
	 * @param c
	 */
	public void setBlockAt(Vector v, Blockc c) {
		myContent.setBlockAt(v, c);
	}
	
	/**
	 * Get the current content matrix of this interface
	 * @return
	 */
	public CuboidData getCuboidData() {
		return new CuboidData(myContent);
	}
	
	/**
	 * Merge the block bag of the givebn cuboidData object into this one.<br>
	 * Also expands the selection data
	 * @param data
	 * @param save false to have the result returned instead of beeing saved
	 * @return null or CuboidData
	 */
	public CuboidData mergeCuboidData(CuboidData data, boolean save) {
		if(save) {
			for(Iterator<Vector> vec = data.getBlockBag().keySet().iterator(); vec.hasNext();) {
				Vector key = (Vector) vec.next();
				Blockc block = data.getBlockAt(key);
				myContent.setBlockAt(key, block);
			}

			return null;
		}
		else {
			for(Iterator<Vector> vec = myContent.getBlockBag().keySet().iterator(); vec.hasNext();) {
				Vector key = (Vector) vec.next();
				Blockc block = myContent.getBlockAt(key);
				data.setBlockAt(key, block);
			}

			return data;
		}
	}
	
	/**
	 * Merges the selection bounds of two selections.
	 * @param data
	 * @return
	 */
	public CuboidData mergeSelectionBounds(CuboidData data) {
		CuboidData tmp = new CuboidData();
		System.out.println("my Start: "+myContent.start.toString());
		System.out.println("my End: "+myContent.end.toString());
		System.out.println("data Start: "+data.start.toString());
		System.out.println("data End: "+data.end.toString());
		System.out.println("---------------------------------------------------");
		System.out.println("-----------------------RESULT----------------------");
		System.out.println("---------------------------------------------------");
		tmp.start.setX(data.start.getX()+(data.start.getX() - myContent.start.getX()));
		tmp.start.setY(data.start.getY()+(data.start.getY() - myContent.start.getY()));
		tmp.start.setZ(data.start.getZ()+(data.start.getZ() - myContent.start.getZ()));
		
		tmp.end.setX(data.end.getX()+(data.end.getX() - myContent.end.getX()));
		tmp.end.setY(data.end.getY()+(data.end.getY() - myContent.end.getY()));
		tmp.end.setZ(data.end.getZ()+(data.end.getZ() - myContent.end.getZ()));
		System.out.println("Start: "+tmp.start.toString());
		System.out.println("End: "+tmp.end.toString());
		return tmp;
	}
	/**
	 * Convert content object to json.
	 * This creates a new file which will be streamed to handle larger files and all.
	 * Lets hope this works out.
	 * @return
	 */
	public String getContentMatrixSerialized(String fileNameNumber, String player) {
		 ObjectOutputStream oos;
		 String path = "plugins/cuboids2/undos/"+fileNameNumber+"_"+player+".undo";
		 try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(path))));
			oos.writeObject(myContent);
			oos.close();
			return path;
		} catch (FileNotFoundException e) {
			Cuboids2.eventLog.logMessage("File not Found Exception while serialaizing", "SEVERE");
			e.printStackTrace();
		} catch (IOException e) {
			Cuboids2.eventLog.logMessage("IOException while serialaizing", "SEVERE");
			e.printStackTrace();
		}
		 return null;
	}
	
	/**
	 * Convert a given CuboidData to json and save.
	 * This creates a new file which will be streamed to handle larger files and all.
	 * Lets hope this works out.
	 * @return
	 */
	public String serializeCuboidData(String fileNameNumber, String player, CuboidData data) {
		 ObjectOutputStream oos;
		 String path = "plugins/cuboids2/undos/"+fileNameNumber+"_"+player+".undo";
		 try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File(path))));
			oos.writeObject(data);
			oos.close();
			return path;
		} catch (FileNotFoundException e) {
			Cuboids2.eventLog.logMessage("File not Found Exception while serialaizing", "SEVERE");
			e.printStackTrace();
		} catch (IOException e) {
			Cuboids2.eventLog.logMessage("IOException while serialaizing", "SEVERE");
			e.printStackTrace();
		}
		 return null;
	}
	
	/**
	 * Load an undo object from file and deserialize it.
	 * @param path
	 * @return CuboidData object or null if somethign went wrong
	 */
	public CuboidData deserializeObject(String path) {
		Cuboids2.eventLog.logMessage("Path: "+path, "INFO");
		try {
            ObjectInputStream ois;
            ois = new ObjectInputStream(
                    new BufferedInputStream(
                    new FileInputStream(
                    new File(path))));
            CuboidData cube = new CuboidData( (CuboidData) ois.readObject());
            ois.close();
            return cube;
        }
		catch (Exception e) {
			Cuboids2.eventLog.logMessage("Problemski while recovering undo file :/", "SEVERE");
            return null;
        }
    }
	
	/**
	 * Initialize a CuboidData Area
	 * @param v1
	 * @param v2
	 * @param player
	 */
	public void initCuboidData(Vector v1, Vector v2, Player player) {
		
		int length_x = (int)Vector.getDistance(v1.getX(), v2.getX())+1;
		int length_y = (int)Vector.getDistance(v1.getY(), v2.getY())+1;
		int length_z = (int)Vector.getDistance(v1.getZ(), v2.getZ())+1;

		
		//We use that to calculate the blocks we wnat to put
		Vector min = Vector.getMinimum(v1, v2);
		
		Vector size = new Vector();
		
		size.setX(length_x);
		size.setY(length_y);
		size.setZ(length_z);
		myContent = new CuboidData();
		myContent.setSizeVector(size);
		myContent.start = Vector.getMinimum(v1, v2);
		myContent.end = Vector.getMaximum(v1, v2);
		//For instanciating we need this ugly monster to get the correct block positions But that should be alright
		for(int x = 0; x < length_x; ++x) {
			
			for(int y = 0; y < length_y; ++y) {
				
				for(int z = 0; z < length_z; ++z) {
					Vector current = new Vector(min.getX()+x, min.getY()+y, min.getZ()+z);
					current = toolBox.adjustWorldPosition(current);

					Block b = player.getWorld().getBlockAt((int)current.getX(),(int)current.getY(),(int)current.getZ());
					
					Blockc bc = new Blockc((byte)b.getData(), (short)b.getType());
					myContent.setBlockAt(current, bc);
				}
			}
		}
	}
	
	/**
	 * This creates a CuboidData object with data from the world based on the coordinates of an already
	 * existing CuboidData object, this is used to retrieve the state to undo when pasting something!
	 * @param data
	 * @return
	 */
	public CuboidData getWorldDataForSelection(Player player, CuboidData cData) {
		CuboidData tmp = new CuboidData();
		tmp.start = cData.start;
		tmp.end = cData.end;
		for(Iterator<Vector> data = cData.getBlockBag().keySet().iterator(); data.hasNext();) {
			Vector key = (Vector) data.next();
			Blockc block = new Blockc((byte)player.getWorld().getBlockData(key.getBlockX(), key.getBlockY(), key.getBlockZ()),
										(short)player.getWorld().getBlockIdAt(key.getBlockX(), key.getBlockY(), key.getBlockZ()));
			tmp.setBlockAt(key, block);
		}
		return tmp;
	}
	
	/**
	 * Scapes world data from world based on an empty cuboidData with only start and end poiutn
	 * @param player
	 * @param data
	 * @return
	 */
	public CuboidData scrapeFromWorld(Player player, CuboidData data) {

		int length_x = (int)Vector.getDistance(data.start.getX(), data.end.getX())+1;
		int length_y = (int)Vector.getDistance(data.start.getY(), data.end.getY())+1;
		int length_z = (int)Vector.getDistance(data.start.getZ(), data.end.getZ())+1;
		//We use that to calculate the blocks we wnat to put
		Vector min = Vector.getMinimum(data.start, data.end);
		
		Vector size = new Vector();
		
		size.setX(length_x);
		size.setY(length_y);
		size.setZ(length_z);
		CuboidData tmp = new CuboidData();
		tmp.setSizeVector(size);
		tmp.start = Vector.getMinimum(data.start, data.end);
		tmp.end = Vector.getMaximum(data.start, data.end);
		
		for(int x = 0; x < length_x; ++x) {
			
			for(int y = 0; y < length_y; ++y) {
				
				for(int z = 0; z < length_z; ++z) {
					Vector current = new Vector(min.getX()+x, min.getY()+y, min.getZ()+z);
					current = toolBox.adjustWorldPosition(current);

					Block b = player.getWorld().getBlockAt((int)current.getX(),(int)current.getY(),(int)current.getZ());
					
					Blockc bc = new Blockc((byte)b.getData(), (short)b.getType());
					tmp.setBlockAt(current, bc);
				}
			}
		}
		return tmp;
	}
	

	
	/**
	 * Replace the CuboidData with a different one
	 * @param c
	 */
	public void setCuboidData(CuboidData c) {
		if(c != null) {
			myContent = new CuboidData(c);
			//log.info(myContent.toString());
		}
	}
	
	/**
	 * Preload a chunk.
	 * @param world 
	 * @param coord
	 */
	private static void preloadChunk(World world, Vector coord) {
        if (!world.isChunkLoaded(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ())) {
            world.loadChunk(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ());
        }
    }
	
	/**
	 * Softly change a block in the world.
	 * This also preloads a chunk if it's not loaded.
	 * @param type
	 * @param data
	 * @param coords
	 * @param world
	 */
	private static void changeBlock(int type, int data, Vector coords, World world) {
		preloadChunk(world, coords);
		int worldType = world.getBlockIdAt(coords.getBlockX(), coords.getBlockY(), coords.getBlockZ());
        short worldData = (short) world.getBlockData(coords.getBlockX(), coords.getBlockY(), coords.getBlockZ());
        
        if(worldType == type && worldData == data) {
        	return;
        }
        if(worldType != type) {
        	world.setBlockAt(type, coords.getBlockX(), coords.getBlockY(), coords.getBlockZ());
        	if(worldData != data) {
        		world.setBlockData(coords.getBlockX(), coords.getBlockY(), coords.getBlockZ(), data);
        	}
        }
		return;
	}
	/**
	 * Reflect the changes to the working area to the world
	 * @param player
	 */
	public void modifyWorld(Player player) {
		synchronized (lock) {
			for(Iterator<Vector> data = myContent.getBlockBag().keySet().iterator(); data.hasNext(); ) {
				Vector coord = (Vector) data.next();
					Blockc block = myContent.getBlockBag().get(coord);
					changeBlock(block.getType(), block.getData(),coord,player.getWorld());
			}
		}
	}
	
	/**
	 * This is a DLH for transporting and copying chest contents etc<br>
	 * It reflects changes made in the working area to the world and<br>
	 * sets chest contents and signs afterwards
	 * @param player
	 * @param content
	 * @param chestContents
	 */
	public static void modifyWorldWithChestData(Player player, 
												CuboidData content, 
												LinkedHashMap<Vector,Item[]> chestContents,
												HashMap<Vector, ArrayList<String>> signText) {
		
		synchronized (lock) {
			for(Iterator<Vector> data = content.getBlockBag().keySet().iterator(); data.hasNext();) {
				Vector coord = (Vector) data.next();
				Blockc block = content.getBlockBag().get(coord);
				changeBlock(block.getType(), block.getData(),coord,player.getWorld());
				
				//handle chests and signs
				if(block.getType() == 63) {
					Sign sign = (Sign)player.getWorld().getComplexBlock(
							player.getWorld().getBlockAt(
									coord.getBlockX(), 
									coord.getBlockY(), 
									coord.getBlockZ()));
					//4 lines per sign, eh?
					sign.setText(0, signText.get(coord).get(0));
					sign.setText(1, signText.get(coord).get(1));
					sign.setText(2, signText.get(coord).get(2));
					sign.setText(3, signText.get(coord).get(3));
				}
				
				if(block.getType() == 54 && chestContents.size() != 0) {
					try {
						Chest chest = (Chest)player.getWorld().getComplexBlock(player.getWorld().getBlockAt(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ()));
						try {
							chest.setContents(chestContents.get(coord));
						}
						catch(NullPointerException e) {
							//log.info("Copying single chest failed");
						}
					}
					catch(ClassCastException e) {
						DoubleChest chest = (DoubleChest)player.getWorld().getComplexBlock(player.getWorld().getBlockAt(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ()));
						try {
							chest.setContents(chestContents.get(coord));
						}
						catch(NullPointerException f) {
							//log.info("Copying double chest failed");
						}
					}
				}
			}
		}
	}
	

}
