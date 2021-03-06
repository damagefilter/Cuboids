package net.playblack.cuboids.selections;

import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Vector3D;

import java.util.LinkedHashMap;

/**
 * Player selection based on a CuboidSelection
 *
 * @author Christoph Ksoll
 */
public class PlayerSelection extends CuboidSelection {
    /*
     * Those are properties for working with "brushes"
     */
    private BlockType brushType = BlockType.Air;
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
    public PlayerSelection(Vector3D v1, Vector3D v2) {
        super(v1, v2);
    }

    /**
     * Construct with vectors and a ready-to-use block list
     *
     * @param v1
     * @param v2
     * @param blocks
     */
    public PlayerSelection(Vector3D v1, Vector3D v2, LinkedHashMap<Vector3D, BlockType> blocks) {
        super(v1, v2, blocks);
    }

    public BlockType getBrushType() {
        return brushType;
    }

    public void setBrushType(BlockType brushType) {
        this.brushType = brushType;
    }

    public int getBrushRadius() {
        return brushRadius;
    }

    public void setBrushRadius(int brushRadius) {
        this.brushRadius = brushRadius;
    }
}
