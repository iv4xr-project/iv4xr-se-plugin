/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package pathfinding;

import helperclasses.astar.Astar;
import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.mesh.TriangleMesh;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

/**
 * Pathfinder class that stores a navMesh and its composed graph
 */
public class Pathfinder
{

    public TriangleMesh navmesh;
    public TriangleGraph graph;
    //List<Vec3> path;
    //Integer[] edges;
    Vec3 prev;
    int left;
    int right;
    int leftend;
    int rightend;
    int lastvert;
    int lefti;
    int righti;
    List<Integer> leftverts;
    List<Integer> rightverts;

    /**
     * The constructor takes a NavmeshContainer (JSON) object as input.
     * This container is used to create the navMesh.
     * This navMesh is then used to create a graph.
     *
     * @param mesh
     */
    public Pathfinder(NavMeshContainer mesh)
    {
        this.navmesh = new TriangleMesh(mesh);
        this.graph = new TriangleGraph(navmesh);

    }

    /**
     * @param from: The starting point of the path.
     * @param to:   The end point of the path.
     * @param knownVertices: The nodes known to the agent when pathfinding
     * @param blockedNodes: A set of nodes which are blocked and cannot be used for the pathfinding
     * @return Triangle indices that the agent will traverse on his path
     */
    public Integer[] trianglePath(Vec3 from, Vec3 to, boolean[] knownVertices, HashSet<Integer> blockedNodes)
    {
        Astar astar = new Astar(this.graph, from, to, knownVertices, blockedNodes);
        return astar.getPath();
    }

    /**
     * This method takes 2 vectors and uses simple stupid funnel algorithm to calculate an efficient path from one vector to the other.
     *
     * @param from: The starting point of the path.
     * @param to:   The end point of the path.
     * @param knownVertices: A boolean array which contains a boolean for each vertice whether the agent has seen the vertice
     * @param blockedNodes: A set of nodes which are blocked and cannot be used for pathfinding
     * @return:     An array of vectors which form a path between the starting point and end point.
     */
    public Vec3[] navigate(Vec3 from, Vec3 to, boolean[] knownVertices, HashSet<Integer> blockedNodes)
    {
        Astar astar = new Astar(this.graph, from, to, knownVertices, blockedNodes);
        Integer[] pathIndices = astar.getPath();
        List<Vec3> path = new ArrayList<>();
        path.add(to);

        //if the length of the path equals 1 then no waypoint generation is needed
        if(pathIndices.length == 1) return new Vec3[]{to};

        Integer[] edges = getEdges(pathIndices);

        prev = to;
        int[] leftright = getleftright(edges[0], prev);
        left = leftright[0];
        right = leftright[1];
        leftend = left;
        rightend = right;
        lastvert = -1;
        lefti = 1;
        righti = 1;
        leftverts = new ArrayList<>();
        rightverts = new ArrayList<>();

        for (int i = 1; i < edges.length; i++)
        {
            int[] verts = navmesh.edges[edges[i]].vertices;

            for (int j = 0; j <= 1; j++)
            {
                if (verts[j] == left)
                {
                    right = verts[1 - j];
                    rightverts.add(right);
                    if (isLeft(prev, navmesh.vertices[leftend], navmesh.vertices[right]))
                    {
                        i = leftCorner(edges,path,i);
                        break;
                    }
                    if (isLeft(prev, navmesh.vertices[rightend], navmesh.vertices[right]))
                    {
                        rightverts.clear();
                        rightend = right;
                        righti = i;
                    }
                    break;
                }
                if (verts[j] == right)
                {
                    left = verts[1 - j];
                    leftverts.add(left);
                    if (isRight(prev, navmesh.vertices[rightend], navmesh.vertices[left]))
                    {
                        i = rightCorner(edges,path,i);
                        break;
                    }
                    if (isRight(prev, navmesh.vertices[leftend], navmesh.vertices[left]))
                    {
                        leftverts.clear();
                        leftend = left;
                        lefti = i;
                    }
                    break;
                }
            }
        }
        // Check if there are any walls blocking movement between from and the last node in path, and add the appropriate corners to path to fix this.
        if (lastvert != leftend && lastvert != rightend)
        {
            if (isLeft(prev, navmesh.vertices[leftend], from))
            {
                path.add(navmesh.vertices[leftend]);
                for (int i = 0; i < leftverts.size(); i++)
                {
                    if (isRight(navmesh.vertices[leftend], navmesh.vertices[leftverts.get(i)], from))
                        break;
                    path.add(navmesh.vertices[leftverts.get(i)]);
                    leftend = leftverts.get(i);
                }
            }
            else if (isRight(prev, navmesh.vertices[rightend], from))
            {
                path.add(navmesh.vertices[rightend]);
                for (int i = 0; i < rightverts.size(); i++)
                {
                    if (isLeft(navmesh.vertices[rightend], navmesh.vertices[rightverts.get(i)], from))
                        break;
                    path.add(navmesh.vertices[rightverts.get(i)]);
                    rightend = rightverts.get(i);
                }
            }
        }
        return path.toArray(new Vec3[path.size()]);
    }

    /**
     * This function handles taking a corner to the left in the pathfinding
     * @param i: The index of the edge that the pathfinding is currently at
     * @return The index of the edge that the pathfinding is at after the corner
     */
    int leftCorner(Integer[] edges, List<Vec3> path, int i)
    {
        leftverts.clear();
        path.add(navmesh.vertices[leftend]);
        prev = navmesh.vertices[leftend];
        lastvert = leftend;
        for (int k = lefti + 1; k < edges.length; k++)
        {
            int[] verts2 = navmesh.edges[edges[k]].vertices;
            // Skip the edges which contain the corner vector
            if (verts2[0] == leftend || verts2[1] == leftend)
                continue;
            int[] leftright = getleftright(edges[k], prev);
            left = leftright[0];
            right = leftright[1];
            i = k;
            lefti = i;
            righti = i;
            leftend = left;
            rightend = right;
            break;
        }
        return i;
    }

    /**
     * This function handles taking a corner to the right in the pathfinding
     * @param i: The index of the edge that the pathfinding is currently at
     * @return The index of the edge that the pathfinding is at after the corner
     */
    int rightCorner(Integer[] edges, List<Vec3> path, int i)
    {
        rightverts.clear();
        path.add(navmesh.vertices[rightend]);
        prev = navmesh.vertices[rightend];
        lastvert = rightend;
        for (int k = righti + 1; k < edges.length; k++)
        {
            int[] verts2 = navmesh.edges[edges[k]].vertices;
            // Skip the edges which contain the corner vector
            if (verts2[0] == rightend || verts2[1] == rightend)
                continue;
            int[] leftright = getleftright(edges[k], prev);
            left = leftright[0];
            right = leftright[1];
            i = k;
            lefti = i;
            righti = i;
            leftend = left;
            rightend = right;
            break;
        }
        return i;
    }

    /**
     *
     * @param pathIndices: The indices of the triangles on the path that was generated by astar
     * @return The indices of the edges between the triangles. (The "gates" through which the path must pass)
     */
    Integer[] getEdges(Integer[] pathIndices)
    {
        Integer[] edges = new Integer[pathIndices.length - 1];
        for(int i = pathIndices.length - 1; i > 0; i--)
        {
            int[] edges1 = navmesh.faces[pathIndices[i]].edgeIndices;
            int[] edges2 = navmesh.faces[pathIndices[i - 1]].edgeIndices;
            for(int j = 0; j < edges1.length; j++)
                for(int k = 0; k < edges2.length; k++)
                    if (edges1[j] == edges2[k])
                    {
                        edges[i - 1] = edges1[j];
                        j = edges1.length;
                        break;
                    }
        }
        return edges;
    }

    /**
     *
     * @param edge An index of an edge in navmesh.edges
     * @param prev
     * @return An array containing the 2 vertices of edge, sorted based on which is to the left
     * and which is to the right from the perspective of prev
     */
    int[] getleftright(Integer edge, Vec3 prev)
    {
        int left = navmesh.edges[edge].vertices[0];
        int right = navmesh.edges[edge].vertices[1];
        if (isLeft(prev, navmesh.vertices[left], navmesh.vertices[right]))
        {
            int temp = left;
            left = right;
            right = temp;
        }
        int[] retval = new int[2];
        retval[0] = left;
        retval[1] = right;
        return retval;
    }

    /**
     * @param lineStart
     * @param lineEnd
     * @param point
     * @return A boolean, indicating whether or not point was to the left of the line between lineStart and lineEnd
     */
    public boolean isLeft(Vec3 lineStart, Vec3 lineEnd, Vec3 point)
    {
        return ((lineEnd.x - lineStart.x) * (point.z - lineStart.z) - (lineEnd.z - lineStart.z) * (point.x - lineStart.x)) >= 0;
    }

    /**
     * @param lineStart
     * @param lineEnd
     * @param point
     * @return A boolean, indication whether or not point was to the right of the line between lineStart and lineEnd
     */
    public boolean isRight(Vec3 lineStart, Vec3 lineEnd, Vec3 point)
    {
        return ((lineEnd.x - lineStart.x) * (point.z - lineStart.z) - (lineEnd.z - lineStart.z) * (point.x - lineStart.x)) <= 0;
    }
}
