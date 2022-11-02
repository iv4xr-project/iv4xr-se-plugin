package spaceEngineers.navigation

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.extensions.distanceTo
import spaceEngineers.graph.DirectedGraph
import spaceEngineers.model.*
import spaceEngineers.model.extensions.allBlocks
import spaceEngineers.model.extensions.blockById
import spaceEngineers.movement.CharacterMovement
import spaceEngineers.movement.CompositeDirection3d
import spaceEngineers.movement.RotationDirection
import spaceEngineers.movement.VectorMovement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface Navigation {
    suspend fun moveInLine(
        targetLocation: Vec3F,
        movementType: CharacterMovementType = CharacterMovementType.RUN,
        distanceTolerance: Float = 1.2f,
        timeout: Duration = 20.seconds,
    )

    suspend fun navigateToBlock(
        id: BlockId,
        movementType: CharacterMovementType = CharacterMovementType.RUN,
        distanceTolerance: Float = 1.2f,
        timeout: Duration = 120.seconds,
    )
}

interface PathFinder<NodeId, NodeData, EdgeId, EdgeData> {
    fun findPath(
        navigableGraph: DirectedGraph<NodeId, NodeData, EdgeId, EdgeData>,
        targetNodeId: NodeId,
        startNodeId: NodeId,  // Should be 0 if the nav graph was generated at the current character position.
    ): List<BlockId>

}

class CharacterNavigation(
    val spaceEngineers: SpaceEngineers,
    val pathFinder: PathFinder<BlockId, Vec3F, String, String>,
    val movement: CharacterMovement = VectorMovement(spaceEngineers),
) : Navigation {

    override suspend fun navigateToBlock(
        id: BlockId,
        movementType: CharacterMovementType,
        distanceTolerance: Float,
        timeout: Duration,
    ) = withTimeout(timeout) {
        val blockObservation = spaceEngineers.observer.observeBlocks()
        val allBlocks = blockObservation.allBlocks
        val target = blockObservation.blockById(id)
        val gridId = blockObservation.grids.first { grid -> grid.blocks.any { block -> block.id == target.id } }.id
        val graph = spaceEngineers.observer.navigationGraph(gridId)

        val richNavGraph = RichNavGraph(graph)

        if (graph.nodes.isEmpty()) {
            error("Graph is empty!")
        }

        val targetNode = graph.nodes
            .minByOrNull { it.data.distanceTo(target.position) } ?: error("Target not found in the graph")
        if (targetNode.data.distanceTo(target.position) > LARGE_BLOCK_CUBE_SIDE_SIZE) {
            error(
                "Target block $id is too far, distance to the closest navigable node: ${
                    targetNode.data.distanceTo(
                        target.position
                    )
                }, (${targetNode.id})"
            )
        }

        val startNode = allBlocks.minByOrNull { it.position.distanceTo(blockObservation.character.position) }
            ?: error("No nodes found!")

        val path = pathFinder.findPath(graph.toDirectedGraph(), targetNode.id, startNode.id)

        path.forEach { nodeId ->
            goToLocation(
                spaceEngineers = spaceEngineers,
                targetLocation = richNavGraph.node(nodeId).data,
                movementType = movementType,
                stepTicks = 20,
                distanceTolerance = distanceTolerance
            )
        }
    }

    override suspend fun moveInLine(
        targetLocation: Vec3F,
        movementType: CharacterMovementType,
        distanceTolerance: Float,
        timeout: Duration,
    ) = withTimeout(timeout) {
        goToLocation(
            spaceEngineers,
            targetLocation,
            movementType,
            stepTicks = 20,
            distanceTolerance = distanceTolerance
        )
        //goToLocation(spaceEngineers, targetLocation, movementType, stepTicks = 6, tolerance = 0.4f)
    }

    private suspend fun goToLocation(
        spaceEngineers: SpaceEngineers,
        targetLocation: Vec3F,
        movementType: CharacterMovementType,
        stepTicks: Int,
        distanceTolerance: Float,
        rotationTolerance: Float = 1.2f
    ) = with(spaceEngineers) {
        var lastDistance = Float.MAX_VALUE;

        fun isNotYetThereButProgressing(maxDistanceRegression: Float = 0.01f): Boolean {
            val distance = observer.distanceTo(targetLocation)
            if (distance < distanceTolerance) {
                return false
            }
            if (distance > lastDistance + maxDistanceRegression) {  // Allow very small worsening of distance.
                return false
            }
            lastDistance = distance
            return true
        }

        while (isNotYetThereButProgressing()) {

            rotateToTarget(targetLocation, rotationTolerance)
            movement.move(CompositeDirection3d.FORWARD, movementType, ticks = stepTicks)
            delay((stepTicks * DELAY_PER_TICKS_MS).toLong())
        }
    }

    private suspend fun rotateToTarget(
        targetLocation: Vec3F,
        orientationTolerance: Float,
        defaultOrientationTolerance: Float = 0.04f
    ) = with(spaceEngineers) {

        val me = observer.observe()
        val direction = (targetLocation - me.position).normalized()
        val distance = targetLocation.distanceTo(me.position)

        // When we are quite close to the target, the rotation can be less precise.
        val orientationDistanceTolerance = if (distance < 3 * orientationTolerance) {
            2 * defaultOrientationTolerance
        } else {
            defaultOrientationTolerance
        }

        val iterationLimit = if (distance < 2 * orientationTolerance) {
            3  // When we are close, only allow fine-tuning to prevent unbounded loop.
        } else {
            (180 / MAX_ROTATION_TICKS)  // 60 ticks is 1s. Limit rotation to 3 seconds.
        }

        rotateToDirection(
            spaceEngineers,
            direction,
            guessRotationDirection(me, direction),
            orientationDistanceTolerance,
            iterationLimit
        )
    }

    private suspend fun rotateToDirection(
        spaceEngineers: SpaceEngineers,
        direction: Vec3F,
        rotationDirection: RotationDirection,
        orientationDistanceTolerance: Float,
        iterationLimit: Int
    ) = repeat(iterationLimit) {
        val forwardOrientationDistance = direction.distanceTo(spaceEngineers.observer.observe().orientationForward)
        if (forwardOrientationDistance < orientationDistanceTolerance) {
            return@repeat
        }

        val ticks = estimateRotationLimit(forwardOrientationDistance)
        movement.rotate(rotationDirection, ticks)
        delay((ticks * DELAY_PER_TICKS_MS).toLong())
    }


    // Estimate rotation limit in ticks for smooth movement. Too many ticks risk overshooting of the rotation,
    // too few ticks lead to jerky movement (commands are not executed fast enough).
    private fun estimateRotationLimit(forwardOrientationDistance: Float) = when (forwardOrientationDistance) {
        in 0.5..2.0 -> MAX_ROTATION_TICKS
        in 0.1..0.5 -> 5
        in 0.05..0.1 -> 3
        in 0.00..0.05 -> 1
        else -> error("Unexpected orientation difference: $forwardOrientationDistance")
    }

    private fun guessRotationDirection(me: CharacterObservation, direction: Vec3F): RotationDirection {
        val rotationMatrix = RotationMatrix.fromForwardAndUp(me.orientationForward, me.orientationUp)

        return if (direction.distanceTo(rotationMatrix.right) < direction.distanceTo(rotationMatrix.left)) {
            RotationDirection.RIGHT
        } else {
            RotationDirection.LEFT
        }
    }

    companion object {
        const val DELAY_PER_TICKS_MS = 12  // One tick lasts ~16.7 ms, wait slightly less to avoid character freezes.
        const val MAX_ROTATION_TICKS = 10
    }
}

