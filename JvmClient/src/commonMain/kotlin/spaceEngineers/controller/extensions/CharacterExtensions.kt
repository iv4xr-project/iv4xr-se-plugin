package spaceEngineers.controller.extensions

import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import spaceEngineers.controller.Character
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.CharacterMovementType
import spaceEngineers.model.CharacterObservation
import spaceEngineers.model.Vec2
import spaceEngineers.model.Vec3
import spaceEngineers.model.extensions.normalizeAsMovement

fun Character.moveForward(characterMovementType: CharacterMovementType): CharacterObservation {
    return moveAndRotate(movement = Vec3.FORWARD.normalizeAsMovement(characterMovementType))
}

fun Character.moveBackward(characterMovementType: CharacterMovementType): CharacterObservation {
    return moveAndRotate(movement = Vec3.BACKWARD.normalizeAsMovement(characterMovementType))
}

suspend fun SpaceEngineers.blockingMoveByDistance(
    distance: Float,
    startPosition: Vec3,
    timeoutMs: Long = 20000,
    orientation: Vec3,
    characterMovementType: CharacterMovementType = CharacterMovementType.RUN,
) {
    withTimeout(timeoutMs) {
        while (observer.observe().position.distanceTo(startPosition) < distance) {
            character.moveAndRotate(movement = orientation.normalizeAsMovement(characterMovementType))
            yield()
        }
    }
}

suspend fun SpaceEngineers.blockingMoveForwardByDistance(
    distance: Float,
    startPosition: Vec3,
    timeoutMs: Long = 20000,
    characterMovementType: CharacterMovementType = CharacterMovementType.RUN,
) {
    blockingMoveByDistance(distance = distance, startPosition = startPosition, timeoutMs = timeoutMs, characterMovementType = characterMovementType, orientation = Vec3.FORWARD)
}

suspend fun SpaceEngineers.blockingMoveBackwardsByDistance(
    distance: Float,
    startPosition: Vec3,
    timeoutMs: Long = 20000,
    characterMovementType: CharacterMovementType = CharacterMovementType.RUN,
) {
    blockingMoveByDistance(distance = distance, startPosition = startPosition, timeoutMs = timeoutMs, characterMovementType = characterMovementType, orientation = Vec3.BACKWARD)
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
