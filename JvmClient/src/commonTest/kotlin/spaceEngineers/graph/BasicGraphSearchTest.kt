package spaceEngineers.graph

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class BasicGraphSearchTest {

    @Test
    fun `findPath returns correct path from from to to`() {
        val nodeA = DataNode("A", "PositionA")
        val nodeB = DataNode("B", "PositionB")
        val nodeC = DataNode("C", "PositionC")
        val graph = DirectedGraph(
            nodes = listOf(nodeA, nodeB, nodeC),
            edges = listOf(
                DataEdge(from = "A", to = "B", id = 1, data = "EdgeData1"),
                DataEdge(from = "B", to = "C", id = 2, data = "EdgeData2")
            )
        )
        val graphExtra = DirectedGraphExtra(graph)
        val search = BasicGraphSearch(graphExtra)

        val path = search.findPath(from = "A", to = "C")

        assertEquals(listOf(1, 2), path)
    }

    @Test
    fun `findPath returns correct path from from to to when there is a cycle`() {
        val nodeA = DataNode("A", "PositionA")
        val nodeB = DataNode("B", "PositionB")
        val nodeC = DataNode("C", "PositionC")
        val graph = DirectedGraph(
            nodes = listOf(nodeA, nodeB, nodeC),
            edges = listOf(
                DataEdge(from = "A", to = "B", id = 1, data = "EdgeData1"),
                DataEdge(from = "B", to = "C", id = 2, data = "EdgeData2"),
                DataEdge(from = "C", to = "A", id = 3, data = "EdgeData3")
            )
        )
        val graphExtra = DirectedGraphExtra(graph)
        val search = BasicGraphSearch(graphExtra)

        val path = search.findPath(from = "A", to = "C")

        assertEquals(listOf(1, 2), path)
    }

    @Test
    fun `findPath throws exception when from node is not in graph`() {
        val nodeA = DataNode("A", "PositionA")
        val nodeB = DataNode("B", "PositionB")
        val nodeC = DataNode("C", "PositionC")
        val graph = DirectedGraph(
            nodes = listOf(nodeA, nodeB, nodeC),
            edges = listOf(
                DataEdge(from = "A", to = "B", id = 1, data = "EdgeData1"),
                DataEdge(from = "B", to = "C", id = 2, data = "EdgeData2")
            )
        )
        val graphExtra = DirectedGraphExtra(graph)
        val search = BasicGraphSearch(graphExtra)

        assertFailsWith(IllegalArgumentException::class) {
            search.findPath(from = "D", to = "C")
        }
    }

    @Test
    fun `findPath throws exception when to node is not in graph`() {
        val nodeA = DataNode("A", "PositionA")
        val nodeB = DataNode("B", "PositionB")
        val nodeC = DataNode("C", "PositionC")
        val graph = DirectedGraph(
            nodes = listOf(nodeA, nodeB, nodeC),
            edges = listOf(
                DataEdge(from = "A", to = "B", id = 1, data = "EdgeData1"),
                DataEdge(from = "B", to = "C", id = 2, data = "EdgeData2")
            )
        )
        val graphExtra = DirectedGraphExtra(graph)
        val search = BasicGraphSearch(graphExtra)

        assertFailsWith(IllegalArgumentException::class) {
            search.findPath(from = "A", to = "D")
        }
    }

    @Test
    fun `findPath throws exception when path is not found`() {
        val nodeA = DataNode("A", "PositionA")
        val nodeB = DataNode("B", "PositionB")
        val nodeC = DataNode("C", "PositionC")
        val graph = DirectedGraph(
            nodes = listOf(nodeA, nodeB, nodeC),
            edges = listOf(
                DataEdge(from = "A", to = "B", id = 1, data = "EdgeData1"),
                DataEdge(from = "B", to = "C", id = 2, data = "EdgeData2")
            )
        )
        val graphExtra = DirectedGraphExtra(graph)
        val search = BasicGraphSearch(graphExtra)

        assertFailsWith(IllegalStateException::class) {
            search.findPath(from = "C", to = "A")
        }
    }

    @Test
    fun `findPath returns empty list when canNavigateToSelf is true and from is equal to to`() {
        val nodeA = DataNode("A", "PositionA")
        val nodeB = DataNode("B", "PositionB")
        val nodeC = DataNode("C", "PositionC")
        val graph = DirectedGraph(
            nodes = listOf(nodeA, nodeB, nodeC),
            edges = listOf(
                DataEdge(from = "A", to = "B", id = 1, data = "EdgeData1"),
                DataEdge(from = "B", to = "C", id = 2, data = "EdgeData2")
            )
        )
        val graphExtra = DirectedGraphExtra(graph)
        val search = BasicGraphSearch(graphExtra, canNavigateToSelf = true)

        val path = search.findPath(from = "A", to = "A")

        assertTrue(path.isEmpty())
    }

    @Test
    fun `findPath throws exception when canNavigateToSelf is false and from is equal to to`() {
        val nodeA = DataNode("A", "PositionA")
        val nodeB = DataNode("B", "PositionB")
        val nodeC = DataNode("C", "PositionC")
        val graph = DirectedGraph(
            nodes = listOf(nodeA, nodeB, nodeC),
            edges = listOf(
                DataEdge(from = "A", to = "B", id = 1, data = "EdgeData1"),
                DataEdge(from = "B", to = "C", id = 2, data = "EdgeData2")
            )
        )
        val graphExtra = DirectedGraphExtra(graph)
        val search = BasicGraphSearch(graphExtra, canNavigateToSelf = false)

        assertFailsWith(IllegalStateException::class) {
            search.findPath(from = "A", to = "A")
        }
    }

    @Test
    fun `findPath returns shortest path when there are multiple paths from from to to`() {
        val nodeA = DataNode("A", "PositionA")
        val nodeB = DataNode("B", "PositionB")
        val nodeC = DataNode("C", "PositionC")
        val nodeD = DataNode("D", "PositionD")
        val graph = DirectedGraph(
            nodes = listOf(nodeA, nodeB, nodeC, nodeD),
            edges = listOf(
                DataEdge(from = "A", to = "B", id = 1, data = "EdgeData1"),
                DataEdge(from = "A", to = "C", id = 2, data = "EdgeData2"),
                DataEdge(from = "B", to = "D", id = 3, data = "EdgeData3"),
                DataEdge(from = "C", to = "D", id = 4, data = "EdgeData4")
            )
        )
        val graphExtra = DirectedGraphExtra(graph)
        val search = BasicGraphSearch(graphExtra)

        val path = search.findPath(from = "A", to = "D")

        assertEquals(listOf(1, 3), path)
    }

    @Test
    fun `findPath throws exception when graph has no edges`() {
        val nodeA = DataNode("A", "PositionA")
        val nodeB = DataNode("B", "PositionB")
        val nodeC = DataNode("C", "PositionC")
        val graph = DirectedGraph(
            nodes = listOf(nodeA, nodeB, nodeC),
            edges = emptyList<DataEdge<String, String, Unit>>()
        )
        val graphExtra = DirectedGraphExtra(graph)
        val search = BasicGraphSearch(graphExtra)

        assertFailsWith(IllegalStateException::class) {
            search.findPath(from = "A", to = "C")
        }
    }

    @Test
    fun `findPath returns empty list when graph has only one node`() {
        val nodeA = DataNode("A", "PositionA")
        val graph = DirectedGraph(
            nodes = listOf(nodeA),
            edges = emptyList<DataEdge<String, String, Unit>>()
        )
        val graphExtra = DirectedGraphExtra(graph)
        val search = BasicGraphSearch(graphExtra)

        val path = search.findPath(from = "A", to = "A")

        assertTrue(path.isEmpty())
    }

    @Test
    fun `findPath throws exception when nodes are disconnected`() {
        val nodeA = DataNode("A", "PositionA")
        val nodeB = DataNode("B", "PositionB")
        val nodeC = DataNode("C", "PositionC")
        val graph = DirectedGraph(
            nodes = listOf(nodeA, nodeB, nodeC),
            edges = listOf(
                DataEdge(from = "A", to = "B", id = 1, data = "EdgeData1")
            )
        )
        val graphExtra = DirectedGraphExtra(graph)
        val search = BasicGraphSearch(graphExtra)

        assertFailsWith(IllegalStateException::class) {
            search.findPath(from = "A", to = "C")
        }
    }

    @Test
    fun `findPath returns correct path when from node has multiple outgoing edges to to node`() {
        val nodeA = DataNode("A", "PositionA")
        val nodeB = DataNode("B", "PositionB")
        val nodeC = DataNode("C", "PositionC")
        val graph = DirectedGraph(
            nodes = listOf(nodeA, nodeB, nodeC),
            edges = listOf(
                DataEdge(from = "A", to = "B", id = 1, data = "EdgeData1"),
                DataEdge(from = "A", to = "B", id = 2, data = "EdgeData2"),
                DataEdge(from = "B", to = "C", id = 3, data = "EdgeData3")
            )
        )
        val graphExtra = DirectedGraphExtra(graph)
        val search = BasicGraphSearch(graphExtra)

        val path = search.findPath(from = "A", to = "C")

        assertEquals(listOf(1, 3), path)
    }

}