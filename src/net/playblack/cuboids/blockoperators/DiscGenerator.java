package net.playblack.cuboids.blockoperators;

import net.playblack.cuboids.SessionManager;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.gameinterface.CWorld;
import net.playblack.cuboids.history.HistoryObject;
import net.playblack.cuboids.selections.CuboidSelection;
import net.playblack.mcutils.Vector;

/**
 * Generate walls along the line of a cuboid selection
 * @author Chris
 *
 */
public class DiscGenerator extends BaseGen {

    private boolean fill;
    private int radius;
    private int height;
    private CBlock material;
    /**
     * The selection you pass along here will be written into the world!
     * @param selection
     * @param world
     */
    public DiscGenerator(CuboidSelection selection, CWorld world) {
        super(selection, world);
    }
    
    /**
     * Set the material of the sphere
     * @param block
     */
    public void setMaterial(CBlock block) {
        this.material = block;
    }
    
    /**
     * Set disc/circle radius
     * @param radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    /**
     * Set disc/circle height
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }
    
    
    /**
     * Set fill true to make a filled sphere, false to make it hollow(rly...)
     * @param wo
     */
    public void setHollow(boolean sleepy) {
        fill = sleepy;
    }
    
    
    private void createDisc() {
        Vector center = selection.getOrigin();
        if(selection.getOffset() == null) {
            //A little work around to evaluate true
            //when modifyWorld is issued as it needs the selection to be complete
            selection.setOffset(center);
        }
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
                        double diff = Math.sqrt(Math.pow(x - center.getBlockX(), 2.0D) + 
                                                Math.pow(z - center.getBlockZ(), 2.0D));
                        if (diff < radius + 0.5 && (fill || (!fill && diff > radius - 0.5))) {
                            selection.setBlock(new Vector(x,y,z), material);
                            
                        }
                    }
                }
            }
        }
    }
    @Override
    public boolean execute(CPlayer player, boolean newHistory) {
        createDisc();
        CuboidSelection world = scanWorld(true);
        
        if(newHistory) {
            SessionManager.getInstance().getPlayerHistory(player.getName()).remember(new HistoryObject(world, selection));
        }
        boolean result = modifyWorld();
        return result;
    }
}
