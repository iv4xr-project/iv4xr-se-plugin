package spaceEngineers.navigation

import spaceEngineers.iv4xr.makeEdge
import spaceEngineers.iv4xr.makeNode
import spaceEngineers.model.Vec3F
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RichNavGraphTest {
    @Test
    fun createFromNavGraph() {
        val nodes = listOf(
            makeNode(0, Vec3F.ZERO),
            makeNode(1, Vec3F.ZERO),
            makeNode(2, Vec3F.ZERO),
            makeNode(3, Vec3F.ZERO),
            makeNode(4, Vec3F.ZERO))
        val edges = listOf(makeEdge(0, 1), makeEdge(0, 2), makeEdge(3, 0), makeEdge(2, 3))
        val navGraph = NavGraph(nodes, edges)

        val richNavGraph = navGraph.toRichGraph()

        assertEquals(3, richNavGraph.neighbours(0.toString()).size)
        assertEquals(1, richNavGraph.neighbours(1.toString()).size)
        assertEquals(2, richNavGraph.neighbours(2.toString()).size)
        assertEquals(0, richNavGraph.neighbours(4.toString()).size)

        assertFalse(richNavGraph.containsEdge(0.toString(), 0.toString()))
        assertTrue(richNavGraph.containsEdge(1.toString(), 0.toString()))
        assertTrue(richNavGraph.containsEdge(2.toString(), 3.toString()))
    }
}
