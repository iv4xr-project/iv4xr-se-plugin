package spaceEngineers.navigation

import spaceEngineers.model.Vec3F
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RichNavGraphTest {
    @Test
    fun createFromNavGraph() {
        val nodes = listOf(
            Node(0, Vec3F.ZERO),
            Node(1, Vec3F.ZERO),
            Node(2, Vec3F.ZERO),
            Node(3, Vec3F.ZERO),
            Node(4, Vec3F.ZERO))
        val edges = listOf(Edge(0, 1), Edge(0, 2), Edge(3, 0), Edge(2, 3))
        val navGraph = NavGraph(nodes, edges)

        val richNavGraph = navGraph.toRichGraph()

        assertEquals(3, richNavGraph.neighbours(0).size)
        assertEquals(1, richNavGraph.neighbours(1).size)
        assertEquals(2, richNavGraph.neighbours(2).size)
        assertEquals(0, richNavGraph.neighbours(4).size)

        assertFalse(richNavGraph.containsEdge(0, 0))
        assertTrue(richNavGraph.containsEdge(1, 0))
        assertTrue(richNavGraph.containsEdge(2, 3))
    }
}
