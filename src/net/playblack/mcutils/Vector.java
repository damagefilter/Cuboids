package net.playblack.mcutils;

import java.io.Serializable;
import java.util.Random;

/**
* Vector object that holds x,y,z coordinates for a point in 3D space.
* 
* @author Chris
*/
@SuppressWarnings("serial")
public class Vector implements Serializable
{
    private double x, y, z;

    /*
     * ******************************************************
     * Constructors with a few type conversions, for your convenience
     * ******************************************************
     */
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public Vector(int x, int y, int z) {
        this.x = (double)x;
        this.y = (double)y;
        this.z = (double)z;
    }


    public Vector(float x, float y, float z) {
        this.x = (double)x;
        this.y = (double)y;
        this.z = (double)z;
    }


    public Vector() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

   /**
    * Retrieve X component of Vector
	* @return double x
	*/
    public double getX() {
        return x;
    }

   

   /**
	* Set x component with native double
	* @param x
	*/
    public void setX(double x) {
        this.x = x;
    }

   /**
	* Set x component with a int2double conversion
	* @param x
	*/
    public void setX(int x) {
        this.x = (double)x;
    }

    /**
     * Retrieve Y component of Vector
 	 * @return double y
 	 */
    public double getY() {
        return y;
    }


    /**
	 * Set y component with native double
	 * @param y
	 */
    public void setY(double y) {
        this.y = y;
    }

   /**
	* Set y component with a int2double conversion
	* @param y
	*/
    public void setY(int y) {
        this.y = (double)y;
    }

    /**
     * Retrieve Z component of Vector
 	 * @return double z
 	 */
    public double getZ() {
        return z;
    }
    
    /**
	 * Set y component with native double
	 * @param z
	 */
    public void setZ(double z) {
        this.z = z;
    }

   /**
	* Set z component with a int2double conversion
	* @param z
	*/
    public void setZ(int z) {
        this.z = (double)z;
    }
 

/*
 * ******************************************************
 * Math Operations for your convenience!
 * ******************************************************
 */

    /*
     * ******************************************************
     * VECTOR LENGHT, DISTANCE CALCS, NORMALIZATION 
     * ******************************************************
     */

    /**
     * Retrieve the length of this vector
     * @return
     */
    public double getVectorLength() {
    	double power =  (Math.pow(getX(), 2) +
    					Math.pow(getY(), 2) +
    					Math.pow(getZ(), 2));
    	
    	return Math.sqrt(power);
    }

    /**
     * Retrieve the distance between this vector and the given Vector v, rooted<br>
     * @param v
     * @return double The Distance
     */
    public double getDistance(Vector v) {
    	double distPower = (
    			Math.pow(v.getX() - this.getX(), 2) +
    			Math.pow(v.getY() - this.getY(), 2) +
    			Math.pow(v.getZ() - this.getZ(), 2)
    			);
    	return Math.sqrt(distPower);
    }
    
    /**
     * Retrieve the distance between 2 given vectors<br>
     * @param v
     * @return double The Distance
     */
    public static double getDistance(Vector v1, Vector v2) {
    	double distPower = (
    			Math.pow(v1.getX() - v2.getX(), 2) +
    			Math.pow(v1.getY() - v2.getY(), 2) +
    			Math.pow(v1.getZ() - v2.getZ(), 2)
    			);
    	return Math.sqrt(distPower);
    }

    
    /**
     * Retrieve the distance between 2 given doubles<br>
     * @param v
     * @return double The Distance
     */
    public static double getDistance(double p1, double p2) {
    	double distPower = (
    			Math.pow(p1 - p2, 2)
    			);
    	return Math.sqrt(distPower);
    }
    
    /**
     * Retrieve the distance between this vector and the given Vector v, unrooted<br>
     * @param v
     * @return double The Distance
     */
    public double getSquareDistance(Vector v) {
    	double distPower = (
    			Math.pow(v.getX() - this.getX(), 2) +
    			Math.pow(v.getY() - this.getY(), 2) +
    			Math.pow(v.getZ() - this.getZ(), 2)
    			);
    	
    	return distPower;
    }
    
    /*
     * ******************************************************
     * AREA AND POINT CALCULATIONS
     * ******************************************************
     */
    
    /**
     * Calculates the number of Blocks in each direction and returns a new Vector with the resulting values.
     * @param v1
     * @param v2
     * @return new Vector holding the amount of blocks in each direction.
     */
    public static Vector getAreaVolume(Vector v1, Vector v2) {
    	Vector min = Vector.getMinor(v1, v2);
    	Vector max = Vector.getMajor(v1, v2);
    	double x = (Vector.getDistance(max.getX(), min.getX()));
    	double y = (Vector.getDistance(max.getY(), min.getY()));
    	double z = (Vector.getDistance(max.getZ(), min.getZ()));
    	
    	return new Vector(
    			x,
    			y,
    			z);
    }
    /**
     * Check if this vector is contained within the range of the given two
     * @param min
     * @param max
     * @return
     */
    public boolean isWithin(Vector min, Vector max) {
    	if(
    			this.getBlockX() >= min.getBlockX() && this.getBlockX() <= max.getBlockX()
    		&&	this.getBlockY() >= min.getBlockY() && this.getBlockY() <= max.getBlockY()
    		&&	this.getBlockZ() >= min.getBlockZ() && this.getBlockZ() <= max.getBlockZ()
    			
    	  ) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    /**
     * Retrieve the major of two vectors (the one farther away from 0,0,0) 
     * @param v1
     * @param v2
     * @return Major Vector, null if something went wrong
     */
    public static Vector getMajor(Vector v1, Vector v2) {
    	double dv1 = v1.getVectorLength();
    	double dv2 = v2.getVectorLength();
    	double max = Math.max(v1.getVectorLength(), v2.getVectorLength());
    	if(max == dv1) {
    		return v1;
    	}
    	else if(max == dv2) {
    		return v2;
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Retrieve the minor of two vectors (the one nearer to 0,0,0
     * @param v1
     * @param v2
     * @return Minor Vector, null if something went wrong
     */
    public static Vector getMinor(Vector v1, Vector v2) {
    	double dv1 = v1.getVectorLength();
    	double dv2 = v2.getVectorLength();
    	double min = Math.min(v1.getVectorLength(), v2.getVectorLength());
    	if(min == dv1) {
    		return v1;
    	}
    	else if(min == dv2) {
    		return v2;
    	}
    	else {
    		return null;
    	}
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
        Vector other = (Vector)obj;
        return other.getX() == this.x && other.getY() == this.y && other.getZ() == this.z;

    }

    
   /**
	* Gets the minimum components of two vectors.
	*
	* @param v1
	* @param v2
	* @return minimum
	*/
	    public static Vector getMinimum(Vector v1, Vector v2) {
        return new Vector(
                Math.min(v1.getX(), v2.getX()),
                Math.min(v1.getY(), v2.getY()),
                Math.min(v1.getZ(), v2.getZ()));
    }
    
   /**
	* Gets the maximum components of two vectors.
	*
	* @param v1
	* @param v2
	* @return maximum
	*/
    public static Vector getMaximum(Vector v1, Vector v2) {
        return new Vector(
                Math.max(v1.getX(), v2.getX()),
                Math.max(v1.getY(), v2.getY()),
                Math.max(v1.getZ(), v2.getZ()));
    }
    
    /**
     * Return a random vector that is within v1 and v2
     * @param v1
     * @param v2
     * @return
     */
    public static Vector randomVector(Vector v1, Vector v2) {
        Vector smaller = Vector.getMinimum(v1, v2);
        Vector bigger = Vector.getMaximum(v1, v2);
        Random rnd = new Random();
        return new Vector(
                rnd.nextInt(smaller.getBlockX())+bigger.getBlockX(),
                rnd.nextInt(smaller.getBlockY())+bigger.getBlockY(),
                rnd.nextInt(smaller.getBlockZ())+bigger.getBlockZ());
    }
    
    /*
     * ******************************************************
     * TYPE CONVERSIONS FOR NON-DOUBLE VECTORS
     * ******************************************************
     */
    
    /**
     * Transform the given Vector to a block Vector (int)floor(Value)
     * @param v
     * @return new Vector
     */
    public static Vector getBlockVector(Vector v) {
    	return new Vector((int)Math.floor(v.getX()),
    					  (int)Math.floor(v.getY()),
    					  (int)Math.floor(v.getZ()));
    }
    
    /**
     * Get component X as int
     * @return int x
     */
    public int getBlockX() {
    	return (int)Math.floor(getX());
    }
    
    /**
     * Get component Y as int
     * @return int y
     */
    public int getBlockY() {
    	return (int)Math.floor(getY());
    }
    
    /**
     * Get component Z as int
     * @return int z
     */
    public int getBlockZ() {
    	return (int)Math.floor(getZ());
    }
    
    @Override
    public String toString() {
    	return  "x: "+getX()+
    			", y: "+getY()+
    			", z: "+getZ();
    }
    
    public StringBuilder serialize() {
        return new StringBuilder().append("[")
                .append(Double.valueOf(x)).append(",")
                .append(Double.valueOf(y)).append(",")
                .append(Duoble.valueOf(z)).append("]");
    }
}