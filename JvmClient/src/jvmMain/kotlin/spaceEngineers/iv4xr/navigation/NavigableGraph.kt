package spaceEngineers.iv4xr.navigation

import eu.iv4xr.framework.extensions.pathfinding.Navigatable
import spaceEngineers.navigation.NavGraph
import spaceEngineers.navigation.RichNavGraph

class NavigableGraph(navGraph: NavGraph) : Navigatable<Int> {
    private val richNavGraph = RichNavGraph(navGraph)

    override fun neighbours(id: Int): MutableIterable<Int> {
        return richNavGraph.neighbours(id).toMutableSet()
    }

    override fun heuristic(from: Int, to: Int): Float {
        return richNavGraph.distance(from, to)
    }

    override fun distance(from: Int, to: Int): Float {
        if (!richNavGraph.containsEdge(from, to))
            error("Distance is only applicable to neighbours, the graph does not contain the edge ($from, $to).")
        return richNavGraph.distance(from, to)
    }
}
