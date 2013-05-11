package net.playblack.cuboids.blockoperators;

import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.exceptions.BlockEditLimitExceededException;
import net.playblack.cuboids.exceptions.SelectionIncompleteException;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.history.HistoryObject;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.Vector;

/**
 * Generate walls along the line of a cuboid selection
 * 
 * @author Chris
 * 
 */
public class SphereGenerator extends BaseGen {

    private boolean fill;
    private int radius;
    private CBlock material;

    /**
     * The selection you pass along here will be written into the world!
     * 
     * @param selection
     * @param world
     */
    public SphereGenerator(CuboidSelection selection, CWorld world) {
        super(selection, world);
    }

    /**
     * Set the material of the sphere
     * 
     * @param block
     */
    public void setMaterial(CBlock block) {
        this.material = block;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Set fill true to make a filled sphere, false to make it hollow(rly...)
     * 
     * @param wo
     */
    public void setHollow(boolean sleepy) {
        fill = sleepy;
    }

    private void createSphere() {
        Vector center = selection.getOrigin();
        if (selection.getOffset() == null) {
            // A little work around to evaluate true
            // when modifyWorld is issued as it needs the selection to be
            // complete
            selection.setOffset(center);
        }
        int Xmin = center.getBlockX() - radius;
        int Xmax = center.getBlockX() + radius;
        int Ymin = center.getBlockY() - radius;
        int Ymax = center.getBlockY() + radius;
        int Zmin = center.getBlockZ() - radius;
        int Zmax = center.getBlockZ() + radius;

        synchronized (lock) {
            for (int x = Xmin; x <= Xmax; x++) {
                for (int y = Ymin; y <= Ymax; y++) {
                    for (int z = Zmin; z <= Zmax; z++) {

                        double diff = Math.sqrt(Math.pow(x - center.getX(),
                                2.0D)
                                + Math.pow(y - center.getY(), 2.0D)
                                + Math.pow(z - center.getZ(), 2.0D));
                        if (diff < radius + 0.5
                                && (fill || (!fill && diff > radius - 0.5))) {
                            selection.setBlock(new Vector(x, y, z), material);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean execute(CPlayer player, boolean newHistory)
            throws BlockEditLimitExceededException,
            SelectionIncompleteException {
        selection.clearBlocks();
        createSphere();
        CuboidSelection world = scanWorld(true, false);

        if (newHistory) {
            SessionManager.get().getPlayerHistory(player.getName())
                    .remember(new HistoryObject(world, selection));
        }
        boolean result = modifyWorld(false);
        return result;
    }
}
