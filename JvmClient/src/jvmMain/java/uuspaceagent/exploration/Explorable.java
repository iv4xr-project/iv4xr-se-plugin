package uuspaceagent.exploration;

import eu.iv4xr.framework.spatial.Vec3;

public interface Explorable<NodeId> {
    /**
     * An iterable that can be used by pathfinders to explore a node's connections.
     *
     * @param id the node to inspect.
     * @return an iterable of the connected neighbours.
     */
    public Iterable<NodeId> neighbours(NodeId id);

    /**
     * An iterable that can be used by pathfinders to explore a node's connections.
     * Different from normal {@link #neighbours}, it allows unexplored nodes as neighbours
     *
     * @param id the node to inspect.
     * @return an iterable of the connected neighbours.
     */
    public Iterable<NodeId> neighbours_explore(NodeId id);

    /**
     * The estimated distance between two arbitrary vertices.
     *
     * @param from: the node to travel from.
     * @param to: the node to travel to.
     * @return the estimated distance.
     */
    public float heuristic(NodeId from, NodeId to);

    public float heuristic(NodeId from, Vec3 to);

    /**
     * The distance between two connected vertices.
     *
     * @return the measured distance.
     */
    public float distance(NodeId from, NodeId to);

    /**
     * If the real_neighbour is bigger than the target node,
     * creates a fake neighbour with the same size inside the real one.
     *
     * @param node: the node to create a neighbour for.
     * @param real_neighbour: the node that is or will contain the fake neighbour.
     * @return the measured size.
     */
    public NodeId phantom_neighbour(NodeId node, NodeId real_neighbour);

    /**
     * The exploration status of a vertex.
     *
     * @param node: the node to check.
     * @return whether the node is explored or not
     */
    public boolean isUnknown(NodeId node);

    /**
     * Updates all unknown nodes inside the observation radius to have the OPEN label.
     * @param pos: the position of the agent
     * @param observation_radius: the observation radius of the agent
     */
    public void updateUnknown(Vec3 pos, float observation_radius);
}
