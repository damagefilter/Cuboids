package net.playblack.cuboids.actions;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.Vector;

public class ItemDropHandler {
    /**
     * Handle item droppings (lol)
     * @param player
     * @param v
     * @return Returns true if a player <b>can't</b> drop an item.
     */
    public static boolean handleItemDrop(CPlayer player, Vector v) {
        return CuboidInterface.getInstance().isCreative(v, player.getWorld());
    }
}
