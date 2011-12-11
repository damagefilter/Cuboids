import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.playblack.cuboid.Blockc;
import com.playblack.cuboid.CuboidData;
import com.playblack.vector.Vector;


/**
 * Handles complex block routines such as copy/paste,
 * spheres, balls, walls, discs, circles etc etc<br>
 * This class contains code from CuboidPlugin.<br>
 * As there is no notice about the license of CuboidPlugins code and it's freely available on GitHub,<br>
 * I assume it's public domain.<br>
 * However, many thanks to Relliktsohg for creating all those loops.<br>
 * Code that is taken from CuboidPlugin is marked as that.
 * @author chris
 *
 */
public class BlockOperator {

	private LinkedHashMap<String,CuboidPlayerSelection> playerSelections = new LinkedHashMap<String,CuboidPlayerSelection>();
	
	private Object lock = new Object(); //lock object for block manip. list stuff
	
	/**
	 * Return the radius of the sculpt tool
	 * @param player
	 * @return
	 */
	public int getSculptRadius(Player player) {
		return playerSelections.get(player.getName()).getSculptRadius();
		//return sculptRadius;
	}

	/**
	 * Set the radius of the sculpt tool
	 * @param sculptRadius
	 * @param player
	 */
	public void setSculptRadius(int sculptRadius, Player player) {
		if(playerSelections.get(player.getName()) == null) {
			playerSelections.put(player.getName(), new CuboidPlayerSelection());
		}
		playerSelections.get(player.getName()).setSculptRadius(sculptRadius);
	}

	/**
	 * Returns the block type for the sculpt tool
	 * @param player
	 * @return
	 */
	public int getSculptType(Player player) {
		if(playerSelections.get(player.getName()) == null) {
			playerSelections.put(player.getName(), new CuboidPlayerSelection());
		}
		return playerSelections.get(player.getName()).getSculptType();
	}

	/**
	 * Sets the block type for the sculpt tool
	 * @param sculptType
	 * @param player
	 */
	public void setSculptType(int sculptType, Player player) {
		if(playerSelections.get(player.getName()) == null) {
			playerSelections.put(player.getName(), new CuboidPlayerSelection());
		}
		playerSelections.get(player.getName()).setSculptType(sculptType);
	}

	/**
	 * Gets data/damage values for the sculpt tool type
	 * @param player
	 * @return
	 */
	public int getSculptData(Player player) {
		if(playerSelections.get(player.getName()) == null) {
			playerSelections.put(player.getName(), new CuboidPlayerSelection());
		}
		return playerSelections.get(player.getName()).getSculptData();
		//return sculptData;
	}

	/**
	 * Sets the data/damage values for the sculpt tool type
	 * @param sculptData
	 * @param player
	 */
	public void setSculptData(int sculptData, Player player) {
		if(playerSelections.get(player.getName()) == null) {
			playerSelections.put(player.getName(), new CuboidPlayerSelection());
		}
		playerSelections.get(player.getName()).setSculptData(sculptData);
		//this.sculptData = sculptData;
	}

	/**
	 * Copy the given Content Data into playerSelection list based on player position
	 * @param player
	 * @param data
	 */
	public void relativeCopy(Player player, CuboidData data) {
		Vector origin = new Vector(player.getX(), player.getY(), player.getZ());
		CuboidPlayerSelection sel = new CuboidPlayerSelection();
		sel.setOrigin(origin);
		sel.setContent(data);
		sel.setOriginal(data);
		playerSelections.put(player.getName(), sel);	
	}
	
	/**
	 * Copy the given Content Data into playerSelection list
	 * @param player
	 * @param data
	 */
	public void fullCopy(Player player, CuboidData data) {
		CuboidPlayerSelection sel = new CuboidPlayerSelection();
		sel.setOrigin(data.start);
		sel.setOffset(data.end);
		sel.setContent(data);
		sel.setOriginal(data);
		playerSelections.put(player.getName(), sel);	
	}
	
	
	
	/**
	 * This adjusts the player selections block coordinates to the new player position.<br>
	 * This function is used for basic copy/paste actions.<br>
	 * TODO: Add auto rotate so the pasted blocks will face the same direction as when copied,
	 * relative to player so this function may start to make sense...
	 * 
	 * @param player
	 * @param simulate If true, we're simulating the operation thus not applying changes to the playerSelection
	 * but returning the temporary data for further usage. If in simulating mode, chest contents and sign texts 
	 * will be ignored
	 * 
	 * @return
	 */
	public CuboidData positionMove(Player player, boolean simulate) {
		Vector offset = new Vector(player.getX(), player.getY(), player.getZ());
		playerSelections.get(player.getName()).setOffset(offset);
		
		double x_distance = playerSelections.get(player.getName()).getOffset().getX() - 
							playerSelections.get(player.getName()).getOrigin().getX();
		
		double y_distance = playerSelections.get(player.getName()).getOffset().getY() - 
							playerSelections.get(player.getName()).getOrigin().getY();
		
		double z_distance = playerSelections.get(player.getName()).getOffset().getZ() - 
							playerSelections.get(player.getName()).getOrigin().getZ();
		CuboidData tmp = new CuboidData();
		CuboidData original = playerSelections.get(player.getName()).getOriginal();
		for(Iterator<Vector> data = original.getBlockBag().keySet().iterator(); data.hasNext();) {
			Vector key = (Vector) data.next();
			Vector key_original = new Vector(key.getX(), key.getY(), key.getZ());
			Blockc value = original.getBlockBag().get(key);
			key = new Vector(key.getX()+x_distance, key.getY()+y_distance, key.getZ()+z_distance);
			tmp.setBlockAt(key, value);
			
			//Do the detailed checks on chest content etc only if we're not simulating
			if(!simulate) {
				//Check and save signs and text
				if(value.getType() == 63) {
					Sign sign = (Sign)player.getWorld().getComplexBlock(
							player.getWorld().getBlockAt(
									key_original.getBlockX(), 
									key_original.getBlockY(), 
									key_original.getBlockZ()));
					//4 lines per sign, eh?
					ArrayList<String> lines = new ArrayList<String>(4);
					lines.add(sign.getText(0));
					lines.add(sign.getText(1));
					lines.add(sign.getText(2));
					lines.add(sign.getText(3));
					playerSelections.get(player.getName()).signText.put(key, lines);
				}
				
				//Check for and save chests, doublechests and the contents
				if(value.getType() == 54) {
					//GAWD THIS IS UGLY AS FUCK!
					try {
						Chest chest = (Chest)player.getWorld().getComplexBlock(
								player.getWorld().getBlockAt(
										key_original.getBlockX(), 
										key_original.getBlockY(), 
										key_original.getBlockZ()));
						playerSelections.get(player.getName()).chestContents.put(key, chest.getContents());
					}
					catch(ClassCastException e) {
						//System.out.println("Not a single chest");
						try {
							DoubleChest dchest = (DoubleChest)player.getWorld().getComplexBlock(
									player.getWorld().getBlockAt(
											key_original.getBlockX(), 
											key_original.getBlockY(), 
											key_original.getBlockZ()));
							playerSelections.get(player.getName()).chestContents.put(key, dchest.getContents());
						}
						
						catch(Exception f) {
							System.out.println("Error: "+f.getMessage());
						}
					}
				}
			}
		}
		if(simulate) {
			return tmp;
		}
		else 
		{
			playerSelections.get(player.getName()).setContent(tmp);
			return null;
		}
	}
	
	/**
	 * This moves the selection into a certain direction by x blocks
	 * @param player
	 * @param direction
	 * @param distance
	 * @param simulate
	 * @return
	 */
	public CuboidData offsetMove(Player player, String direction, int distance, boolean simulate) {
		/*
		 * MAPPINGS
		 * SOUTH 	: -z
		 * EAST		: -x
		 * NORTH	: +z
		 * WEST		: +x
		 * UP		: +y
		 * DOWN		: -y
		 */

		//Check directions and make a number of them
		int dir=-1;
		if(direction.equalsIgnoreCase("SOUTH")) { dir=0; }
		else if(direction.equalsIgnoreCase("EAST")) { dir=1; }
		else if(direction.equalsIgnoreCase("NORTH")) { dir=2; }
		else if(direction.equalsIgnoreCase("WEST")) { dir=3; }
		else if(direction.equalsIgnoreCase("UP")) { dir=4; }
		else if(direction.equalsIgnoreCase("DOWN")) { dir=5; }
		CuboidData tmp = new CuboidData();
		CuboidData original = playerSelections.get(player.getName()).getOriginal();
		tmp.start = playerSelections.get(player.getName()).getOrigin();
		tmp.end = playerSelections.get(player.getName()).getOffset();		
		for(Iterator<Vector> data = original.getBlockBag().keySet().iterator(); data.hasNext();) {
			Vector key = (Vector) data.next();
			Vector key_original = new Vector(key.getX(), key.getY(), key.getZ());
			Blockc value = original.getBlockBag().get(key);
			//switch for directions they got remapped further up
			switch(dir) {
			case 0:
				key = new Vector(key.getX(), key.getY(), key.getZ()-distance);
				break;
			case 1:
				key = new Vector(key.getX()-distance, key.getY(), key.getZ());
			case 2:
				key = new Vector(key.getX(), key.getY(), key.getZ()+distance);
				break;
			case 3:
				key = new Vector(key.getX()+distance, key.getY(), key.getZ());
				break;
			case 4:
				key = new Vector(key.getX(), key.getY()+distance, key.getZ());
				break;
			case 5:
				key = new Vector(key.getX(), key.getY()-distance, key.getZ());
				break;
			case -1:
				continue; //this ain't good ... nope...
			}
			//key = new Vector(key.getX()+x_distance, key.getY()+y_distance, key.getZ()+z_distance);
			tmp.setBlockAt(key, value);
			
			//Do the detailed checks on chest content etc only if we're not simulating
			if(!simulate) {
				//Check and save signs and text
				if(value.getType() == 63) {
					Sign sign = (Sign)player.getWorld().getComplexBlock(
							player.getWorld().getBlockAt(
									key_original.getBlockX(), 
									key_original.getBlockY(), 
									key_original.getBlockZ()));
					//4 lines per sign, eh?
					ArrayList<String> lines = new ArrayList<String>(4);
					lines.add(sign.getText(0));
					lines.add(sign.getText(1));
					lines.add(sign.getText(2));
					lines.add(sign.getText(3));
					playerSelections.get(player.getName()).signText.put(key, lines);
				}
				
				//Check for and save chests, doublechests and the contents
				if(value.getType() == 54) {
					try {
						Chest chest = (Chest)player.getWorld().getComplexBlock(
								player.getWorld().getBlockAt(
										key_original.getBlockX(), 
										key_original.getBlockY(), 
										key_original.getBlockZ()));
						playerSelections.get(player.getName()).chestContents.put(key, chest.getContents());
					}
					catch(ClassCastException e) {
						//System.out.println("Not a single chest");
						try {
							DoubleChest dchest = (DoubleChest)player.getWorld().getComplexBlock(
									player.getWorld().getBlockAt(
											key_original.getBlockX(), 
											key_original.getBlockY(), 
											key_original.getBlockZ()));
							playerSelections.get(player.getName()).chestContents.put(key, dchest.getContents());
						}
						
						catch(Exception f) {
							System.out.println("Error: "+f.getMessage());
						}
					}
				}
			}
		}
		//Re-define cuboid bounding rectangle
		switch(dir) {
		case 0:
			tmp.start = new Vector(tmp.start.getX(), tmp.start.getY(), tmp.start.getZ()-distance);
			tmp.end = new Vector(tmp.end.getX(), tmp.end.getY(), tmp.end.getZ()-distance);
			break;
		case 1:
			tmp.start = new Vector(tmp.start.getX()-distance, tmp.start.getY(), tmp.start.getZ());
			tmp.end = new Vector(tmp.end.getX()-distance, tmp.end.getY(), tmp.end.getZ()-distance);
		case 2:
			tmp.start = new Vector(tmp.start.getX(), tmp.start.getY(), tmp.start.getZ()+distance);
			tmp.end = new Vector(tmp.end.getX(), tmp.end.getY(), tmp.end.getZ()+distance);
			break;
		case 3:
			tmp.start = new Vector(tmp.start.getX()+distance, tmp.start.getY(), tmp.start.getZ());
			tmp.end = new Vector(tmp.end.getX()+distance, tmp.end.getY(), tmp.end.getZ()-distance);
			break;
		case 4:
			tmp.start = new Vector(tmp.start.getX(), tmp.start.getY()+distance, tmp.start.getZ());
			tmp.end = new Vector(tmp.end.getX(), tmp.end.getY()+distance, tmp.end.getZ()-distance);
			break;
		case 5:
			tmp.start = new Vector(tmp.start.getX(), tmp.start.getY()-distance, tmp.start.getZ());
			tmp.end = new Vector(tmp.end.getX(), tmp.end.getY()-distance, tmp.end.getZ()-distance);
			break;
		}
		if(simulate) {
			
			return tmp;
		}
		else 
		{
			playerSelections.get(player.getName()).setContent(tmp);
			return null;
		}
	}
	
	/**
	 * Performs paste operation, call an undo before calling this!<br>
	 * undo.setUndo()<br>
	 * paste();
	 * @param player
	 */
	public void paste(Player player) {
		playerSelections.get(player.getName()).setOffset(new Vector(player.getX(), player.getY(), player.getZ()));
		if(playerSelections.get(player.getName()).isComplete()) {
			positionMove(player, false); //run and don't simulate
			CuboidContentInterface.modifyWorldWithChestData(player, playerSelections.get(player.getName()).getContent(), playerSelections.get(player.getName()).chestContents, playerSelections.get(player.getName()).signText);
			//return data;
		}
		else {
			player.sendMessage(Colors.Rose+"pasteFailed");
		}
	}
	
	/**
	 * Performs paste operation, call an undo before calling this!<br>
	 * undo.setUndo()<br>
	 * @param player
	 * @param direction
	 * @param distance
	 */
	public void pasteOffset(Player player, String direction, int distance) {
		//playerSelections.get(player.getName()).setOffset(new Vector(player.getX(), player.getY(), player.getZ()));
		if(playerSelections.get(player.getName()).isComplete()) {
			offsetMove(player, direction, distance, false); //run and don't simulate
			CuboidContentInterface.modifyWorldWithChestData(player, playerSelections.get(player.getName()).getContent(), playerSelections.get(player.getName()).chestContents, playerSelections.get(player.getName()).signText);
			//return data;
		}
		else {
			player.sendMessage(Colors.Rose+"pasteFailed");
		}
	}
	/**
	 * Creates a new CuboidData object that can be applied to the world.<br>
	 * This will create a sphere.<br>
	 * I took the maths from CuboidPlugin because I'm terrible at procedural terrain generation
	 * @param player
	 * @param radius
	 * @param block
	 * @param fill
	 * @param center
	 * @return CuboidData , CuboidData Object that can be applied to the world by CuboidContentInterface 
	 */
	public CuboidData buildSphere(Player player, int radius, Blockc block, boolean fill, Vector center) {
		if(playerSelections.get(player.getName()) == null) {
			playerSelections.put(player.getName(), new CuboidPlayerSelection());
		}
		playerSelections.get(player.getName()).clearContentData();
        int Xmin = center.getBlockX() - radius;
        int Xmax = center.getBlockX() + radius;
        int Ymin = center.getBlockY() - radius;
        int Ymax = center.getBlockY() + radius;
        int Zmin = center.getBlockZ() - radius;
        int Zmax = center.getBlockZ() + radius;
        
        //Code from CuboidPlugin, modded a bit
        synchronized (lock) {
	        for (int x = Xmin; x <= Xmax; x++) {
	            for (int y = Ymin; y <= Ymax; y++) {
	                for (int z = Zmin; z <= Zmax; z++) {
	                	
	                    double diff = Math.sqrt(Math.pow(x - center.getX(), 2.0D) + 
	                    						Math.pow(y - center.getY(), 2.0D) + 
	                    						Math.pow(z - center.getZ(), 2.0D));
	                    if (diff < radius + 0.5 && (fill || (!fill && diff > radius - 0.5))) {
	                    	//Vector v = new Vector(x,y,z);
	                    	playerSelections.get(player.getName()).getContent().setBlockAt(new Vector(x,y,z), block);
	                    }
	                }
	            }
	        }
        }    
        return playerSelections.get(player.getName()).getContent();
	}
	
	/**
	 * Creates a new CuboidData object that can be applied to the world.<br>
	 * This will create a circle.<br>
	 * I took the maths from CuboidPlugin because I'm terrible at procedural terrain generation
	 * @param player
	 * @param radius
	 * @param block
	 * @param height
	 * @param fill
	 * @param center
	 * @return CuboidData , CuboidData Object that can be applied to the world by CuboidContentInterface 
	 */
	public CuboidData buildCircle(Player player, int radius, Blockc block, int height, boolean fill, Vector center) {
		if(playerSelections.get(player.getName()) == null) {
			playerSelections.put(player.getName(), new CuboidPlayerSelection());
		}
		playerSelections.get(player.getName()).clearContentData();

		int Xmin = center.getBlockX() - radius;
        int Xmax = center.getBlockX() + radius;
        int Zmin = center.getBlockZ() - radius;
        int Zmax = center.getBlockZ() + radius;
        int Ymin = (height + center.getBlockY() >= center.getBlockY()) ? center.getBlockY() : height + center.getBlockY();
        int Ymax = (height + center.getBlockY() <= center.getBlockY()) ? center.getBlockY() : height + center.getBlockY();
        synchronized (lock) {
            for (int x = Xmin; x <= Xmax; x++) {
                for (int y = Ymin; y <= Ymax; y++) {
                    for (int z = Zmin; z <= Zmax; z++) {
                        double diff = Math.sqrt(Math.pow(x - center.getBlockX(), 2.0D) + 
                        						Math.pow(z - center.getBlockZ(), 2.0D));
                        
                        if (diff < radius + 0.5 && (fill || (!fill && diff > radius - 0.5))) {
                            //changeBlock(world, new int[]{x, y, z}, blocktype, dmgValue);
                            playerSelections.get(player.getName()).getContent().setBlockAt(new Vector(x,y,z), block);
                            
                        }
                    }
                }
            }
        }
        return playerSelections.get(player.getName()).getContent();
    }
	
	/**
	 * Creates a new CuboidData object that can be applied to the world.<br>
	 * This will create a pyramid.<br>
	 * I took the maths from CuboidPlugin because I'm terrible at procedural terrain generation 
	 * @param player
	 * @param radius
	 * @param block
	 * @param fill
	 * @param center
	 * @return CuboidData , CuboidData Object that can be applied to the world by CuboidContentInterface 
	 */
	public CuboidData buildPyramid(Player player, int radius, Blockc block, boolean fill, Vector center) {
		if(playerSelections.get(player.getName()) == null) {
			playerSelections.put(player.getName(), new CuboidPlayerSelection());
		}
		playerSelections.get(player.getName()).clearContentData();
        int Xcenter = center.getBlockX();
        int Ycenter = center.getBlockY();
        int Zcenter = center.getBlockZ();
        int Xmin = Xcenter - radius;
        int Xmax = Xcenter + radius;
        int Zmin = Zcenter - radius;
        int Zmax = Zcenter + radius;
        int Ymin = Ycenter;
        int Ymax = Ycenter + radius;
        //copyCuboid(selection, Xmin, Xmax, Ymin, Ymax, Zmin, Zmax);
        // xyz = ijk
        synchronized(lock) {
        	for (int y = Ymin; y <= Ymax; y++) {
                for (int x = Xmin; x <= Xmax; x++) {
                    for (int z = Zmin; z <= Zmax; z++) {
                    	playerSelections.get(player.getName()).getContent().setBlockAt(new Vector(x,y,z), block);
                        //changeBlock(world, new int[]{i, j, k}, blockType, dmgValue);
                    }
                }
                Xmin += 1;
                Xmax -= 1;
                Zmin += 1;
                Zmax -= 1;
            }
            //This'll hollow out the pyramid.
            if (!fill && radius > 2) {	// easy, but destructive way
                Xmin = Xcenter - radius + 2;
                Xmax = Xcenter + radius - 2;
                Zmin = Zcenter - radius + 2;
                Zmax = Zcenter + radius - 2;
                Ymin = Ycenter + 1;
                Ymax = Ycenter + radius - 1;
                Blockc air = new Blockc((byte)0,(short)0);
                for (int y = Ymin; y <= Ymax; y++) {
                    for (int x = Xmin; x <= Xmax; x++) {
                        for (int z = Zmin; z <= Zmax; z++) {
                            //world.setBlockAt(0, i, j, k);
                            playerSelections.get(player.getName()).getContent().setBlockAt(new Vector(x,y,z), air);
                        }
                    }
                    Xmin += 1;
                    Xmax -= 1;
                    Zmin += 1;
                    Zmax -= 1;
                }
            }
        }
        return playerSelections.get(player.getName()).getContent();
    }
	
	/**
	 * Creates a new CuboidData object that can be applied to the world.<br>
	 * This will create walls or a cube along the player selection.<br>
	 * I took the maths from CuboidPlugin because I'm terrible at procedural terrain generation 
	 * @param player
	 * @param block - the block this wall/cube shall be made of
	 * @param cube - true if we want a cube
	 */
	public CuboidData buildWalls(Player player, Blockc block, boolean cube) {
        //CuboidSelection selection = getPlayerSelection(playerName);
        CuboidPlayerSelection selection = playerSelections.get(player.getName());

       // World world = selection.world;
        CuboidData tmp = new CuboidData();
        tmp.end = selection.getOffset();
        tmp.start = selection.getOrigin();
        synchronized (lock) {
            for (int x = selection.getOrigin().getBlockX(); x <= selection.getOffset().getBlockX(); x++) {
                for (int y = selection.getOrigin().getBlockY(); y <= selection.getOffset().getBlockY(); y++) {
                	tmp.setBlockAt(new Vector(x,y,selection.getOrigin().getBlockZ()), block);
                	tmp.setBlockAt(new Vector(x,y,selection.getOffset().getBlockZ()), block);
                }
            }
            
            for (int y = selection.getOrigin().getBlockY(); y <= selection.getOffset().getBlockY(); y++) {
                for (int z = selection.getOrigin().getBlockZ(); z <= selection.getOffset().getBlockZ(); z++) {
                	tmp.setBlockAt(new Vector(selection.getOrigin().getBlockX(),y,z), block);
                	tmp.setBlockAt(new Vector(selection.getOffset().getBlockX(),y,z), block);
                }
            }
            
            if (cube) {
                for (int x = selection.getOrigin().getBlockX(); x <= selection.getOffset().getBlockX(); x++) {
                    for (int z = selection.getOrigin().getBlockZ(); z <= selection.getOffset().getBlockZ(); z++) {
                    	tmp.setBlockAt(new Vector(x,selection.getOrigin().getBlockY(),z), block);
                    	tmp.setBlockAt(new Vector(x,selection.getOffset().getBlockY(),z), block);
                    }
                }
            }
        }
        return tmp;
    }

}