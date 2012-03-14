
import java.io.File;

import net.playblack.blocks.WorldBlock;
import net.playblack.cuboid.CuboidE;
import net.playblack.cuboid.CuboidSelection;
import net.playblack.cuboid.tree.CuboidNode;
import net.playblack.datasource.CuboidDeserializer;
import net.playblack.datasource.CuboidSerializer;
import net.playblack.datasource.FlatFileSerializer;
import net.playblack.mcutils.Vector;


public class CommandListener extends PluginListener {
	
	//if stuff like cfill cannot be undone by exceeding max undoable blocks
	//private boolean ignoreSizeWarning = false;
	/**
	 * *********************************************************************************************
	 * AREA MANIPULATION COMMAND LISTENER
	 * *********************************************************************************************
	 */
	@Override
	public boolean onCommand(Player player, String[] split) {

		if(!split[0].equalsIgnoreCase("/reloadplugin")) {
			if(Cuboids2.sel.get(player.getName()) == null) {
				Cuboids2.sel.put(player.getName(), new CuboidSelection());
			}
		}
		
		if(split[0].equalsIgnoreCase("/cdel")) {
			//Dirty or elegant? You decide!
			player.command("/cfill 0");
			return true;
		}
		
		if(split[0].equalsIgnoreCase("/cbrush")) {
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cWorldMod")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
				if(!player.canUseCommand("/cbrush")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
					
				}
			}
			
			//command, radius, type
			if(split.length == 3) {
				int radius = parseInt(split[1]);
				int type = parseInt(split[2]);
				Cuboids2.sel.get(player.getName()).setSculptData(0);
				Cuboids2.sel.get(player.getName()).setSculptRadius(radius);
				Cuboids2.sel.get(player.getName()).setSculptType(type);
				player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("brushSet"));
				return true;
				
			}
			//command, radius, type, data
			else if(split.length >= 4) {
				int radius = convertItem(split[1]);
				int type = convertItem(split[2]);
				int data = convertData(split[3]);
				Cuboids2.sel.get(player.getName()).setSculptData(data);
				Cuboids2.sel.get(player.getName()).setSculptRadius(radius);
				Cuboids2.sel.get(player.getName()).setSculptType(type);

				player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("brushSet"));
				return true;
			}
			else if(split.length<3){
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cbrush"));
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("missingArguments"));
				return true;
			}
			else {
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cbrush"));
				return true;
			}
		}
		
		/*
		 * **************************************************
		 * COPY SELECTION
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/ccopy")) {
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cWorldMod")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
			}
			if(Cuboids2.sel.get(player.getName()).isComplete()) {
				Cuboids2.blockOp.relativeCopy(  player.getName(), 
												Cuboids2.content.getBlocksFromWorld(player, Cuboids2.sel.get(player.getName()))
												//gr��e an alex
												,new Vector(player.getX(), player.getY(), player.getZ()) 
				);
				player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidCopied"));
			}
			return true;
		}
		
		/*
		 * **************************************************
		 * PASTE SELECTION
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/cpaste")) {
			
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cWorldMod")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
			}
			Vector position;
			position =  new Vector(player.getX(), player.getY(), player.getZ());
			CuboidSelection tmp = Cuboids2.blockOp.clipboardMoveByVector(player.getName(),
																		 position
																		 ,false); //simulate new block positions for backup
			if(tmp != null) {
				Cuboids2.blockOp.rememberBlocks(player.getName(), Cuboids2.content.getBlocksFromWorld(player, tmp), false);
				
				if(Cuboids2.content.modifyWorld(player, tmp)) {
					player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidPasted"));
					if(Cuboids2.cfg.isVerbose()) {
						Cuboids2.eventLog.logMessage(player.getName()+" used /cpaste, modifying "+tmp.getBlockList().size()+" blocks.","INFO");
					}
				}
				else {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCopy"));
				}
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCopy"));
			}
			return true;
		}
		
		/*
		 * **************************************************
		 * MOVE SELECTION
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/cmove")) {
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cWorldMod")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
			}
			if(split.length < 3) {
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmove_base"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmove_directions"));
				return true;
			}

			if(Cuboids2.sel.get(player.getName()).isComplete()) {
				CuboidSelection selection = Cuboids2.content.getBlocksFromWorld(player, Cuboids2.sel.get(player.getName()));
				
				Cuboids2.blockOp.rawCopy(player.getName(), selection);
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("selectionIncomplete"));
				return true;
			}
		
			int distance = parseInt(split[2]);
			if(distance == -1) {
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmove_base"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmove_directions"));
				return true;
			}

			CuboidSelection tmp = Cuboids2.blockOp.fill(player.getName(), Cuboids2.blockOp.getClipboard(player.getName()), new WorldBlock((byte)0,(short)0), true);
			Cuboids2.blockOp.rememberBlocks(player.getName(), Cuboids2.content.getBlocksFromWorld(player, tmp), true);
			Cuboids2.content.modifyWorld(player, tmp);
			//And now paste stuff at the new positions
			tmp = Cuboids2.blockOp.moveByOffset(player.getName(), split[1], distance, Cuboids2.blockOp.getClipboard(player.getName()), true);
			Cuboids2.blockOp.rememberBlocks(player.getName(), Cuboids2.content.getBlocksFromWorld(player, tmp), false);
			if(Cuboids2.content.modifyWorld(player,tmp)) {
				player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidMoved"));
				if(Cuboids2.cfg.isVerbose()) {
					Cuboids2.eventLog.logMessage(player.getName()+" used /cmove, modifying "+tmp.getBlockList().size()+" blocks.","INFO");
				}
			}
			else {
				player.sendMessage(Colors.Rose+"Cuboid cannot be moved! There was an internal error concerning the selection");
			}
			return true;
		}
		
		/*
		 * **************************************************
		 * BUILD WALLS FROM SELECTION
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/cwalls") || split[0].equalsIgnoreCase("/cfaces")) {
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cWorldMod")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
			}
			if(split.length < 2) {
				if(split[0].equalsIgnoreCase("/cfaces")) {
					player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cfaces"));
					return true;
				}
				if(split[0].equalsIgnoreCase("/cwalls")) {
					player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cwalls"));
					return true;
				}
			}
			CuboidSelection selection = Cuboids2.sel.get(player.getName());
			CuboidSelection tmp;
			if(selection.isComplete()) {
				if(split[0].equalsIgnoreCase("/cwalls")) {
					tmp = Cuboids2.blockOp.buildWalls(player.getName(), selection, false);
				}
				else {
					tmp = Cuboids2.blockOp.buildWalls(player.getName(), selection, true);
				}
				WorldBlock block = new WorldBlock();
				if(split.length == 2) {
					short type = convertType(split[1]);
					if(type == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
					else {
						block.setType(type);
						block.setData((byte)0);
					}
				}
				else if(split.length > 2) {
					short type = convertType(split[1]);
					byte data = convertData(split[2]);
					if(type == -1 | data == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
					else {
						block.setType(type);
						block.setData(data);
					}
				}
				
				Cuboids2.blockOp.rememberBlocks(player.getName(), Cuboids2.content.getBlocksFromWorld(player, tmp), true);
				tmp = Cuboids2.blockOp.replace(player.getName(), tmp, new WorldBlock((byte)0,(short)1), block, true);
				if(Cuboids2.content.modifyWorld(player, tmp)) {
					player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("wallsCreated"));
					if(Cuboids2.cfg.isVerbose()) {
						Cuboids2.eventLog.logMessage(player.getName()+" used /cwalls, modifying "+tmp.getBlockList().size()+" blocks.","INFO");
					}
					return true;
				}
				else {
					return true;
				}
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.help.get("selectionIncomplete"));
				return true;
			}
		}
		
		/*
		 * **************************************************
		 * CUBOID FILL
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/cfill")) {
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cWorldMod")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
			}
			if(split.length > 1) {
				if(Cuboids2.sel.get(player.getName()).isComplete()) {	
					//Assign block type
					short type = (short)convertType(split[1]);
					if(type == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
					WorldBlock b = new WorldBlock();
					if(split.length > 2) {
						byte data = convertData(split[2]);
						if(data == -1) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
							return true;
						}
						b.setData(data);
						b.setType(type);
					}
					else {
						b.setType(type);
					}

					CuboidSelection selection;
					selection = Cuboids2.blockOp.fill(player.getName(), 
										  Cuboids2.content.getBlocksFromWorld(player, Cuboids2.sel.get(player.getName())),
										  b,
										  false);

					Cuboids2.content.modifyWorld(player, selection);
					player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("selectionFilled"));
					if(Cuboids2.cfg.isVerbose()) {
						Cuboids2.eventLog.logMessage(player.getName()+" used /cfill, modifying "+selection.getBlockList().size()+" blocks.","INFO");
					}
					return true;
				}
				else {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("selectionIncomplete"));
					return true;
				}
				
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("missingArgumentsBlockId"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cfill"));
				return true;
			}
					
		}
		
		/*
		 * **************************************************
		 * CUBOID REPLACE BLOCKS
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/creplace")) {
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cWorldMod")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
			}
			if(split.length > 2) {
				if(Cuboids2.sel.get(player.getName()).isComplete()) {
					//Assign block type
					WorldBlock a = new WorldBlock();
					short a_type = 0;
					byte a_data = 0;
					WorldBlock b = new WorldBlock();
					short b_type = 0;
					byte b_data = 0;
					if(split.length > 4) {
						a_type = convertType(split[1]);
						a_data = convertData(split[2]);
						if(a_type == -1 || a_data == -1) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
							return true;
						}
						b_type = convertType(split[3]);
						b_data = convertData(split[4]);
						if(b_type == -1 || b_data == -1) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
							return true;
						}
					}
					else if(split.length > 3){
						a_type = convertType(split[1]);
						a_data = 0;
						if(a_type == -1 || a_data == -1) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
							return true;
						}
						b_type = convertType(split[2]);
						b_data = convertData(split[3]);
						if(b_type == -1 || b_data == -1) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
							return true;
						}
						
					}
					else {
						a_type = convertType(split[1]);
						b_type = convertType(split[2]);
						if(a_type == -1 || b_type == -1) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
							return true;
						}
						a_data=0;
						b_data=0;
					}
					//if anything went wrong with the data/types it wont get here
					a.setData(a_data);
					a.setType(a_type);
					b.setData(b_data);
					b.setType(b_type);
					CuboidSelection selection = Cuboids2.content.getBlocksFromWorld(player, Cuboids2.sel.get(player.getName()));

					Cuboids2.content.modifyWorld(player, Cuboids2.blockOp.replace(player.getName(), selection, a, b, false));
					player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("selectionReplaced"));
					if(Cuboids2.cfg.isVerbose()) {
						Cuboids2.eventLog.logMessage(player.getName()+" used /creplace, modifying "+selection.getBlockList().size()+" blocks.","INFO");
					}
					return true;
				}
				else {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("selectionIncomplete"));
					return true;
				}
				
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("missingArgumentsBlockId"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("creplace"));
				return true;
			}
					
		}
		
		/* *******************************************************************************
		 * 
		 * CSPHERE AND STUFF
		 * 
		 * *******************************************************************************/
		if(split[0].equalsIgnoreCase("/csphere") && (player.canUseCommand("/cWorldMod") || player.canUseCommand("/cIgnorerestrictions"))) {
			if(Cuboids2.sel.get(player.getName()) == null) {
				player.sendMessage(Colors.Rose+"No point set to start with.");
				return true;
			}
			
			if(Cuboids2.sel.get(player.getName()).originSet()) {
				WorldBlock block = new WorldBlock();
				int radius = 0;
				
				boolean fill = true; //this is default and means it is NOT HOLLOW
				
				//0: csphere 1: radius 2:block
				if(split.length == 3) {
					radius = parseInt(split[1]);
					if(radius == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidRadius"));
						return true;
					}
					short type = convertType(split[2]);
					if(type != -1) {
						block.setType(type);
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
				}
				
				//0: csphere 1: radius 2:block 3:Data or hollow
				else if(split.length == 4) {
					radius = parseInt(split[1]);
					if(radius == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidRadius"));
						return true;
					}
					short type = convertType(split[2]);
					if(type != -1) {
						block.setType(type);
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
				
					byte data = convertData(split[3]);
					if(data != -1) {
						block.setData(data);
					}
					else {
						//player.sendMessage("Something was no number!");
						if(split[3].equalsIgnoreCase("hollow")) {
							fill = false;
						}
					}
					//return true;
				}
				
				//0: csphere 1: radius 2:block 3:Data 4:hollow
				else if(split.length == 5) {
					radius = parseInt(split[1]);
					if(radius == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidRadius"));
						return true;
					}
					short type = convertType(split[2]);
					if(type != -1) {
						block.setType(type);
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
				
					byte data = convertData(split[3]);
					if(data == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
					else {
						block.setData(data);
					}
					
					if(split[4].equalsIgnoreCase("hollow")) {
						fill = false;
					}
				}
				else {
					player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("csphere"));
					return true;
				}

				CuboidSelection sphere = Cuboids2.blockOp.buildSphere(player.getName(), radius, Cuboids2.sel.get(player.getName()).getOrigin(),fill);
				sphere = Cuboids2.blockOp.replace(player.getName(), sphere, new WorldBlock((byte)0,(short)1), block, true);
				Cuboids2.blockOp.rememberBlocks(player.getName(), Cuboids2.content.getBlocksFromWorld(player, sphere), true);
				Cuboids2.content.modifyWorld(player, sphere);
				player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("sphereCreated"));
				if(Cuboids2.cfg.isVerbose()) {
					Cuboids2.eventLog.logMessage(player.getName()+" used /csphere, modifying "+sphere.getBlockList().size()+" blocks.","INFO");
				}
				return true;
			}
		}
		
		
		/* *******************************************************************************
		 * 
		 * CPYRAMID AND STUFF
		 * 
		 * *******************************************************************************/
		if(split[0].equalsIgnoreCase("/cpyramid") && (player.canUseCommand("/cWorldMod") || player.canUseCommand("/cIgnorerestrictions"))) {
			if(Cuboids2.sel.get(player.getName()) == null) {
				player.sendMessage(Colors.Rose+"No point set to start with.");
				return true;
			}
			 //    0        1           2               3
			 ///cpyramid <radius> <block id/name> <data (optional)>
			if(Cuboids2.sel.get(player.getName()).originSet()) {
				WorldBlock block = new WorldBlock((byte)0,(short)0);
				int radius = 0;
				
				boolean fill = true; //this is default and means it is NOT HOLLOW
				
				//0: csphere 1: radius 2:block
				if(split.length == 3) {
					radius = convertItem(split[1]);
					if(radius == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidRadius"));
						return true;
					}
					short type = convertType(split[2]);
					if(type != -1) {
						block.setType(type);
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
				}
				
				//0: csphere 1: radius 2:block 3:Data or hollow
				else if(split.length == 4) {
					radius = parseInt(split[1]);
					if(radius == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidRadius"));
						return true;
					}
					short type = convertType(split[2]);
					if(type != -1) {
						block.setType(type);
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
				
					byte data = convertData(split[3]);
					if(data != -1) {
						block.setData(data);
					}
					else {
						//player.sendMessage("Something was no number!");
						if(split[3].equalsIgnoreCase("hollow")) {
							fill = false;
						}
					}
					//return true;
				}
				
				//0: csphere 1: radius 2:block 3:Data 4:hollow
				else if(split.length == 5) {
					radius = parseInt(split[1]);
					if(radius == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidRadius"));
						return true;
					}
					short type = convertType(split[2]);
					if(type != -1) {
						block.setType(type);
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
				
					byte data = convertData(split[3]);
					if(data == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
					else {
						block.setData(data);
					}
					
					if(split[4].equalsIgnoreCase("hollow")) {
						fill = false;
					}
				}
				else {
					player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cpyramid"));
					return true;
				}

				CuboidSelection pyramid = Cuboids2.blockOp.buildPyramid(player.getName(), radius, Cuboids2.sel.get(player.getName()).getOrigin(), fill);
				pyramid = Cuboids2.blockOp.replace(player.getName(), pyramid, new WorldBlock((byte)0, (short)1), block, true);
				Cuboids2.blockOp.rememberBlocks(player.getName(), Cuboids2.content.getBlocksFromWorld(player, pyramid), true);
				Cuboids2.content.modifyWorld(player, pyramid);
				player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("pyramidCreated"));
				if(Cuboids2.cfg.isVerbose()) {
					Cuboids2.eventLog.logMessage(player.getName()+" used /cpyramid, modifying "+pyramid.getBlockList().size()+" blocks.","INFO");
				}
				return true;
			}
		}
		
		/* *******************************************************************************
		 * 
		 * CCIRCLE AND STUFF
		 * 
		 * *******************************************************************************/
		if((split[0].equalsIgnoreCase("/ccircle") || split[0].equalsIgnoreCase("/cdisc")) && (player.canUseCommand("/cWorldMod") || player.canUseCommand("/cIgnorerestrictions"))) {
			if(Cuboids2.sel.get(player.getName()) == null) {
				player.sendMessage(Colors.Rose+"No point set to start with.");
				return true;
			}
			boolean disc = split[0].equalsIgnoreCase("/cdisc") ? true : false;
			
			 //    0        1           2          3		4
			///cpyramid <radius> <block id/name> <data (optional)>
			///cdisc    <radius> <block id/name  <data> [height]");
			if(Cuboids2.sel.get(player.getName()).originSet()) {
				WorldBlock block = new WorldBlock((byte)0,(short)0);
				int radius = 0;
				int height = 1;
				//0: cmd  1: radius 2:block
				if(split.length == 3) {
					radius = parseInt(split[1]);
					if(radius == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidRadius"));
						return true;
					}
					short type = convertType(split[2]);
					if(type != -1) {
						block.setType(type);
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
				}
				
				//0: csphere 1: radius 2:block 3:Data
				else if(split.length == 4) {
					radius = parseInt(split[1]);
					if(radius == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidRadius"));
						return true;
					}
					short type = convertType(split[2]);
					if(type != -1) {
						block.setType(type);
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
				
					byte data = convertData(split[3]);
					if(data != -1) {
						block.setData(data);
					}
					else {
						block.setData((byte)0);
					}
					//return true;
				}
				
				//0: csphere 1: radius 2:block 3:Data 4:height
				else if(split.length == 5) {
					radius = parseInt(split[1]);
					if(radius == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidRadius"));
						return true;
					}
					short type = convertType(split[2]);
					if(type != -1) {
						block.setType(type);
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
				
					byte data = convertData(split[3]);
					if(data == -1) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidBlock"));
						return true;
					}
					else {
						block.setData(data);
					}
					
					height = parseInt(split[4]);
					if(height == -1) {
						height=0;
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidHeight"));
						return true;
					}
				}
				else {
					if(disc) {
						player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cdisc"));
					}
					else {
						player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("ccircle"));
					}
					return true;
				}

				CuboidSelection tmp = Cuboids2.blockOp.buildCircle(player.getName(), radius, height, Cuboids2.sel.get(player.getName()).getOrigin(), disc);
				tmp = Cuboids2.blockOp.replace(player.getName(), tmp, new WorldBlock((byte)0, (short)1), block, true);
				Cuboids2.blockOp.rememberBlocks(player.getName(), Cuboids2.content.getBlocksFromWorld(player, tmp), true);
				Cuboids2.content.modifyWorld(player, tmp);

				if(disc) {
					player.sendMessage(Colors.Yellow+Cuboids2.msg.messages.get("discCreated"));
					if(Cuboids2.cfg.isVerbose()) {
						Cuboids2.eventLog.logMessage(player.getName()+" used /cdics, modifying "+tmp.getBlockList().size()+" blocks.","INFO");
					}
				}
				else {
					player.sendMessage(Colors.Yellow+Cuboids2.msg.messages.get("circleCreated"));
					if(Cuboids2.cfg.isVerbose()) {
						Cuboids2.eventLog.logMessage(player.getName()+" used /ccircle, modifying "+tmp.getBlockList().size()+" blocks.","INFO");
					}
				}
				return true;
			}
		}
		
		/*
		 * **************************************************
		 * CUBOID UNDO/REDO
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/cundo")) {
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cWorldMod")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
			}
			if(Cuboids2.cfg.allowUndo() == false) {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("UndoDisabled"));
				return true;
			}
			int undoSteps = -1;
			if(split.length > 1) {
				
				try {
					undoSteps = parseInt(split[1]);
				}
				catch(NumberFormatException e) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidParameterTypeInteger"));
					return true;
				}
				if(undoSteps < 0) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("negativeNumber"));
					return true;
				}
			}
			if(undoSteps == -1) {
				undoSteps = 1;
			}
			int blocks = 0;
			for(int i = 0; i < undoSteps; i++) {
				//log.info("Undoing stuff.");	
				CuboidSelection d = Cuboids2.blockOp.undo(player.getName());
				if(Cuboids2.content.modifyWorld(player, d)) {
					blocks+=d.getBlockList().size();
				}
			}
			player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("actionsUndone"));
			if(Cuboids2.cfg.isVerbose()) {
				Cuboids2.eventLog.logMessage(player.getName()+" used /cundo "+undoSteps+" times, modifying "+blocks+" blocks.","INFO");
			}
			return true;
					
		}
		
		/*
		 * **************************************************
		 * MANAGE CUBOIDS
		 * TODO: add -r flag to ceiling and floor so people can also use relative nums ?
		 * **************************************************
		 */
		
		if(split[0].equalsIgnoreCase("/cceiling") || split[0].equalsIgnoreCase("/cceil")) {
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cselect")) {
					if(!player.canUseCommand("/ccreate")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return true;
					}
				}
			}
			if(split.length > 1) {
				int ceiling = 0;
				try {
					ceiling = Integer.parseInt(split[1]);
				}
				catch(NumberFormatException e) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidParameterTypeInteger"));
					return true;
				}
				if(ceiling >= 0) {
					Cuboids2.sel.get(player.getName()).setCeiling(ceiling);
					player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidCeiled"));
					return true;
				}
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("negativeNumber"));
				return true;
			}
			player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("missingArguments"));
			player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cceiling"));
			return true;
			
		}
		
		if(split[0].equalsIgnoreCase("/cfloor")) {
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/cWorldMod")) {
					if(!player.canUseCommand("/ccreate")) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
						return true;
					}
				}
			}
			
			if(split.length > 1) {
				int floor = 0;
				try {
					floor = Integer.parseInt(split[1]);
				}
				catch(NumberFormatException e) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidParameterTypeInteger"));
					return true;
				}
				if(floor >= 0) {
					Cuboids2.sel.get(player.getName()).setFloor(floor);
					player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidFloored"));
					return true;
				}
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("negativeNumber"));
				return true;
			}
			player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("missingArguments"));
			player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cfloor"));
			return true;
			
		}
		
		if(split[0].equalsIgnoreCase("/cmod")) {
			/*
			 * **************************************************
			 * MANIPULATE SELECTION, EXPAND, SHRINK ETC
			 * **************************************************
			 */
			if(split.length >= 2) {
				/*
				 * EXPAND VERTICALLY
				 */
				
				if(split[1].equalsIgnoreCase("expand")) {
					
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						if(!player.canUseCommand("/ccselect")) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
							return true;
						}
					}
					Cuboids2.sel.get(player.getName()).expandVert();
					player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("selectionExpanded"));
					return true;
				}
				
				if(split[1].equalsIgnoreCase("list")) {
					if(split.length == 2) {
						Cuboids2.cuboids.displayCuboidList(player, 1);
					}
					else {
						try {
							Cuboids2.cuboids.displayCuboidList(player, Integer.parseInt(split[2]));
						}
						catch(NumberFormatException e) {
							player.sendMessage(Cuboids2.msg.messages.get(Colors.Rose+"invalidParameterTypeInteger"));
						}
					}
					return true;
				}
			}
			
			/*
			 * **************************************************
			 * CREATE||ADD / INFO||EXPLAIN / PRIO SETTING / ALLOW / DISALLOW
			 * **************************************************
			 */
			if(split.length > 2) {
				/*
				 * CREATE / ADD
				 */
				if(split[2].equalsIgnoreCase("create") || split[2].equalsIgnoreCase("add")) {
					
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						if(!player.canUseCommand("/ccreate")) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
							return true;
						}
					}
					if(Cuboids2.sel.get(player.getName()).isComplete()) {
						if(Cuboids2.cuboids.cuboidExist(split[1], player.getWorld().getType().name()) == false) {
							String owner = "o:"+player.getName();
							if(split[1].indexOf(",") != -1 || split[1].indexOf(":") != -1) {
								player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidCharacters"));
								return true;
							}
							CuboidE newCube = Cuboids2.sel.get(player.getName()).toCuboid(owner, 
																	split[1], 
																	Cuboids2.cfg.getDefaultCuboidSettings(player),
																	player.getWorld().getType().name());
							if(Cuboids2.cuboids.addCuboid(newCube)) {
								player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidAdded"));
								if(Cuboids2.cfg.isVerbose()) {
									Cuboids2.eventLog.logMessage(player.getName()+" used /cmod create", "INFO");
								}
								return true;
							}
							else {
								player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("cuboidNotAdded"));
								return true;
							}
						}
						else {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("cuboidExist"));
							return true;
						}
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("selectionIncomplete"));
						return true;
					}
				}
				
				
				/*
				 * BACKUP
				 */
                if (split[2].equalsIgnoreCase("backup")) {
                    if(!player.canUseCommand("/cIgnoreRestrictions")) {
                        if(!player.canUseCommand("/cbackup")) {
                            if(!player.canUseCommand("/cAreaMod")) {
                                player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
                                return true;
                            }
                        }
                    }
                    CuboidNode cuboid = Cuboids2.treeHandler.getCuboidByName(split[1], player.getWorld().getType().name());
                    CuboidSelection tmp = new CuboidSelection(cuboid.getCuboid().getFirstPoint(), cuboid.getCuboid().getSecondPoint());
                    CuboidSelection selection = Cuboids2.content.getBlocksFromWorld(player, tmp);
                    
                    CuboidSerializer ser = new FlatFileSerializer(selection);
                    ser.save(split[1], player.getWorld().getType().name());
                    player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidBackupSuccess"));
                    return true;
                }
				
				/*
				 * RESTORE
				 */
				if(split[2].equalsIgnoreCase("restore")) {
					if(!player.canUseCommand("/cIgnoreRestrictions")) {
						if(!player.canUseCommand("/cbackup")) {
							player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
							return true;
						}
					}
					if(split.length < 3) {
					    player.notify(Cuboids2.msg.messages.get("missingArguments"));
					    return true;
					}
					String world = player.getWorld().getType().name();
					File f = new File("plugins/cuboids2/backups/blocks_"+world+"_"+split[1]);
					if(f.exists()) {
					    CuboidDeserializer des = new CuboidDeserializer(split[1], world);
					    CuboidSelection restore = des.convert();
					    boolean success = Cuboids2.content.modifyWorld(player, restore);
					    if(success) {
					        player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidRestoreSuccess"));
					        return true;
					    }
					    else {
					        player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("cuboidRestoreFail"));
					        return true;
					    }
					}
					else {
					    player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("cuboidRestoreFail"));
                        player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("noCuboidFoundOnCommand"));
                        return true;
					}
				}
				
				/*
				 * RESTRICT COMMAND
				 */
				if(split[2].equalsIgnoreCase("restrictcommand")) {
					if(split.length > 3) {
						//player.sendMessage("split more 3");
						if(!player.canUseCommand("/cIgnoreRestrictions")) {
							if(!player.canUseCommand("/ccommand")) {
								player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
								return true;
							}
						}
					//	player.sendMessage("Trying to restrict");
						Cuboids2.cuboids.restrictCommand(player, split, split[1]);
						player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidCommandBlacklisted"));
						if(Cuboids2.cfg.isVerbose()) {
							Cuboids2.eventLog.logMessage(player.getName()+" used /cmod restrictcommand", "INFO");
						}
						return true;
					}
				}
				
				/*
				 * ALLOW COMMAND
				 */
				if(split[2].equalsIgnoreCase("allowcommand")) {
					if(split.length > 3) {
						if(!player.canUseCommand("/cIgnoreRestrictions")) {
							if(!player.canUseCommand("/ccommand")) {
								player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
								return true;
							}
						}
						Cuboids2.cuboids.allowCommand(player, split, split[1]);
						player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidCommandAllowed"));
						if(Cuboids2.cfg.isVerbose()) {
							Cuboids2.eventLog.logMessage(player.getName()+" used /cmod allowcommand", "INFO");
						}
						return true;
					}
				}
				
				
				/*
				 * REMOVE / DELETE
				 */
				if(split[2].equalsIgnoreCase("remove") || split[2].equalsIgnoreCase("delete")) {
					Cuboids2.cuboids.removeCuboid(player, split[1]);
					if(Cuboids2.cfg.isVerbose()) {
						Cuboids2.eventLog.logMessage(player.getName()+" used /cmod delete", "INFO");
					}
					return true;
				}
				
				/*
				 * INFO / EXPLAIN
				 */
				if(split[2].equalsIgnoreCase("info") || split[2].equalsIgnoreCase("explain")) {
					Cuboids2.cuboids.explainCuboid(player, split[1]);
					return true;
				}
				
				/*
				 * INFO / EXPLAIN
				 */
				if(split[2].equalsIgnoreCase("cmdblacklist") || split[2].equalsIgnoreCase("cmdlist")) {
					Cuboids2.cuboids.showCommandBlacklist(player, split[1]);
					return true;
				}
				
				/*
				 * PRIORITY SETTING
				 */
				if(split[2].equalsIgnoreCase("priority") || split[2].equalsIgnoreCase("prio")) {
					int prio = -1; 
					try {
						prio = Integer.parseInt(split[3]);
					}
					catch(NumberFormatException e) {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("invalidParameterTypeInteger"));
						return true;
					}
					Cuboids2.cuboids.setPriority(player, split[1], prio);
					if(Cuboids2.cfg.isVerbose()) {
						Cuboids2.eventLog.logMessage(player.getName()+" used /cmod priority", "INFO");
					}
					return true;
				}
				
				/*
				 * PARENT SETTING
				 */
				if(split[2].equalsIgnoreCase("parent")) {
					if(Cuboids2.cuboids.setParent(player, split[1], split[3])) {
						if(Cuboids2.cfg.isVerbose()) {
							Cuboids2.eventLog.logMessage(player.getName()+" used /cmod parent", "INFO");
						}
					}
					return true;
				}
				
				/*
				 * ALLOW STUFF TO CUBOID
				 */
				if(split[2].equalsIgnoreCase("allow")) {
					if(Cuboids2.cuboids.allowEntity(player, split)) {
						if(Cuboids2.cfg.isVerbose()) {
							Cuboids2.eventLog.logMessage(player.getName()+" used /cmod allow", "INFO");
						}
					}
					return true;
				}
				
				/*
				 * DISALLOW STUFF TO CUBOID
				 */
				if(split[2].equalsIgnoreCase("disallow")) {
					if(Cuboids2.cuboids.disallowEntity(player, split)) {
						if(Cuboids2.cfg.isVerbose()) {
							Cuboids2.eventLog.logMessage(player.getName()+" used /cmod disallow", "INFO");
						}
					}
					return true;
				}
				
				/*
				 * RESIZE / MOVE CUBOID BOUNDING RECTANGLE
				 */
				if(split[2].equalsIgnoreCase("resize") || split[2].equalsIgnoreCase("move")) {
						if(Cuboids2.cuboids.resize(player, Cuboids2.sel.get(player.getName()), split[1])) {
							if(Cuboids2.cfg.isVerbose()) {
								Cuboids2.eventLog.logMessage(player.getName()+" used /cmod resize or /cmod move", "INFO");
							}
						}
						return true;
				}
				
				/*
				 * WELCOME MESSAGE
				 */
				if(split[2].equalsIgnoreCase("welcome")) {
					if(split.length==3) {
						Cuboids2.cuboids.setWelcomeMessage(player, split[1], null);
						//player.sendMessage(Colors.LightGreen+Cuboids2.msg.help.get("cuboidUpdated"));
						return true; //cuboidUpdated
					}
					else if(split.length > 3) {
						String message = "";
						for(int i = 3; i<split.length; i++) {
							if(i > 3) {
								message +=" ";
							}
							message += split[i];
						}
						if(Cuboids2.cuboids.setWelcomeMessage(player, split[1], message)) {
							if(Cuboids2.cfg.isVerbose()) {
								Cuboids2.eventLog.logMessage(player.getName()+" used /cmod welcome", "INFO");
							}
						}
						//player.sendMessage(Colors.LightGreen+Cuboids2.msg.help.get("cuboidUpdated"));
						return true;
					}
					player.sendMessage(Colors.Rose+Cuboids2.msg.help.get("cmodWelcome"));
					return true;
				}
				
				/*
				 * FAREWELL MESSAGE
				 */
				if(split[2].equalsIgnoreCase("farewell") || split[2].equalsIgnoreCase("goodbye")) {
					if(split.length==3) {
						Cuboids2.cuboids.setFarewellMessage(player, split[1], null);
					}
					else if(split.length > 3) {
						String message = "";
						for(int i = 3; i<split.length; i++) {
							if(i > 3) {
								message +=" ";
							}
							message += split[i];
						}
						if(Cuboids2.cuboids.setFarewellMessage(player, split[1], message)) {
							if(Cuboids2.cfg.isVerbose()) {
								Cuboids2.eventLog.logMessage(player.getName()+" used /cmod farewell", "INFO");
							}
						}
						//player.sendMessage(Colors.LightGreen+Cuboids2.msg.help.get("cuboidUpdated"));
						return true;
						
					}
					player.sendMessage(Colors.Rose+Cuboids2.msg.help.get("cmodFarewell"));
					return true;
				}
				
				/*
				 * TOGGLE  ___
				 */
				
				/*
				 * GLOBAL TOGGLES
				 */
				if(split[1].equalsIgnoreCase("toggle")) {
					if(player.canUseCommand("/cToggleGlobals") || player.canUseCommand("/cIgnoreRestrictions")) {
						if(split.length > 2) {
							if(split[2].equalsIgnoreCase("creeper")) {
								if(Cuboids2.cfg.globalDisableCreeperSecure()) {
									Cuboids2.cfg.setGlobalDisableCreeperSecure(false);
									Cuboids2.cfg.updateSetting("disable-creeper-secure-global", false);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalCreeperEnabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle creeper", "INFO");
									}
									return true;
								}
								else {
									Cuboids2.cfg.setGlobalDisableCreeperSecure(true);
									Cuboids2.cfg.updateSetting("disable-creeper-secure-global", true);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalCreeperDisabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle creeper", "INFO");
									}
									return true;
								}
							}
							
							if(split[2].equalsIgnoreCase("sanctuary")) {
								if(Cuboids2.cfg.globalSanctuary()) {
									Cuboids2.cfg.setGlobalSanctuary(false);
									Cuboids2.cfg.updateSetting("sanctuary-global", false);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalSanctuaryDisabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle sanctuary", "INFO");
									}
									return true;
								}
								else {
									Cuboids2.cfg.setGlobalSanctuary(true);
									Cuboids2.cfg.updateSetting("sanctuary-global", true);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalSanctuaryEnabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle sanctuary", "INFO");
									}
									return true;
								}
							}
							
							if(split[2].equalsIgnoreCase("sanctuary-animalspawn")) {
								if(Cuboids2.cfg.globalSanctuaryAnimalSpawn()) {
									Cuboids2.cfg.setGlobalSanctuarySpawnAnimals(false);
									Cuboids2.cfg.updateSetting("sanctuary-animal-spawn-global", false);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalSanctuaryAnimalsDisabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle sanctuary-animalspawn", "INFO");
									}
									return true;
								}
								else {
									Cuboids2.cfg.setGlobalSanctuarySpawnAnimals(true);
									Cuboids2.cfg.updateSetting("sanctuary-animal-spawn-global", true);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalSanctuaryAnimalsEnabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle sanctuary-animalspawn", "INFO");
									}
									return true;
								}
							}
							
							if(split[2].equalsIgnoreCase("lavaflow") || split[2].equalsIgnoreCase("lava")) {
								if(Cuboids2.cfg.stopLavaFlowGlobal()) {
									Cuboids2.cfg.setStopLavaFlow(false); //global 
									Cuboids2.cfg.updateSetting("lava-flow-global", false);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalLavaFlowDisabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle lavaflow", "INFO");
									}
									return true;
								}
								else {
									Cuboids2.cfg.setStopLavaFlow(true);
									Cuboids2.cfg.updateSetting("stop-lava-flow-global", true);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalLavaFlowEnabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle lavaflow", "INFO");
									}
									return true;
								}
							}
							
							if(split[2].equalsIgnoreCase("waterflow") || split[2].equalsIgnoreCase("water")) {
								if(Cuboids2.cfg.stopWaterFlowGlobal()) {
									Cuboids2.cfg.setStopWaterFlow(false); //global
									Cuboids2.cfg.updateSetting("stop-water-flow-global", false);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalWaterFlowDisabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle waterflow", "INFO");
									}
									return true;
								}
								else {
									Cuboids2.cfg.setStopWaterFlow(true);
									Cuboids2.cfg.updateSetting("stop-water-flow-global", true);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalWaterFlowEnabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle waterflow", "INFO");
									}
									return true;
								}
							}
							
							if(split[2].equalsIgnoreCase("pvp")) {
								if(Cuboids2.cfg.globalDisablePvp()) {
									Cuboids2.cfg.setGlobalPvp(false);
									Cuboids2.cfg.updateSetting("disable-pvp-global", false);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalPvpDisabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle pvp", "INFO");
									}
									return true;
								}
								else {
									Cuboids2.cfg.setGlobalPvp(true);
									Cuboids2.cfg.updateSetting("disable-pvp-global", true);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalPvpEnabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle pvp", "INFO");
									}
									return true;
								}
							}
							
							if(split[2].equalsIgnoreCase("tnt")) {
								if(Cuboids2.cfg.globalTntSecure()) {
									Cuboids2.cfg.setTntGlobal(false);
									Cuboids2.cfg.updateSetting("tnt-secure-global", false);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalTntDisabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle tnt", "INFO");
									}
									return true;
								}
								else {
									Cuboids2.cfg.setTntGlobal(true);
									Cuboids2.cfg.updateSetting("tnt-secure-global", true);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalTntEnabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle tnt", "INFO");
									}
									return true;
								}
							}
							
							if(split[2].equalsIgnoreCase("firespread") || split[2].equalsIgnoreCase("fire")) {
								if(Cuboids2.cfg.globalFirespreadBlock()) {
									Cuboids2.cfg.setGlobalFirespreadBlock(false);
									Cuboids2.cfg.updateSetting("firespread-block-global", false);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalFirespreadDisabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle firespread", "INFO");
									}
									return true;
								}
								else {
									Cuboids2.cfg.setGlobalFirespreadBlock(true);
									Cuboids2.cfg.updateSetting("firespread-block-global", true);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalFirespreadEnabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle firespread", "INFO");
									}
									return true;
								}
							}
							
							if(split[2].equalsIgnoreCase("protection") || split[2].equalsIgnoreCase("protect")) {
								if(Cuboids2.cfg.globalProtection()) {
									Cuboids2.cfg.setGlobalProtection(false);
									Cuboids2.cfg.updateSetting("protection-global", false);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalProtectionDisabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle protection", "INFO");
									}
									return true;
								}
								else {
									Cuboids2.cfg.setGlobalProtection(true);
									Cuboids2.cfg.updateSetting("protection-global", true);
									player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("globalProtectionEnabled"));
									if(Cuboids2.cfg.isVerbose()) {
										Cuboids2.eventLog.logMessage(player.getName()+" used /cmod toggle protection", "INFO");
									}
									return true;
								}
							}
						}
						player.sendMessage(Colors.Rose+Cuboids2.msg.help.get("cmodGlobalToggle"));
					}
				}
				/*
				 * AREA TOGGLES
				 */
				if(split[2].equalsIgnoreCase("toggle")) {
					if(split.length > 3) {
						if(split[3].equalsIgnoreCase("creeper")) {
							Cuboids2.cuboids.toggleCreeper(player, split[1]);
							return true;
						}
						if(split[3].equalsIgnoreCase("pvp")) {
							Cuboids2.cuboids.togglePvp(player, split[1]);
							return true;
						}
						if(split[3].equalsIgnoreCase("sanctuary")) {
							Cuboids2.cuboids.toggleSanctuary(player, split[1]);
							return true;
						}
						if(split[3].equalsIgnoreCase("protection") || split[3].equalsIgnoreCase("protect")) {
							Cuboids2.cuboids.toggleProtection(player, split[1]);
							return true;
						}
						if(split[3].equalsIgnoreCase("heal") || split[3].equalsIgnoreCase("healing")) {
							Cuboids2.cuboids.toggleHealing(player, split[1]);
							return true;
						}	
						if(split[3].equalsIgnoreCase("freebuild") || split[3].equalsIgnoreCase("creative")) {
							Cuboids2.cuboids.toggleFreebuild(player, split[1]);
							return true;
						}
						if(split[3].equalsIgnoreCase("firespread") || split[3].equalsIgnoreCase("fire")) {
							Cuboids2.cuboids.toggleFirespread(player, split[1]);
							return true;
						}
						//v 1.2.0
						if(split[3].equalsIgnoreCase("lavaflow") || split[3].equalsIgnoreCase("lava")) {
							Cuboids2.cuboids.toggleLavaControl(player, split[1]);
							return true;
						}
						if(split[3].equalsIgnoreCase("waterflow") || split[3].equalsIgnoreCase("water")) {
							Cuboids2.cuboids.toggleWaterControl(player, split[1]);
							return true;
						}
						if(split[3].equalsIgnoreCase("tnt")) {
							Cuboids2.cuboids.toggleTnt(player, split[1]);
							return true;
						}
						if(split[3].equalsIgnoreCase("restriction") || split[3].equalsIgnoreCase("restrict")) {
							Cuboids2.cuboids.toggleRestriction(player, split[1]);
							return true;
						}
						if(split[3].equalsIgnoreCase("farmland") || split[3].equalsIgnoreCase("farm")) {
							Cuboids2.cuboids.toggleFarmland(player, split[1]);
							return true;
						}
						if(split[3].equalsIgnoreCase("sanctuary-animalspawn") || split[3].equalsIgnoreCase("animalspawn")) {
							Cuboids2.cuboids.toggleSanctuaryAnimals(player, split[1]);
							return true;
						}
						if(Cuboids2.cfg.isVerbose()) {
							Cuboids2.eventLog.logMessage(player.getName()+" used /cmod "+split[1]+" toggle "+split[3],"INFO");
						}
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("missingArguments"));
						player.sendMessage(Colors.Rose+Cuboids2.msg.help.get("cmodToggle"));
						return true;
					}
				}
			}
			else {				
				player.sendMessage(Colors.Rose+Cuboids2.msg.help.get("cmodHelp"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmodCreate"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmodDelete"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmodAllow"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmodDisallow"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmodAllowCommand"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmodDisallowCommand"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmodToggle"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmodList"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmodResize"));
				player.sendMessage(Colors.Yellow+Cuboids2.msg.help.get("cmodPrio"));
				return true;
			}
		}
		
		/*
		 * **************************************************
		 * SAVE
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/csave")) {
			  if (split.length >= 2) {
			    Cuboids2.cuboids.saveCuboid(player, split[1]);
			    if(Cuboids2.cfg.isVerbose()) {
					Cuboids2.eventLog.logMessage(player.getName()+" used /csave","INFO");
				}
			    return true;
			  }
			  player.sendMessage(Colors.Yellow + Cuboids2.msg.help.get("csave"));
			  return true;
		}
		
		/*
		 * **************************************************
		 * SAVE ALL
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/csave-all") && player.isAdmin()) {
			  if(Cuboids2.treeHandler != null) {
				  Cuboids2.treeHandler.save(false, true);
				  player.sendMessage(Colors.LightGreen + Cuboids2.msg.messages.get("cuboidSaved"));
				  if(Cuboids2.cfg.isVerbose()) {
						Cuboids2.eventLog.logMessage(player.getName()+" used /csave-all","INFO");
					}
				  return true;
			  }
			  else {
				  player.sendMessage(Colors.Rose + Cuboids2.msg.messages.get("cuboidNotSaved")+" - tree isn't initialized yet :O");  
			  }
		}
		
		/*
		 * **************************************************
		 * LOAD FROM CURRENT DATASOURCE
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/cload")) {
			  if (split.length >= 2) {
			    Cuboids2.cuboids.loadCuboid(player, split[1]);
			    if(Cuboids2.cfg.isVerbose()) {
					Cuboids2.eventLog.logMessage(player.getName()+" used /cload","INFO");
				}
			    return true;
			  }
			
			  player.sendMessage(Colors.Yellow + Cuboids2.msg.help.get("cload"));
			  return true;
		}
		
		/*
		 * **************************************************
		 * LOAD FROM SPECIFIED DATASOURCE
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/cloadfrom")) {
			  if (split.length >= 2) {
				  if(split[1].equalsIgnoreCase("mysql")) {
					  Cuboids2.loadFromDatabase();
				  }
				  else if(split[1].equalsIgnoreCase("flatfile")) {
					  Cuboids2.loadFromFlatFile();
				  }
				  else {
					  player.sendMessage(Colors.Yellow + Cuboids2.msg.help.get("cloadfrom"));
				  }
				  if(Cuboids2.cfg.isVerbose()) {
						Cuboids2.eventLog.logMessage(player.getName()+" used /cloadfrom "+split[1],"INFO");
				  }
			    return true;
			  }
			  player.sendMessage(Colors.Yellow + Cuboids2.msg.help.get("cloadfrom"));
			  return true;
		}
		
		/*
		 * **************************************************
		 * PROTECT SHORTCUTS
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/protect")) {
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/ccreate")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
				if(!player.canUseCommand("/cprotection")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
			}
			if(split.length < 3) {
				player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("missingArguments"));
				player.sendMessage(Colors.Rose+Cuboids2.msg.help.get("protect"));
				return true;
			}
			if(Cuboids2.sel.get(player.getName()).isComplete()) {
				StringBuilder playerlist = new StringBuilder();
				String cubeName = "";
				for(int i=1;i<split.length-1;i++) {
					if(i > 1) {
						playerlist.append(" ");
					}
					playerlist.append(split[i]);
				}
				cubeName = split[split.length-1];

				CuboidE cube = Cuboids2.sel.get(player.getName()).toCuboid( playerlist.toString(), 
																			cubeName, 
																			Cuboids2.cfg.getDefaultCuboidSettings(player),
																			player.getWorld().getType().name());
			
				cube.setProtection(true);

				if(Cuboids2.cuboids.addCuboid(cube)) {
					if(Cuboids2.cfg.isVerbose()) {
							Cuboids2.eventLog.logMessage(player.getName()+" used /protect","INFO");
					}
					player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidAdded"));
					return true;
				}
				else {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("cuboidExists"));
					return true;
				}
				
			}
		}
		/*
		 * **************************************************
		 * HIGHPROTECT
		 * **************************************************
		 */
		if(split[0].equalsIgnoreCase("/highprotect")) {
			if(!player.canUseCommand("/cIgnoreRestrictions")) {
				if(!player.canUseCommand("/ccreate")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
				if(!player.canUseCommand("/cprotection")) {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("permissionDenied"));
					return true;
				}
			}
			if(split.length > 2) { 
				if(Cuboids2.sel.get(player.getName()).isComplete()) {
					String cubeName = split[split.length-1];
					StringBuilder playerName= new StringBuilder();
					Cuboids2.sel.get(player.getName()).expandVert();
					for(int i = 1; i < split.length-1;i++) {
						if(i > 1) {
							playerName.append(" ");
						}
						playerName.append(split[i]);
					}
					CuboidE cube = Cuboids2.sel.get(player.getName()).toCuboid( playerName.toString(), 
																				cubeName, 
																				Cuboids2.cfg.getDefaultCuboidSettings(player),
																				player.getWorld().getType().name());
					cube.setProtection(true);
					if(Cuboids2.cuboids.addCuboid(cube)) {
						if(Cuboids2.cfg.isVerbose()) {
							Cuboids2.eventLog.logMessage(player.getName()+" used /highprotect","INFO");
						}
						player.sendMessage(Colors.LightGreen+Cuboids2.msg.messages.get("cuboidAdded"));
						return true;
					}
					else {
						player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("cuboidNotAdded"));
						return true;
					}
				}
				else {
					player.sendMessage(Colors.Rose+Cuboids2.msg.messages.get("selectionIncomplete"));
					return true;
				}
			}
			else {
				player.sendMessage(Colors.Rose+Cuboids2.msg.help.get("highprotect"));
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Convert a string to item ID depending on data source.
	 * @param item
	 * @return int item ID -1 if somethign was wrong.
	 */
	private int convertItem(String item) {
		int i = 0;
		try {
			//if that fails it must be a name
			i = Integer.parseInt(item);
		}
		catch(NumberFormatException e) {
			int x = etc.getDataSource().getItem(item);
			if(x == 0 && item.equalsIgnoreCase("air")) {
				return x;
			}
			else if(x > 0) {
				return x;
			}
			else {
				return -1;
			}
		}
		if(etc.getDataSource().getItem(i).equalsIgnoreCase(String.valueOf(i))) {
			return -1;
		}
		return i;
	}
	
	/**
	 * Convert a string to a data value
	 * @param data
	 * @return
	 */
	private byte convertData(String data) {
		byte i = 0;
		try {
			//if that fails it must be a name
			i = (byte)Integer.parseInt(data);
		}
		catch(NumberFormatException e) {
			i = -1;
		}
		return i;
	}
	
	/**
	 * Convert a string to a type value
	 * @param data
	 * @return
	 */
	private short convertType(String data) {
		short i = 0;
		try {
			//if that fails it must be a name
			i = (short)Integer.parseInt(data);
		}
		catch(NumberFormatException e) {
			short x = (short)etc.getDataSource().getItem(data);
			if(x == 0 && data.equalsIgnoreCase("air")) {
				return x;
			}
			else if(x > 0) {
				return x;
			}
			else {
				return -1;
			}
		}
		if(etc.getDataSource().getItem(i).equalsIgnoreCase(String.valueOf(i))) {
			return -1;
		}
		return i;
	}
	
	private int parseInt(String i) {
		int out=-1;
		try {
			out = Integer.parseInt(i);
		}
		catch(NumberFormatException e) {
			Cuboids2.eventLog.logMessage("Cuboids2: Failed to parse integer", "WARNING");
		}
		return out;
	}

}
