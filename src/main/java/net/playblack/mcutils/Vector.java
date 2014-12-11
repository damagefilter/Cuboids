package net.playblack.mcutils;

import net.canarymod.api.world.position.Position;
import net.canarymod.api.world.position.Vector3D;

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
}
