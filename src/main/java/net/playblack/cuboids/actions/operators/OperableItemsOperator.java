package net.playblack.cuboids.actions.operators;

import net.canarymod.api.world.blocks.BlockType;
import net.playblack.cuboids.actions.ActionHandler;
import net.playblack.cuboids.actions.ActionListener;
import net.playblack.cuboids.actions.ActionManager;
import net.playblack.cuboids.actions.events.forwardings.BlockLeftClickEvent;
import net.playblack.cuboids.actions.events.forwardings.BlockRightClickEvent;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CServer;
import net.playblack.cuboids.regions.Region;
import net.playblack.cuboids.regions.Region.Status;
import net.playblack.cuboids.regions.RegionManager;
import net.playblack.mcutils.CLocation;

public class OperableItemsOperator implements ActionListener {

    /**
     * Checks if the player can use the item in his hand (right-mouse-use)
     * where ever he is currently standing in.
     *
     * @param player
     * @return
     */
    public boolean canUseItem(CPlayer player) {
        return player.canUseItem(player.getItemInHand());
    }

    /**
     * Check if player can use a block (switch, button, chests etc etc)
     *
     * @param player
     * @param block
     * @return
     */
    public boolean canUseBlock(CPlayer player, BlockType block, CLocation point) {
        if (player.hasPermission("cuboids.super.admin")) {
            return true;
        }
        Region r = RegionManager.get().getActiveRegion(point, false);
        return r.playerIsAllowed(player.getName(), player.getGroups()) || !r.isItemRestricted(block.getId());
    }

    public boolean canUseBucket(CPlayer player, CLocation point, boolean lavaBucket) {
        if (player.hasPermission("cuboids.super.admin")) {
            return true;
        }
        Region r = RegionManager.get().getActiveRegion(point, false);
        if (r.playerIsAllowed(player.getName(), player.getGroups())) {
            return true;
        }
        if (lavaBucket) {
            return r.getProperty("lava-bucket") != Status.DENY;
        }
        return r.getProperty("water-bucket") != Status.DENY;
    }


    // *******************************
    // Listener creation stuff
    // *******************************
    @ActionHandler
    public void onBlockRightClick(BlockRightClickEvent event) {
        if (!canUseBlock(CServer.getServer().getPlayer(event.getPlayer().getName()), event.getBlock(), event.getLocation())) {
            event.cancel();
        }
    }

    @ActionHandler
    public void onBlockLeftClick(BlockLeftClickEvent event) {
        if (!canUseBlock(CServer.getServer().getPlayer(event.getPlayer().getName()), event.getBlock(), event.getLocation())) {
            event.cancel();
        }
    }

    static {
        ActionManager.registerActionListener("Cuboids", new OperableItemsOperator());
    }
}
