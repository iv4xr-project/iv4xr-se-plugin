package spaceEngineers.game.mockable

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.model.CharacterMovementType
import spaceEngineers.model.Vec3F
import spaceEngineers.movement.*
import testhelp.MockOrRealGameTest
import testhelp.assertVecEquals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class MovementWrapperTest : MockOrRealGameTest() {

    private val vectorAbsoluteTolerance = 0.001f

    @Test
    fun vectorRotate() = testContext {
        testRotation(VectorMovement(this))
    }

    @Test
    fun replayRotate() = testContext {
        testRotation(ReplayMovement(this))
    }

    @Test
    fun vectorMovement() = testContext {
        testMovementForward(VectorMovement(this))
    }

    @Test
    fun replayMovement() = testContext {
        testMovementForward(ReplayMovement(this))
    }


    private suspend fun SpaceEngineers.testMovementForward(movement: CharacterMovement) {
        val position = observer.observe().position
        movement.move(CompositeDirection3d.FORWARD, CharacterMovementType.WALK, ticks = 110)
        delay(2000)
        assertEquals(5.4397435f, (position - observer.observe().position).length(), absoluteTolerance = 0.06f)
    }

    private suspend fun SpaceEngineers.testRotation(movement: CharacterMovement) {
        character.turnOnJetpack()
        delay(1500)
        assertTrue(observer.observe().jetpackRunning)
        delay(100)
        admin.character.teleport(position = Vec3F.ZERO, orientationUp = Vec3F.UP, orientationForward = Vec3F.FORWARD)
        delay(100)
        movement.rotate(RotationDirection.UP, ticks = 50)
        delay(1000)
        observer.observe().apply {
            assertVecEquals(
                orientationForward,
                Vec3F(x = 0.0, y = 0.92074883, z = -0.3901558),
                vectorAbsoluteTolerance
            )
            assertVecEquals(
                orientationUp,
                Vec3F(x = 0.0, y = 0.3901558, z = 0.92074883),
                vectorAbsoluteTolerance
            )
        }
    }

}
