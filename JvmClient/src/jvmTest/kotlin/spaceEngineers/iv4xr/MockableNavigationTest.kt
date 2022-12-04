package spaceEngineers.iv4xr

import eu.iv4xr.framework.extensions.pathfinding.AStar
import spaceEngineers.controller.DataExtendedSpaceEngineers
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.iv4xr.navigation.Iv4XRAStarPathFinder
import spaceEngineers.iv4xr.navigation.NavigableGraph
import spaceEngineers.model.TerminalBlock
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.largestGrid
import spaceEngineers.navigation.CharacterNavigation
import spaceEngineers.navigation.NodeId
import testhelp.MockOrRealGameTest
import testhelp.assertGreaterThan
import testhelp.assertLessThan
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class MockableNavigationTest : MockOrRealGameTest(
    scenarioId = "amaze-20x20",
) {

    // @Ignore("This test is quite slow.")
    @Test
    fun navigateMaze() = testContext {
        val graph = observer.navigationGraph(observer.observeBlocks().largestGrid().id)
        assertGreaterThan(graph.nodes.size, 70)

        val navigableGraph = NavigableGraph(graph)
        // TODO:
        // val targetNode = navigableGraph.node(nodeId = 20.toString())

        val blockObservation = observer.observeBlocks()
        val allBlocks = blockObservation.allBlocks

        val target =
            allBlocks.filterIsInstance<TerminalBlock>().map { println(it.customName); it }
                .firstOrNull { it.customName == "MazeTarget" } ?: error("Target not found!")
        val targetNode =
            graph.nodes.minByOrNull { it.data.distanceTo(target.position) } ?: error("Target not found in the graph")
        // TODO:
        val startNode = allBlocks.minByOrNull { it.position.distanceTo(blockObservation.character.position) }
            ?: error("No nodes found!")
        val path = getPath(navigableGraph, targetNode.id, startNode.id)
        assertGreaterThan(path.size, 10)

        val navigator = CharacterNavigation(this, pathFinder = Iv4XRAStarPathFinder())
        for (nodeId in path) {
            navigator.moveInLine(navigableGraph.node(nodeId).data, timeout = 5.seconds)
        }

        assertLessThan(observer.observe().position.distanceTo(targetNode.data), 0.7f)
    }

    @Test
    fun navigateMaze2() = testContext {
        val target = observer.observeBlocks().allBlocks
            .filterIsInstance<TerminalBlock>().map { println(it.customName); it }
            .firstOrNull { it.customName == "MazeTarget" } ?: error("Target not found!")

        val extra = DataExtendedSpaceEngineers(spaceEngineers = this, pathFinder = Iv4XRAStarPathFinder())
        extra.extensions.character.navigation.navigateToBlock(
            target.id,
        )
    }

    private fun SpaceEngineers.getPath(
        navigableGraph: NavigableGraph,
        targetNodeId: NodeId,
        startNodeId: NodeId, // Should be 0 if the nav graph was generated at the current character position.
    ): List<NodeId> {
        val pathfinder = AStar<NodeId>()
        return pathfinder.findPath(navigableGraph, startNodeId, targetNodeId)
    }
}
