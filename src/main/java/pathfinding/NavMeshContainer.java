/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package pathfinding;

import helperclasses.datastructures.Vec3;

public class NavMeshContainer {

    public int[] indices;
    public Vec3[] vertices;

    public NavMeshContainer(int[] indices, Vec3[] vertices) {
        this.indices = indices;
        this.vertices = vertices;
    }
}
