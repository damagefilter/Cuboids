package net.playblack.cuboids.blockoperators;

import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.gameinterface.CPlayer;

public interface IShapeGen {
    /**
     * Execute the block operation and also do a new undo step
     *
     * @param simulate
     * @throws BlockEditLimitExceededException
     *
     * @throws SelectionIncompleteException
     */
    public boolean execute(CPlayer player, boolean newHistory)
            throws BlockEditLimitExceededException,
            SelectionIncompleteException;
}
