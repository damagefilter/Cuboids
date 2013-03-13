package net.playblack.cuboids.actions.deprecated;

import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.CuboidInterface;
import net.playblack.mcutils.Location;

public class ItemDropHandler {
    /**
     * Handle item droppings (lol)
     * 
     * @param player
     * @param v
     * @return Returns true if a player <b>can't</b> drop an item.
     */
    public static boolean handleItemDrop(CPlayer player, Location v) {
        return CuboidInterface.get().isCreative(v);
    }
}
