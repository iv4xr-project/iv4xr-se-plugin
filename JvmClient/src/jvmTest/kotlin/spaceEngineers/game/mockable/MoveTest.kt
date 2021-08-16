package spaceEngineers.game.mockable

import spaceEngineers.controller.extensions.blockingMoveBackwardsByDistance
import spaceEngineers.controller.extensions.blockingMoveForwardByDistance
import spaceEngineers.controller.extensions.moveBackward
import spaceEngineers.model.CharacterMovementType
import spaceEngineers.model.Vec3
import spaceEngineers.model.extensions.normalizeAsRun
import spaceEngineers.model.extensions.normalizeAsSprint
import spaceEngineers.model.extensions.normalizeAsWalk
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals


class MoveTest : MockOrRealGameTest(loadScenario = false) {

    @Test
    fun singleWalkForward100Times() = testContext {
        val position = observer.observe().position
        repeat(100) {
            character.moveAndRotate(Vec3.FORWARD.normalizeAsWalk())
        }
        val newPosition = observer.observe().position
        assertEquals(4.9258146f, (newPosition - position).size, 0.025f)
    }

    @Test
    fun singleRunForward100Times() = testContext {
        val position = observer.observe().position
        repeat(100) {
            character.moveAndRotate(Vec3.FORWARD.normalizeAsRun())
        }
        val newPosition = observer.observe().position
        assertEquals(9.698216f, (newPosition - position).size, 0.025f)
    }

    @Test
    fun singleSprintForward100Times() = testContext {
        val position = observer.observe().position
        repeat(100) {
            character.moveAndRotate(Vec3.FORWARD.normalizeAsSprint())
        }
        val newPosition = observer.observe().position
        assertEquals(16.238842f, (newPosition - position).size, 0.025f)
    }


    @Test
    fun singleWalkBackward100Times() = testContext {
        val position = observer.observe().position
        repeat(3) {
            character.moveBackward(CharacterMovementType.WALK)
        }
        val newPosition = observer.observe().position
        assertEquals(0.08351894f, (newPosition - position).size, 0.001f)
    }

    @Test
    fun blockingMoveForward() = testContext {
        val startPosition = observer.observe().position
        blockingMoveForwardByDistance(
            distance = 5f,
            startPosition = startPosition
        )
        val newPosition = observer.observe().position
        assertEquals(5f, (startPosition - newPosition).size, 0.05f)
    }

    @Test
    fun blockingMoveBackward() = testContext {
        val startPosition = observer.observe().position
        blockingMoveBackwardsByDistance(
            distance = 5f,
            startPosition = startPosition
        )
        val newPosition = observer.observe().position
        assertEquals(5f, (startPosition - newPosition).size, 0.05f)
    }

}
