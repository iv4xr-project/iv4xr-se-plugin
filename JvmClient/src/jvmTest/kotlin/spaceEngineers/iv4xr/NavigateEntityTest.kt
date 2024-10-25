package spaceEngineers.iv4xr

import eu.iv4xr.framework.extensions.pathfinding.AStar
import org.junit.jupiter.api.Disabled
import spaceEngineers.controller.Observer
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.iv4xr.navigation.Iv4XRAStarPathFinder
import spaceEngineers.iv4xr.navigation.NavigableGraph
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.largestGrid
import spaceEngineers.navigation.CharacterNavigation
import spaceEngineers.navigation.NodeId
import spaceEngineers.navigation.toRichGraph
import testhelp.MockOrRealGameTest
import testhelp.assertLessThan
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.time.Duration.Companion.seconds

class NavigateEntityTest : MockOrRealGameTest(
    scenarioId = "small",
    forceRealGame = true,
    loadScenario = true
) {

    @Disabled("This test required a game instance running, enable manually by uncommenting.")
    @Test
    fun test_observed_grids() = testContext {
        assertEquals(observer.observeBlocks().grids.size, 1)
        assertEquals(observer.observeBlocks().grids.size, 1)
        assertEquals(observer.observeBlocks().grids.size, 1)
    }

    //@Disabled("This test required a game instance running, enable manually by uncommenting.")
    @Test
    fun navigateMaze() = testContext {
        val graph = observer.navigationGraph(observer.observeBlocks().largestGrid().id)

        // Functional blocks are not included in the navigational graph as nodes
        // We need to find the closest nav node related to the desired block position
        val blockPosition = desiredBlockPosition("BlockCryoChamber", observer)
        assertNotEquals(blockPosition, Vec3F(0, 0, 0))

        var reachablePosition = Vec3F(0, 0, 0)
        var reachableNode = ""
        var closestDistance = 3f

        val richGraph = graph.toRichGraph()
        System.out.println("richGraph.nodeMap.size: " + richGraph.nodeMap.size)
        richGraph.nodeMap.forEach { entry ->
            println("${entry.key} : ${entry.value}")
            val distance = entry.value.data.distanceTo(blockPosition)
            println(" --> distance: $distance ")
            if (distance < closestDistance) {
                reachablePosition = entry.value.data
                reachableNode = entry.value.id
                closestDistance = distance
            }
        }

        assertNotEquals(reachableNode, "")
        assertLessThan(closestDistance, 3f)

        System.out.println("reachablePosition: " + reachablePosition)
        System.out.println("reachableNode: " + reachableNode)

        //items.setToolbarItem(DefinitionId.physicalGun("AngleGrinder2Item"), ToolbarLocation(3, 0))
        //val grinderLocation = items.toolbar().findLocation("AngleGrinder2Item") ?: error("No grinder found")
        //items.equip(grinderLocation)

        val navigableGraph = NavigableGraph(graph)
        val targetNode = navigableGraph.node(nodeId = reachableNode)
        val startNode: NodeId = richGraph.nodeMap.minByOrNull { it.value.data.distanceTo(observer.observeBlocks().character.position) }?.key ?: ""
        val path = getPath(navigableGraph, startNode, targetNode.id)

        val navigator = CharacterNavigation(this, pathFinder = Iv4XRAStarPathFinder())
        for (nodeId in path) {
            navigator.moveInLine(navigableGraph.node(nodeId).data, timeout = 5.seconds)
        }
    }

    private fun SpaceEngineers.getPath(
        navigableGraph: NavigableGraph,
        startNodeId: NodeId,
        targetNodeId: NodeId
    ): List<NodeId> {
        val pathfinder = AStar<NodeId>()
        return pathfinder.findPath(navigableGraph, startNodeId, targetNodeId)
    }

    private fun desiredBlockPosition(
        desiredBlock: String,
        observer: Observer
    ): Vec3F {
        for (block in observer.observeBlocks().allBlocks) {
            if (desiredBlock in block.definitionId.toString()) {
                System.out.println("Math desired block in position: " + block.position)
                System.out.println("block.definitionId: " + block.definitionId)
                System.out.println("block.id: " + block.id)
                return block.position
            }
        }
        return Vec3F(0, 0, 0)
    }
}
