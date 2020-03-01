/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.mesh;

import helperclasses.datastructures.Vec3;
import static org.junit.jupiter.api.Assertions.* ;
import org.junit.jupiter.api.Test;
import pathfinding.NavMeshContainer;

public class TriangleMeshTest {

    public static void assertVec3Equals(Vec3 v0, Vec3 v1, double delta) {
        assertEquals(v0.x, v1.x, delta);
        assertEquals(v0.y, v1.y, delta);
        assertEquals(v0.z, v1.z, delta);
    }

    @Test
    public void constructor() {
        var mesh = new TriangleMesh(
                new NavMeshContainer(
                        new int[]{0, 1, 2},
                        new Vec3[]{
                                new Vec3(2, 0, 3),
                                new Vec3(4, 0, 0),
                                new Vec3(-6, 0, -3)
                        }
                ));

        double delta = 0.00000000000001;

        // vertices
        assertNotNull(mesh.vertices);
        assertEquals(3, mesh.vertices.length);
        assertVec3Equals(new Vec3(2, 0, 3), mesh.vertices[0], delta);
        assertVec3Equals(new Vec3(4, 0, 0), mesh.vertices[1], delta);
        assertVec3Equals(new Vec3(-6, 0, -3), mesh.vertices[2], delta);

        // edges
        assertNotNull(mesh.edges);
        assertEquals(3, mesh.edges.length);
        assertEquals(new Edge(0, 1), mesh.edges[0]);
        assertEquals(new Edge(0, 2), mesh.edges[1]);
        assertEquals(new Edge(1, 2), mesh.edges[2]);

        // faces
        assertNotNull(mesh.faces);
        assertEquals(1, mesh.faces.length);
        ConvexPolygon triangle = mesh.faces[0];
        assertEquals(0, triangle.index);
        assertArrayEquals(new int[]{0, 1, 2}, triangle.edgeIndices);
        assertVec3Equals(new Vec3(0, 0, 0), triangle.centre, delta);
    }

    @Test
    public void constructor2() {
        var mesh = new TriangleMesh(
                new NavMeshContainer(
                        new int[]{0, 1, 2, 1, 2, 3},
                        new Vec3[]{
                                new Vec3(0, 0, 0),
                                new Vec3(3, 0, 0),
                                new Vec3(0, 0, 3),
                                new Vec3(3, 0, 3)
                        }
                )
        );

        double delta = 0.00000000000001;

        // vertices
        assertNotNull(mesh.vertices);
        assertEquals(4, mesh.vertices.length);
        assertVec3Equals(new Vec3(0, 0, 0), mesh.vertices[0], delta);
        assertVec3Equals(new Vec3(3, 0, 0), mesh.vertices[1], delta);
        assertVec3Equals(new Vec3(0, 0, 3), mesh.vertices[2], delta);
        assertVec3Equals(new Vec3(3, 0, 3), mesh.vertices[3], delta);

        // edges
        assertNotNull(mesh.edges);
        assertEquals(5, mesh.edges.length);
        assertEquals(new Edge(0, 1), mesh.edges[0]);
        assertEquals(new Edge(0, 2), mesh.edges[1]);
        assertEquals(new Edge(1, 2), mesh.edges[2]);
        assertEquals(new Edge(1, 3), mesh.edges[3]);
        assertEquals(new Edge(2, 3), mesh.edges[4]);

        // faces
        assertNotNull(mesh.faces);
        assertEquals(2, mesh.faces.length);

        ConvexPolygon triangle = mesh.faces[0];
        assertEquals(0, triangle.index);
        assertArrayEquals(new int[]{0, 1, 2}, triangle.edgeIndices);
        assertVec3Equals(new Vec3(1, 0, 1), triangle.centre, delta);

        triangle = mesh.faces[1];
        assertEquals(1, triangle.index);
        assertArrayEquals(new int[]{2, 3, 4}, triangle.edgeIndices);
        assertVec3Equals(new Vec3(2, 0, 2), triangle.centre, delta);
    }


    @Test
    public void constructorFailInvalidTriangle() {
    	assertThrows(IllegalArgumentException.class, 
    		() -> { 
    	        var mesh = new TriangleMesh(
    	                new NavMeshContainer(
    	                        new int[]{0, 1, 1}, // <--- this will not instantiate to a triangle
    	                        new Vec3[]{
    	                                new Vec3(2, 0, 3),
    	                                new Vec3(4, 0, 0),
    	                                new Vec3(-6, 0, -3)
    	                        }
    	                )
    	        );
    		} 
    	) ;
    }

    @Test
    public void constructorFailNonExistingTriangle() {
    	assertThrows(IllegalArgumentException.class, 
    		() -> { 
    			var mesh = new TriangleMesh(
    	                new NavMeshContainer(
    	                        new int[]{0, 1, 2, 1, 2, 3}, // <--- vertices[3] does not exist
    	                        new Vec3[]{
    	                                new Vec3(2, 0, 3),
    	                                new Vec3(4, 0, 0),
    	                                new Vec3(-6, 0, -3)
    	                        }
    	                )
    	        ); 
    		} 
    	) ;
        
    }

    @Test
    public void constructorFailDuplicateVertex() {
    	assertThrows(IllegalArgumentException.class, 
    		() -> { 
    	        var mesh = new TriangleMesh(
    	                new NavMeshContainer(
    	                        new int[]{0, 1, 2},
    	                        new Vec3[]{
    	                                new Vec3(2, 0, 3),
    	                                new Vec3(4, 0, 0),
    	                                new Vec3(2, 0, 3)
    	                        }
    	                )
    	        ) ;
    		} 
    	) ;
    }


}
