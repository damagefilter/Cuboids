package com.playblack.cuboid;


import java.util.Iterator;
import com.playblack.blocks.BaseBlock;
import com.playblack.blocks.ChestBlock;
import com.playblack.blocks.SignBlock;
import com.playblack.blocks.WorldBlock;
import com.playblack.vector.Vector;

/**
 * This thing takes a CuboidSelection and does crazy things with it,<br>
 * such as filling it, replacing it, making a sphere, walls etc etc
 * @author Chris
 *
 */
public class CuboidBlockOperator {
	
	/**
	 * For undoing stuff
	 */
	private CuboidHistory history;
	
	/**
	 * The lock object for manipulation locking
	 */
	private Object lock = new Object();
	
	public CuboidBlockOperator(int maxUndo) {
		history = new CuboidHistory(maxUndo);
	}
	
	/**
	 * Get the CuboidSelection that is currently stored in the player clipboard
	 * @param player
	 * @return
	 */
	public CuboidSelection getClipboard(String player) {
		return history.getClipboard(player);
	}
	
	/**
	 * Get the last saved CuboidSelection
	 * @param player
	 * @return
	 */
	public CuboidSelection undo(String player) {
		return history.historyUndo(player);
	}

	/**
	 * Remember a set of blocks for undoing stuff.
	 * @param player
	 * @param selection
	 * @param newStep True if you want to make a new step in history, false to add it to the current history step.
	 */
	public void rememberBlocks(String player, CuboidSelection selection, boolean newStep) {
		if(newStep) {
			history.makeHistory(player);
		}
		for(Iterator<Vector> data = selection.getBlockList().keySet().iterator(); data.hasNext();) {
			Vector key = (Vector) data.next();
			//remember the block in history before it was changed
			history.remember(player, key, selection.getBlockAt(key));
		}
	}
	
	/**
	 * This basically returns the content of the clipboard.<br>
	 * That means whatever action had to be done before (like moving) should have been executed by now.
	 * At this point, logically, there should be an undo already.
	 * @param player
	 * @param position
	 */
	public CuboidSelection pasteFromClipboard(String player) {
		//history.makeHistory(player);
		return history.getClipboard(player);
	}
	
	/**
	 * Copy the given CuboidSelection and set the origin to the given vector.<br>
	 * Used for relative position copy
	 * @param player
	 * @param data
	 */
	public void relativeCopy(String player, CuboidSelection data, Vector pos) {
		data.setOrigin(pos);
		//data.sortEdgesOffsetFirst();
		history.copyToClipboard(player, data);
	}
	
	/**
	 * Plainly copy a selection into the player clipboard.
	 * @param player
	 * @param selection
	 */
	public void rawCopy(String player, CuboidSelection selection) {
		history.copyToClipboard(player, selection);
	}
	
	/**
	 * Moves the selection in player clipboard to the new vector, based on the origin point
	 * @param player
	 * @param position
	 * @param simulate true to only simulate things. This will not make history and stuff
	 * @return
	 */
	public CuboidSelection clipboardMoveByVector(String player, Vector position, boolean simulate) {
		if(!simulate) {
			history.makeHistory(player);
		}
		CuboidSelection selection = history.getClipboard(player);
		//selection.setOffset(position);
		double x_distance = position.getX() - 
							selection.getOrigin().getX();

		double y_distance = position.getY() - 
							selection.getOrigin().getY();
		
		double z_distance = position.getZ() - 
							selection.getOrigin().getZ();
		
		CuboidSelection tmp = new CuboidSelection(selection.getOrigin(), selection.getOffset(), selection.getBlockList().size());
		synchronized(lock) {
			for(Iterator<Vector> data = selection.getBlockList().keySet().iterator(); data.hasNext();) {
				Vector key = (Vector) data.next();
				BaseBlock b = selection.getBlockList().get(key);
				//remember the block in history before it was changed
				if(!simulate) {
					history.remember(player, key, b);
				}
				//key = 
				tmp.setBlockAt(new Vector(key.getX()+x_distance, key.getY()+y_distance, key.getZ()+z_distance), b);
			}
		}	
		return tmp;
	}
	
	
	
	public CuboidSelection clipboardMove(String player, boolean simulate) {
		if(!simulate) {
			history.makeHistory(player);
		}
		CuboidSelection selection = history.getClipboard(player);
		double x_distance = selection.getOffset().getX() - 
							selection.getOrigin().getX();

		double y_distance = selection.getOffset().getY() - 
							selection.getOrigin().getY();
		
		double z_distance = selection.getOffset().getZ() - 
							selection.getOrigin().getZ();
		int Xmin = Vector.getMinimum(selection.getOffset(), selection.getOrigin()).getBlockX();
        int Ymin = Vector.getMinimum(selection.getOffset(), selection.getOrigin()).getBlockY();
        int Zmin = Vector.getMinimum(selection.getOffset(), selection.getOrigin()).getBlockZ();
		CuboidSelection tmp = new CuboidSelection(selection.getOrigin(), selection.getOffset(), selection.getBlockList().size());
		synchronized(lock) {
			for(int x = 0; x < x_distance; x++) {
				for(int y = 0; y < y_distance; y++) {
					for(int z = 0; z < z_distance; z++) {
						Vector key = new Vector(Xmin+x, Ymin+y, Zmin+z);
						BaseBlock b = selection.getBlockAt(key);
						if(b != null) {
							if(b instanceof WorldBlock) {
								tmp.setBlockAt(key, new WorldBlock((Byte)b.getData(), (Short)b.getType()));
							}
							else if(b instanceof ChestBlock) {
								tmp.setBlockAt(key, new ChestBlock((ChestBlock)b));
							}
							else if(b instanceof SignBlock) {
								tmp.setBlockAt(key, new SignBlock(((SignBlock) b).getSignTextArray()));
							}
						}
					}
				}
			}
			
//			for(Iterator<Vector> data = selection.getBlockList().keySet().iterator(); data.hasNext();) {
//				Vector key = (Vector) data.next();
//				BaseBlock b = selection.getBlockList().get(key);
//				//remember the block in history before it was changed
//				if(!simulate) {
//					history.remember(player, key, b);
//				}
//				/*
//				 * curX = selection.pastePoint[0] + i;
//                        curY = selection.pastePoint[1] + j;
//                        curZ = selection.pastePoint[2] + k;
//				 */
//				//key = new Vector(key.getX()+x_distance, key.getY()+y_distance, key.getZ()+z_distance);
//				key = new Vector(Xmin+key.getX(), key.getY()+y_distance, key.getZ()+z_distance);
//				
//				if(b instanceof WorldBlock) {
//					tmp.setBlockAt(key, new WorldBlock((Byte)b.getData(), (Short)b.getType()));
//				}
//				else if(b instanceof ChestBlock) {
//					tmp.setBlockAt(key, new ChestBlock((ChestBlock)b));
//				}
//				else if(b instanceof SignBlock) {
//					tmp.setBlockAt(key, new SignBlock(((SignBlock) b).getSignTextArray()));
//				}
//			}
//		}	
			if(!simulate) {
				selection.setBlockList(tmp.getBlockList());
				return selection;
			}
			else {
				return tmp; //may be used to retrieve more blocks for undoing things
			}
		}
	}
	
	public CuboidSelection moveByOffset(String player, String direction, int distance, CuboidSelection selection, boolean simulate) {
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
		CuboidSelection tmp = new CuboidSelection(selection.getOrigin(), selection.getOffset(), selection.getBlockList().size());
		
		if(!simulate) {
			//make a new step if we're not simulating for new block positions!
			history.makeHistory(player);
		}
		for(Iterator<Vector> data = selection.getBlockList().keySet().iterator(); data.hasNext();) {
			Vector key = (Vector) data.next();
			
			BaseBlock b = selection.getBlockList().get(key);
			if(!simulate) {
				history.remember(player, key, b);
			}
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
			tmp.setBlockAt(key, b);
		}
		//Re-define cuboid bounding rectangle
		switch(dir) {
		case 0:
			tmp.setOrigin(new Vector(tmp.getOrigin().getX(), tmp.getOrigin().getY(), tmp.getOrigin().getZ()-distance));
			tmp.setOffset(new Vector(tmp.getOffset().getX(), tmp.getOffset().getY(), tmp.getOffset().getZ()-distance));
			break;
		case 1:
			tmp.setOrigin(new Vector(tmp.getOrigin().getX()-distance, tmp.getOrigin().getY(), tmp.getOrigin().getZ()));
			tmp.setOffset(new Vector(tmp.getOffset().getX()-distance, tmp.getOffset().getY(), tmp.getOffset().getZ()));
		case 2:
			tmp.setOrigin(new Vector(tmp.getOrigin().getX(), tmp.getOrigin().getY(), tmp.getOrigin().getZ()+distance));
			tmp.setOffset(new Vector(tmp.getOffset().getX(), tmp.getOffset().getY(), tmp.getOffset().getZ()+distance));
			break;
		case 3:
			tmp.setOrigin(new Vector(tmp.getOrigin().getX()+distance, tmp.getOrigin().getY(), tmp.getOrigin().getZ()));
			tmp.setOffset(new Vector(tmp.getOffset().getX()+distance, tmp.getOffset().getY(), tmp.getOffset().getZ()));
			break;
		case 4:
			tmp.setOrigin(new Vector(tmp.getOrigin().getX(), tmp.getOrigin().getY()+distance, tmp.getOrigin().getZ()));
			tmp.setOffset(new Vector(tmp.getOffset().getX(), tmp.getOffset().getY()+distance, tmp.getOffset().getZ()));
			break;
		case 5:
			tmp.setOrigin(new Vector(tmp.getOrigin().getX(), tmp.getOrigin().getY()-distance, tmp.getOrigin().getZ()));
			tmp.setOffset(new Vector(tmp.getOffset().getX(), tmp.getOffset().getY()-distance, tmp.getOffset().getZ()));
			break;
		}
		if(simulate) {
			
			return tmp;
		}
		else 
		{
			selection = tmp; //We use this to redo
			return selection;
		}
	}
	
	/**
	 * Replace target with sub blocks in the given CuboidSelections block list
	 * @param player
	 * @param target
	 * @param sub
	 * @param selection
	 * @return modified CuboidSelection
	 */
	public CuboidSelection replace(String player, CuboidSelection selection, BaseBlock target, BaseBlock sub, boolean simulate) {
		BaseBlock block;
		if(!simulate) {
			history.makeHistory(player);
		}
		for(Iterator<Vector> data = selection.getBlockList().keySet().iterator(); data.hasNext();) {
			Vector key = (Vector) data.next();
			block = selection.getBlockAt(key);
			if(block.equalsSlack(target)) {
				if(!simulate) {
					history.remember(player, key, block);
				}
				selection.setBlockAt(key, sub);
			}
		}
		return selection;
	}
	
	/**
	 * Fill the given selection with the given Block b
	 * @param b
	 */
	public CuboidSelection fill(String player, CuboidSelection selection, BaseBlock b, boolean simulate) {
		if(!simulate) {
			history.makeHistory(player);
		}
		CuboidSelection tmp = new CuboidSelection();
		for(Iterator<Vector> data = selection.getBlockList().keySet().iterator(); data.hasNext();) {
			Vector key = (Vector) data.next();
			if(!simulate) {
				history.remember(player, key, selection.getBlockAt(key));
			}
			tmp.setBlockAt(key, b);
		}
		return tmp;
	}
	/**
	 * Creates a new CuboidSelection object containing dummy data!<br>
	 * To change block type etc, call a fill on this selection!<br>
	 * This will create a sphere.<br>
	 * I took the maths from CuboidPlugin because I'm terrible at procedural terrain generation
	 * @param player
	 * @param radius
	 * @param center
	 * @param fill
	 * @return
	 */
	public CuboidSelection buildSphere(String player, int radius, Vector center, boolean fill) {

		CuboidSelection selection = new CuboidSelection();
		BaseBlock tmpBlock = new WorldBlock((byte)0,(short)1);
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
	                    	/*
	                    	 * NOTICE: This is only a soimulator method to create the hull for a complex object.
	                    	 * This does not affect world or history!
	                    	 */
	                    		selection.setBlockAt(new Vector(x,y,z), tmpBlock);
	                    }
	                }
	            }
	        }
        }    
        return selection;
	}
	
	/**
	 * Creates a new CuboidSelection object containing dummy data!<br>
	 * To change block type etc, call a fill on this selection!<br>
	 * This will create a circle or disk!<br>
	 * I took the maths from CuboidPlugin because I'm terrible at procedural terrain generation
	 * @param player
	 * @param radius
	 * @param center
	 * @param fill
	 * @return
	 */
	public CuboidSelection buildCircle(String player, int radius, int height, Vector center, boolean fill) {
		CuboidSelection selection = new CuboidSelection();
		BaseBlock block = new WorldBlock((byte)0, (short)1);
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
                        /*
                    	 * NOTICE: This is only a soimulator method to create the hull for a complex object.
                    	 * This does not affect world or history!
                    	 */
                        if (diff < radius + 0.5 && (fill || (!fill && diff > radius - 0.5))) {
                            selection.setBlockAt(new Vector(x,y,z), block);
                            
                        }
                    }
                }
            }
        }
        return selection;
    }
	
	/**
	 * Creates a new CuboidSelection object containing dummy data!<br>
	 * To change block type etc, call a !!!replace!!! on this selection!<br>
	 * This will create a pyramid!<br>
	 * I took the maths from CuboidPlugin because I'm terrible at procedural terrain generation
	 * @param player
	 * @param radius
	 * @param center
	 * @param fill
	 * @return
	 */
	public CuboidSelection buildPyramid(String player, int radius, Vector center, boolean fill) {
		CuboidSelection selection = new CuboidSelection();
		BaseBlock block = new WorldBlock((byte)0, (short)1);
        int Xcenter = center.getBlockX();
        int Ycenter = center.getBlockY();
        int Zcenter = center.getBlockZ();
        int Xmin = Xcenter - radius;
        int Xmax = Xcenter + radius;
        int Zmin = Zcenter - radius;
        int Zmax = Zcenter + radius;
        int Ymin = Ycenter;
        int Ymax = Ycenter + radius;
        synchronized(lock) {
        	for (int y = Ymin; y <= Ymax; y++) {
                for (int x = Xmin; x <= Xmax; x++) {
                    for (int z = Zmin; z <= Zmax; z++) {
                    	selection.setBlockAt(new Vector(x,y,z), block);
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
                BaseBlock air = new WorldBlock((byte)0,(short)0);
                for (int y = Ymin; y <= Ymax; y++) {
                    for (int x = Xmin; x <= Xmax; x++) {
                        for (int z = Zmin; z <= Zmax; z++) {
                            //world.setBlockAt(0, i, j, k);
                            selection.setBlockAt(new Vector(x,y,z), air);
                        }
                    }
                    Xmin += 1;
                    Xmax -= 1;
                    Zmin += 1;
                    Zmax -= 1;
                }
            }
        }
        return selection;
    }
	
	/**
	 * Creates a new CuboidSelection object containing dummy data!<br>
	 * To change block type etc, call a !!!replace!!! on this selection!<br>
	 * This will create walls/faces!<br>
	 * I took the maths from CuboidPlugin because I'm terrible at procedural terrain generation
	 * @param player
	 * @param selection
	 * @param cube
	 * @return
	 */
	public CuboidSelection buildWalls(String player, CuboidSelection selection, boolean cube) {

		BaseBlock block = new WorldBlock((byte)0, (short)1);
		selection.sortEdgesOffsetFirst();
		CuboidSelection tmp = new CuboidSelection(selection.getOrigin(), selection.getOffset());
        synchronized (lock) {
            for (int x = tmp.getOrigin().getBlockX(); x <= tmp.getOffset().getBlockX(); x++) {
                for (int y = tmp.getOrigin().getBlockY(); y <= tmp.getOffset().getBlockY(); y++) {

                	tmp.setBlockAt(new Vector(x,y,tmp.getOrigin().getBlockZ()), block);
                	tmp.setBlockAt(new Vector(x,y,tmp.getOffset().getBlockZ()), block);
                }
            }
            
            for (int y = tmp.getOrigin().getBlockY(); y <= tmp.getOffset().getBlockY(); y++) {
                for (int z = tmp.getOrigin().getBlockZ(); z <= tmp.getOffset().getBlockZ(); z++) {

                	tmp.setBlockAt(new Vector(tmp.getOrigin().getBlockX(),y,z), block);
                	tmp.setBlockAt(new Vector(tmp.getOffset().getBlockX(),y,z), block);
                }
            }
            
            if (cube) {
                for (int x = tmp.getOrigin().getBlockX(); x <= tmp.getOffset().getBlockX(); x++) {
                    for (int z = tmp.getOrigin().getBlockZ(); z <= tmp.getOffset().getBlockZ(); z++) {

                    	tmp.setBlockAt(new Vector(x,tmp.getOrigin().getBlockY(),z), block);
                    	tmp.setBlockAt(new Vector(x,tmp.getOffset().getBlockY(),z), block);
                    }
                }
            }
        }
        return tmp;
    }
}
