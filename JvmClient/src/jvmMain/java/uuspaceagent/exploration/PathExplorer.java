package uuspaceagent.exploration;

import eu.iv4xr.framework.spatial.Vec3;

import java.util.ArrayList;

public interface PathExplorer<NodeId> {
    /**
     * Finds a path in a navigatable environment.
     *
     * @param graph: The environment to find a path in.
     * @param start: The starting position of the pathfinder.
     * @param goal: The goal of the pathfinder.
     * @return An arraylist with the path. Null if no path is found.
     */
    public ArrayList<NodeId> findPath(Explorable<NodeId> graph, NodeId start, NodeId goal);

    /**
     * Finds a path to the closest unknown location in a navigatable environment.
     *
     * @param graph: The environment to find a path in.
     * @param start: The starting position of the pathfinder.
     * @return An arraylist with the path. Null if no path to an unknown location exists.
     */
    public ArrayList<NodeId> explore(Explorable<NodeId> graph, NodeId start);

    /**
     * Finds a path to the closest known location in a navigatable environment.
     *
     * @param graph: The environment to find a path in.
     * @param start: The starting position of the pathfinder.
     * @return An arraylist with the path. Null if no path to an unknown location exists.
     */
    public ArrayList<NodeId> exploreTo(Explorable<NodeId> graph, NodeId start, Vec3 goal);
}
