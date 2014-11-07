package net.playblack.cuboids.blockoperators;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.BlockType;
import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.history.HistoryObject;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.Vector;

/**
 * Generate walls along the line of a cuboid selection
 *
 * @author Chris
 */
public class DiscGenerator extends BaseGen {

    private boolean fill;
    private int radius;
    private int height;
    private BlockType material;

    /**
     * The selection you pass along here will be written into the world!
     *
     * @param selection
     * @param world
     */
    public DiscGenerator(CuboidSelection selection, World world) {
        super(selection, world);
    }

    /**
     * Set the material of the sphere
     *
     * @param block
     */
    public void setMaterial(BlockType block) {
        this.material = block;
    }

    /**
     * Set disc/circle radius
     *
     * @param radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Set disc/circle height
     *
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Set fill true to make a filled sphere, false to make it hollow(rly...)
     */
    public void setHollow(boolean sleepy) {
        fill = sleepy;
    }

    private void createDisc() {
        Vector center = selection.getOrigin();
        selection.clearBlocks();
        int Xmin = center.getBlockX() - radius;
        int Xmax = center.getBlockX() + radius;
        int Ymin = (height + center.getBlockY() >= center.getBlockY()) ? center.getBlockY() : height + center.getBlockY();
        int Ymax = (height + center.getBlockY() <= center.getBlockY()) ? center.getBlockY() : height + center.getBlockY();
        int Zmin = center.getBlockZ() - radius;
        int Zmax = center.getBlockZ() + radius;

        synchronized (lock) {
            for (int x = Xmin; x <= Xmax; x++) {
                for (int y = Ymin; y <= Ymax; y++) {
                    for (int z = Zmin; z <= Zmax; z++) {
                        double diff = Math.sqrt(Math.pow(x - center.getBlockX(), 2.0D) + Math.pow(z - center.getBlockZ(), 2.0D));
                        if (diff < radius + 0.5 && (fill || (!fill && diff > radius - 0.5))) {
                            selection.setBlock(new Vector(x, y, z), material);

                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean execute(Player player, boolean newHistory) throws BlockEditLimitExceededException, SelectionIncompleteException {
        createDisc();
        CuboidSelection world = scanWorld(true, false);

        if (newHistory) {
            SessionManager.get().getPlayerHistory(player.getName()).remember(new HistoryObject(world, selection));
        }
        boolean result = modifyWorld(false);
        return result;
    }
}
