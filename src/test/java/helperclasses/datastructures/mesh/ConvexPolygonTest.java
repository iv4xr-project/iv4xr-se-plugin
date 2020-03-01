/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.mesh;

import helperclasses.datastructures.Vec3;
import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Test;
import testhelp.PrivateMethod;

public class ConvexPolygonTest {

    @Test
    public void testCentreCalculation() {

        Vec3[] vertices = new Vec3[]{
                new Vec3(-6, 3, -1),
                new Vec3(4, 0, 2),
                new Vec3(5, 0, 2),
                new Vec3(10, 10, 10) // random vertex that does not belong to the triangle
        };

        Edge[] edges = new Edge[]{
                new Edge(0, 1),
                new Edge(1, 2),
                new Edge(2, 0),
                new Edge(2, 3) // random edge that does not belong to the triangle
        };

        int[] edgeIndices = new int[]{0, 1, 2};

        double delta = 0.00000000000001;
        String methodName = "calculateCentre";

        ConvexPolygon instance = new ConvexPolygon(0, vertices, edges, edgeIndices);
        PrivateMethod<Vec3> calculateCentre = new PrivateMethod<>(instance, "calculateCentre");
        Vec3 centre = calculateCentre.invoke(vertices, edges, edgeIndices);

        assertEquals(1, centre.x, delta);
        assertEquals(1, centre.y, delta);
        assertEquals(1, centre.z, delta);
    }
}