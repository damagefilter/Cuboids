import java.util.ArrayList;
import java.util.Iterator;

import com.playblack.ToolBox;
import com.playblack.blocks.BaseBlock;
import com.playblack.blocks.BaseItem;
import com.playblack.blocks.ChestBlock;
import com.playblack.blocks.SignBlock;
import com.playblack.blocks.WorldBlock;
import com.playblack.cuboid.CuboidSelection;
import com.playblack.vector.Vector;


public class WorldObserver {
	private static Object lock = new Object();
	private static ToolBox toolBox = new ToolBox();
	
	/**
	 * Collect blocks from the world and convert them to Cuboids2 block format
	 * @param player
	 * @param selection
	 * @return
	 */
	public CuboidSelection getBlocksFromWorld(Player player, CuboidSelection selection) {
		CuboidSelection tmp = new CuboidSelection(selection.getOrigin(), selection.getOffset());
		if(selection.getBlockList().isEmpty()) {
			int length_x = (int)Vector.getDistance(selection.getOrigin().getX(), selection.getOffset().getX())+1;
			int length_y = (int)Vector.getDistance(selection.getOrigin().getY(), selection.getOffset().getY())+1;
			int length_z = (int)Vector.getDistance(selection.getOrigin().getZ(), selection.getOffset().getZ())+1;
			//We use that to calculate the blocks we wnat to put
			Vector min = Vector.getMinimum(selection.getOrigin(), selection.getOffset());
			
			Vector size = new Vector();
			
			size.setX(length_x);
			size.setY(length_y);
			size.setZ(length_z);
			synchronized(lock) {
				for(int x = 0; x < length_x; ++x) {		
					for(int y = 0; y < length_y; ++y) {		
						for(int z = 0; z < length_z; ++z) {
							Vector current = new Vector(min.getX()+x, min.getY()+y, min.getZ()+z);
							current = toolBox.adjustWorldPosition(current);
							Block b = player.getWorld().getBlockAt((int)current.getX(),(int)current.getY(),(int)current.getZ());
							//Are we a chest or double chest?
							if(b.getType() == 54) {
								//Replace cast exception with this: getOnlyComplexBlock   if (chest.findAttachedChest != null)
								try {
									Chest chest = (Chest)player.getWorld().getComplexBlock(b);
									if(chest != null) {
										ChestBlock bc = new ChestBlock();
										bc.putItemList(itemsToArrayList(chest.getContents()));
										bc.setData((byte)chest.getBlock().getData());
										tmp.setBlockAt(current, bc);
									}
								}
								catch(ClassCastException e) {
									DoubleChest chest = (DoubleChest)player.getWorld().getComplexBlock(b);
									if(chest != null) {
										ChestBlock bc = new ChestBlock();
										bc.putItemList(itemsToArrayList(chest.getContents()));
										bc.setData((byte)chest.getBlock().getData());
										tmp.setBlockAt(current, bc);
									}
								}
							}
							//Are we a sign?
							else if(b.getType() == 63) {
									Sign sign = (Sign)player.getWorld().getComplexBlock(b);
									//4 lines per sign, eh?
									String[] text = new String[4];
									text[0] = sign.getText(0);
									text[1] = sign.getText(1);
									text[2] = sign.getText(2);
									text[3] = sign.getText(3);
									SignBlock bc = new SignBlock(text);
									bc.setData((byte)sign.getBlock().getData());
									tmp.setBlockAt(current, bc);
									
							}
							else {
								WorldBlock bc = new WorldBlock((byte)b.getData(), (short)b.getType());
								tmp.setBlockAt(current, bc);
							}
						}
					}
				}
			}
		}
		
		else {
			synchronized(lock) {
				for(Iterator<Vector> data = selection.getBlockList().keySet().iterator(); data.hasNext();) {
					Vector key = (Vector) data.next();
					
					//Are we a chest or double chest?
					Block b = player.getWorld().getBlockAt(key.getBlockX(), key.getBlockY(), key.getBlockZ());
					if(b.getType() == 54) {
						try {
							Chest chest = (Chest)player.getWorld().getComplexBlock(b);
							if(chest != null) {
								ChestBlock bc = new ChestBlock();
								bc.putItemList(itemsToArrayList(chest.getContents()));
								bc.setData((byte)chest.getBlock().getData());
								tmp.setBlockAt(key, bc);
							}
						}
						catch(ClassCastException e) {
							DoubleChest chest = (DoubleChest)player.getWorld().getComplexBlock(b);
							if(chest != null) {
								ChestBlock bc = new ChestBlock();
								bc.putItemList(itemsToArrayList(chest.getContents()));
								bc.setData((byte)chest.getBlock().getData());
								tmp.setBlockAt(key, bc);
							}
						}
					}
					//Are we a sign?
					else if(b.getType() == 63) {
							Sign sign = (Sign)player.getWorld().getComplexBlock(b);
							//4 lines per sign, eh?
							String[] text = new String[4];
							text[0] = sign.getText(0);
							text[1] = sign.getText(1);
							text[2] = sign.getText(2);
							text[3] = sign.getText(3);
							SignBlock block = new SignBlock(text);
							block.setData((byte)sign.getBlock().getData());
							tmp.setBlockAt(key, block);
							
					}
					else {
						WorldBlock block = new WorldBlock((byte)b.getData(), (short)b.getType());
						tmp.setBlockAt(key, block);
					}
				}
			}
		}
		return tmp;
	}
	 
	
	/**
	 * Convert Canary Item list to Cuboids2 Item list
	 */
	private ArrayList<BaseItem> itemsToArrayList(Item[] items) {
		if(items == null || items.length == 0) {
			return new ArrayList<BaseItem>(0);
		}
		
		ArrayList<BaseItem> newItems = new ArrayList<BaseItem>(items.length);
//		for(int i = 0; i < items.length; i++) {
//			Logger.getLogger("Minecraft").info("Item"+items[i].toString());
//		}
	//	Logger.getLogger("Minecraft").info("item amount: "+items.length);
		for(Item i: items) {
			if(i != null) {
				newItems.add(new BaseItem(i.getItemId(), i.getDamage(), i.getAmount(), i.getSlot()));
			}	
		}
		return newItems;
	}
	
	/**
	 * Turn Cuboids2 item list to Canary Item List
	 * @param items
	 * @return
	 */
	private Item[] itemsToArray(ArrayList<BaseItem> items) {
		if(items.isEmpty() || items.size() == 0) {
			return new Item[1];
		}
		Item[] nItem = new Item[27];
		//ArrayList<Item> newItems = new ArrayList<Item>();
//		for(BaseItem i : items) {	
//			Item it = new Item();
//			it.setItemId(i.getItemId());
//			it.setDamage(i.getItemData());
//			it.setAmount(i.getAmount());
//			it.setSlot(i.getSlot());
//			newItems.add(it);
//		}
		for(int i = 0; i < nItem.length; i++) {
			Item it = new Item();

			if(items.size() > i) {
				it.setItemId(items.get(i).getItemId());
				it.setDamage(items.get(i).getItemData());
				it.setAmount(items.get(i).getAmount());
				it.setSlot(items.get(i).getSlot());
				nItem[i] = it;
			}

		}
//		Logger.getLogger("Minecraft").info("Items in clipboard: "+items.toString());
		//Logger.getLogger("Minecraft").info("Items in new: "+newItems.toString());
		//return (Item[]) newItems.toArray();
		return nItem;
	}
	
	
	/**
	 * Softly change a block in the world.<br>
	 * This recognizes chests etc
	 * This also preloads a chunk if it's not loaded.
	 * @param type
	 * @param data
	 * @param coords
	 * @param world
	 */
	private void changeBlock(BaseBlock block, Vector coords, World world) {
		preloadChunk(world, coords);
		Block b = world.getBlockAt(coords.getBlockX(), coords.getBlockY(), coords.getBlockZ());

        if(b.getType() == (Short)block.getType() && b.getData() == (Byte)block.getData()) {
        	return;
        }
        if(b.getType() != (Short)block.getType()) {
        	world.setBlockAt((Short)block.getType(), coords.getBlockX(), coords.getBlockY(), coords.getBlockZ());
        	if(b.getData() != (Byte)block.getData()) {
        		world.setBlockData(coords.getBlockX(), coords.getBlockY(), coords.getBlockZ(), (Byte)block.getData());
        	}
        }
        Block test = world.getBlockAt(coords.getBlockX(), coords.getBlockY(), coords.getBlockZ()); //after setting zee chest
        if((Short)block.getType()  == 54 && block instanceof ChestBlock) {
        	//Logger.getLogger("Minecraft").info("block Type is chest");
    		ChestBlock c = (ChestBlock)block;
    		//Logger.getLogger("Minecraft").info("Chest Data: "+c.getData());
			try {
				Chest chest = (Chest)world.getComplexBlock(test);
				if(chest != null) {
					//Logger.getLogger("Minecraft").info("Class: "+chest.getClass());
					try {
						chest.setContents(itemsToArray(c.getItemList()));
						chest.getBlock().setData(c.getData());
						chest.update();
					}
					catch(ArrayIndexOutOfBoundsException f) {
						
					}
				}
			}
			catch(ClassCastException e) {
				//Logger.getLogger("Minecraft").info(e.getMessage());
				try {
					DoubleChest dchest = (DoubleChest)world.getComplexBlock(test);
					if(dchest != null) {
						//Logger.getLogger("Minecraft").info("Class: "+dchest.getClass());
						try {
							dchest.setContents(itemsToArray(c.getItemList()));
							dchest.getBlock().setData(c.getData());
							dchest.update();
						}
						catch(ArrayIndexOutOfBoundsException f) {
							
						}
					}
				}
				catch(ClassCastException f) {
					//Logger.getLogger("Minecraft").info(f.getMessage());
				}
			}
        }
        if((Short)block.getType()  == 63 && block instanceof SignBlock) {
        	//Logger.getLogger("Minecraft").info("block Type is Sign");
        	SignBlock c = (SignBlock)block;
        	try {
        		Sign sign = (Sign)world.getComplexBlock(test);  	
            	sign.setText(0, c.getTextOnLine(0));
            	sign.setText(1, c.getTextOnLine(1));
            	sign.setText(2, c.getTextOnLine(2));
            	sign.setText(3, c.getTextOnLine(3));
            	//Logger.getLogger("Minecraft").info("Sign Data: "+c.getData());
            	sign.getBlock().setData(c.getData());
            	sign.update();
        	}
        	catch(ClassCastException e) {
        		
        	}
        }
		return;
	}
	
	/**
	 * Preload a chunk.
	 * @param world 
	 * @param coord
	 */
	private void preloadChunk(World world, Vector coord) {
        if (!world.isChunkLoaded(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ())) {
            world.loadChunk(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ());
        }
    }
	
	/**
	 * Apply a set of changes to the world
	 * @param player
	 * @param selection
	 */
	public boolean modifyWorld(Player player, CuboidSelection selection) {
		if(selection == null) {
			//player.sendMessage(Colors.Red+"Cannot modify world, the selection was invalid!");
			return false;
		}
		synchronized (lock) {
			for(Iterator<Vector> data = selection.getBlockList().keySet().iterator(); data.hasNext(); ) {
				Vector coord = (Vector) data.next();
					changeBlock(selection.getBlockList().get(coord),coord,player.getWorld());
			}
			return true;
		}
	}
}