/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.mesh;

import helperclasses.Helper;
import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.linq.QArrayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * This class represents the Triangle used in our NavMesh, but it can be used for any size of polygon as long as it is convex.
 *
 * @author Maurin 11/11/2019
 */
public class ConvexPolygon {

    public int index;
    public Vec3 centre;
    public int[] edgeIndices;
    public int[] vertices;

    /**
     * The constuctor uses the predefined vertices and edges to create and store the centre as a Vec3.
     * This centre is only used for path finding and is not part of the actual Mesh.
     *
     * @param index       value in mesh.faces
     * @param vertices    reference to pre defined vertices
     * @param edges       reference to pre defined edges
     * @param edgeIndices the indices of the edges in mesh.edges that make up this polygon
     */
    public ConvexPolygon(int index, Vec3[] vertices, Edge[] edges, int[] edgeIndices) {

        if (!new QArrayList<>(Helper.toClassType(edgeIndices)).allTrue(i -> i >= 0 && i < edges.length))
            throw new IllegalArgumentException("Edge index can not be found in the edge list!");

        this.index = index;
        this.edgeIndices = edgeIndices; // store the edge indices that make up this polygon
        this.centre = calculateCentre(vertices, edges, edgeIndices);
        this.vertices = getVertexIndices(edges, edgeIndices);

        if (!isConvex(vertices, edges, edgeIndices))
            throw new IllegalArgumentException("These edges do not make a convex polygon!");
    }

    private Vec3 calculateCentre(Vec3[] vertices, Edge[] edges, int[] edgeIndices) {

        /**
         * this variable is a list that contains: (startVertex + endVertex) for every edge
         */
        var edgeSums = QArrayList.FromPrimitives(edgeIndices) // convert to Integer[]
                .select(i -> edges[i]) // select the real edges
                .select(edge -> edge.vertices) // select the vertex indices of the edges
                .select(pair -> new Vec3[]{vertices[pair[0]], vertices[pair[1]]}) // get the real vertices
                .select(Vec3::sum) // calculate the sum of these vertices
                .foldl((v1, v2) -> Vec3.sum(v1, v2), Vec3.zero()); // then add them all together to one Vec3

        /**
         * To calculate the middle of a polygon, you take the sum of all the vectors and divide it by the amount of vertices
         * In the previous step we took the sum for every edge (for simplicity). This means that every vertex is added TWICE,
         * so we will need to divide extra by a factor of 2.
         */
        return Vec3.divide(edgeSums, 2 * edgeIndices.length);
    }

    private int[] getVertexIndices(Edge[] edges, int[] edgeIndices){
        HashSet<Integer> vertices = new HashSet<>();
        for(var edge :QArrayList.FromPrimitives(edgeIndices).select(i -> edges[i]))
            for(var v : edge.vertices)
                vertices.add(v);
        int[] verticesArray = new int[3];
        int i = 0;
        for(var a : vertices)
            verticesArray[i++] = a;
        return verticesArray;
    }

    /**
     * @return Indication whether the polygon is convex and therefore valid in this class.
     */
    public boolean isConvex(Vec3[] vertices, Edge[] edges, int[] edgeIndices) {
        // not important for this project
        return true;
    }

    @Override
    public String toString() {
        return String.format("Triangle<%s, %s, %s, %s>", edgeIndices[0], edgeIndices[1], edgeIndices[2], centre.toString());
    }
}
