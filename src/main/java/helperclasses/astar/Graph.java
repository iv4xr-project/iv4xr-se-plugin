/*
This program has been developed by students from the bachelor Computer Science
at Utrecht University within the Software and Game project course.

©Copyright Utrecht University (Department of Information and Computing Sciences)
*/

package helperclasses.astar;

import helperclasses.datastructures.Vec3;

import java.util.HashSet;

/**
 * This class defines the way a belief state is transformed into a node based graph which can be used for pathfinding.
 * It is abstract and can therefore be adjusted for different types of environment to modify how the pathfinding will
 * find a path.
 */
public abstract class Graph {
    /**
     * This method defines the heuristic for distance between a start and a goal position vector
     *
     * @param start: Id of the start node
     * @param goal:  The Vec3 location of the goal
     * @return This will return the euclidean distance between start and goal position
     */
    public double heuristic(int start, Vec3 goal) {
        return this.toVec3(start).distanceSquared(goal);
    }

    /**
     * Return the weight of the connection between node from and node to
     *
     * @param idFrom: The id of the node from which to start
     * @param idTo:   The id of the node on which to end
     * @return Null if there is no connection between the two nodes otherwise a double representing
     * the weight of the connection
     */
    public abstract Double weightOfConnection(int idFrom, int idTo);

    /**
     * Return the id's of all neighbours of this node
     *
     * @param id: The id of the node of which the neighbours should be returned
     * @return An array of all neighbours
     */
    public abstract int[] getNeighbours(int id);

    /**
     * Return the id's of all known neighbours of this node
     *
     * @param id: The id of the node of which the neighbours should be returned
     * @param knownVertices: A list of booleans which indicate whether a node is known to the agent
     * @param blockedNodes: A set of nodes which are blocked and con not be used for pathfinding
     * @return An array of all neighbours
     */
    public abstract int[] getKnownNeighbours(int id, boolean[] knownVertices, HashSet<Integer> blockedNodes);

    /**
     * This method is used to define the pathfinding position vector for a node
     *
     * @param id: The id of the node
     * @return The position vector of the node
     */
    public abstract Vec3 toVec3(int id);

    /**
     * This method will transform a position into a corresponding node if possible
     *
     * @param position: A vector position
     * @return A node id if the position is on this node otherwise null
     */
    public abstract Integer vecToNode(Vec3 position);
}
