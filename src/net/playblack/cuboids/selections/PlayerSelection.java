package net.playblack.cuboids.selections;

import java.util.LinkedHashMap;

import net.playblack.cuboids.blocks.CBlock;
import net.playblack.mcutils.Vector;

/**
 * Player selection based on a CuboidSelection
 * 
 * @author Christoph Ksoll
 * 
 */
public class PlayerSelection extends CuboidSelection {
    /*
     * Those are properties for working with "brushes"
     */
    private int brushData = 0;
    private int brushType = 0;
    private int brushRadius = 3;

    /**
     * Empty CTOR
     */
    public PlayerSelection() {
        super();
    }

    /**
     * Construct with vectors
     * 
     * @param v1
     * @param v2
     */
    public PlayerSelection(Vector v1, Vector v2) {
        super(v1, v2);
    }

    /**
     * Construct with vectors and a ready-to-use block list
     * 
     * @param v1
     * @param v2
     * @param blocks
     */
    public PlayerSelection(Vector v1, Vector v2,
            LinkedHashMap<Vector, CBlock> blocks) {
        super(v1, v2, blocks);
    }

    public int getBrushData() {
        return brushData;
    }

    public void setBrushData(int brushData) {
        this.brushData = brushData;
    }

    public int getBrushType() {
        return brushType;
    }

    public void setBrushType(int brushType) {
        this.brushType = brushType;
    }

    public int getBrushRadius() {
        return brushRadius;
    }

    public void setBrushRadius(int brushRadius) {
        this.brushRadius = brushRadius;
    }
}
