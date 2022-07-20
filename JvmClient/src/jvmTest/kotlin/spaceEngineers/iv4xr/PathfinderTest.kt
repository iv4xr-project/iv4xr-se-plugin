package spaceEngineers.iv4xr

import eu.iv4xr.framework.extensions.pathfinding.AStar
import spaceEngineers.iv4xr.navigation.NavigableGraph
import spaceEngineers.model.Vec3F
import spaceEngineers.navigation.Edge
import spaceEngineers.navigation.NavGraph
import spaceEngineers.navigation.Node
import spaceEngineers.navigation.NodeId
import kotlin.test.Test
import kotlin.test.assertEquals

class PathfinderTest {

    @Test
    fun findPath() {
        val nodes = listOf(
            Node(0, Vec3F(0, 1, 0)),
            Node(1, Vec3F(1, 1, 0)),
            Node(2, Vec3F(2, 2, 0)),
            Node(3, Vec3F(3, 1, 0)),
            Node(4, Vec3F(2, 0, 0)),
            Node(5, Vec3F(4, 0, 0)),
        )
        val edges = listOf(
            Edge(0, 1), Edge(1, 2), Edge(2, 3),
            Edge(3, 5), Edge(1, 4), Edge(4, 5)
        )
        val navGraph = NavGraph(nodes, edges)
        val navigableGraph = NavigableGraph(navGraph)

        val pathfinder = AStar<NodeId>()

        val path = pathfinder.findPath(navigableGraph, 1, 3)
        assertEquals(3, path.size)
        assertEquals(1, path[0])
        assertEquals(2, path[1])
        assertEquals(3, path[2])

        val road = pathfinder.findPath(navigableGraph, 0, 5)
        assertEquals(4, road.size)
        assertEquals(4, road[2])
    }
}
