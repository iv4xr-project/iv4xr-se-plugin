package spaceEngineers.iv4xr

import eu.iv4xr.framework.extensions.pathfinding.AStar
import eu.iv4xr.framework.spatial.Vec3
import spaceEngineers.controller.Observer
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.iv4xr.navigation.NavigableGraph
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.blockById
import spaceEngineers.model.extensions.largestGrid
import spaceEngineers.navigation.CharacterNavigation
import spaceEngineers.navigation.NodeId
import spaceEngineers.navigation.toRichGraph
import testhelp.MockOrRealGameTest
import testhelp.assertGreaterThan
import testhelp.assertLessThan
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertNotEquals
import kotlin.time.Duration.Companion.seconds

class NavigateEntityTest : MockOrRealGameTest(
    scenarioId = "small",
    forceRealGame = true,
    loadScenario = true
) {

    @Ignore("This test required a game instance running")
    //@Test
    fun navigateMaze() = testContext {
        val graph = observer.navigationGraph(observer.observeBlocks().largestGrid().id)

        // Functional blocks are not included in the navigational graph as nodes
        // We need to find the closest nav node related to the desired block position
        val blockPosition = desiredBlockPosition("BlockCryoChamber", observer)
        assertNotEquals(blockPosition, Vec3F(0,0,0))

        var reachablePosition = Vec3F(0,0,0)
        var reachableNode = -1
        var closestDistance = 3f

        val richGraph = graph.toRichGraph()
        System.out.println("richGraph.nodeMap.size: " + richGraph.nodeMap.size);
        richGraph.nodeMap.forEach { entry ->
            println("${entry.key} : ${entry.value}")
            val distance = entry.value.position.distanceTo(blockPosition)
            println(" --> distance: $distance ")
            if(distance < closestDistance){
                reachablePosition = entry.value.position
                reachableNode = entry.value.id
                closestDistance = distance
            }
        }

        assertNotEquals(reachableNode, -1)
        assertLessThan(closestDistance, 3f)

        System.out.println("reachablePosition: " + reachablePosition);
        System.out.println("reachableNode: " + reachableNode);

        val navigableGraph = NavigableGraph(graph)
        val targetNode = navigableGraph.node(nodeId = reachableNode)
        val path = getPath(navigableGraph, targetNode.id)

        val navigator = CharacterNavigation(this)
        for (nodeId in path) {
            navigator.moveInLine(navigableGraph.node(nodeId).position, timeout = 5.seconds)
        }
    }

    private fun SpaceEngineers.getPath(
        navigableGraph: NavigableGraph,
        targetNodeId: Int,
        startNodeId: Int = 0  // Should be 0 if the nav graph was generated at the current character position.
    ): List<NodeId> {
        val pathfinder = AStar<NodeId>()
        return pathfinder.findPath(navigableGraph, startNodeId, targetNodeId)
    }

    private fun desiredBlockPosition(
            desiredBlock: String,
            observer: Observer
    ): Vec3F {
        for(block in observer.observeBlocks().allBlocks){
            if(desiredBlock in block.definitionId.toString()){
                System.out.println("Math desired block in position: " + block.position);
                System.out.println("block.definitionId: " + block.definitionId);
                System.out.println("block.id: " + block.id);
                return block.position
            }
        }
        return Vec3F(0,0,0)
    }
}
