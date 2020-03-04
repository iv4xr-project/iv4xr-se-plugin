/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.Intersections;

import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.mesh.TriangleMesh;
import world.InteractiveEntity;

import java.util.*;

/**
 * This class holds the logic for intersections between entities and nodes
 */
public class EntityNodeIntersection {
    private static TriangleMesh m;

    /**
     * This method will determine which nodes are blocked by a given entity
     * @param e: The interactive entity
     * @param m: The triangle mesh
     * @return The nodes which are blocked
     */
    public static Integer[] getNodesBlockedByInteractiveEntity(InteractiveEntity e, TriangleMesh m){
        double sizeX = e.extents.x;//size of the square
        double sizeZ = e.extents.z;//size of the square
        Vec3 center = e.center; // the center of the square

        //define which objects are blocking
        String[] whitelistBlockingEntities = new String[]{
                "Door"
        };

        //check if the entity is blocking
        boolean valid = false;
        for(int i = 0; i< whitelistBlockingEntities.length; i++){
        	if(e.tag.equals(whitelistBlockingEntities[i])) valid = true;

        }
        if(!valid) return new Integer[0];

        //construct the 4 point rectangle which will form a bounding rectangle on the floor level
        Vec3[] points = new Vec3[]{
                Vec3.sum(center, new Vec3(-sizeX, 0, -sizeZ)),
                Vec3.sum(center, new Vec3(sizeX, 0, -sizeZ)),
                Vec3.sum(center, new Vec3(sizeX, 0, sizeZ)),
                Vec3.sum(center, new Vec3(-sizeX, 0, sizeZ))
        };

        //get the y coordinate of the bottom of the entity
        double entityY = center.y - e.extents.y;

        //TODO: once the quad tree is implemented use it here to optimize the speed of the program
        List<Integer> blockedNodes = new LinkedList<>();
        //check for each node if it blocks yes or no
        for(int i = 0; i < m.faces.length; i++){
            //check if the entity and the navmesh triangle are on the same floor
            if(Math.abs(m.faces[i].centre.y - entityY) < 0.5){
                Vec3[] triangleVertices = getTriangleVertices(m, i);

                //check if they intersect
                boolean intersect = TrianglePolygonIntersection.isPolygonIntersectingTriangle(
                        triangleVertices[0],triangleVertices[1],triangleVertices[2], points);
                if(intersect) blockedNodes.add(i);
            }
        }
        return blockedNodes.toArray(new Integer[blockedNodes.size()]);
    }

    /**
     * Return the three vertices which form the triangle
     * @param m: The triangle mesh
     * @param id: The id of the triangle
     * @return The vertices of the triangle
     */
    private static Vec3[] getTriangleVertices(TriangleMesh m, int id){
        //get the vertices from a triangle
        HashSet<Integer> indices = new LinkedHashSet<>();
        for(int i = 0; i < m.faces[id].edgeIndices.length; i++){
            indices.add(m.edges[m.faces[id].edgeIndices[i]].vertices[0]);
            indices.add(m.edges[m.faces[id].edgeIndices[i]].vertices[1]);
        }

        //return the unique vertices in an array
        Vec3[] result = new Vec3[3];
        int i = 0;
        for (Integer n: indices ) {
            result[i] = m.vertices[n];
            i++;
        }

        return result;
    }
}
