package net.playblack.cuboids.selections;

import net.playblack.cuboids.Config;
import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;
import net.playblack.cuboids.regions.Region;
import net.playblack.mcutils.Vector;

import java.util.LinkedHashMap;

/**
 * Cuboid shaped selection
 *
 * @author Christoph Ksoll
 */
public class CuboidSelection implements ISelection {

    protected LinkedHashMap<Vector, CBlock> blockList;

    protected String world = "__NOWORLD__";

    protected Vector origin;
    protected Vector offset;

    /**
     * Empty CTOR
     */
    public CuboidSelection() {
        origin = null;
        offset = null;
        blockList = new LinkedHashMap<Vector, CBlock>();
    }

    /**
     * Construct with vectors
     *
     * @param v1
     * @param v2
     */
    public CuboidSelection(Vector v1, Vector v2) {
        origin = new Vector(v1.getBlockX(), v1.getBlockY(), v1.getBlockZ());
        offset = new Vector(v2.getBlockX(), v2.getBlockY(), v2.getBlockZ());
        blockList = new LinkedHashMap<Vector, CBlock>((int) Vector.getAreaVolume(origin, offset));
    }

    /**
     * Construct with vectors and a ready-to-use block list
     *
     * @param v1
     * @param v2
     * @param blocks
     */
    public CuboidSelection(Vector v1, Vector v2, LinkedHashMap<Vector, CBlock> blocks) {
        origin = new Vector(v1.getBlockX(), v1.getBlockY(), v1.getBlockZ());
        offset = new Vector(v2.getBlockX(), v2.getBlockY(), v2.getBlockZ());
        blockList = blocks;
    }

    public CuboidSelection(LinkedHashMap<Vector, CBlock> blocks) {
        blockList = blocks;
    }

    public CuboidSelection(CuboidSelection sel) {
        this.origin = sel.getOrigin();
        this.offset = sel.getOffset();
        this.blockList = sel.getBlockList();
    }

    public CuboidSelection(Vector v1, Vector v2, int size) {
        origin = new Vector(v1.getBlockX(), v1.getBlockY(), v1.getBlockZ());
        offset = new Vector(v2.getBlockX(), v2.getBlockY(), v2.getBlockZ());
        blockList = new LinkedHashMap<Vector, CBlock>(size);
    }

    /**
     * Set the current origin vector
     *
     * @param o
     */
    public void setOrigin(Vector o) {
        this.origin = new Vector(o.getBlockX(), o.getBlockY(), o.getBlockZ());
    }

    /**
     * Get the current origin vector
     *
     * @return
     */
    public Vector getOrigin() {
        return origin;
    }

    /**
     * Set current offset Vector
     *
     * @param o
     */
    public void setOffset(Vector o) {
        this.offset = new Vector(o.getBlockX(), o.getBlockY(), o.getBlockZ());
    }

    /**
     * Get current offset vector
     *
     * @return
     */
    public Vector getOffset() {
        return offset;
    }

    @Override
    public void setBlock(Vector v, CBlock b) {
        blockList.put(v, b);
    }

    @Override
    public CBlock getBlock(Vector v) {
        return blockList.get(v);
    }

    @Override
    public LinkedHashMap<Vector, CBlock> getBlockList() {
        return blockList;
    }

    @Override
    public void setBlockList(LinkedHashMap<Vector, CBlock> newList) {
        this.blockList = newList;

    }

    @Override
    public long getSize() {
        return blockList.size();
    }

    @Override
    public long getBoundarySize() {
        return (long) Vector.getAreaVolume(origin, offset);
    }

    @Override
    public String getWorld() {
        return null;
    }

    @Override
    public void setWorld(String world) {
        this.world = world;

    }

    /**
     * Sort the selection points.
     *
     * @param offsetFirst true: offset has the greater components - false: origin has
     *                    the greater components
     */
    public void sortEdges(boolean offsetFirst) {
        if (offsetFirst) {
            sortEdgesOffsetFirst();
        }
        else {
            Vector or_temp = Vector.getMaximum(origin, offset);
            Vector off_temp = Vector.getMinimum(origin, offset);
            origin = or_temp;
            offset = off_temp;
        }
    }

    /**
     * Sort the selection edges so offset is the greater one and origin the
     * smaller
     */
    private void sortEdgesOffsetFirst() {
        Vector or_temp = Vector.getMaximum(origin, offset);
        origin = Vector.getMinimum(origin, offset);
        offset = or_temp;
    }

    /**
     * Check if this selection is complete.
     *
     * @return
     */
    public boolean isComplete() {
        return (origin != null) && offset != null;
    }

    @Override
    public void clearBlocks() {
        blockList.clear();

    }

    /**
     * Expand the selection vertically from top to bottom
     */
    public void expandVert() {
        sortEdges(false);
        origin.setY(255);
        offset.setY(0);
    }

    /**
     * Turn this selection into a CuboidE. The result is a cuboid object with
     * the default settings.
     *
     * @return
     */
    public Region toRegion(CPlayer player, String[] playerlist) {
        Region cube = new Region();
        String name = null;
        cube.setBoundingBox(this.origin, this.offset);
        for (int i = 0; i < playerlist.length; i++) {
            if (i == (playerlist.length - 1)) { // last element is name!
                name = playerlist[i];
                continue;
            }
            if (playerlist[i].startsWith("g:")) {
                cube.addGroup(playerlist[i]);
            }
            else {
                cube.addPlayer(playerlist[i]);
            }
        }
        cube.setWorld(world);
        cube.setName(name);
        cube.putAll(Config.get().getDefaultCuboidSetting(
                player).getAllProperties());

        return cube;
    }

    /**
     * Resets selection points and emptys block list
     */
    public void reset() {
        origin = null;
        offset = null;
        clearBlocks();
    }

    /**
     * Returns true if the origin point is set
     *
     * @return
     */
    public boolean hasOrigin() {
        return origin != null;
    }

    /**
     * Returns true if the offset point is set
     *
     * @return
     */
    public boolean hasOffset() {
        return offset != null;
    }
}
