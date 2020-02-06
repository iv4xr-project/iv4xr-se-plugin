/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.mesh;

import helperclasses.Helper;
import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.linq.QArrayList;
import pathfinding.NavMeshContainer;

import java.util.HashMap;

/**
 * This class is used to represent the navMesh in an efficient way in terms of memory and data requests
 *
 * @author Maurin 11/11/2019
 */
public class TriangleMesh extends Mesh {

    /**
     * The constructor takes a triangulation struct from Unity.
     *
     * @param triangulation this is a navMesh triangulation received from Unity
     */
    public TriangleMesh(NavMeshContainer triangulation) {

        if (!new QArrayList<Vec3>(triangulation.vertices).isDistinct())
            throw new IllegalArgumentException("There are duplicates in the vertex array!");

        //The triangles are not stored in tuples.
        // triangulation.indices has a format like: [t0v0, t0v1, t0v2, t1v0, t1v1, t1v2, .. ]
        int triangleCount = triangulation.indices.length / 3;
        this.vertices = triangulation.vertices;

        // Temporary edge data set (we don't know how many edges the mesh will have). Every edge can only have one edge index value
        HashMap<Edge, Integer> edgeIndices = new HashMap<>(); //

        // create the edges and also fill the edgeIndices hashMap
        this.edges = createEdges(triangleCount, triangulation.indices, edgeIndices);

        for (Edge e : this.edges)
            if (e.vertices[0] >= this.vertices.length || e.vertices[1] >= this.vertices.length)
                throw new IllegalArgumentException("The vertex in " + e.toString() + " does not exist!");

        // now we can use the hashMap to create the actual faces using the edge indices
        this.faces = createTriangles(triangleCount, triangulation.indices, edgeIndices);
    }

    /**
     * This method generates the edges while filling the hashMap to prevent duplicates.
     * The hashMap can later be used by 'createTriangles' to find the edge indices really fast.
     *
     * @param triangleCount final length of this.faces
     * @param indices       indices of the vertices that make up the triangle
     * @param edgeIndices   hashMap to find the edge indices fast
     * @return Edge structs that make up the mesh
     */
    private Edge[] createEdges(int triangleCount, int[] indices, HashMap<Edge, Integer> edgeIndices) {

        QArrayList<Edge> edgeList = new QArrayList<>();

        for (int triangle = 0; triangle < triangleCount; triangle++) {
            int vstart = triangle * 3;

            // loop over the vertices that belong to this triangle
            // for a triangle this executes 3 times
            for (int from = vstart; from < vstart + 3; from++) {
                for (int to = from + 1; to < vstart + 3; to++) {

                    // Create the edge
                    Edge e = new Edge(indices[from], indices[to]);

                    // if we have not seen this edge, add it to the list and save its index value in the hashmap
                    if (!edgeIndices.containsKey(e)) {
                        edgeIndices.put(e, edgeList.size()); // store the new index
                        edgeList.add(e); // add the edge at this index
                    }
                }
            }
        }

        return edgeList.toArray(new Edge[0]);
    }

    /**
     * This method generates the faces by using the hashMap for efficiency.
     *
     * @param triangleCount final length of this.faces
     * @param indices       indices of the vertices that make up the triangle
     * @param edgeIndexHash hashMap to find the edge indices fast
     * @return Triangle structs that make up the mesh
     */
    private ConvexPolygon[] createTriangles(int triangleCount, int[] indices, HashMap<Edge, Integer> edgeIndexHash) {

        ConvexPolygon[] triangles = new ConvexPolygon[triangleCount];

        for (int triangle = 0; triangle < triangleCount; triangle++) {
            int vstart = triangle * 3;

            QArrayList<Integer> edgeIndicesTemp = new QArrayList<>();

            // loop over the vertices that belong to this triangle
            for (int from = vstart; from < vstart + 3; from++) {
                for (int to = from + 1; to < vstart + 3; to++) {
                    Edge edge = new Edge(indices[from], indices[to]); // create edge
                    int index = edgeIndexHash.get(edge); // get the index of the edge from the hashMap
                    edgeIndicesTemp.add(index);
                }
            }

            // add a new triangle to the array, and let the face remember its own index value
            // this is needed for fast path finding in the graph
            triangles[triangle] = new ConvexPolygon(triangle, this.vertices, this.edges, Helper.primitive(edgeIndicesTemp)); // create the triangle
        }

        return triangles;
    }
}
