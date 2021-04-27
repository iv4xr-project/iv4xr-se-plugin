package spaceEngineers.controller

import spaceEngineers.commands.InteractionArgs
import spaceEngineers.commands.MovementArgs
import spaceEngineers.commands.ObservationArgs
import spaceEngineers.commands.ObservationMode
import spaceEngineers.model.Observation
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.Vec3

fun CharacterController.equip(toolbarLocation: ToolbarLocation) {
    interact(InteractionArgs.equip(toolbarLocation = toolbarLocation))
}

fun CharacterController.moveForward(velocity: Float = 1f): Observation {
    moveAndRotate(MovementArgs(movement = Vec3(0f, 0f, -velocity)))
    return observe()
}

fun Character.moveForward(velocity: Float = 1f): Observation {
    return moveAndRotate(movement = Vec3(0f, 0f, -velocity))
}

fun CharacterController.blockingMoveForwardByDistance(
    distance: Float,
    velocity: Float = 1f,
    maxTries: Int = 1000
): Observation {
    val startPosition = observe().position
    repeat(maxTries) {
        moveForward(velocity = velocity).let {
            if (it.position.distanceTo(startPosition) >= distance) {
                return it
            }
        }
    }
    error("timeout after $maxTries tries, currentDistance: ${observe().position.distanceTo(startPosition)}")
}

fun Character.blockingMoveForwardByDistance(
    distance: Float,
    velocity: Float = 1f,
    maxTries: Int = 1000,
    startPosition: Vec3,
): Observation {
    repeat(maxTries) {
        moveForward(velocity = velocity).let {
            if (it.position.distanceTo(startPosition) >= distance) {
                return it
            }
        }
    }
    error("timeout after $maxTries tries")
}

fun Character.blockingMoveBackwardsByDistance(
    distance: Float,
    velocity: Float = 1f,
    maxTries: Int = 1000,
    startPosition: Vec3,
): Observation {
    return blockingMoveForwardByDistance(distance, -velocity, maxTries, startPosition)
}


fun CharacterController.observe(): Observation {
    return observe(ObservationArgs())
}

fun CharacterController.observe(observationMode: ObservationMode): Observation {
    return observe(ObservationArgs(observationMode))
}

fun Character.blockingRotateUntilOrientationForward(
    finalOrientation: Vec3,
    rotation: Vec3,
    delta: Float = 0.01f,
    maxTries: Int = 1000,
): Observation {
    repeat(maxTries) {
        val observation = moveAndRotate(rotation3 = rotation)
        observation.orientationForward
            .let { orientationForward ->
                if (finalOrientation.similar(orientationForward, delta = delta)) {
                    return observation
                }
            }
    }
    error("timeout after $maxTries tries")
}

fun Character.blockingRotateUntilOrientationUp(
    finalOrientation: Vec3,
    rotation: Vec3,
    delta: Float = 0.01f,
    maxTries: Int = 1000,
): Observation {
    repeat(maxTries) {
        val observation = moveAndRotate(rotation3 = rotation)
        observation.orientationUp
            .let { orientationUp ->
                if (finalOrientation.similar(orientationUp, delta = delta)) {
                    return observation
                }
            }
    }
    error("timeout after $maxTries tries")
}
