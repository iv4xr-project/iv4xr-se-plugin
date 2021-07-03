package spaceEngineers.controller.extensions

import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import spaceEngineers.controller.Character
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.CharacterObservation
import spaceEngineers.model.Observation
import spaceEngineers.model.Vec2
import spaceEngineers.model.Vec3

fun Character.moveForward(velocity: Float = 1f): CharacterObservation {
    return moveAndRotate(movement = Vec3.FORWARD * velocity)
}

fun Character.moveBackward(velocity: Float = 1f): CharacterObservation {
    return moveAndRotate(movement = Vec3.BACKWARD * velocity)
}

suspend fun SpaceEngineers.blockingMoveForwardByDistance(
    distance: Float,
    velocity: Float = 1f,
    startPosition: Vec3,
    timeoutMs: Long = 20000,
) {
    withTimeout(timeoutMs) {
        while (observer.observe().position.distanceTo(startPosition) < distance) {
            character.moveForward(velocity = velocity)
            yield()
        }
    }
}

suspend fun SpaceEngineers.blockingMoveBackwardsByDistance(
    distance: Float,
    velocity: Float = 1f,
    startPosition: Vec3,
    timeoutMs: Long = 20000,
) {
    blockingMoveForwardByDistance(distance, -velocity, startPosition, timeoutMs)
}

suspend fun SpaceEngineers.blockingRotateUntilOrientationForward(
    finalOrientation: Vec3,
    rotation: Vec2,
    delta: Float = 0.01f,
    timeoutMs: Long = 30000,
) {
    withTimeout(timeoutMs) {
        while (observer.observe().let {
                !finalOrientation.similar(it.camera.orientationForward, delta = delta)
            }) {
            character.moveAndRotate(rotation3 = rotation)
            yield()
        }
    }
}

suspend fun SpaceEngineers.blockingRotateUntilOrientationUp(
    finalOrientation: Vec3,
    rotation: Vec2,
    delta: Float = 0.01f,
    timeoutMs: Long = 30000,
) {
    println(finalOrientation)
    withTimeout(timeoutMs) {
        while (observer.observe().let {
                !finalOrientation.similar(
                    it.camera.orientationUp,
                    delta = delta
                )
            }) {
            character.moveAndRotate(rotation3 = rotation)
            yield()
        }
    }
}
