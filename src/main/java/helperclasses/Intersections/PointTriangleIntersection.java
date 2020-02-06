/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.Intersections;

import helperclasses.datastructures.Vec3;

/**
 * This class will hold the calculation for an intersection between a triangle and a point on the x,z plane
 */
public class PointTriangleIntersection {

    /**
     * This method will check whether a point lies inside the triangle on the x,z plane
     * @param tp1: First point of the triangle
     * @param tp2: Second point of the triangle
     * @param tp3: Third point of the triangle
     * @param p: The point
     * @return A boolean whether the point intersect the triangle
     */
    public static boolean isPointIntersectingTriangle(Vec3 tp1, Vec3 tp2, Vec3 tp3, Vec3 p){
        //compute the area of the main triangle and the 3 sub triangles
        double areaMain = computeTriangleArea(tp1, tp2, tp3);
        double subTriangle1 = computeTriangleArea(tp1, tp2, p);
        double subTriangle2 = computeTriangleArea(tp1, p, tp3);
        double subTriangle3 = computeTriangleArea(p, tp2, tp3);

        //check if the area of the main triangle and the sub triangles are equal within delta difference
        return (Math.abs(areaMain - subTriangle1 - subTriangle2 - subTriangle3) <= 0.00000001);
    }

    /**
     * This method will calculate the area of a triangle on a x,z plane
     * @param tp1: First point of the triangle
     * @param tp2: Second point of the triangle
     * @param tp3: Third point of the triangle
     * @return The area of the triangle on the x,z plane
     */
    private static double computeTriangleArea(Vec3 tp1, Vec3 tp2, Vec3 tp3){
        return Math.abs((tp1.x*(tp2.z-tp3.z) + tp2.x*(tp3.z-tp1.z)+
                tp3.x*(tp1.z-tp2.z))/2.0);
    }
}
