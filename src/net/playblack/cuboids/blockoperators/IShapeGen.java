package net.playblack.cuboids.blockoperators;

import net.playblack.cuboids.gameinterface.CPlayer;


public interface IShapeGen {
    /**
     * Execute the block operation and also do a new undo step
     * @param simulate
     */
    public boolean execute(CPlayer player, boolean newHistory);
}
