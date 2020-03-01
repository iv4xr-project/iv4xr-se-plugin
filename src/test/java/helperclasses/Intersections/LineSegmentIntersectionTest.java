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
 * This class will hold the unit tests for line segment intersections
 */
public class LineSegmentIntersectionTest {

    @Test
    public void parallelLineSegmentTest(){
        //create the points
        Vec3 p1 = new Vec3(1, 2,1);
        Vec3 p2 = new Vec3(10, 2,1);
        Vec3 q1 = new Vec3(1, 2,2);
        Vec3 q2 = new Vec3(10,2, 2);

        //check if no intersection is found
        assertFalse(LineSegmentIntersection.areLinesIntersecting(p1,p2,q1,q2));
    }

    @Test
    public void looseEndsTest(){
        //create the points
        Vec3 p1 = new Vec3(16.2, 2,25.6);
        Vec3 q1 = new Vec3(9.9, 2,25.6);
        Vec3 p2 = new Vec3(10.75, 2,14.25);
        Vec3 q2 = new Vec3(10.75,2, 15.75);

        //check if no intersection is found
        boolean b = LineSegmentIntersection.areLinesIntersecting(p1,q1,p2,q2);
        assertFalse(LineSegmentIntersection.areLinesIntersecting(p1,q1,p2,q2));
    }

    @Test
    public void crossShapedLineSegmentTest(){
        //create the points
        Vec3 p1 = new Vec3(10, 2,1);
        Vec3 p2 = new Vec3(0, 2,10);
        Vec3 q1 = new Vec3(0, 1,0);
        Vec3 q2 = new Vec3(10,1, 10);

        //check if the intersection is found
        assertTrue(LineSegmentIntersection.areLinesIntersecting(p1,p2,q1,q2));
    }

    @Test
    public void crossShapedLineSegmentNegativeValuesTest(){
        //create the points
        Vec3 p1 = new Vec3(-10, 2,1);
        Vec3 p2 = new Vec3(0, 2,10);
        Vec3 q1 = new Vec3(0, 1,0);
        Vec3 q2 = new Vec3(-10,1, 10);

        //check if the intersection is found
        assertTrue(LineSegmentIntersection.areLinesIntersecting(p1,p2,q1,q2));
    }


    @Test
    public void colinearLineSegmentsTest(){
        //create the points
        Vec3 p1 = new Vec3(1, 2,1);
        Vec3 p2 = new Vec3(10, 2,1);
        Vec3 q1 = new Vec3(11, 1,1);
        Vec3 q2 = new Vec3(15,1, 1);

        //check if no intersection is found
        assertFalse(LineSegmentIntersection.areLinesIntersecting(p1,p2,q1,q2));
    }
}
