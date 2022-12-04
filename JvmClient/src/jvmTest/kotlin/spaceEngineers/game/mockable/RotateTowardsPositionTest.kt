package spaceEngineers.game

import spaceEngineers.controller.extensions.blockingRotateUntilOrientationForward
import spaceEngineers.controller.extensions.blockingRotateUntilOrientationUp
import spaceEngineers.model.Vec2F
import spaceEngineers.model.Vec3F
import testhelp.MockOrRealGameTest
import testhelp.assertVecEquals
import kotlin.test.Test

class RotateTowardsPositionTes : MockOrRealGameTest() {

    @Test
    fun rotateTowardsPosition() = testContext {
        val observation = observer.observe()
        delay(500)
        character.turnOnJetpack()
        delay(500)
        admin.character.teleport(
            position = observation.position,
            orientationForward = Vec3F.FORWARD,
            orientationUp = Vec3F.UP,
        )
        delay(500)
        character.turnOffJetpack()
        delay(500)
        blockingRotateUntilOrientationForward(
            finalOrientation = Vec3F.LEFT,
            rotation = Vec2F.ROTATE_LEFT,
        )
        assertVecEquals(Vec3F.LEFT, observer.observe().orientationForward)
    }

    @Test
    fun rotateTowardsPositionUp() = testContext {
        val observation = observer.observe()
        admin.character.teleport(
            position = observation.position,
            orientationForward = Vec3F.FORWARD,
            orientationUp = Vec3F.UP,
        )
        blockingRotateUntilOrientationUp(
            finalOrientation = Vec3F(0f, 1f, 1f).normalized(),
            rotation = Vec2F.ROTATE_UP,
        )
        assertVecEquals(Vec3F(0f, 1f, 1f).normalized(), observer.observe().camera.orientationUp)
        assertVecEquals(Vec3F(0f, 1f, -1f).normalized(), observer.observe().camera.orientationForward)
    }
}
