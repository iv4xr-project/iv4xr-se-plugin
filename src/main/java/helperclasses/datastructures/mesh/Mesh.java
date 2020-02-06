/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.datastructures.mesh;

/**
 * This is the basic stuct of the Mesh
 *
 * @author Maurin 11/11/2019
 */

import helperclasses.Helper;
import helperclasses.datastructures.Vec3;

public abstract class Mesh {
    public Vec3[] vertices;
    public Edge[] edges;
    public ConvexPolygon[] faces;

    @Override
    public String toString() {
        int max = 5;
        return Helper.StringArray(vertices, max) + "\n" + Helper.StringArray(edges, max) + "\n" + Helper.StringArray(faces, max);
    }


}
