package spaceEngineers.iv4xr.navigation

import eu.iv4xr.framework.extensions.pathfinding.AStar
import eu.iv4xr.framework.extensions.pathfinding.Navigatable
import spaceEngineers.graph.DataNode
import spaceEngineers.graph.DirectedGraph
import spaceEngineers.model.BlockId
import spaceEngineers.model.Vec3F
import spaceEngineers.navigation.NavGraph
import spaceEngineers.navigation.NodeId
import spaceEngineers.navigation.PathFinder
import spaceEngineers.navigation.RichNavGraph


class Iv4XRAStarPathFinder : PathFinder<BlockId, Vec3F, String, String> {

    private fun DirectedGraph<BlockId, Vec3F, String, String>.toNavGraph(): NavGraph {
        return NavGraph(
            nodes = this.nodes.map { it as DataNode },
            edges = this.edges,
        )
    }


    private fun DirectedGraph<BlockId, Vec3F, String, String>.asNavigatable(): Navigatable<NodeId> {
        return NavigableGraph(
            this.toNavGraph(),
        )
    }

    override fun findPath(
        navigableGraph: DirectedGraph<BlockId, Vec3F, String, String>,
        targetNodeId: BlockId,
        startNodeId: BlockId
    ): List<BlockId> {
        val pathfinder = AStar<BlockId>()
        return pathfinder.findPath(navigableGraph.asNavigatable(), startNodeId, targetNodeId)

    }
}

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
