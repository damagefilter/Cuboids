package net.playblack.cuboids.generators;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.Chest;
import net.canarymod.api.world.blocks.Sign;
import net.canarymod.api.world.blocks.TileEntity;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.history.HistoryObject;
import net.playblack.cuboids.selections.CuboidSelection;

import java.util.Map;

/**
 * Takes information about a previously stored region content and puts it back into the world
 *
 * @author Chris
 */
public class RestoreRegionGenerator extends BaseGen {

    private Map<Vector3D, String[]> signs;
    private Map<Vector3D, Item[]> items;
    /**
     * The selection you pass along here will be written into the world!
     *
     * @param selection
     * @param world
     */
    public RestoreRegionGenerator(CuboidSelection selection, Map<Vector3D, String[]> signs, Map<Vector3D, Item[]> items,  World world) {
        super(selection, world);
        this.signs = signs;
        this.items = items;
    }

    @Override
    public boolean execute(Player player, boolean newHistory) throws BlockEditLimitExceededException, SelectionIncompleteException {
        if (newHistory) {
            CuboidSelection world = scanWorld(true, false);
            SessionManager.get().getPlayerHistory(player.getName()).remember(new HistoryObject(world, selection));
        }
        return modifyWorld(false);
    }

    @Override
    public boolean modifyWorld(boolean requireSelectionComplete) {
        if ((selection == null) || selection.getBlockList().isEmpty()) {
            return false;
        }
        if (requireSelectionComplete && !selection.isComplete()) {
            return false;
        }
        // Process blocks, 1st run
        synchronized (lock) {
            for (Vector3D v : selection.getBlockList().keySet()) {
                changeBlock(selection.getBlock(v), v, world, false);
                if (signs.containsKey(v)) {
                    TileEntity te = world.getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getTileEntity();
                    if (te instanceof Sign) {
                        Sign sign = (Sign)te;
                        String[] lines = signs.get(v);
                        for (int i = 0; i < lines.length; ++i) {
                            sign.setComponentOnLine(Canary.factory().getChatComponentFactory().compileChatComponent(lines[i]), i);
                        }
                    }
                }
                if (items.containsKey(v)) {
                    TileEntity te = world.getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getTileEntity();
                    if (te instanceof Chest) {
                        Chest chest = (Chest)te;
                        chest.setContents(items.get(v));
                    }
                }
            }
        }

        // process queued blocks
        synchronized (lock) {
            for (Vector3D v : placeLast.keySet()) {
                changeBlock(placeLast.get(v), v, world, true);
                if (signs.containsKey(v)) {
                    TileEntity te = world.getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getTileEntity();
                    if (te instanceof Sign) {
                        Sign sign = (Sign)te;
                        String[] lines = signs.get(v);
                        for (int i = 0; i < lines.length; ++i) {
                            sign.setComponentOnLine(Canary.factory().getChatComponentFactory().compileChatComponent(lines[i]), i);
                        }
                    }
                }
                if (items.containsKey(v)) {
                    TileEntity te = world.getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).getTileEntity();
                    if (te instanceof Chest) {
                        Chest chest = (Chest)te;
                        chest.setContents(items.get(v));
                    }
                }
            }
            placeLast.clear();
        }
        return true;
    }
}
