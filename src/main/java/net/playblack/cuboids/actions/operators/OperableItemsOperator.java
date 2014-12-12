package net.playblack.cuboids.actions.operators;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.RegionManager;

public class OperableItemsOperator {

//    /**
//     * Checks if the player can use the item in his hand (right-mouse-use)
//     * where ever he is currently standing in.
//     *
//     * @param player
//     * @return
//     */
//    public boolean canUseItem(CPlayer player) {
//        return player.canUseItem(player.getItemInHand());
//    }

    /**
     * Check if player can use a block (switch, button, chests etc etc)
     *
     * @param player
     * @param block
     * @return
     */
    public boolean canUseBlock(Player player, BlockType block, Location point) {
        if (player.hasPermission("cuboids.super.admin")) {
            return true;
        }
        Region r = RegionManager.get().getActiveRegion(point, false);
        return r.playerIsAllowed(player, player.getPlayerGroups()) || !r.isItemRestricted(block.getMachineName());
    }

//    public boolean canUseBucket(CPlayer player, Location point, boolean lavaBucket) {
//        if (player.hasPermission("cuboids.super.admin")) {
//            return true;
//        }
//        Region r = RegionManager.get().getActiveRegion(point, false);
//        if (r.playerIsAllowed(player.getName(), player.getGroups())) {
//            return true;
//        }
//        if (lavaBucket) {
//            return r.getProperty("lava-bucket") != Status.DENY;
//        }
//        return r.getProperty("water-bucket") != Status.DENY;
//    }


    // *******************************
    // Listener creation stuff
    // *******************************

    /**
     * Returns true if the right clicking should be canceled
     * @param player
     * @param type
     * @param location
     * @return
     */
    public boolean onBlockClick(Player player, BlockType type, Location location) {
        return !canUseBlock(player, type, location);
    }
}
