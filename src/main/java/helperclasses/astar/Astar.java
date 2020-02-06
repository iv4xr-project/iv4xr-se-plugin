/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.astar;

import helperclasses.datastructures.Tuple;
import helperclasses.datastructures.TupleWithPriority;
import helperclasses.datastructures.Vec3;

import java.util.*;

/**
 * This class implements a variant of the A* algorithm which is used to turn a graph,
 * starting position and a goal into a path from the start to the goal. If this path is possible, an edge list will
 * be given in order from 0-end node to n-start node. If the start location and/or goal location is/are not known
 * in the belief state or there is no possible path,
 * then the path will return null.
 */
public class Astar {
    private boolean[] knownVertices; //A boolean for each vertice whether the node is known to the agent
    private Integer[] path = null;//Store the path here once found
    private Graph g;//Store the graph which is use to calculate the path
    private HashMap<Integer, Tuple<Integer, Double>> closedList;//keep track of node id's if a node is expanded and what the previous node was
    private HashSet<Integer> blockedNodes;//a set of nodes which cannot be used for pathfinding

    /**
     * The constructor will take the necessary input to try to calculate a path between start and goal
     *
     * @param g:     The graph on which the pathfinding can be executed
     * @param start: The position where the pathfinding will start
     * @param knownVertices: A boolean array which contains a boolean for each vertice whether the vertice is known to the agent
     * @param blockedNodes: A set of nodes which are blocked and can not be used for pathfinding
     * @param goal:  The goal position where the pathfinding should end
     */
    public Astar(Graph g, Vec3 start, Vec3 goal, boolean[] knownVertices, HashSet<Integer> blockedNodes) {
        //turn start and end into node positions
        Integer startNode = g.vecToNode(start);
        Integer goalNode = g.vecToNode(goal);
        this.knownVertices = knownVertices;
        this.blockedNodes = blockedNodes;
        this.g = g;

        //check if the node positions are known in the belief state if not throw an exception
        if (startNode == null) throw new IllegalArgumentException("The starting node could not be found");
        if (goalNode == null) throw new IllegalArgumentException("The goal node could not be found");

        //check if the nodes are equal if so add only the goal to the path
        if (startNode.equals(goalNode)) {
            path = new Integer[]{goalNode};
            return;
        }

        //calculate the path
        calcPath(startNode, goalNode, g.toVec3(goalNode));
    }

    /**
     * This is the point of entry which will return a path from start to goal or return null if impossible
     *
     * @return An edge list of nodes between the start and goal location as specified by the constructor
     */
    public Integer[] getPath() {
        return path;
    }

    /**
     * This method will try to calculate a path from the start node to the goal node
     *
     * @param start:   Id of the start node
     * @param goal:    Id of the goal node
     * @param goalVec: Goal location
     */
    private void calcPath(int start, int goal, Vec3 goalVec) {
        //start a fringe to keep track of current open nodes with their priority their cost and from which node they came
        PriorityQueue<TupleWithPriority<Tuple<Integer, Integer>>> fringe = new PriorityQueue<>();
        //Start a new closed list
        closedList = new HashMap<Integer, Tuple<Integer, Double>>();

        //start the first iteration of the algorithm
        closedList.put(start, new Tuple(null, Double.valueOf(0)));
        for (int id : g.getKnownNeighbours(start, knownVertices, blockedNodes)) {
            //add the neighbours to the fringe
            double nextWeight = g.weightOfConnection(start, id) + g.heuristic(id, goalVec);
            fringe.add(new TupleWithPriority<Tuple<Integer, Integer>>(new Tuple<>(id, start), nextWeight));
        }

        //start the repeating part of the algorithm
        while (!fringe.isEmpty()) {
            TupleWithPriority<Tuple<Integer, Integer>> current = fringe.remove();
            Tuple<Integer, Integer> nodeInfo = current.object;//tuple with id's object1 is to and object2 from
            double previousWeight = closedList.get(nodeInfo.object2).object2;

            //weight between this node and origin calculated by value from closed list + dist between previous and this node
            double weight = previousWeight + g.weightOfConnection(nodeInfo.object2, nodeInfo.object1);
            //add the new expanded node to the closed list if it has a better value
            Tuple<Integer, Double> currentClosedListVal = closedList.get(nodeInfo.object1);
            if (currentClosedListVal == null || currentClosedListVal.object2 > weight) {
                Tuple<Integer, Double> element = new Tuple<>(nodeInfo.object2, weight);
                closedList.put(nodeInfo.object1, element);
            }

            //check if we have found the goal
            if (nodeInfo.object1 == goal) {
                reconstruct(goal);
                return;
            }

            //otherwise add new options
            for (int id : g.getKnownNeighbours(nodeInfo.object1, knownVertices, blockedNodes)) {
                //only add new options to the fringe if they are viable for improvement eg don't add elements which
                //are already in the closed list
                if (!closedList.containsKey(id)) {
                    //get the weight to the neighbor through the current node
                    double nextWeight = weight + g.weightOfConnection(nodeInfo.object1, id);
                    double priority = nextWeight + g.heuristic(id, goalVec);
                    Tuple<Integer, Integer> toFrom = new Tuple<>(id, nodeInfo.object1);
                    TupleWithPriority<Tuple<Integer, Integer>> element = new TupleWithPriority<>(toFrom, priority);
                    fringe.add(element);
                }
            }
        }

        //a path to the goal could not be found
        throw new IllegalStateException("A path from " + g.toVec3(start) + " to " + g.toVec3(goal) + " with the goal id " + goal + " could not be found");
    }

    /**
     * Reconstruct the shortest path from the closed nodes and store this path in the path variable
     *
     * @param goalId: Id of the goal node
     */
    private void reconstruct(int goalId) {
        //check if a path to the goal exists
        if (!closedList.containsKey(goalId)) {
            path = null;
            throw new IllegalArgumentException("A path between the starting node and the goal node does not exists");
        }

        //construct the path
        List<Integer> currentPath = new ArrayList<>();

        //trace back the closed list to the starting node
        Integer id = goalId;
        while (id != null) {
            currentPath.add(id);//store the node in our path
            id = closedList.get(id).object1;
        }

        //store the found path in the path variable
        path = currentPath.toArray(new Integer[currentPath.size()]);
    }
}

