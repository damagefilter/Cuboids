package net.playblack.mcutils;

import net.canarymod.api.world.position.Position;
import net.playblack.cuboids.exceptions.DeserializeException;

import java.util.Random;

public class Vector {
    protected double x, y, z;

    /*
     * ******************************************************
     * Constructors with a few type conversions, for your convenience
     * ******************************************************
     */
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
//        ToolBox.adjustWorldPosition(this);
    }

    public Vector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
//        ToolBox.adjustWorldPosition(this);
    }

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
//        ToolBox.adjustWorldPosition(this);
    }

    public Vector() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector(Position key) {
        this.x = key.getX();
        this.y = key.getY();
        this.z = key.getZ();
    }

    /**
     * Copy constructor copies the primitives
     *
     * @param key
     */
    public Vector(Vector key) {
        this.x = key.x;
        this.y = key.y;
        this.z = key.z;
    }

    /**
     * Retrieve the distance between 2 given vectors<br>
     *
     * @return double The Distance
     */
    public static double getDistance(Vector v1, Vector v2) {
        double distPower = (Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2) + Math.pow(v1.getZ() - v2.getZ(), 2));
        return Math.sqrt(distPower);
    }

    /**
     * Retrieve the distance between 2 given doubles<br>
     *
     * @return double The Distance
     */
    public static double getDistance(double p1, double p2) {
        double distPower = (Math.pow(p1 - p2, 2));
        return Math.sqrt(distPower);
    }

    /**
     * Calculates the number of Blocks in each direction and returns a new
     * Vector with the resulting values.
     *
     * @param v1
     * @param v2
     * @return new Vector holding the amount of blocks in each direction.
     */
    public static Vector getAreaVolumeVector(Vector v1, Vector v2) {
        Vector min = Vector.getMinor(v1, v2);
        Vector max = Vector.getMajor(v1, v2);
        double x = (Vector.getDistance(max.getX(), min.getX()));
        double y = (Vector.getDistance(max.getY(), min.getY()));
        double z = (Vector.getDistance(max.getZ(), min.getZ()));

        return new Vector(x, y, z);
    }

    /**
     * Get the number of blocks that are encompassed by the given two vectors
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double getAreaVolume(Vector v1, Vector v2) {
        Vector min = Vector.getMinor(v1, v2);
        Vector max = Vector.getMajor(v1, v2);
        double x = (Vector.getDistance(max.getX(), min.getX()));
        double y = (Vector.getDistance(max.getY(), min.getY()));
        double z = (Vector.getDistance(max.getZ(), min.getZ()));

        return x * y * z;
    }

    public static double getAreaVolume(Position v1, Position v2) {
        return getAreaVolume(new Vector(v1), new Vector(v2));
    }

    /**
     * Retrieve the major of two vectors (the one farther away from 0,0,0)
     *
     * @param v1
     * @param v2
     * @return Major Vector, null if something went wrong
     */
    public static Vector getMajor(Vector v1, Vector v2) {
        double dv1 = v1.getVectorLength();
        double dv2 = v2.getVectorLength();
        double max = Math.max(v1.getVectorLength(), v2.getVectorLength());
        if (max == dv1) {
            return v1;
        }
        else if (max == dv2) {
            return v2;
        }
        else {
            return null;
        }
    }

    /**
     * Retrieve the minor of two vectors (the one nearer to 0,0,0
     *
     * @param v1
     * @param v2
     * @return Minor Vector, null if something went wrong
     */
    public static Vector getMinor(Vector v1, Vector v2) {
        double dv1 = v1.getVectorLength();
        double dv2 = v2.getVectorLength();
        double min = Math.min(v1.getVectorLength(), v2.getVectorLength());
        if (min == dv1) {
            return v1;
        }
        else if (min == dv2) {
            return v2;
        }
        else {
            return null;
        }
    }

    /**
     * Gets the minimum components of two vectors.
     *
     * @param v1
     * @param v2
     * @return minimum
     */
    public static Vector getMinimum(Vector v1, Vector v2) {
        return new Vector(Math.min(v1.getX(), v2.getX()), Math.min(v1.getY(), v2.getY()), Math.min(v1.getZ(), v2.getZ()));
    }

    public static Vector getMinimum(Position v1, Position v2) {
        return new Vector(Math.min(v1.getX(), v2.getX()), Math.min(v1.getY(), v2.getY()), Math.min(v1.getZ(), v2.getZ()));
    }

    /**
     * Gets the maximum components of two vectors.
     *
     * @param v1
     * @param v2
     * @return maximum
     */
    public static Vector getMaximum(Vector v1, Vector v2) {
        return new Vector(Math.max(v1.getX(), v2.getX()), Math.max(v1.getY(), v2.getY()), Math.max(v1.getZ(), v2.getZ()));
    }

    public static Vector getMaximum(Position v1, Position v2) {
        return new Vector(Math.max(v1.getX(), v2.getX()), Math.max(v1.getY(), v2.getY()), Math.max(v1.getZ(), v2.getZ()));
    }

    /**
     * Return a random vector that is within v1 and v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static Vector randomVector(Vector v1, Vector v2) {
        Vector smaller = Vector.getMinimum(v1, v2);
        Vector bigger = Vector.getMaximum(v1, v2);
        Random rnd = new Random();
        double distanceX = Vector.getDistance(smaller.getX(), bigger.getX());
        double distanceY = Vector.getDistance(smaller.getY(), bigger.getY());
        double distanceZ = Vector.getDistance(smaller.getZ(), bigger.getZ());

        double x = smaller.getX() + rnd.nextInt((int) distanceX);
        double y = smaller.getY() + rnd.nextInt((int) distanceY);
        double z = smaller.getZ() + rnd.nextInt((int) distanceZ);
        return new Vector(x, y, z);
    }

    /*
     * ******************************************************
     * Math Operations for your convenience!
     * ******************************************************
     */

    /**
     * Transform the given Vector to a block Vector (int)floor(Value)
     *
     * @param v
     * @return new Vector
     */
    public static Vector getBlockVector(Vector v) {
        return new Vector((int) Math.floor(v.getX()), (int) Math.floor(v.getY()), (int) Math.floor(v.getZ()));
    }

    /**
     * Calculates the center point between 2 points
     *
     * @param p1
     * @param p2
     * @return Vector between p1 and p2
     */
    public static Vector getCenterPoint(Vector p1, Vector p2) {
        double x = (p1.getX() + p2.getX()) / 2;
        double y = (p1.getY() + p2.getY()) / 2;
        double z = (p1.getZ() + p2.getZ()) / 2;
        return new Vector(x, y, z);
    }

    public static Vector deserialize(String data) throws DeserializeException {
        data = data.replace("[", "").replace("]", "");
        String[] values = data.split(",");
        if (values.length != 3) {
            throw new DeserializeException("Could not deserialize Vector object. Invalid serialized data!", data);
        }
        Vector tr = new Vector(0, 0, 0);
        tr.setX(Double.parseDouble(values[0]));
        tr.setY(Double.parseDouble(values[1]));
        tr.setZ(Double.parseDouble(values[2]));

        return tr;
    }
    /*
     * ******************************************************
     * VECTOR LENGHT, DISTANCE CALCS, NORMALIZATION
     * ******************************************************
     */

    public static Vector adjustToCanaryPosition(Vector v) {
        ToolBox.adjustWorldPosition(v);
        return v;
    }

    /**
     * Retrieve X component of Vector
     *
     * @return double x
     */
    public double getX() {
        return x;
    }

    /**
     * Set x component with a int2double conversion
     *
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set x component with native double
     *
     * @param x
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Retrieve Y component of Vector
     *
     * @return double y
     */
    public double getY() {
        return y;
    }

    /*
     * ******************************************************
     * AREA AND POINT CALCULATIONS
     * ******************************************************
     */

    /**
     * Set y component with a int2double conversion
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Set y component with native double
     *
     * @param y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Retrieve Z component of Vector
     *
     * @return double z
     */
    public double getZ() {
        return z;
    }

    /**
     * Set z component with a int2double conversion
     *
     * @param z
     */
    public void setZ(int z) {
        this.z = z;
    }

    /**
     * Set y component with native double
     *
     * @param z
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Check if the x and z coords are the same.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean samePosition2D(double x, double y, double z) {
        return x == this.x && z == this.z;
    }

    /**
     * Check if the x and z coords are the same
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean samePosition2D(int x, int y, int z) {
        return x == this.x && z == this.z;
    }

    /**
     * Check if the x and z coords of this and the other vector are the same
     *
     * @param tmp
     * @return
     */
    public boolean samePosition2D(Vector tmp) {
        return getBlockX() == tmp.x && getBlockZ() == tmp.z;
    }

    /**
     * Retrieve the length of this vector
     *
     * @return
     */
    public double getVectorLength() {
        double power = (Math.pow(getX(), 2) + Math.pow(getY(), 2) + Math.pow(getZ(), 2));

        return Math.sqrt(power);
    }

    /*
     * ******************************************************
     * TYPE CONVERSIONS FOR NON-DOUBLE VECTORS
     * ******************************************************
     */

    /**
     * Retrieve the distance between this vector and the given Vector v, rooted<br>
     *
     * @param v
     * @return double The Distance
     */
    public double getDistance(Vector v) {
        double distPower = (Math.pow(v.getX() - this.getX(), 2) + Math.pow(v.getY() - this.getY(), 2) + Math.pow(v.getZ() - this.getZ(), 2));
        return Math.sqrt(distPower);
    }

    /**
     * Retrieve the distance between this vector and the given Vector v,
     * unrooted<br>
     *
     * @param v
     * @return double The Distance
     */
    public double getSquareDistance(Vector v) {
        double distPower = (Math.pow(v.getX() - this.getX(), 2) + Math.pow(v.getY() - this.getY(), 2) + Math.pow(v.getZ() - this.getZ(), 2));

        return distPower;
    }

    /**
     * Check if this vector is contained within the range of the given two
     *
     * @param min
     * @param max
     * @return
     */
    public boolean isWithin(Vector min, Vector max) {
        return this.getBlockX() >= min.getBlockX() && this.getBlockX() <= max.getBlockX() && this.getBlockY() >= min.getBlockY() && this.getBlockY() <= max.getBlockY() && this.getBlockZ() >= min.getBlockZ() && this.getBlockZ() <= max.getBlockZ();
    }

    /**
     * Checks if another object is equivalent.
     *
     * @param obj
     * @return whether the other object is equivalent
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector)) {
            return false;
        }
        Vector other = (Vector) obj;
        return other.getX() == this.x && other.getY() == this.y && other.getZ() == this.z;

    }

    /**
     * Get component X as int
     *
     * @return int x
     */
    public int getBlockX() {
        int i = (int) x;
        return x < i ? i - 1 : i;

    }

    /**
     * Get component Y as int
     *
     * @return int y
     */
    public int getBlockY() {
        int i = (int) y;
        return y < i ? i - 1 : i;
    }

    /**
     * Get component Z as int
     *
     * @return int z
     */
    public int getBlockZ() {
        int i = (int) z;
        return z < i ? i - 1 : i;
    }

    @Override
    public String toString() {
        return "x: " + getX() + ", y: " + getY() + ", z: " + getZ();
    }

    @Override
    public int hashCode() {
        return (int) (x + y + z);
    }

    public String explain() {
        return "(x: " + getBlockX() + ", y: " + getBlockY() + ", z: " + getBlockZ() + ")";
    }

    /**
     * Migration helper. If migration is done, find instances and remove with canatry positions
     *
     * @return
     */
    public Position toNative() {
        return new Position(this.x, this.y, this.z);
    }

    /**
     * Serialize this Vector into a Stringbuilder. This returns [x,y,z]
     *
     * @return
     */
    public StringBuilder serialize() {
        return new StringBuilder().append("[").append(Double.valueOf(x)).append(",").append(Double.valueOf(y)).append(",").append(Double.valueOf(z)).append("]");
    }
}
