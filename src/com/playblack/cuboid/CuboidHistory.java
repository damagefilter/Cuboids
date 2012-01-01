package com.playblack.cuboid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.playblack.blocks.BaseBlock;
import com.playblack.blocks.ChestBlock;
import com.playblack.blocks.SignBlock;
import com.playblack.blocks.WorldBlock;
import com.playblack.vector.Vector;

/**
 * This stores a list of player selections per player, creating a recoverable history
 * @author Chris
 *
 */
public class CuboidHistory {
	private int maxUndo;
	private HashMap<String, HistoryPointer> history = new HashMap<String, HistoryPointer>(0);
	public CuboidHistory(int maxUndo) {
		this.maxUndo = maxUndo;
	}
	
	/* **************************************************************************
	 * HISTORY BRIDGE
	 * **************************************************************************/
	
	/**
	 * Undo a bit of history.<br>
	 * If this returns null, there id nothing to undo.
	 * @param player
	 * @return
	 */
	public CuboidSelection historyUndo(String player) {
		if(history.get(player) == null) {
			return null;
		}
		else {
			return history.get(player).undo();
		}
	}
	
	/**
	 * Remember the block and the position
	 * @param player
	 * @param v
	 * @param block
	 */
	public void remember(String player, Vector v, BaseBlock block) {
		if(history.get(player) == null) {
			history.put(player, new HistoryPointer(maxUndo));
		}
		history.get(player).getCurrent().setBlockAt(v, block);
	}
	
	/**
	 * Create a new step in history.<br>
	 * Literally, make a new CuboidSelection in the History Pointer to store blocks in
	 */
	public void makeHistory(String player) {
		if(history.get(player) == null) {
			history.put(player, new HistoryPointer(maxUndo));
		}
		history.get(player).addNext();
	}
	
	public int getCurrentIndex(String player) {
		return history.get(player).getCurrentIndex();
	}
	
	public CuboidSelection getAtIndex(String player, int index) {
		return history.get(player).getAtIndex(index);
	}
	
	/* **************************************************************************
	 * CLIPBOARD BRIDGE
	 * **************************************************************************/
	
	/**
	 * Copy the given selection into players clipboard
	 * @param player
	 * @param selection
	 */
	public void copyToClipboard(String player, CuboidSelection selection) {
		if(history.get(player) == null) {
			history.put(player, new HistoryPointer(maxUndo));
		}
		history.get(player).copyToClipboard(selection);
	}
	
	/**
	 * Clear the clipboard data completely
	 * @param player
	 */
	public void clearClipboard(String player) {
		history.get(player).emptyClipboard();
	}
	
	public CuboidSelection getClipboard(String player) {
		return history.get(player).getClipboard();
	}
	
	private class HistoryPointer {
		private ArrayList<CuboidSelection> history = new ArrayList<CuboidSelection>();
		private CuboidSelection clipBoard = new CuboidSelection();
		private int currentIndex=-1;
		private int maxIndex=10;
		public HistoryPointer(int maxIndex) {
			this.maxIndex = maxIndex;
		}
		
		/**
		 * Copy a CuboidSelection here for temporary storage.
		 * @param clip
		 */
		public void copyToClipboard(CuboidSelection clip) {
			clipBoard.clearAll();
			for(Iterator<Vector> data = clip.getBlockList().keySet().iterator(); data.hasNext();) {
				Vector key = (Vector) data.next();
				BaseBlock b = clip.getBlockAt(key);
				if(b instanceof WorldBlock) {
					clipBoard.setBlockAt(key, new WorldBlock((Byte)b.getData(), (Short)b.getType()));
				}
				else if(b instanceof ChestBlock) {
					clipBoard.setBlockAt(key, new ChestBlock((ChestBlock)b));
				}
				else if(b instanceof SignBlock) {
					clipBoard.setBlockAt(key, new SignBlock(((SignBlock) b).getSignTextArray()));
				}
				//clipBoard.setBlockAt(key, new BaseBlock(b.getType(), b.getData()));
			}
			clipBoard.setOrigin(clip.getOrigin());
			clipBoard.setOffset(clip.getOffset());
			//clipBoard = clip;
		}
		
		public void emptyClipboard() {
			clipBoard.clearAll();
		}
		
		public CuboidSelection getClipboard() {
			return clipBoard;
		}
		
		/**
		 * Get the history at any given point of time
		 * @param index
		 * @return
		 */
		public CuboidSelection getAtIndex(int index) {
			if(index <= history.size()) {
				return history.get(index);
			}
			return null;
		}
		
		/**
		 * Add the next history step (the future or a new set of blocks)<br>
		 * This will increase the current index
		 * @return
		 */
		public CuboidSelection addNext() {
			if(currentIndex+1 > maxIndex) {
				currentIndex = 0;
				//return null;
			}
			else {
				currentIndex++;
			}
			if(currentIndex == -1) {
				currentIndex=0;
			}
			if(currentIndex >= history.size() || history.isEmpty()) {
				history.add(new CuboidSelection());
			}
			return history.get(currentIndex);
		}
		
		/**
		 * Get the current history step
		 * @return
		 */
		public CuboidSelection getCurrent() {
			if(currentIndex <= history.size()) {
				return history.get(currentIndex);
			}
			else {
				addNext();
				return history.get(currentIndex);
			}
		}
		
		/**
		 * Go back in time one step. Literally remove the last kown element and return it
		 * @return
		 */
		public CuboidSelection undo() {
			if(currentIndex >=0 && currentIndex <= history.size()) {
				currentIndex--;
				return history.remove(currentIndex+1);
			}
			else {
				currentIndex=0;
				return null;
			}
		}
		
		/**
		 * Get the current index (or amount of undos)
		 * @return
		 */
		public int getCurrentIndex() {
			return currentIndex;
		}
	}
}
