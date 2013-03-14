package net.playblack.cuboids.actions.deprecated;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;

public class ExplainCuboid {

    /**
     * Explain cuboid in this region
     * 
     * @param player
     * @param point
     * @param setOffset
     *            True to set offset instead of origin
     */
    public static void explain(CPlayer player) {
        CuboidInterface.get().explainRegion(player,
                player.getLocation());
    }
}
