package net.playblack.mcutils;

import net.canarymod.api.world.position.Position;
import net.canarymod.api.world.position.Vector3D;
import net.playblack.cuboids.exceptions.DeserializeException;

import java.util.Random;

public class Vector {
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
     * Return a random vector that is within v1 and v2
     *
     * @param v1
     * @param v2
     * @return
     */
    public static Vector3D randomVector(Vector3D v1, Vector3D v2) {
        Vector3D smaller = Vector3D.getMinimum(v1, v2);
        Vector3D bigger = Vector3D.getMaximum(v1, v2);
        Random rnd = new Random();
        double distanceX = Vector.getDistance(smaller.getX(), bigger.getX());
        double distanceY = Vector.getDistance(smaller.getY(), bigger.getY());
        double distanceZ = Vector.getDistance(smaller.getZ(), bigger.getZ());

        double x = smaller.getX() + rnd.nextInt((int) distanceX);
        double y = smaller.getY() + rnd.nextInt((int) distanceY);
        double z = smaller.getZ() + rnd.nextInt((int) distanceZ);
        return new Vector3D(x, y, z);
    }

    /*
     * ******************************************************
     * Math Operations for your convenience!
     * ******************************************************
     */
    /**
     * Calculates the center point between 2 points
     *
     * @param p1
     * @param p2
     * @return Vector between p1 and p2
     */
    public static Vector3D getCenterPoint(Vector3D p1, Vector3D p2) {
        double x = (p1.getX() + p2.getX()) / 2;
        double y = (p1.getY() + p2.getY()) / 2;
        double z = (p1.getZ() + p2.getZ()) / 2;
        return new Vector3D(x, y, z);
    }

    public static boolean samePosition2D(Position p1, Position p2) {
        return p1.getBlockX() == p2.getBlockX() && p1.getBlockZ() == p2.getBlockZ();
    }

//    /**
//     * Retrieve X component of Vector
//     *
//     * @return double x
//     */
//    public double getX() {
//        return x;
//    }
//
//    /**
//     * Set x component with a int2double conversion
//     *
//     * @param x
//     */
//    public void setX(int x) {
//        this.x = x;
//    }
//
//    /**
//     * Set x component with native double
//     *
//     * @param x
//     */
//    public void setX(double x) {
//        this.x = x;
//    }
//
//    /**
//     * Retrieve Y component of Vector
//     *
//     * @return double y
//     */
//    public double getY() {
//        return y;
//    }
//
//    /*
//     * ******************************************************
//     * AREA AND POINT CALCULATIONS
//     * ******************************************************
//     */
//
//    /**
//     * Set y component with a int2double conversion
//     *
//     * @param y
//     */
//    public void setY(int y) {
//        this.y = y;
//    }
//
//    /**
//     * Set y component with native double
//     *
//     * @param y
//     */
//    public void setY(double y) {
//        this.y = y;
//    }
//
//    /**
//     * Retrieve Z component of Vector
//     *
//     * @return double z
//     */
//    public double getZ() {
//        return z;
//    }
//
//    /**
//     * Set z component with a int2double conversion
//     *
//     * @param z
//     */
//    public void setZ(int z) {
//        this.z = z;
//    }
//
//    /**
//     * Set y component with native double
//     *
//     * @param z
//     */
//    public void setZ(double z) {
//        this.z = z;
//    }
//
//    /**
//     * Check if the x and z coords are the same.
//     *
//     * @param x
//     * @param y
//     * @param z
//     * @return
//     */
//    public boolean samePosition2D(double x, double y, double z) {
//        return x == this.x && z == this.z;
//    }
//
//    /**
//     * Check if the x and z coords are the same
//     *
//     * @param x
//     * @param y
//     * @param z
//     * @return
//     */
//    public boolean samePosition2D(int x, int y, int z) {
//        return x == this.x && z == this.z;
//    }
//
//    /**
//     * Check if the x and z coords of this and the other vector are the same
//     *
//     * @param tmp
//     * @return
//     */
//    public boolean samePosition2D(Vector tmp) {
//        return getBlockX() == tmp.x && getBlockZ() == tmp.z;
//    }
//
//    /**
//     * Retrieve the length of this vector
//     *
//     * @return
//     */
//    public double getVectorLength() {
//        double power = (Math.pow(getX(), 2) + Math.pow(getY(), 2) + Math.pow(getZ(), 2));
//
//        return Math.sqrt(power);
//    }
//
//    /*
//     * ******************************************************
//     * TYPE CONVERSIONS FOR NON-DOUBLE VECTORS
//     * ******************************************************
//     */
//
//    /**
//     * Retrieve the distance between this vector and the given Vector v, rooted<br>
//     *
//     * @param v
//     * @return double The Distance
//     */
//    public double getDistance(Vector v) {
//        double distPower = (Math.pow(v.getX() - this.getX(), 2) + Math.pow(v.getY() - this.getY(), 2) + Math.pow(v.getZ() - this.getZ(), 2));
//        return Math.sqrt(distPower);
//    }
//
//    /**
//     * Retrieve the distance between this vector and the given Vector v,
//     * unrooted<br>
//     *
//     * @param v
//     * @return double The Distance
//     */
//    public double getSquareDistance(Vector v) {
//        double distPower = (Math.pow(v.getX() - this.getX(), 2) + Math.pow(v.getY() - this.getY(), 2) + Math.pow(v.getZ() - this.getZ(), 2));
//
//        return distPower;
//    }
//
//    /**
//     * Check if this vector is contained within the range of the given two
//     *
//     * @param min
//     * @param max
//     * @return
//     */
//    public boolean isWithin(Vector min, Vector max) {
//        return this.getBlockX() >= min.getBlockX() && this.getBlockX() <= max.getBlockX() && this.getBlockY() >= min.getBlockY() && this.getBlockY() <= max.getBlockY() && this.getBlockZ() >= min.getBlockZ() && this.getBlockZ() <= max.getBlockZ();
//    }
//
//    /**
//     * Checks if another object is equivalent.
//     *
//     * @param obj
//     * @return whether the other object is equivalent
//     */
//    @Override
//    public boolean equals(Object obj) {
//        if (!(obj instanceof Vector)) {
//            return false;
//        }
//        Vector other = (Vector) obj;
//        return other.getX() == this.x && other.getY() == this.y && other.getZ() == this.z;
//
//    }
//
//    /**
//     * Get component X as int
//     *
//     * @return int x
//     */
//    public int getBlockX() {
//        int i = (int) x;
//        return x < i ? i - 1 : i;
//
//    }
//
//    /**
//     * Get component Y as int
//     *
//     * @return int y
//     */
//    public int getBlockY() {
//        int i = (int) y;
//        return y < i ? i - 1 : i;
//    }
//
//    /**
//     * Get component Z as int
//     *
//     * @return int z
//     */
//    public int getBlockZ() {
//        int i = (int) z;
//        return z < i ? i - 1 : i;
//    }
//
//    @Override
//    public String toString() {
//        return "x: " + getX() + ", y: " + getY() + ", z: " + getZ();
//    }
//
//    @Override
//    public int hashCode() {
//        return (int) (x + y + z);
//    }
//
//    public String explain() {
//        return "(x: " + getBlockX() + ", y: " + getBlockY() + ", z: " + getBlockZ() + ")";
//    }
//
//    /**
//     * Migration helper. If migration is done, find instances and remove with canatry positions
//     *
//     * @return
//     */
//    public Position toNative() {
//        return new Position(this.x, this.y, this.z);
//    }
//
//    /**
//     * Serialize this Vector into a Stringbuilder. This returns [x,y,z]
//     *
//     * @return
//     */
//    public StringBuilder serialize() {
//        return new StringBuilder().append("[").append(Double.valueOf(x)).append(",").append(Double.valueOf(y)).append(",").append(Double.valueOf(z)).append("]");
//    }
}
