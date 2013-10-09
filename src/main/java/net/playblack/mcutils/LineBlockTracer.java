package net.playblack.mcutils;

import net.playblack.cuboids.blocks.CBlock;
import net.playblack.cuboids.gameinterface.CPlayer;

/**
 * LineBlockTracer is a derivative class from Ho0ber's HitBlox class,
 * currently in use by CanaryMod. (https://github.com/FallenMoonNetwork/CanaryMod/blob/crow/src/HitBlox.java)
 * I stripped it down to what Cuboids2 needs specifically.
 *
 * @author chris
 * @author Ho0ber
 */
public class LineBlockTracer {
    private CPlayer player;
    private double rot_x, rot_y, view_height;
    private double length, h_length, step;
    private int range;
    private double x_offset, y_offset, z_offset;
    private int last_x, last_y, last_z;
    private int target_x, target_y, target_z;

    /**
     * Constructor requiring player, uses default values
     *
     * @param in_player
     */
    public LineBlockTracer(CPlayer in_player) {
        init(in_player, 200, 0.2, 1.65); // Reasonable default
        // values
    }

    /**
     * Constructor requiring player, max range, and a stepping value
     *
     * @param in_player
     * @param in_range
     * @param in_step
     */
    public LineBlockTracer(CPlayer in_player, int in_range, double in_step) {
        init(in_player, in_range, in_step, 1.65);
    }

    /**
     * Initialization method
     *
     * @param in_location
     * @param in_range
     * @param in_step
     * @param in_view_height
     */
    public void init(CPlayer in_location, int in_range, double in_step, double in_view_height) {
        player = in_location;
        view_height = in_view_height;
        range = in_range;
        step = in_step;
        length = 0;

        rot_x = (player.getPitch() + 90) % 360;
        rot_y = player.getRotation() * -1;

        target_x = (int) Math.floor(player.getX());
        target_y = (int) Math.floor(player.getY() + view_height);
        target_z = (int) Math.floor(player.getZ());
        last_x = target_x;
        last_y = target_y;
        last_z = target_z;
    }

    /**
     * Returns the block at the cursor, or null if out of range
     *
     * @return CBlock
     */
    public CBlock getTargetBlock() {
        while ((getNextBlock() != null) && (getCurBlock().getType() == 0)) {

        }
        return getCurBlock();
    }

    /**
     * Returns the block in the direction of the cursor, ignoring certain block types.
     * Null if out of range.
     *
     * @param blockIds The block ids to ignore.
     * @return
     */
    public CBlock getTargetBlockIgnoring(int... blockIds) {
        blockLoop:
        while (getNextBlock() != null) {
            for (int i : blockIds) {
                if (getCurBlock().getType() == i) {
                    continue blockLoop;
                }
            }
            break;
        }
        return getCurBlock();
    }

    /**
     * Returns the Position attached to the face at the cursor, or null if out of
     * range
     *
     * @return Vector
     */
    public Vector getTargetVector() {
        while ((getNextBlock() != null) && (getCurBlock().getType() == 0)) {

        }
        if (getCurBlock() != null) {
            return new Vector(last_x, last_y, last_z);
        }
        else {
            return null;
        }
    }

    /**
     * Returns STEPS forward along line of vision and returns block
     *
     * @return CBlock
     */
    public CBlock getNextBlock() {
        last_x = target_x;
        last_y = target_y;
        last_z = target_z;

        do {
            length += step;

            h_length = (length * Math.cos(Math.toRadians(rot_y)));
            y_offset = (length * Math.sin(Math.toRadians(rot_y)));
            x_offset = (h_length * Math.cos(Math.toRadians(rot_x)));
            z_offset = (h_length * Math.sin(Math.toRadians(rot_x)));

            target_x = (int) Math.floor(x_offset + player.getX());
            target_y = (int) Math.floor(y_offset + player.getY() + view_height);
            target_z = (int) Math.floor(z_offset + player.getZ());

        } while ((length <= range) && ((target_x == last_x) && (target_y == last_y) && (target_z == last_z)));

        if (length > range) {
            return null;
        }

        return player.getWorld().getBlockAt(new Vector(target_x, target_y, target_z));
    }

    /**
     * Returns the current block along the line of vision
     *
     * @return CBlock
     */
    public CBlock getCurBlock() {
        if (length > range) {
            return null;
        }
        else {
            return player.getWorld().getBlockAt(new Vector(target_x, target_y, target_z));
        }
    }

    /**
     * Returns the previous block along the line of vision
     *
     * @return CBlock
     */
    public CBlock getLastBlock() {
        return player.getWorld().getBlockAt(new Vector(last_x, last_y, last_z));
    }
}
