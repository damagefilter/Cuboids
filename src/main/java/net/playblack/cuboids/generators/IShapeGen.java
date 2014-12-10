package net.playblack.cuboids.generators;

import net.canarymod.api.entity.living.humanoid.Player;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;

public interface IShapeGen {
    /**
     * Execute the block operation and also do a new undo step
     *
     * @throws BlockEditLimitExceededException
     * @throws SelectionIncompleteException
     */
    public boolean execute(Player player, boolean newHistory) throws BlockEditLimitExceededException, SelectionIncompleteException;
}
