package spaceEngineers.navigation

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.extensions.distanceTo
import spaceEngineers.model.CharacterMovementType
import spaceEngineers.model.Vec3F
import spaceEngineers.movement.CharacterMovement
import spaceEngineers.movement.CompositeDirection3d
import spaceEngineers.movement.VectorMovement
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface Navigation {
    suspend fun moveInLine(
        targetLocation: Vec3F,
        movementType: CharacterMovementType = CharacterMovementType.RUN,
        timeout: Duration = 20.seconds
    )
}

class CharacterNavigation(
    val spaceEngineers: SpaceEngineers,
    val movement: CharacterMovement = VectorMovement(spaceEngineers)
    ) : Navigation {

    override suspend fun moveInLine(
        targetLocation: Vec3F,
        movementType: CharacterMovementType,
        timeout: Duration
    ) = withTimeout(timeout) {
        moveInLine(targetLocation, movementType)
    }

    private suspend fun moveInLine(
        targetLocation: Vec3F,
        movementType: CharacterMovementType
    ) = with(spaceEngineers) {
        val me = observer.observe()
        val direction = (targetLocation - me.position).normalized()

        // TODO("Replace this teleport hack")
        admin.character.teleport(me.position, direction, me.orientationUp)

        goForwardToLocation(spaceEngineers, targetLocation, movementType, stepTicks = 20, tolerance = 1.2f)
        goForwardToLocation(spaceEngineers, targetLocation, movementType, stepTicks = 6, tolerance = 0.4f)
    }

    private suspend fun goForwardToLocation(
        spaceEngineers: SpaceEngineers,
        targetLocation: Vec3F,
        movementType: CharacterMovementType,
        stepTicks: Int, tolerance: Float
    ) = with(spaceEngineers) {
        var lastDistance = Float.MAX_VALUE;

        fun isNotYetThereButProgressing(maxDistanceRegression: Float = 0.01f): Boolean {
            val distance = observer.distanceTo(targetLocation)
            if (distance < tolerance) {
                return false
            }
            if (distance > lastDistance + maxDistanceRegression) {  // Allow very small worsening of distance.
                return false
            }
            lastDistance = distance
            return true
        }

        while (isNotYetThereButProgressing()) {
            // TODO("Correct the course from time to time")

            movement.move(CompositeDirection3d.FORWARD, movementType, ticks = stepTicks)
            delay((stepTicks * DELAY_PER_TICKS_MS).toLong())
        }
    }

    companion object {
        const val DELAY_PER_TICKS_MS = 12  // One tick lasts ~16.7 ms, wait slightly less to avoid character freezes.
    }
}

