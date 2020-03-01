/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.Intersections;

import helperclasses.datastructures.Vec3;
import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Test;
/**
 * This class will hold the unit tests for point triangle intersections
 */
public class PointTriangleIntersectionTest {
    @Test
    public void pointOutsideTriangleTest(){
        //create the points
        Vec3 tp1 = new Vec3(0,3,0);
        Vec3 tp2 = new Vec3(5,3,0);
        Vec3 tp3 = new Vec3(3,3,3);
        Vec3 p = new Vec3(-1,1,0);

        //check if no intersection is found
        assertFalse(PointTriangleIntersection.isPointIntersectingTriangle(tp1,tp2,tp3,p));
    }

    @Test
    public void pointInsideTriangleTest(){
        //create the points
        Vec3 tp1 = new Vec3(0,3,0);
        Vec3 tp2 = new Vec3(5,5,0);
        Vec3 tp3 = new Vec3(3,3,3);
        Vec3 p = new Vec3(2,3,1);

        //check if the intersection is found
        assertTrue(PointTriangleIntersection.isPointIntersectingTriangle(tp1,tp2,tp3,p));
    }

    @Test
    public void pointOnTriangleTest(){
        //create the points
        Vec3 tp1 = new Vec3(0,3,0);
        Vec3 tp2 = new Vec3(5,3,0);
        Vec3 tp3 = new Vec3(3,4,3);
        Vec3 p = new Vec3(2,3,0);

        //check if the intersection is found
        assertTrue(PointTriangleIntersection.isPointIntersectingTriangle(tp1,tp2,tp3,p));
    }
}
