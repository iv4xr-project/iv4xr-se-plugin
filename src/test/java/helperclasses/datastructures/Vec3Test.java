/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures;

import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Test;

/**
 * This class holds all unit tests for Vec3
 */
public class Vec3Test {

    /**
     * Check if the vec3 is correctly stored on the initialization with 3 values
     */
    @Test
    public void checkInitFrom3Values() {
        Vec3 v = new Vec3(2, 3, -2.5);

        assertEquals(2, v.x, 0.1);
        assertEquals(3, v.y, 0.1);
        assertEquals(-2.5, v.z, 0.1);
    }

    /**
     * Check if the vec3 is correctly stored on the initialization from a copy
     */
    @Test
    public void checkInitFromCopy() {
        Vec3 v1 = new Vec3(2, 3, -2.5);
        Vec3 v2 = new Vec3(v1);

        assertEquals(v1.x, v2.x, 0.1);
        assertEquals(v1.y, v2.y, 0.1);
        assertEquals(v1.z, v2.z, 0.1);
    }

    /**
     * Check if the vec3 is correctly stored on the initialization from a single value
     */
    @Test
    public void checkInitFromSingleValue() {
        Vec3 v = new Vec3(-2.5);

        assertEquals(-2.5, v.x, 0.1);
        assertEquals(-2.5, v.y, 0.1);
        assertEquals(-2.5, v.z, 0.1);
    }

    /**
     * Check if the zero vector is correctly instantiated
     */
    @Test
    public void checkZeroInit() {
        Vec3 v = Vec3.zero();

        assertEquals(0, v.x, 0.1);
        assertEquals(0, v.y, 0.1);
        assertEquals(0, v.z, 0.1);
    }

    /**
     * Check if the vec3 properly subtracts
     */
    @Test
    public void subtractStaticVectors() {
        //define vector values
        Vec3 v1 = new Vec3(2, 3, -2);
        Vec3 v2 = new Vec3(1, 1, 1);

        //execute the subtraction
        Vec3 v3 = Vec3.subtract(v1, v2);

        //check if the subtraction works
        assertEquals(1, v3.x, 0.1);
        assertEquals(2, v3.y, 0.1);
        assertEquals(-3, v3.z, 0.1);

        //check if the original values are still the same
        assertEquals(2, v1.x, 0.1);
        assertEquals(3, v1.y, 0.1);
        assertEquals(-2, v1.z, 0.1);
        assertEquals(1, v2.x, 0.1);
        assertEquals(1, v2.y, 0.1);
        assertEquals(1, v2.z, 0.1);
    }

    /**
     * Check if the vec3 properly subtracts by another vector
     */
    @Test
    public void subtractVector() {
        //define vector values
        Vec3 v1 = new Vec3(2, 3, -2);
        Vec3 v2 = new Vec3(1, 1, 1);

        //execute the subtraction
        v1.subtract(v2);

        //check if the subtraction works
        assertEquals(1, v1.x, 0.1);
        assertEquals(2, v1.y, 0.1);
        assertEquals(-3, v1.z, 0.1);

        //check if the original values of v2 are still the same
        assertEquals(1, v2.x, 0.1);
        assertEquals(1, v2.y, 0.1);
        assertEquals(1, v2.z, 0.1);
    }

    /**
     * Check if the vec3 properly subtracts by a group of numbers
     */
    @Test
    public void subtractNumbers() {
        //define vector values
        Vec3 v = new Vec3(2, 3, -2);

        //execute the subtraction
        v.subtract(1, 1, 1);

        //check if the subtraction works
        assertEquals(1, v.x, 0.1);
        assertEquals(2, v.y, 0.1);
        assertEquals(-3, v.z, 0.1);
    }

    /**
     * Check if the sum of positive vec3 values are correctly calculated
     */
    @Test
    public void checkSumPositive() {
        //define vectors
        Vec3 v1 = new Vec3(1);
        Vec3 v2 = new Vec3(2.5, 0.5, 0.3);
        Vec3 v3 = new Vec3(1);

        //sum the vectors
        Vec3 v4 = Vec3.sum(v1, v2, v3);

        //check the sum of the vectors
        assertEquals(4.5, v4.x, 0.1);
        assertEquals(2.5, v4.y, 0.1);
        assertEquals(2.3, v4.z, 0.1);
    }

    /**
     * Check if the sum of mixed vec3 values are correctly calculated
     */
    @Test
    public void checkSumMixed() {
        //define vectors
        Vec3 v1 = new Vec3(1);
        Vec3 v2 = new Vec3(-2.5, 0.5, 0.3);
        Vec3 v3 = new Vec3(1, 2, -3);

        //sum the vectors
        Vec3 v4 = Vec3.sum(v1, v2, v3);

        //check the sum of the vectors
        assertEquals(-0.5, v4.x, 0.1);
        assertEquals(3.5, v4.y, 0.1);
        assertEquals(-1.7, v4.z, 0.1);
    }

    /**
     * Check the addition of vectors
     */
    @Test
    public void checkAdditionVectors() {
        //define the vectors
        Vec3 v1 = new Vec3(2, -0.5, 1);
        Vec3 v2 = new Vec3(2.3, 1, 1);

        //calculate the addition
        v1.add(v2);

        //check if the addition works
        assertEquals(4.3, v1.x, 0.1);
        assertEquals(0.5, v1.y, 0.1);
        assertEquals(2, v1.z, 0.1);

        //check if the original values of v2 are still the same
        assertEquals(2.3, v2.x, 0.1);
        assertEquals(1, v2.y, 0.1);
        assertEquals(1, v2.z, 0.1);
    }

    /**
     * Check the addition of numbers
     */
    @Test
    public void checkAdditionNumbers() {
        //define the vectors
        Vec3 v1 = new Vec3(2, -0.5, 1);

        //calculate the addition
        v1.add(2.3, 1, 1);

        //check if the addition works
        assertEquals(4.3, v1.x, 0.1);
        assertEquals(0.5, v1.y, 0.1);
        assertEquals(2, v1.z, 0.1);
    }

    /**
     * This method will check if the division by a constant works
     */
    @Test
    public void checkDivision() {
        //define vector
        Vec3 v = new Vec3(0, 1, 2.5);

        //divide
        v.divide(2);

        //check if the addition works
        assertEquals(0, v.x, 0.1);
        assertEquals(0.5, v.y, 0.1);
        assertEquals(1.25, v.z, 0.1);
    }

    /**
     * This method will check if the division by zero will throw an error
     */
    @Test
    public void checkDivisionByZero() {
    	assertThrows(IllegalArgumentException.class, 
    		() -> { 
    	        //define vector
    	        Vec3 v = new Vec3(0, 1, 2.5);
    	        //divide by zero
    	        v.divide(0);
    		; }
    	) ;
    }

    /**
     * Check if the scalar multiplication is correct
     */
    @Test
    public void checkMultiplyScalar() {
        //define the vector values
        Vec3 v = new Vec3(2, 3, -2);

        //execute the multiplication
        v.multiply(2);

        //check if the multiplication works
        assertEquals(4, v.x, 0.1);
        assertEquals(6, v.y, 0.1);
        assertEquals(-4, v.z, 0.1);
    }

    /**
     * Check if the multiplication by a group of numbers is correct
     */
    @Test
    public void checkMultiplyNumbers() {
        //define the vector values
        Vec3 v = new Vec3(2, 3, -2);

        //execute the multiplication
        v.multiply(1, 0, 0.5);

        //check if the multiplication works
        assertEquals(2, v.x, 0.1);
        assertEquals(0, v.y, 0.1);
        assertEquals(-1, v.z, 0.1);
    }

    /**
     * Check if the dynamic multiplication is correct
     */
    @Test
    public void checkMultiplyDynamic() {
        //define the vector values
        Vec3 v1 = new Vec3(2, 3, -2);
        Vec3 v2 = new Vec3(0, 2, 1);

        //execute the multiplication
        v1.multiply(v2);

        //check if the multiplication works
        assertEquals(0, v1.x, 0.1);
        assertEquals(6, v1.y, 0.1);
        assertEquals(-2, v1.z, 0.1);

        //check if the original values of v2 is still the same
        assertEquals(0, v2.x, 0.1);
        assertEquals(2, v2.y, 0.1);
        assertEquals(1, v2.z, 0.1);
    }

    /**
     * Check if the static multiplication is correct
     */
    @Test
    public void checkMultiplyStatic() {
        //define the vector values
        Vec3 v1 = new Vec3(2, 3, -2);
        Vec3 v2 = new Vec3(0, 2, 1);

        //execute the multiplication
        Vec3 v3 = Vec3.multiply(v1, v2);

        //check if the multiplication works
        assertEquals(0, v3.x, 0.1);
        assertEquals(6, v3.y, 0.1);
        assertEquals(-2, v3.z, 0.1);

        //check if the original values are still the same
        assertEquals(2, v1.x, 0.1);
        assertEquals(3, v1.y, 0.1);
        assertEquals(-2, v1.z, 0.1);
        assertEquals(0, v2.x, 0.1);
        assertEquals(2, v2.y, 0.1);
        assertEquals(1, v2.z, 0.1);
    }

    /**
     * Check if the static scalar multiplication is correct
     */
    @Test
    public void checkMultiplyScalarStatic() {
        //define the vector values
        Vec3 v1 = new Vec3(2, 3, -2);

        //execute the multiplication
        Vec3 v = Vec3.multiply(2, v1);

        //check if the multiplication works
        assertEquals(4, v.x, 0.1);
        assertEquals(6, v.y, 0.1);
        assertEquals(-4, v.z, 0.1);
    }

    /**
     * Test the size of a vector squared
     */
    @Test
    public void checkSizeSquared() {
        //define vector
        Vec3 v = new Vec3(2, -3, 4);

        //check if the size squared is correct
        assertEquals(29, v.lengthSquared(), 0.1);
    }

    /**
     * Test the size of a vector
     */
    @Test
    public void checkSize() {
        //define vector
        Vec3 v = new Vec3(2, -3, 4);

        //check if the size is correct
        assertEquals(Math.sqrt(29), v.length(), 0.01);
    }

    /**
     * Test the normalization of a vector
     */
    @Test
    public void checkNormalised() {
        //define vector
        Vec3 v = new Vec3(2, -3, 4);
        double s = v.length();

        //normalize v
        v.normalize();

        //check if the size is correct
        assertEquals(1, v.length(), 0.1);

        //check if the values are correct
        assertEquals(2 / s, v.x, 0.1);
        assertEquals(-3 / s, v.y, 0.1);
        assertEquals(4 / s, v.z, 0.1);
    }

    /**
     * Test the normalization of a vector with a size of zero
     */
    @Test
    public void checkNormalisedZero() {
    	assertThrows(IllegalArgumentException.class, 
    		() -> { 
    			 //create the zero vector
    	        Vec3 v = Vec3.zero();
    	        //normalise
    	        v.normalize();
    		} 
    	) ; 
    }

    /**
     * Check if the distance squared function is correct
     */
    @Test
    public void checkDistanceSquared() {

        Vec3 v1 = new Vec3(-3, 1, 1);
        Vec3 v2 = new Vec3(3, 1, 1);

        assertEquals(0, v1.distanceSquared(v1), 0.1);
        assertEquals(0, v2.distanceSquared(v2), 0.1);
        assertEquals(36, v1.distanceSquared(v2), 0.1);
        assertEquals(36, v2.distanceSquared(v1), 0.1);
    }

    /**
     * Check if the distance function is correct
     */
    @Test
    public void checkDistance() {

        Vec3 v1 = new Vec3(-3, 1, 1);
        Vec3 v2 = new Vec3(3, 1, 1);

        assertEquals(0, v1.distance(v1), 0.1);
        assertEquals(0, v2.distance(v2), 0.1);
        assertEquals(6, v1.distance(v2), 0.1);
        assertEquals(6, v2.distance(v1), 0.1);
    }
}
