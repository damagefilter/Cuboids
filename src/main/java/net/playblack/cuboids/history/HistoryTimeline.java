package net.playblack.cuboids.history;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.selections.CuboidSelection;

import java.util.LinkedList;

/**
 * Every player has a history timeline where blocks are stored that have been
 * modified into a linked list.
 *
 * @author Chris
 */
public class HistoryTimeline {
    private int maxListSize = Config.get().getUndoSteps();
    private int pointer = 0;
    private LinkedList<HistoryObject> history;

    /**
     * Remove any future history has there shall be none!
     */
    private void purgeHistory() {
        while (pointer < history.size()) {
            history.remove(pointer);
        }
    }

    /**
     * Trim history by removing older elements
     */
    private void trimHistory() {
        while (history.size() > maxListSize) {
            history.remove(0);
        }
    }

    /**
     * Create a new history timeline
     */
    public HistoryTimeline() {
        history = new LinkedList<HistoryObject>();
    }

    /**
     * Clear the history, that will also free up some memory with the next GC
     * cycle
     */
    public void clear() {
        history.clear();
        pointer = 0;
    }

    /**
     * Remember a bit of history
     *
     * @param rem
     */
    public void remember(HistoryObject rem) {
        if ((rem.getModifiedBlocks().size() == 0)
                || (!Config.get().isAllowUndo())) {
            return;
        }
        purgeHistory();
        history.add(rem);
        trimHistory();
        pointer = history.size();

    }

    /**
     * Go back one step in history and return the historyobject created back
     * then
     *
     * @return CuboidSelection to use in a world
     */
    public CuboidSelection undo() {
        pointer--;
        if (pointer >= 0) {
            return new CuboidSelection(history.get(pointer).getBlocksBefore());
        }
        else {
            pointer = 0;
            return null;
        }
    }

    /**
     * Redo an undo step.
     *
     * @return CuboidSelection to use in a world
     */
    public CuboidSelection redo() {
        if (pointer < history.size()) {
            CuboidSelection ret = new CuboidSelection(history.get(pointer)
                    .getModifiedBlocks());
            pointer++;
            return ret;
        }
        return null;
    }
}
