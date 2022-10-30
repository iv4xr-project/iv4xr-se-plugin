package spaceEngineers.iv4xr.navigation

import eu.iv4xr.framework.extensions.pathfinding.Navigatable
import spaceEngineers.navigation.NavGraph
import spaceEngineers.navigation.NodeId
import spaceEngineers.navigation.RichNavGraph

class NavigableGraph(navGraph: NavGraph) : Navigatable<NodeId> {
    private val richNavGraph = RichNavGraph(navGraph)

    override fun neighbours(id: NodeId): MutableIterable<NodeId> {
        return richNavGraph.neighbours(id).toMutableSet()
    }

    override fun heuristic(from: NodeId, to: NodeId): Float {
        return richNavGraph.distance(from, to)
    }

    override fun distance(from: NodeId, to: NodeId): Float {
        if (!richNavGraph.containsEdge(from, to))
            error("Distance is only applicable to neighbours, the graph does not contain the edge ($from, $to).")
        return richNavGraph.distance(from, to)
    }

    fun node(nodeId: NodeId) = richNavGraph.node(nodeId)
}
