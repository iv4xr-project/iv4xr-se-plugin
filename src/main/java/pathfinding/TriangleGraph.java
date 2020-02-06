/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package pathfinding;

import environments.EnvironmentConfig;
import helperclasses.astar.Graph;
import helperclasses.datastructures.Vec3;
import helperclasses.datastructures.functions.Function0;
import helperclasses.datastructures.linq.QArrayList;
import helperclasses.datastructures.mesh.ConvexPolygon;
import helperclasses.datastructures.mesh.TriangleMesh;

import java.util.*;

public class TriangleGraph extends Graph {

    /**
     * The triangle centres represent the nodes of the graph.
     * The neighbors are generated during object construction.
     */
    public QArrayList<ConvexPolygon> nodes; // QArrayList for sorting speed
    private int[][] neighbours;
    private TriangleMesh mesh;// the mesh

    /**
     * The constructor takes a TriangleMesh to generate the neighbors
     *
     * @param mesh TriangleMesh
     */
    public TriangleGraph(TriangleMesh mesh) {
        this.mesh = mesh;
        this.nodes = new QArrayList<>(mesh.faces);
        this.neighbours = new int[mesh.faces.length][];

        // for every edge we keep a list of triangle indices the edge belongs to
        QArrayList<QArrayList<Integer>> trianglePairs = QArrayList.fill(mesh.edges.length, (Function0<QArrayList<Integer>>) QArrayList::new); // create new instances of lists
        for (ConvexPolygon triangle : this.nodes) { // triangle
            for (int edgeIndex : triangle.edgeIndices) // 3 edges
            {
                trianglePairs.get(edgeIndex).add(triangle.index);
            }
        }

        ArrayList<HashSet<Integer>> neighbours = new ArrayList<HashSet<Integer>>();
        for (int i = 0; i < mesh.faces.length; i++) {
            neighbours.add(new HashSet<>());
        }

        // Add all neighbours of a node to their own neighbours HashSet.
        for (var pair : trianglePairs.where(pair -> pair.size() >= 2)) {
            int n = pair.size();
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    int t0 = pair.get(i);
                    int t1 = pair.get(j);
                    neighbours.get(t0).add(t1);
                    neighbours.get(t1).add(t0);
                }
            }
        }

        // Convert neighbour data to Array
        for (int i = 0; i < mesh.faces.length; i++) {
            this.neighbours[i] = new int[neighbours.get(i).size()];
            int index = 0;
            for (int neighbour : neighbours.get(i)) {
                this.neighbours[i][index] = neighbour;
                index++;
            }
        }
    }

    /**
     * Get the unexplored nodes which neighbour to the mesh of known nodes from our current position
     * @param idFrom: The id of the node from which we want to start the mesh of known nodes
     * @param knownVertices: A boolean list of vertices which are known to the agent
     * @param blockedNodes: a set of nodes which are blocked and can not be used for pathfinding
     * @return A array of indices of nodes which are unknown neighbours of the known nodes
     */
    public HashMap<Integer, Integer> getUnexploredNeighbouringNodes(int idFrom, boolean[] knownVertices, HashSet<Integer> blockedNodes){
        //use breadth first search to get a list of all reachable nodes
        HashMap<Integer, Integer> closedList = getNeighbouringNodesMesh(idFrom, knownVertices, blockedNodes);

        //make a list of neighbouring nodes which are not known to the agent
        HashMap<Integer, Integer> unknownNeighbours = new HashMap<>();

        for (int i:closedList.keySet()) {
            int[] neighbours = getNeighbours(i);
            for (int n: neighbours) {
                if(!closedList.containsKey(n) && !blockedNodes.contains(n)) unknownNeighbours.put(n,i);
            }
        }

        return unknownNeighbours;
    }

    /**
     * The method will return a list of nodes which are reachable from the id
     * @param idFrom: node from which to start
     * @param knownVertices: A boolean list of vertices which are known to the agent
     * @param blockedNodes: a set of nodes which are blocked and can not be used for pathfinding
     * @return The closed list with the list of known nodes, the keys are the indices the values are unused
     */
    public HashMap<Integer, Integer> getNeighbouringNodesMesh(int idFrom, boolean[] knownVertices, HashSet<Integer> blockedNodes){
        //use breath first search to get a list of all reachable nodes
        HashMap<Integer, Integer> closedList = new HashMap<>();//store the expanded node id's as keys
        Queue<Integer> idQueue = new PriorityQueue<>();//the queue which will be used for the breath first search

        idQueue.add(idFrom);
        while (!idQueue.isEmpty()){
            int node = idQueue.remove();
            //add the node the the closed list
            closedList.put(node, 0);

            //get the known neighbours of this node
            int[] knownNeighbours = getKnownNeighbours(node, knownVertices, blockedNodes);

            //add the new nodes the the queue if they are not already explored
            for (int i: knownNeighbours) {
                if(!closedList.containsKey(i)) idQueue.add(i);
            }
        }
        return closedList;
    }

    /**
     * Weight of the connection between two triangle centres
     *
     * @param idFrom triangle id
     * @param idTo   triangle id
     * @return Real squared distance between two triangle centres
     */
    @Override
    public Double weightOfConnection(int idFrom, int idTo) {
        return nodes.get(idFrom).centre.distanceSquared(nodes.get(idTo).centre);
    }

    /**
     * Neighbours of a triangle
     *
     * @param id of the triangle
     * @return The neighbour id's of the triangle
     */
    @Override
    public int[] getNeighbours(int id) {
        return neighbours[id];
    }

    /**
     * Get the known neighbours of a triangle
     *
     * @param id: id of the triangle
     * @param knownVertices: the vertices from the triangle which are known to the agent
     * @param blockedNodes: a set of nodes which are blocked and can not be used for pathfinding
     * @return The neighbour id's of the triangle
     */
    @Override
    public int[] getKnownNeighbours(int id, boolean[] knownVertices, HashSet<Integer> blockedNodes) {
        Stack<Integer>knownNeighbours = new Stack<>();
        for(int i = 0; i < neighbours[id].length; i++){
            int count = 0; //keep track of how many nodes of the triangle are known to the agent
            for(int j = 0; j < 3; j++){
                //get the edge
                int edge = nodes.get(neighbours[id][i]).edgeIndices[j];
                //if the vertice from the edge is known add one to the count
                if(knownVertices[mesh.edges[edge].vertices[0]]) count++;
                if(knownVertices[mesh.edges[edge].vertices[1]]) count++;
            }
            //add the node if at least 2 neighbours are known (count 4 because edges count vertices twice)
            if(count >=4 && !blockedNodes.contains(neighbours[id][i])){
                knownNeighbours.add(neighbours[id][i]);
            }

        }
        //change the format of the neighbours into a array
        int[] r = new int[knownNeighbours.size()];
        int i = 0;
        while(!knownNeighbours.empty()){
            r[i] = knownNeighbours.pop();
            i++;
        }
        return r;
    }

    @Override
    public Vec3 toVec3(int id) {
        return nodes.get(id).centre;
    }

    /**
     * Request the centre of triangle id
     *
     * @param position index of triangle
     * @return Real position of the triangle centre
     */
    @Override
    public Integer vecToNode(Vec3 position) {
        // TODO: use quadtree to find the correct triangle
        //return the closest node with a maximum y distance of 1.5
        var withinNodes = nodes.where(n -> pointInTriangleXZ(position, n));
        if(withinNodes.size() == 0)
            withinNodes = nodes;
        return withinNodes.orderBy(t -> position.distanceSquared(t.centre)).first().index;
    }

    // form https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle

    private double sign(Vec3 p1, Vec3 p2, Vec3 p3)
    {
        return (p1.x - p3.x) * (p2.z - p3.z) - (p2.x - p3.x) * (p1.z - p3.z);
    }

    public boolean pointInTriangleXZ(Vec3 pt, ConvexPolygon triangle) {

        Vec3 v1 = mesh.vertices[triangle.vertices[0]];
        Vec3 v2 = mesh.vertices[triangle.vertices[1]];
        Vec3 v3 = mesh.vertices[triangle.vertices[2]];

        double d1, d2, d3;
        boolean has_neg, has_pos;

        d1 = sign(pt, v1, v2);
        d2 = sign(pt, v2, v3);
        d3 = sign(pt, v3, v1);

        has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(has_neg && has_pos);
    }
}
