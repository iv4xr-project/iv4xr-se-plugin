package spaceEngineers.controller.extensions

import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.Vec2F
import spaceEngineers.model.Vec3F

suspend fun SpaceEngineers.blockingRotateUntilOrientationForward(
    finalOrientation: Vec3F,
    rotation: Vec2F,
    delta: Float = 0.01f,
    timeoutMs: Long = 30000,
) {
    withTimeout(timeoutMs) {
        while (observer.observe().let {
            !finalOrientation.similar(it.camera.orientationForward, delta = delta)
        }
        ) {
            character.moveAndRotate(rotation3 = rotation)
            yield()
        }
    }
}

suspend fun SpaceEngineers.blockingRotateUntilOrientationUp(
    finalOrientation: Vec3F,
    rotation: Vec2F,
    delta: Float = 0.01f,
    timeoutMs: Long = 30000,
) {
    withTimeout(timeoutMs) {
        while (observer.observe().let {
            !finalOrientation.similar(
                    it.camera.orientationUp,
                    delta = delta
                )
        }
        ) {
            character.moveAndRotate(rotation3 = rotation)
            yield()
        }
    }
}
