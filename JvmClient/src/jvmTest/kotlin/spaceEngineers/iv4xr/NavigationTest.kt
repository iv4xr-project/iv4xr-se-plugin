package spaceEngineers.iv4xr

import eu.iv4xr.framework.extensions.pathfinding.AStar
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.extensions.navigationGraph
import spaceEngineers.iv4xr.navigation.NavigableGraph
import spaceEngineers.navigation.CharacterNavigation
import spaceEngineers.navigation.NodeId
import testhelp.MockOrRealGameTest
import testhelp.assertGreaterThan
import testhelp.assertLessThan
import java.util.ArrayList
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class NavigationTest : MockOrRealGameTest(
    scenarioId = "amaze",
    //forceRealGame = true,
    //loadScenario = true,
) {

    @Ignore("This test is very slow.")
    @Test
    fun navigateMaze() = testContext {
        delay(timeMillis = 500)
        val graph = observer.navigationGraph()
        assertGreaterThan(graph.nodes.size, 70)

        val navigableGraph = NavigableGraph(graph)
        val targetNode = navigableGraph.node(nodeId = 50)
        val path = getPath(navigableGraph, targetNode.id)
        assertGreaterThan(path.size, 10)

        val navigator = CharacterNavigation(this)
        for (nodeId in path) {
            navigator.moveInLine(navigableGraph.node(nodeId).position, timeout = 5.seconds)
        }

        assertLessThan(observer.observe().position.distanceTo(targetNode.position), 0.7f)
    }

    private fun SpaceEngineers.getPath(
        navigableGraph: NavigableGraph,
        targetNodeId: Int,
        startNodeId: Int = 0  // Should be 0 if the nav graph was generated at the current character position.
    ): List<NodeId> {
        val pathfinder = AStar<NodeId>()
        return pathfinder.findPath(navigableGraph, startNodeId, targetNodeId)
    }
}
