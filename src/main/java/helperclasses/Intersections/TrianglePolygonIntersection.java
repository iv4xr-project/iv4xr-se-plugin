/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.Intersections;

import helperclasses.datastructures.Vec3;

/**
 * This class will hold the calculation for whether a triangle and a square collide
 */
public class TrianglePolygonIntersection {
    /**
     * This method will check if a polygon intersects a triangle
     * @param t1: First point of the triangle
     * @param t2: Second point of the triangle
     * @param t3: Third point of the triangle
     * @param polygon: A set of points in order which form the polygon
     * @return A boolean whether the polygon and the triangle intersect
     */
    public static boolean isPolygonIntersectingTriangle(Vec3 t1,Vec3 t2, Vec3 t3, Vec3...polygon){
        //for each point check if it is inside the triangle
        for(Vec3 p : polygon){
            if(PointTriangleIntersection.isPointIntersectingTriangle(t1,t2,t3,p)) return true;
        }

        //check for intersections between edges and the triangle
        for(int i = 0; i < polygon.length -1; i++){
            if(isLineIntersectingTriangle(t1,t2,t3,polygon[i], polygon[i+1])) return true;
        }
        //check for the last intersection which closes the polygon
        if(polygon.length >=2){
            if(isLineIntersectingTriangle(t1,t2,t3,polygon[0], polygon[polygon.length-1])) return true;
        }

        //no intersects are found
        return false;
    }

    /**
     * This method will check if a line intersects a triangle
     * @param t1: First point of the triangle
     * @param t2: Second point of the triangle
     * @param t3: Third point of the triangle
     * @param l1: First point of the line
     * @param l2: Second point of the line
     * @return A boolean whether the line and the triangle intersect
     */
    public static boolean isLineIntersectingTriangle(Vec3 t1, Vec3 t2, Vec3 t3, Vec3 l1, Vec3 l2){
        boolean intersect = false;

        //intersect the line segment with the 3 edges from the triangle
        intersect |= LineSegmentIntersection.areLinesIntersecting(t1,t2,l1,l2);
        intersect |= LineSegmentIntersection.areLinesIntersecting(t2,t3,l1,l2);
        intersect |= LineSegmentIntersection.areLinesIntersecting(t1,t3,l1,l2);
        return intersect;
    }
}
