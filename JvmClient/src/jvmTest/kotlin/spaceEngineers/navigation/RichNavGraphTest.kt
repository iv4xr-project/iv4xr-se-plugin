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
            Node(3, Vec3F.ZERO))
        val edges = listOf(Edge(0, 1), Edge(0, 2), Edge(3, 0), Edge(2, 3))
        val navGraph = NavGraph(nodes, edges)

        val richNavGraph = navGraph.toRichGraph()

        assertEquals(3, richNavGraph.edgeMap[0]?.count())
        assertEquals(1, richNavGraph.edgeMap[1]?.count())
        assertEquals(2, richNavGraph.edgeMap[2]?.count())

        assertFalse(richNavGraph.edgeMap[0]!!.contains(0))
        assertTrue(richNavGraph.edgeMap[1]!!.contains(0))
        assertTrue(richNavGraph.edgeMap[2]!!.contains(3))
    }
}
