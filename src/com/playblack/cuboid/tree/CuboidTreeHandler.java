package com.playblack.cuboid.tree;

import java.util.ArrayList;
import java.util.Timer;

import com.playblack.EventLogger;
import com.playblack.ToolBox;
import com.playblack.cuboid.CuboidE;
import com.playblack.cuboid.CuboidMessages;
import com.playblack.cuboid.CuboidSaveThread;
import com.playblack.datasource.BaseData;
import com.playblack.vector.Vector;

/**
 * Handles a list of multiple trees which are base nodes or "the big CuboidEs"
 * When this all starts to work there must be a big big party.
 * TODO: Make a party
 * @author Christoph Ksoll
 *
 */
public class CuboidTreeHandler {
	/**
	 * A list of all existing cuboid trees
	 */
	ArrayList<CuboidTree> treeList = new ArrayList<CuboidTree>(0);
	
	CuboidMessages messages = new CuboidMessages();
	Timer saveTimer = new Timer();
	public EventLogger log;
	BaseData ds;
	private ArrayList<CuboidNode> nodeList = new ArrayList<CuboidNode>();
	//private Object lock = new Object();
	
	public CuboidTreeHandler(EventLogger log, BaseData ds) {
		this.log = log;
		this.ds = ds;
	}
	public void scheduleSave(long delay) {
		log.logMessage("Cuboids2: Scheduling next save in "+delay+" seconds.", "INFO");
		saveTimer.schedule(new CuboidSaveThread(delay, this), delay);
	}
	
	public void cancelSaves() {
		saveTimer.cancel();
	}
	
	/*
	 * ***********************************************************************************
	 * CUBOID MANAGEMENT METHODS FOR ADDING/MOVING/DELETING
	 * ***********************************************************************************
	 */
	
	/**
	 * Add a new tree, do this only if the given CuboidE has no parent.
	 * @param root
	 */
	public void addTree(CuboidE root) {
		//CuboidNode rootNode = new CuboidNode(root);
		treeList.add(new CuboidTree(new CuboidNode(root), log));
	}
	
	public void addTree(CuboidNode root) {
		treeList.add(new CuboidTree(root, log));
	}
	/**
	 * This crunches through all cuboid areas and clears the parent
	 * relations if childs are not 100% inside their parent.
	 * This is legacy support!
	 */
	public void cleanParentRelations() {
		CuboidNode parent;
		for(CuboidTree tree : treeList) {
			for(CuboidNode node : tree.toList()) {
				//parent = null;
				parent = getCuboidByName(node.getCuboid().getParent(), node.getCuboid().getWorld());
				if(parent != null) {
					//Check if the child is truley completely inside its parent
					if (!node.getCuboid().cuboidIsWithin(parent.getCuboid().getMinorPoint(), 
														 parent.getCuboid().getMajorPoint(), 
														 true, 
														 parent.getCuboid().getWorld())) {
						//If not, remove the parent and set to null
						node.getCuboid().setParent(ToolBox.stringToNull("null"));
						node.getCuboid().hasChanged=true;
						updateCuboidNode(node.getCuboid());
						
					}
				}
				else {
					getPossibleParent(node.getCuboid());
				}
			}
		}
	}
	
	/**
	 * This'll try to find the best parent for a given cuboid, if there can be one
	 * Returns null if no suitable parent was found
	 * @param cube
	 * @return
	 */
	public CuboidNode getPossibleParent(CuboidE cube) {
		//log.logMessage("Going to find a suitable parent for "+cube.getName(), "INFO");
		ArrayList<CuboidNode> list = new ArrayList<CuboidNode>(0);
		for(CuboidTree tree : treeList) {
			if(tree.getWorld().equalsIgnoreCase(cube.getWorld())) {
				for(CuboidNode node : tree.toList()) {
					if(cube.cuboidIsWithin(node.getCuboid().getMajorPoint(), node.getCuboid().getMinorPoint(), true, node.getCuboid().getWorld())) {
						//log.logMessage("Adding possible parent to list!", "INFO");
						list.add(node);
					}
				}
			}
		}
		if(list.size() > 0) {
			//log.logMessage("Finding Parent from List of possibilities!", "INFO");
			CuboidNode min=null;
			for(int e = 0; e<list.size();e++) {
				//log.logMessage("Running through list", "INFO");
				if(min == null) {
					min = list.get(e);
				}
				if(min.getCuboid().getPriority() <= list.get(e).getCuboid().getPriority()) {
					//log.logMessage("Something happening", "INFO");
					if(!list.get(e).getCuboid().getName().equalsIgnoreCase(cube.getName())) {
						//log.logMessage("Changing min!!", "INFO");
						min = list.get(e);
					}
				}
			}
			//log.logMessage("Returning result!", "INFO");
			return min;
		}
		//log.logMessage("Returning null!", "INFO");
		return null;
	}
	/**
	 * Remove a tree from the list. If it isn't empty, this will also remove every child.
	 * This will purge the tree and remove its files from disk
	 * @param name
	 */
	private void deleteTree(String name)
	  {
	    for (int i = 0; i < this.treeList.size(); i++)
	      if (this.treeList.get(i).getName().equalsIgnoreCase(name)) {
	        for (CuboidNode node : this.treeList.get(i).toList()) {
	          removeNodeFile(node);
	        }
	        removeNodeFile(((CuboidTree)this.treeList.get(i)).getRoot());
	        this.treeList.remove(i);
	      }
	  }
	/**
	 * Turn CuboidE to a CuboidNode
	 * @param cube
	 * @return
	 */
	public CuboidNode createNode(CuboidE cube) {
		return new CuboidNode(cube);
	}
	/**
	 * Extend the tree by adding a new node or create a new tree if the given ndoe has no parents
	 * @param cube
	 */
	private void addNode(CuboidNode cube) {
		if(cube.getCuboid().getParent() == null) {
			addTree(cube);
			saveSingle(cube);
		}
		else {
			for(CuboidTree tree : treeList) {
				
				if(tree.getWorld().equalsIgnoreCase(cube.getCuboid().getWorld())) {
					
					for(CuboidNode node: tree.toList()) {
						
						if(node.getCuboid().getName().equalsIgnoreCase(cube.getCuboid().getParent())) {
							
							node.addChild(cube);
							cube.getCuboid().hasChanged = true;
							node.getCuboid().hasChanged = true;
							saveSingle(cube);
							saveSingle(node);
						}
					}
				}	
			}
		}
	}
	
	/**
	 * Scan for nodes inside a node and parent them if they don't have a parent yet
	 * @param node
	 * @return
	 */
	public void reverseFindChildNodes(CuboidNode node) {
		CuboidE c = node.getCuboid();
		ArrayList<CuboidNode> childs = new ArrayList<CuboidNode>();
		System.out.println("Checking for possible childs in "+c.getName());
		for(CuboidTree tree : treeList) {
			if(tree.getWorld().equalsIgnoreCase(node.getCuboid().getWorld())) {
				for(CuboidNode n : tree.toList()) {
					//if(n.getCuboid().isWithin(c.getFirstPoint()) && n.getCuboid().isWithin(c.getSecondPoint())) {
					if(n.getCuboid().cuboidIsWithin(c.getMajorPoint(), c.getMinorPoint(), true)) {
						System.out.println(n.getCuboid().getName()+" is within "+c.getName());
						if(n.getCuboid().getParent() == null) {
							System.out.println("We have no parent yet!");
							if(n.getCuboid().getName().equalsIgnoreCase(c.getName())) {
								System.out.println("Same name, stupid");
								continue;
							}
							n.getCuboid().setParent(c.getName());
							n.getCuboid().hasChanged=true;
							childs.add(n);
						}
					}
					else {
						System.out.println(n.getCuboid().getName()+" is not within "+c.getName());
					}
				}
			}
		}
		for(CuboidNode n : childs) {
			updateCuboidNode(n.getCuboid());
		}
		save(false, true);
	}
	
	/**
	 * Take a starting Cuboid and recursively travel the tree upwards by checking for parents,
	 * until it hits the root. All passed nodes will be saved and returned in an arraylist.
	 * 
	 * @param base The cuboid to start with
	 * @param list the list you want to be filled.
	 * @return
	 */
	public ArrayList<CuboidE> reverseSeekParents(CuboidE base, ArrayList<CuboidE> list) {
		if(base.getParent() != null) {
			CuboidE parent = getCuboidByName(base.getParent(), base.getWorld()).getCuboid();			
			list.add(parent);
			reverseSeekParents(parent, list);
		}
		return list;
	}
	/**
	 * Removes the file corresponding to the given node object from disk
	 * @param node
	 */
	private void removeNodeFile(CuboidNode node) {
//		File file = new File("plugins/cuboids2/cuboids/"+node.getCuboid().getWorld()+"_"+node.getCuboid().getName()+".node");
//		if(file.exists()) {
//			file.delete();
//		}
		ds.removeNode(node);
	}
	
	/**
	 * Extend the tree by adding a new node, given a CuboidE area.
	 * This'll force a save to file everytime it is called. addNode will NOT force a save.
	 * @param cube
	 * @return True if Cuboid has been savd, false otherwise-
	 */
	public boolean addCuboid(CuboidE cube) {
		if(cuboidExists(cube.getName(), cube.getWorld())) {
			log.logMessage("Cuboids2: Cuboid already exists :O", "INFO");
			return false;
		}
		CuboidNode nodee = createNode(cube);
		if(cube.getParent() == null) {
			cube.hasChanged = true;
			addTree(cube);
		}	
		else {
			for(CuboidTree tree : treeList) {				
				if(tree.getWorld().equalsIgnoreCase(cube.getWorld())) {					
					for(CuboidNode node: tree.toList()) {					
						if(node.getCuboid().getName().equalsIgnoreCase(cube.getParent())) {
							cube.hasChanged = true;
							node.addChild(nodee);
						}
					}
				}	
			}
		}
		reverseFindChildNodes(nodee);
		//save(true);
		return true;
	}
	
	/**
	 * Remove a Cuboid from a tree or if it has no parent this will remove the tree with the given CuboidE as root.
	 * @param cube
	 * @param force Force removal	 
	 * @return messages
	 */
	public String removeCuboid(CuboidE cube, boolean force)
	  {
	    for (CuboidTree tree : this.treeList) {
	      for (CuboidNode node : tree.toList()) {
	        if (node.getCuboid().getName().equalsIgnoreCase(cube.getName())) {
	        	//Found proper cuboid
	          if (node.getChilds().size() <= 0) {
	        	  //has no childs
	            if (node.getCuboid().getParent() == null)
	            {
	            	//cuboid is a tree
	              deleteTree(node.getCuboid().getName());
	              return "REMOVED";
	            }

	            CuboidNode parent = getCuboidByName(node.getCuboid().getParent(), node.getCuboid().getWorld());
	            if (parent != null) {
	            	//Cuboid as a parent so we take it and remove the cuboid from the parents child list
	              parent.getChilds().remove(node);
	              removeNodeFile(node);
	              return "REMOVED";
	            }

	            return "NOT_REMOVED";
	          }
	          else {
	        	  if (force) {
		        	  //We have childs
		            if (node.getCuboid().getParent() == null)
		            {
		            	//we have no parent
		            	//When forcing just remove the whole tree including child nodes
		            	//This also removes the files form disk
		              deleteTree(node.getCuboid().getName());
		              return "REMOVED";
		            }

		            CuboidNode parent = getCuboidByName(node.getCuboid().getParent(), node.getCuboid().getWorld());
		            if (parent != null) {
		            	//We have a parent
		            	for(CuboidNode childNode : node.getChilds()) {
		            		//de-parent child nodes
		            		childNode.getCuboid().setParent(ToolBox.stringToNull(parent.getCuboid().getName()));
		            		childNode.getCuboid().hasChanged=true;
		            		addTree(childNode);
				            saveSingle(childNode);
		            	}
		              parent.getChilds().remove(node);

		              removeNodeFile(node);
		              return "REMOVED";
		            }

		            return "NOT_REMOVED";
		          }
		          if (node.getCuboid().getParent() == null)
		          {
		        	  CuboidNode parentNode;
		            for (CuboidNode child : node.getChilds()) {
		              child.getCuboid().setParent(ToolBox.stringToNull("null"));
		              addTree(child);
		              saveSingle(child);
		              parentNode = getCuboidByName(child.getCuboid().getParent(), 
		                child.getCuboid().getWorld());
		              
		              if (parentNode != null) {
		                parentNode.getChilds().remove(child);
		                node.getChilds().remove(child);
		                removeNodeFile(node);
		              }
		            }
		            deleteTree(node.getCuboid().getName());
		            return "REMOVED";
		          }
		          else {
		        	  CuboidNode parent = getCuboidByName(node.getCuboid().getParent(), node.getCuboid().getWorld());
			          for (CuboidNode child : node.getChilds()) {
			        	  //Make childs root nodes
			            //child.getCuboid().setParent(ToolBox.stringToNull("null"));
			            child.getCuboid().setParent(ToolBox.stringToNull(parent.getCuboid().getName()));
			            child.getCuboid().hasChanged = true;
			            addTree(child);
			            saveSingle(child);
			          }
			          
			          if (parent != null) {
			              parent.getChilds().remove(node);
			              removeNodeFile(node);
			          }
			          else {
			        	  deleteTree(node.getCuboid().getName());
			          }
			          return "REMOVED";
		          }  
	          }
	        }

	      }

	    }

	    return "NOT_REMOVED_NOT_FOUND";
	  }
	
	/**
	 * Check if a cuboid area is saved in one of the data trees that are valid for the given world
	 * @param cube
	 * @param world
	 * @return True if exists, false otherwise
	 */
	public boolean cuboidExists(String cube, String world) {
		for(CuboidTree tree : treeList) {
			if(tree.getWorld().equalsIgnoreCase(world)) {
					for(CuboidNode node : tree.toList()) {
						if(node.getCuboid().getName().equalsIgnoreCase(cube)) {
							return true;
						}
					}
			}
			
		}
		return false;
	}
	
	/**
	 * Check if the given cuboid name is a tree
	 * @param cube
	 * @param world
	 * @return true is is tree, false otherwise
	 */
	public boolean cuboidIsTree(String cube, String world) {
		for(CuboidTree tree : treeList) {
			if(tree.getWorld().equalsIgnoreCase(world)) {
				if(tree.getName().equalsIgnoreCase(cube)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Update the CuboidNode with the given CuboidE inside. That is:<br>
	 * Move it from child to another parent or make it a new tree if there is no parent,
	 * or if parent is now set remove the tree and put it into the new parent.
	 * TODO: This was taken from a GD-GUI recovery action, needs refactoring much
	 * @param cube
	 */
	public boolean updateCuboidNode(CuboidE cube)
	  {
	    for (CuboidTree tree : this.treeList) {
	      if (tree.getWorld().equalsIgnoreCase(cube.getWorld())) {
	        for (CuboidNode node : tree.toList()) {
	        	//log.logMessage("Unfolding Tree in updateCuboidNode - looking for "+cube.getName(), "INFO");
	          if (!node.getCuboid().getName().equalsIgnoreCase(cube.getName())) {
	        	  //no match, continue with next set
	            continue;
	          }
	          
	          if ((cube.getParent() != null) && (node.getCuboid().getParent().equalsIgnoreCase(cube.getParent())))
	          {
					//log.logMessage("Parent NOT null AND node parent is same!", "INFO");
					if ((node.getCuboid().getParent() != null) && (cuboidIsTree(cube.getName(), cube.getWorld())))
					{
						 //log.logMessage("Old Cuboid is tree! Moving ...", "INFO");
						 node.getCuboid().hasChanged = true;
						 deleteTree(node.getCuboid().getName());
						 addNode(node);
						 return true;
					}
					if (cuboidExists(cube.getParent(), cube.getWorld())) {
						//log.logMessage("Parent Exists, setting data!", "INFO");
						node.getCuboid().hasChanged = true;
					    node.setCuboid(cube);
					
					  return true;
					}
					
					return false;
	          }

	          if ((cube.getParent() != null) && (!node.getCuboid().getParent().equalsIgnoreCase(cube.getParent())))
	          {
	        	 // log.logMessage("Parent Node has changed!!", "INFO");
					if (cuboidExists(cube.getParent(), cube.getWorld())) {
						//log.logMessage("Parent Node has changed!!", "INFO");
					  CuboidNode newNode = createNode(cube);
					  newNode.setChilds(node.getChilds());
					
					  removeCuboid(node.getCuboid(), true);
					  removeNodeFile(node);
					  newNode.getCuboid().hasChanged = true;
					  addNode(newNode);
					
					  return true;
					}
					
					return false;
	          }

	        //If we're here the cuboid has no parent
	          if (cube.getParent() == null && node.getCuboid().getParent() == null)
	          {
					//log.logMessage("Parent is null AND node Parent is null (no structural changes)", "INFO");
	        	  //Nothing severe changed, only update
	            cube.hasChanged = true;
	            node.setCuboid(cube);
	            return true;
	          }
	          if (cube.getParent() == null && node.getCuboid().getParent() != null) {
	        	 // log.logMessage("Parent is null AND node Parent is NOT null (converting to tree)", "INFO");
	        	  CuboidNode newNode = createNode(cube);
		          newNode.setChilds(node.getChilds());
		          removeCuboid(node.getCuboid(), true);
		          
		          newNode.getCuboid().hasChanged = true;
		          addTree(newNode);
		          return true;
	          }
	        }

	      }

	    }
	    return false;
	  }
	
	/*
	 * ***********************************************************************************
	 * CUBOID QUERYING METHODS TO RETRIEVE CUBOID LISTS AND ALL THAT
	 * ***********************************************************************************
	 */
	
//	/**
//	 * Invokes the removePlayerWithin method for the given player name on every cuboid in tree.
//	 * @param player
//	 */
//	public void removePlayerFromAllNodes(String player) {
//		for(CuboidTree tree : treeList) {
//			for(CuboidNode node : tree.toList()) {
//				node.getCuboid().removePlayerWithin(player);
//			}
//		}
//	}
	
	/**
	 * Create a list of CuboidNodes that contain the given Vector in the given world
	 * @param v
	 * @param world
	 * @return
	 */
	public ArrayList<CuboidE> getCuboidsContaining(Vector v, String world) {
		ArrayList<CuboidE> list = new ArrayList<CuboidE>(0);
		if(v == null) {
			return list;
		}
		for(CuboidTree tree : treeList) {
			if(tree.getRoot().getCuboid().isWithin(v) && tree.getWorld().equalsIgnoreCase(world)) {
				for(CuboidNode node : tree.toList()) {
					if(node.getCuboid().isWithin(v)) {
						list.add(node.getCuboid());
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * This will return the cuboid with the highest priority at the Vector given
	 * @return CuboidE
	 */
	public CuboidNode getActiveCuboid(Vector v, String world) {
		nodeList.clear();
		if(v == null) {
			return null;
		}
		for(CuboidTree tree : treeList) {
			if(!tree.getRoot().getCuboid().isWithin(v)) {
				continue;
			}
			if(tree.getWorld().equalsIgnoreCase(world)) {
				for(CuboidNode node : tree.toList()) {
					if(node.getCuboid().isWithin(v)) {
						nodeList.add(node);
					}
				}
			}
		}
		CuboidNode max=null;
		for(CuboidNode node : nodeList) {
			if(max == null) {
				max = node;
			}
			else if(max.getCuboid().getPriority() < node.getCuboid().getPriority()) {
				max = node;
			}
			else if(max.getCuboid().getPriority() == node.getCuboid().getPriority()) {
				if(max.getCuboid().getSize() > node.getCuboid().getSize()) {
					max = node;
				}
			}
		}

		return max;
	}
	
	/**
	 * Return the CuboidNode with the given name or null if not existent
	 * @param name
	 * @param world
	 * @return CuboidNode or null if not existent
	 */
	public CuboidNode getCuboidByName(String name, String world) {
		for(CuboidTree tree : treeList) {
			if(tree.getWorld().equalsIgnoreCase(world)) {
				for(CuboidNode node : tree.toList()) {
					if(node.getCuboid().getName().equalsIgnoreCase(name)) {
						return node;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Get a list of all cuboids in the given world
	 * @param world
	 * @return CuboidNode List or null if there were no cuboids
	 */
	public ArrayList<CuboidNode> getAllInWorld(String world) {
		ArrayList<CuboidNode> list = new ArrayList<CuboidNode>(0);
		for(CuboidTree tree : treeList) {
			if(tree.getWorld().equalsIgnoreCase(world)) {
				for(CuboidNode node : tree.toList()) {
					list.add(node);
				}
			}
		}
		
		if(list.size() > 0) {
			return list;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Get the parent of a given node returns the given node if there is no parent
	 * @param child
	 * @return
	 */
	public CuboidNode getParent(CuboidE child) {
		if(child.getParent() != null) {
			for(CuboidTree tree : treeList) {
				if(tree.getWorld().equalsIgnoreCase(child.getWorld())) {
					for(CuboidNode parent : tree.toList()) {
						if(parent.getCuboid().getName().equalsIgnoreCase(child.getName())) {
							return parent;
						}
					}
				}
			}
		}
		else {
			return createNode(child);
		}
		return createNode(child);
	}
	
	/**
	 * Let the system sort cuboids to their parents and assign
	 * proper relations + priorities, this is useful for migrating
	 * CuboidD areas, that have no parent informations, to the cuboidTree system.
	 */
	public void autoSortCuboidAreas() {
		ArrayList<String> visited = new ArrayList<String>();
		ArrayList<CuboidTree> workerList = new ArrayList<CuboidTree>(0);
		for(CuboidTree tree : treeList) {
			workerList.add(tree);
		}
		for(CuboidTree tree : workerList) {
			if(!visited.contains(tree.getName())) {
				CuboidE cube = tree.getRoot().getCuboid();
				CuboidNode parent = getPossibleParent(cube);
				//log.logMessage("VISITING "+cube.getName(), "INFO");
				if(parent != null) {
					//log.logMessage(cube.getName()+" HAS PARENT", "INFO");
					if(parent.getCuboid().getName().equalsIgnoreCase(cube.getName())) {
						//log.logMessage(cube.getName()+" IS FALSE ALARM PARENT", "INFO");
						continue;
					}
					//log.logMessage(" HAS PARENT", "INFO");
					//log.logMessage("SETTING PARENT AND PRIO FOR "+cube.getName(), "INFO");
					cube.setParent(parent.getCuboid().getName());
					if(cube.getPriority() <= parent.getCuboid().getPriority()) {
						cube.setPriority(parent.getCuboid().getPriority()+1);
					}
					cube.hasChanged = true;
					//log.logMessage("UPDATING "+cube.getName(), "INFO");
					updateCuboidNode(cube);
					//log.logMessage("UPDATE DONE", "INFO");
				}
				visited.add(tree.getName());
			}
		}
		treeList = workerList;
	}
	
	/*
	 * ***********************************************************************************
	 * CUBOID SERIALIZATION LAYER READ/WRITE SHIT AND DO STUFF TO TREE LIST
	 * ***********************************************************************************
	 */

	public ArrayList<CuboidTree> getTreeList() {
		return treeList;
	}
	/**
	 * 
	 * @param node
	 */
	public void saveSingle(CuboidNode node) {
		ds.saveCuboid(node);
	}
	
	/**
	 * 
	 * @param node
	 */
	public boolean saveSingle(String name, String world) {		
		ds.saveCuboid(getCuboidByName(name, world));
		return true;
	}
	
	
	/**
	 * Serialize the data trees and sort stuff out
	 */
	public void save(boolean silent, boolean force) {
		ds.saveAll(treeList, silent, force);
	}
	
	
	public boolean loadSingle(String name, String world) {
		ds.loadCuboid(this, name, world);
		return true;
	}
	
	/**
	 * Deserialize and load data trees into treeList
	 */
	public void load() {
		ds.loadAll(this);
	}
}
