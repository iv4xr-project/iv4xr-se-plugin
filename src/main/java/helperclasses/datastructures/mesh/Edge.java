/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.mesh;

import java.util.Arrays;
import java.util.Objects;

/**
 * This class stores 2 vertexIndices that make up the Edge.
 *
 * @author Maurin 11/11/2019
 */
public class Edge {

    // storing as an array so edge.vertices can be used an an iterator
    public int[] vertices = new int[2];

    /**
     * Constructor always takes exactly 2 vertex indices.
     *
     * @param vertex0 index
     * @param vertex1 index
     */
    public Edge(int vertex0, int vertex1) {

        // Exceptions
        if (vertex0 == vertex1) throw new IllegalArgumentException("Vertex indices can not be the same in an Edge!");
        if (vertex0 < 0) throw new IllegalArgumentException("Vertex index of the first argument can not be negative!");
        if (vertex1 < 0) throw new IllegalArgumentException("Vertex index of the second argument can not be negative!");

        // storing the smallest value first for hashing purposes and
        if (vertex0 < vertex1) {
            this.vertices[0] = vertex0;
            this.vertices[1] = vertex1;
        } else {
            this.vertices[0] = vertex1;
            this.vertices[1] = vertex0;
        }
    }

    /**
     * Override the hash function. In our program, the edge(0,1) should be considered the same as edge(1,0).
     * That is why we store the lowest value first.
     *
     * @return hashCode based on the vertex indices.
     */
    @Override
    public int hashCode() {
        // passing the vertices reference int the hash() method result in different outcomes, so keep it like this
        return Objects.hash(vertices[0], vertices[1]);
    }

    /**
     * Override the equals function. In our program, the edge(0,1) should be considered the same as edge(1,0).
     *
     * @param o the other object.
     * @return Boolean that indicates whether the Edge is equal to the other object.
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof Edge) && Arrays.equals(this.vertices, ((Edge) o).vertices);
    }

    /**
     * @return a compact string representation of the edge
     */
    @Override
    public String toString() {
        return String.format("Edge<%s,%s>", vertices[0], vertices[1]);
    }
}
