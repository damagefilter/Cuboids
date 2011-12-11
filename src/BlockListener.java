import com.playblack.ToolBox;
import com.playblack.cuboid.Blockc;
import com.playblack.cuboid.CuboidData;
import com.playblack.cuboid.CuboidSelection;
import com.playblack.vector.Vector;

/**
 * This will listen for events that are happening to blocks of all sorts
 * @author chris
 *
 */
public class BlockListener extends PluginListener{
	private ToolBox toolBox = new ToolBox();
	private long theTime = 0L;
	/*
	 * *********************************************************************************************
	 * BLOCKS RIGHT CLICK HANDLING
	 * *********************************************************************************************
	 */
	@Override
	public boolean onBlockRightClick(Player player, Block block, Item item) {	
		//player.sendMessage("Block ID: "+block.getType());
		//player.sendMessage("Block Data: "+block.getData());
		
		if(item.getItemId() == Cuboids2.cfg.getRegionItem()) {
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cselect")) {
					return true;
				}
			}
			/*
			 * *****************************************************
			 * If we use double action tools this will ONLY set the offset value!
			 * *****************************************************
			 */
			
			//Create new entry if empty
			if(Cuboids2.sel.get(player.getName()) == null) {
				Cuboids2.sel.put(player.getName(), new CuboidSelection());
			}
			if (Cuboids2.cfg.useDoubleAction()) {
		        if (Cuboids2.sel.get(player.getName()).getWorld().equalsIgnoreCase("no_world")) {
		          Cuboids2.sel.get(player.getName()).setWorld(player.getWorld().getType().name());
		        }

		        Vector blockPosition = new Vector(block.getX(), block.getY(), block.getZ());

		        Cuboids2.sel.get(player.getName()).setWorld(player.getWorld().getType().name());
		        Cuboids2.sel.get(player.getName()).setOffset(this.toolBox.adjustWorldBlock(blockPosition));

		        player.sendMessage(Colors.LightGray+Cuboids2.msg.messages.get("secondPointSet"));
		        return true;
		    }
			
			/*
			 * *****************************************************
			 * If we're not using double-action tools we swtich between first and second point and eventually reset the selection
			 * *****************************************************
			 */
			else {
				if(Cuboids2.sel.get(player.getName()).getWorld().equalsIgnoreCase("no_world")) {
					Cuboids2.sel.get(player.getName()).setWorld(player.getWorld().getType().name());
				}
				
				Vector blockPosition = new Vector(block.getX(), block.getY(), block.getZ());
				Vector v = toolBox.adjustWorldBlock(blockPosition);
				//log.info("Point: "+v.toString());
				
				if(!Cuboids2.sel.get(player.getName()).isComplete()) {
					
					if(!Cuboids2.sel.get(player.getName()).originSet()) {
						//player.sendMessage(Colors.Blue+"Setting origin");
						Cuboids2.sel.get(player.getName()).setOrigin(v);
						player.sendMessage(Colors.LightGray+Cuboids2.msg.messages.get("firstPointSet"));
						return true;
					}
					else if(!Cuboids2.sel.get(player.getName()).offsetSet()) {
						Cuboids2.sel.get(player.getName()).setOffset(v);
						player.sendMessage(Colors.LightGray+Cuboids2.msg.messages.get("secondPointSet"));
						return true;
					}
				}
				
				if(Cuboids2.sel.get(player.getName()).isComplete()) {
					Cuboids2.sel.get(player.getName()).clear();
					Cuboids2.sel.get(player.getName()).setWorld(player.getWorld().getType().name());
					Cuboids2.sel.get(player.getName()).setOrigin(v);
					//This means a new area so we need to re-initialize the content matrix
				//	Cuboids2.content.setInitStatus(false);
					player.sendMessage(Colors.LightGray+Cuboids2.msg.messages.get("firstPointSet"));
					return true;
				}
			}
		}
		
		/*
		 * *****************************************************
		 * INSPECTOR TOOL AND OPERATIONS
		 * *****************************************************
		 */
		
		if(item.getItemId() == Cuboids2.cfg.getInspectorItem()) {
			
			Vector blockPosition = new Vector(block.getX(), block.getY(), block.getZ());
			blockPosition = toolBox.adjustWorldBlock(blockPosition);
			Cuboids2.cuboids.explainCuboid(player, blockPosition);
			return true;
		}
		
		/*
		 * *****************************************************
		 * OPERABLE ITEMS HANDLING
		 * *****************************************************
		 */
		//v = toolBox.adjustWorldPosition(v);
		if(Cuboids2.cuboids.isInCuboid(player.getWorld().getType().name(), new Vector(player.getX(), player.getY(), player.getZ()))) {
			if(!Cuboids2.cfg.isInOperables(block.getType()) || player.canUseCommand("/cIgnoreRestrictions")) {
				//allow
				return false;
			}
			else if(Cuboids2.cfg.isInOperables(block.getType()) && Cuboids2.cuboids.canModifyBlock(player, block)) {
				return false; //allow
			}
			else {
				//deny
				return true;
			}
		}
		return false;
	}

	@Override
	public void onArmSwing(Player player)
	{
		/*
		 * *****************************************************
		 * HITBLOX ETC
		 * *****************************************************
		 */
		if(System.currentTimeMillis() <= theTime+200) {
			//break the operation if not enough time has passed.
			//this is applied to prevent onArmSwing from beeing called uncontrollably
			return;
		}
		if(Cuboids2.sel.get(player.getName()) == null) {
			Cuboids2.sel.put(player.getName(), new CuboidSelection());
		}
		
		if(player.getItemInHand() == Cuboids2.cfg.getRemoteSelectionItem()) {
			if(player.canUseCommand("/cIgnoreRestrictions") || player.canUseCommand("/cselect")) {
				HitBlox hb = new HitBlox(player);
				Block b = hb.getFaceBlock();
				boolean offsetSet = false;
				if(b != null) {
					
					if(Cuboids2.sel.get(player.getName()).getWorld().equalsIgnoreCase("no_world")) {
						Cuboids2.sel.get(player.getName()).setWorld(player.getWorld().getType().name());
					}
					
					Vector blockPosition = new Vector(b.getX(), b.getY(), b.getZ());
					Vector v = toolBox.adjustWorldBlock(blockPosition);
					//log.info("Point: "+v.toString());
					
					if(Cuboids2.sel.get(player.getName()).isComplete() == false) {
						
						if(!Cuboids2.sel.get(player.getName()).originSet()) {
							//player.sendMessage(Colors.Blue+"Setting origin");
							Cuboids2.sel.get(player.getName()).setOrigin(v);
							player.sendMessage(Colors.LightGray+Cuboids2.msg.messages.get("firstPointSet"));
						}
						else if(!Cuboids2.sel.get(player.getName()).offsetSet()) {
							Cuboids2.sel.get(player.getName()).setOffset(v);
							player.sendMessage(Colors.LightGray+Cuboids2.msg.messages.get("secondPointSet"));
							offsetSet = true;
						}
					}
					
					else if(Cuboids2.sel.get(player.getName()).isComplete() && offsetSet == false) {
						Cuboids2.sel.get(player.getName()).clear();
						Cuboids2.sel.get(player.getName()).setWorld(player.getWorld().getType().name());
						Cuboids2.sel.get(player.getName()).setOrigin(v);
						//This means a new area so we need to re-initialize the content matrix
						//Cuboids2.content.setInitStatus(false);
						player.sendMessage(Colors.LightGray+Cuboids2.msg.messages.get("firstPointSet"));
					}
				}
			}
		}
		
		if(player.getItemInHand() == Cuboids2.cfg.getSculptItem()) {
			if(player.canUseCommand("/cIgnoreRestrictions") || player.canUseCommand("/cWorldMod")) {
				if(player.canUseCommand("/cbrush") || player.canUseCommand("/cIgnoreRestrictions")) {
					HitBlox hb = new HitBlox(player);
					Block b = hb.getFaceBlock();
					if(b != null && Cuboids2.cuboids.canModifyBlock(player, b)) {
						
						Vector v = new Vector(b.getX(), b.getY(),b.getZ());
						v = toolBox.adjustWorldBlock(v);
						Blockc block = new Blockc((byte)Cuboids2.blockOp.getSculptData(player), (short)Cuboids2.blockOp.getSculptType(player));
						CuboidData sphere = Cuboids2.blockOp.buildSphere(player, Cuboids2.blockOp.getSculptRadius(player), block, true, v);
						//log.info("Area Volume: "+volume);
						if(!Cuboids2.noBackup && Cuboids2.cfg.allowUndo()) {
							Cuboids2.content.setCuboidData(
									Cuboids2.content.getWorldDataForSelection(player, sphere));
							Cuboids2.undoList.add(Cuboids2.content.getContentMatrixSerialized(
									Integer.toString(Cuboids2.undoList.getUndoList(player.getName()).size()), player.getName()), player.getName());
							
						}
						Cuboids2.content.setCuboidData(sphere);
						Cuboids2.content.modifyWorld(player);
						//player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("sphereCreated"));
						
						
					}
					
					
				}
			}
		}
		theTime = System.currentTimeMillis();
	}
	
	/**
	 * *********************************************************************************************
	 * BLOCKS LEFT CLICK HANDLING
	 * *********************************************************************************************
	 */
	@Override
	public boolean onBlockDestroy(Player player, Block block) {
		if(player.getItemInHand() == Cuboids2.cfg.getRegionItem()) {
			
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cselect")) {
					//player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
			}
			
			/*
			 * *****************************************************
			 * If we use double action tools this will ONLY set the origin value!
			 * *****************************************************
			 */
			if(Cuboids2.sel.get(player.getName()) == null) {
				Cuboids2.sel.put(player.getName(), new CuboidSelection());
			}
			if (Cuboids2.cfg.useDoubleAction()) {
		        if (Cuboids2.sel.get(player.getName()).getWorld().equalsIgnoreCase("no_world")) {
		          Cuboids2.sel.get(player.getName()).setWorld(player.getWorld().getType().name());
		        }

		        Vector blockPosition = new Vector(block.getX(), block.getY(), block.getZ());

		        Cuboids2.sel.get(player.getName()).setWorld(player.getWorld().getType().name());
		        Cuboids2.sel.get(player.getName()).setOrigin(this.toolBox.adjustWorldBlock(blockPosition));

		        player.sendMessage(Colors.LightGray+Cuboids2.msg.messages.get("firstPointSet"));
		        return true;
		    }
		}
		
		/*
		 * *****************************************************
		 * OPERABLE ITEMS HANDLING
		 * *****************************************************
		 */
		Vector v = new Vector(player.getX(), player.getY(), player.getZ());
		//v = toolBox.adjustWorldPosition(v);
		if(Cuboids2.cuboids.isInCuboid(player.getWorld().getType().name(), v)) {
			if(!Cuboids2.cfg.isInOperables(block.getType()) || player.canUseCommand("/cIgnoreRestrictions")) {
				//allow
				return false;
			}
			else if(Cuboids2.cfg.isInOperables(block.getType()) && Cuboids2.cuboids.canModifyBlock(player, block)) {
				return false; //allow
			}
			else {
				//deny
				return true;
			}
		}
		return false;
	}
	
	@Override
    public boolean onBlockPlace(Player player, Block blockPlaced, Block blockClicked, Item itemInHand) {
		if(Cuboids2.cuboids.canModifyBlock(player, blockClicked)) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }

    @Override
    public boolean onBlockBreak(Player player, Block block) {
    	if(Cuboids2.cuboids.canModifyBlock(player, block)) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    @Override
    public boolean onExplode(Block block) {
    	if(Cuboids2.cuboids.isCreeperSecure(block)) {
    		return true;
    	}
    	else if(Cuboids2.cuboids.isTntSecure(block)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    @Override
	public boolean onIgnite(Block block, Player player) {
		if(block.getStatus() == 2) { //lighter
			if(player.canUseCommand("/cIgnoreRestrictions") || Cuboids2.cuboids.canModifyBlock(player, block))
			{
				return false;
			}
			else {
				return true;
			}
		}
		else if(block.getStatus() > 2) { 
			//manage spread and other fire damage in one thing
			return Cuboids2.cuboids.isFireProof(block);
		}
		else {
			return false;
		}
	}
}
