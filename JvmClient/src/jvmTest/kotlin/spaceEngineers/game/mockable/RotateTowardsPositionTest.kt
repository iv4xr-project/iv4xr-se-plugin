package spaceEngineers.game

import kotlinx.coroutines.delay
import spaceEngineers.controller.extensions.blockingRotateUntilOrientationForward
import spaceEngineers.controller.extensions.blockingRotateUntilOrientationUp
import spaceEngineers.model.Vec2
import spaceEngineers.model.Vec3
import testhelp.MockOrRealGameTest
import testhelp.assertVecEquals
import testhelp.spaceEngineersSimplePlaceGrindTorchSuspend
import kotlin.test.Test


class RotateTowardsPositionTes: MockOrRealGameTest() {


    @Test
    fun rotateTowardsPosition() = testContext {
        val observation = observer.observe()
        delay(500)
        character.turnOnJetpack()
        delay(500)
        admin.character.teleport(
            position = observation.position,
            orientationForward = Vec3.FORWARD,
            orientationUp = Vec3.UP,
        )
        delay(500)
        character.turnOffJetpack()
        delay(500)
        blockingRotateUntilOrientationForward(
            finalOrientation = Vec3.LEFT,
            rotation = Vec2.ROTATE_LEFT,
        )
        assertVecEquals(Vec3.LEFT, observer.observe().orientationForward)
    }


    @Test
    fun rotateTowardsPositionUp() = testContext {
        val observation = observer.observe()
        admin.character.teleport(
            position = observation.position,
            orientationForward = Vec3.FORWARD,
            orientationUp = Vec3.UP,
        )
        blockingRotateUntilOrientationUp(
            finalOrientation = Vec3(0f, 1f, 1f).normalized(),
            rotation = Vec2.ROTATE_UP,
        )
        assertVecEquals(Vec3(0f, 1f, 1f).normalized(), observer.observe().camera.orientationUp)
        assertVecEquals(Vec3(0f, 1f, -1f).normalized(), observer.observe().camera.orientationForward)
    }
}
