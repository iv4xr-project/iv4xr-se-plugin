/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.Intersections;

import helperclasses.datastructures.Vec3;

/**
 * This class will contain the calculation for the intersection between two line segments
 * The code is based on https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
 */
public class LineSegmentIntersection {

    /**
     * Check if the 2 lines would intersect on a 2d plane (x and z axis)
     * @param p1: First point of the first line
     * @param q1: Second point of the first line
     * @param p2: First point of the Second line
     * @param q2: Second point of the Second line
     * @return Return whether the lines do intersect on the x,z plane
     */
    public static boolean areLinesIntersecting(Vec3 p1, Vec3 q1, Vec3 p2, Vec3 q2){
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and q2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Doesn't fall in any of the above cases
    }

    /**
     * Check if point q lies on segment pr
     * @param p: The first colinear point
     * @param q: The second colinear point
     * @param r: The third colinear point
     * @return A boolean whether point q lies on segment pr
     */
    private static boolean onSegment(Vec3 p, Vec3 q, Vec3 r)
    {
        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.z <= Math.max(p.z, r.z) && q.z >= Math.min(p.z, r.z))
            return true;

        return false;
    }

    /**
     * Find the orientation of the triplet (p,q,r)
     * @param p: The first point
     * @param q: The second point
     * @param r: The third point
     * @return Whether pqr are colinear(0), clockwise(1), counter clockwise(2)
     */
    private static int orientation(Vec3 p, Vec3 q, Vec3 r)
    {
        //calculate the order of the points
        double val = (q.z - p.z) * (r.x - q.x) -
                (q.x - p.x) * (r.z - q.z);

        if (val == 0) return 0; // colinear

        return (val > 0)? 1: 2; // clock or counterclock wise
    }
}
