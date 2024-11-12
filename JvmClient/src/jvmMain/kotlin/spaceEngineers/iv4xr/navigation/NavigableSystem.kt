package spaceEngineers.iv4xr.navigation

import eu.iv4xr.framework.extensions.pathfinding.AStar
import spaceEngineers.controller.Observer
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.CharacterMovementType
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.largestGrid
import spaceEngineers.movement.RotationDirection
import spaceEngineers.movement.VectorMovement
import spaceEngineers.navigation.CharacterNavigation
import spaceEngineers.navigation.NodeId
import spaceEngineers.navigation.toRichGraph
import kotlin.time.Duration.Companion.seconds

class NavigableSystem(
    val spaceEngineers: SpaceEngineers,
    val observer: Observer
) {

    private val navGraph = observer.navigationGraph(observer.observeBlocks().largestGrid().id)
    private var desiredBlockPosition = Vec3F(0, 0, 0)

    fun getNavigableGraph(): NavigableGraph {
        return NavigableGraph(navGraph)
    }

    /**
     * Functional blocks are not included in the navigational graph as nodes.
     * We need to find the closest nav node related to the desired block position.
     */
    fun setDesiredBlockPosition(
        desiredBlock: String
    ): Vec3F {
        // Reset position values
        desiredBlockPosition = Vec3F(0, 0, 0)

        for (block in observer.observeBlocks().allBlocks) {
            if (desiredBlock in block.definitionId.toString()) {
                desiredBlockPosition = block.position
                return desiredBlockPosition
            }
        }

        return desiredBlockPosition
    }

    fun getClosestPathToDesiredBlock(
        closestDistance: Float = 9f
    ): List<NodeId> {
        var reachablePosition = Vec3F(0, 0, 0)
        var reachableNode = ""
        var currentClosestDistance = closestDistance

        val richNavGraph = navGraph.toRichGraph()
        richNavGraph.nodeMap.forEach { entry ->
            val distance = entry.value.data.distanceTo(desiredBlockPosition)
            if (distance < currentClosestDistance) {
                reachablePosition = entry.value.data
                reachableNode = entry.value.id
                currentClosestDistance = distance
            }
        }

        System.out.println("reachablePosition: " + reachablePosition)
        System.out.println("reachableNode: " + reachableNode)

        val navigableGraph: NavigableGraph = getNavigableGraph()
        val startNode: NodeId = richNavGraph.nodeMap.minByOrNull { it.value.data.distanceTo(observer.observeBlocks().character.position) }?.key ?: ""
        val targetNode: NodeId = navigableGraph.node(nodeId = reachableNode).id

        val pathfinder = AStar<NodeId>()
        return pathfinder.findPath(navigableGraph, startNode, targetNode)
    }

    fun rotateToBlock(
        desiredBlock: String
    ) {
        val movement = VectorMovement(spaceEngineers)

        val AIMTRIES = 300
        var tries = 1
        while (!targetBlockFound(desiredBlock) && tries < AIMTRIES) {
            movement.rotate(RotationDirection.RIGHT, 3)
            tries++
        }
    }

    private fun targetBlockFound(
        desiredBlock: String
    ): Boolean {
        return (desiredBlock in observer.observe().targetBlock?.definitionId.toString())
    }

    suspend fun navigatePath(
        navigableGraph: NavigableGraph,
        navigablePath: List<NodeId>,
        distancePathTolerance: Float = 1.2f
    ) {
        val navigator = CharacterNavigation(spaceEngineers = spaceEngineers, pathFinder = Iv4XRAStarPathFinder())

        println("Complete navigatePath:")
        navigablePath.forEach { nodeId ->
            print(" ${navigableGraph.node(nodeId).data},")
        }

        for (nodeId in navigablePath) {
            println("Next navigatePath node: ${navigableGraph.node(nodeId).data}")
            navigator.moveInLine(
                navigableGraph.node(nodeId).data,
                movementType = CharacterMovementType.RUN,
                distanceTolerance = distancePathTolerance,
                timeout = 10.seconds
            )
        }
    }

    suspend fun navigateDynamicPath(
        navigableSystem: NavigableSystem,
        distanceTolerance: Float
    ) {
        val navigableGraph = navigableSystem.getNavigableGraph()
        val navigablePath = navigableSystem.getClosestPathToDesiredBlock(distanceTolerance)

        val navigator = CharacterNavigation(spaceEngineers = spaceEngineers, pathFinder = Iv4XRAStarPathFinder())

        val firstNavigableNode = navigablePath.first()

        println("Next navigateDynamicPath node: ${navigableGraph.node(firstNavigableNode).data}")
        navigator.moveInLine(
            navigableGraph.node(firstNavigableNode).data,
            movementType = CharacterMovementType.RUN,
            distanceTolerance = distanceTolerance,
            timeout = 10.seconds
        )
    }
}
