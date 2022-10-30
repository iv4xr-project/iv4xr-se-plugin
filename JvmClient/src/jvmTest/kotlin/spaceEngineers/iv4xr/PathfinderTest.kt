package spaceEngineers.iv4xr

import eu.iv4xr.framework.extensions.pathfinding.AStar
import spaceEngineers.iv4xr.navigation.NavigableGraph
import spaceEngineers.model.Vec3F
import spaceEngineers.navigation.*
import kotlin.test.Test
import kotlin.test.assertEquals

fun makeNode(id: Int, position: Vec3F): Node {
    return Node(id.toString(), position)
}

fun makeEdge(from: Int, to: Int): Edge {
    return Edge(from = from.toString(), to = to.toString(), id = makeEdgeId(from, to), data = "Unit")
}

class PathfinderTest {

    @Test
    fun findPath() {
        val nodes = listOf(
            makeNode(0, Vec3F(0, 1, 0)),
            makeNode(1, Vec3F(1, 1, 0)),
            makeNode(2, Vec3F(2, 2, 0)),
            makeNode(3, Vec3F(3, 1, 0)),
            makeNode(4, Vec3F(2, 0, 0)),
            makeNode(5, Vec3F(4, 0, 0)),
        )
        val edges = listOf(
            makeEdge(0, 1), makeEdge(1, 2), makeEdge(2, 3),
            makeEdge(3, 5), makeEdge(1, 4), makeEdge(4, 5)
        )
        val navGraph = NavGraph(nodes, edges)
        val navigableGraph = NavigableGraph(navGraph)

        val pathfinder = AStar<NodeId>()

        val path = pathfinder.findPath(navigableGraph, 1.toString(), 3.toString())
        assertEquals(3, path.size)
        assertEquals(1.toString(), path[0])
        assertEquals(2.toString(), path[1])
        assertEquals(3.toString(), path[2])

        val road = pathfinder.findPath(navigableGraph, 0.toString(), 5.toString())
        assertEquals(4, road.size)
        assertEquals(4.toString(), road[2])
    }
}
