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
        Vec3 center = new Vec3(e.center.x, e.center.y, e.center.z) ;
       
        // WP note:
        // HACK: the original algorithm is problematical because the navigation triangles
        // are too large; so we may end up blocking too large section of navmesh, leaving
        // the agent to have no route to its destination.
        // A possible solution is to spilt the initial navmesh to make the traingle smallers,
        // at the cost of slowing down path planning.
        // I currently will just introduce this hack here, that solves the problem,
        // provided THE ONLY type of entity that is considered as solid is DOOR.
        // The hack is by making the door bounding to appear as 0. 
        // I also discover that the "center" of the door is not literally its center;
        // rather, its "position" X and Z coord is the X/Y center. 
        // So ... I add 0.5 to center.x to move it to the actual center.
        //
        // For example a door center = (9.5,1,6) and its "position" is (10,0,6).
        // so.. in the calculaition below, I add 0.5 to x.center:
        if (e.tag.equals("Door")) { 
        	// HACK!
        	//if (sizeX < sizeZ) sizeX = 0d ;
            //else sizeZ = 0d ;
        	center.x += 0.5 ;
        	sizeX = 0 ;
        	sizeZ = 0 ;
        }
             
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
        //System.out.println("oooo " + center.y + " , " + e.extents.y) ;
        
        for(int i = 0; i < m.faces.length; i++){
            //check if the entity and the navmesh triangle are on the same floor
        	//System.out.println("zzzz " + entityY + " vs " + m.faces[i].centre.y) ;
            if(Math.abs(m.faces[i].centre.y - entityY) < 0.55){
                Vec3[] triangleVertices = getTriangleVertices(m, i);
                //System.out.println("xxxx " + triangleVertices.length) ;
                
                //check if they intersect
                boolean intersect = TrianglePolygonIntersection.isPolygonIntersectingTriangle(
                        triangleVertices[0],triangleVertices[1],triangleVertices[2], points);
                if(intersect) {
                	//System.out.println("### Found intersection " + e.id + " at " + e.center + " with: ") ;
                	//System.out.println("        " + triangleVertices[0]) ;
                	//System.out.println("        " + triangleVertices[1]) ;
                	//System.out.println("        " + triangleVertices[2]) ;
                	blockedNodes.add(i);
                }
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
    public static Vec3[] getTriangleVertices(TriangleMesh m, int id){
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
