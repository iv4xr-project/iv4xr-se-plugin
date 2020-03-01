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
public class TrianglePolygonIntersectionTest {
    @Test
    public void lineTriangleSingleIntersectTest(){
        //define the triangle and the line
        Vec3 tp1 = new Vec3(0,3,0);
        Vec3 tp2 = new Vec3(5,3,0);
        Vec3 tp3 = new Vec3(3,3,3);
        Vec3 l1 = new Vec3(2,1,1);
        Vec3 l2 = new Vec3(2,1,-1);

        //check if the intersect is found
        assertTrue(TrianglePolygonIntersection.isLineIntersectingTriangle(tp1,tp2,tp3,l1,l2));
    }

    @Test
    public void lineTriangleDoubleIntersectTest(){
        //define the triangle and the line
        Vec3 tp1 = new Vec3(0,3,0);
        Vec3 tp2 = new Vec3(5,3,0);
        Vec3 tp3 = new Vec3(3,3,3);
        Vec3 l1 = new Vec3(2,1,5);
        Vec3 l2 = new Vec3(2,1,-1);

        //check if the intersect is found
        assertTrue(TrianglePolygonIntersection.isLineIntersectingTriangle(tp1,tp2,tp3,l1,l2));
    }

    @Test
    public void lineInsideTriangleTest(){
        //define the triangle and the line
        Vec3 tp1 = new Vec3(0,3,0);
        Vec3 tp2 = new Vec3(5,3,0);
        Vec3 tp3 = new Vec3(3,3,3);
        Vec3 l1 = new Vec3(3,1,0.5);
        Vec3 l2 = new Vec3(3,2,2);

        //check if no intersect is found
        assertFalse(TrianglePolygonIntersection.isLineIntersectingTriangle(tp1,tp2,tp3,l1,l2));
    }

    @Test
    public void lineOutsideTriangleTest(){
        //define the triangle and the line
        Vec3 tp1 = new Vec3(0,3,0);
        Vec3 tp2 = new Vec3(5,3,0);
        Vec3 tp3 = new Vec3(3,3,3);
        Vec3 l1 = new Vec3(-1,1,4);
        Vec3 l2 = new Vec3(-3,2,2);

        //check if no intersect is found
        assertFalse(TrianglePolygonIntersection.isLineIntersectingTriangle(tp1,tp2,tp3,l1,l2));
    }

    @Test
    public void squarePolygonTwoIntersectTriangleTest(){
        //define the triangle and the polygon
        Vec3 tp1 = new Vec3(0,3,0);
        Vec3 tp2 = new Vec3(5,3,0);
        Vec3 tp3 = new Vec3(3,3,3);
        Vec3[] polygon = new Vec3[]{
                new Vec3(-1, 1, -3),
                new Vec3(2, 2, 1),
                new Vec3(4, 2, -1),
                new Vec3(1, 2, -5)
        };

        //check if the intersection is found
        assertTrue(TrianglePolygonIntersection.isPolygonIntersectingTriangle(tp1,tp2,tp3,polygon));
    }

    @Test
    public void linePolygonInsideTriangleTest(){
        //define the triangle and the polygon
        Vec3 tp1 = new Vec3(0,3,0);
        Vec3 tp2 = new Vec3(5,3,0);
        Vec3 tp3 = new Vec3(3,3,3);
        Vec3[] polygon = new Vec3[]{
                new Vec3(2, 2, 1),
                new Vec3(4, 2, 1)
        };

        //check if the intersection is found
        assertTrue(TrianglePolygonIntersection.isPolygonIntersectingTriangle(tp1,tp2,tp3,polygon));
    }

    @Test
    public void pointPolygonInsideTriangleTest(){
        //define the triangle and the polygon
        Vec3 tp1 = new Vec3(0,3,0);
        Vec3 tp2 = new Vec3(5,3,0);
        Vec3 tp3 = new Vec3(3,3,3);
        Vec3[] polygon = new Vec3[]{
                new Vec3(2, 2, 1)
        };

        //check if the intersection is found
        assertTrue(TrianglePolygonIntersection.isPolygonIntersectingTriangle(tp1,tp2,tp3,polygon));
    }

    @Test
    public void trianglePolygonOutsideTriangleTest(){
        //define the triangle and the polygon
        Vec3 tp1 = new Vec3(0,3,0);
        Vec3 tp2 = new Vec3(5,3,0);
        Vec3 tp3 = new Vec3(3,3,3);
        Vec3[] polygon = new Vec3[]{
                new Vec3(2, 2, -1),
                new Vec3(4, 2, -1),
                new Vec3(3,-2,-3)
        };

        //check if no intersection is found
        assertFalse(TrianglePolygonIntersection.isPolygonIntersectingTriangle(tp1,tp2,tp3,polygon));
    }
}
