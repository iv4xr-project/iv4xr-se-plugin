/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures;

import java.io.Serializable;
import java.util.Objects;

/**
 * Basic class to store 3 doubles together
 */
public class Vec3 implements Serializable {

    public double x, y, z;

    /**
     * This constructor will build a vec3 from the 3 input values
     *
     * @param x: The x value
     * @param y: The y value
     * @param z: The z value
     */
    public Vec3(Number x, Number y, Number z) {
        this.x = x.doubleValue();
        this.y = y.doubleValue();
        this.z = z.doubleValue();
    }

    /**
     * This constructor will make a copy of another vec3
     *
     * @param copy: Vec3 to be copied
     */
    public Vec3(Vec3 copy) {
        this.x = copy.x;
        this.y = copy.y;
        this.z = copy.z;
    }

    /**
     * This constructor will create a vec3 with 3 identical values
     *
     * @param value: The value of x,y and z
     */
    public Vec3(Number value) {
        this.x = value.doubleValue();
        this.y = value.doubleValue();
        this.z = value.doubleValue();
    }

    /**
     * The zero vector
     *
     * @return Return a new zero vector
     */
    public static Vec3 zero() {
        return new Vec3(0);
    }

    /**
     * Subtract two vectors from each other and return the value
     *
     * @param v1: First vector
     * @param v2: Second vector
     * @return a Vec3 with the values of v1-v2
     */
    public static Vec3 subtract(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    /**
     * Sum a list of Vec3 values
     *
     * @param vertices: The list of vec3 values
     * @return A new vec3 with the value of all input vec3 added
     */
    public static Vec3 sum(Vec3... vertices) {
        Vec3 res = Vec3.zero();
        for (Vec3 v : vertices)
            res.add(v);
        return res;
    }

    /**
     * Point wise division of a Vec3 by a number
     *
     * @param v:       The vec3 which is to be divided
     * @param divisor: The value to be devised by
     * @return Return a new vec3 with v/divisor
     */
    public static Vec3 divide(Vec3 v, Number divisor) {
        Vec3 copy = new Vec3(v);
        v.divide(divisor);
        return v;
    }

    /**
     * Multiply two vectors
     *
     * @param v1: The first vector
     * @param v2: The Second vector
     * @return The value of v1 and v2 multiplied
     */
    public static Vec3 multiply(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
    }

    /**
     * Static scalar multiplication
     *
     * @param t: Scalar
     * @param v: Vec3 to be multiplied
     * @return A new vec3 with the value of t*v
     */
    public static Vec3 multiply(Number t, Vec3 v) {
        return new Vec3(t.doubleValue() * v.x, t.doubleValue() * v.y, t.doubleValue() * v.z);
    }

    /**
     * Subtract another vector from this vector
     *
     * @param v: The vector to subtract this vector from
     */
    public void subtract(Vec3 v) {
        this.x -= v.x;
        this.y -= v.y;
        this.z -= v.z;
    }

    /**
     * Subtract each of the stored values by a certain value
     *
     * @param x: The value by which x will be subtracted
     * @param y: The value by which y will be subtracted
     * @param z: The value by which z will be subtracted
     */
    public void subtract(Number x, Number y, Number z) {
        this.x -= x.doubleValue();
        this.y -= y.doubleValue();
        this.z -= z.doubleValue();
    }

    /**
     * Add another vec3 to this vec3
     *
     * @param v: The vec3 to add to this vec3
     */
    public void add(Vec3 v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
    }

    /**
     * Add a certain value to each of the stored values
     *
     * @param x: The value to add to x
     * @param y: The value to add to y
     * @param z: The value to add to z
     */
    public void add(Number x, Number y, Number z) {
        this.x += x.doubleValue();
        this.y += y.doubleValue();
        this.z += z.doubleValue();
    }

    /**
     * Point wise division of this Vec3 by a number
     *
     * @param divisor: The value to be devised by
     */
    public void divide(Number divisor) {
        double d = divisor.doubleValue();
        if (d == 0) throw new IllegalArgumentException("Cannot divide by zero");
        this.x /= d;
        this.y /= d;
        this.z /= d;
    }

    /**
     * The scalar multiplication of a vector
     *
     * @param times: The scalar
     */
    public void multiply(Number times) {
        double d = times.doubleValue();
        this.x *= d;
        this.y *= d;
        this.z *= d;
    }

    /**
     * Multiply the values stored in this vec3 by certain values
     *
     * @param x: Multiply x by this value
     * @param y: Multiply y by this value
     * @param z: Multiply z by this value
     */
    public void multiply(Number x, Number y, Number z) {
        this.x *= x.doubleValue();
        this.y *= y.doubleValue();
        this.z *= z.doubleValue();
    }

    /**
     * Multiply this vector by another vector
     *
     * @param v: The vector to multiply with
     */
    public void multiply(Vec3 v) {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
    }

    /**
     * This method will compute the length squared
     *
     * @return The length squared
     */
    public double lengthSquared() {
        return (x * x) + (y * y) + (z * z);
    }

    /**
     * This method will compute the length
     *
     * @return The length
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * This method will compute the normalized value of this vector
     */
    public void normalize() {
        double s = length();
        if (s == 0) throw new IllegalArgumentException();

        x /= s;
        y /= s;
        z /= s;
    }

    /**
     * This method will compute the normalized distance squared.
     * "Normalized" here means that it is always positive. So
     * let d = q - p. Then we calculate d.x^2 + d.y^2 + d.z^2.
     *
     * @return The normalized distance squared
     */
    public double distanceSquared(Vec3 other) {
        Vec3 r = Vec3.subtract(other, this);
        return (r.x * r.x) + (r.y * r.y) + (r.z * r.z);
    }

    /**
     * This method will compute the normalized distance.
     * "Normalized" here means that it is always positive.
     *
     * @return The normalized distance
     */
    public double distance(Vec3 other) {
        return Math.sqrt(distanceSquared(other));
    }

    /**
     * Return this vector as a string format
     *
     * @return This vector in a string format
     */
    @Override
    public String toString() {
        return String.format("<%s,%s,%s>", x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vec3)) return false;
        Vec3 v = (Vec3) obj;
        return x == v.x && y == v.y && z == v.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
