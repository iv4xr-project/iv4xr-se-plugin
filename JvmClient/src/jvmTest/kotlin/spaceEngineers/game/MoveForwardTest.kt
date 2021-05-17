package spaceEngineers.game

import spaceEngineers.controller.blockingMoveForwardByDistance
import spaceEngineers.controller.observe
import testhelp.assertFloatEquals
import testhelp.controller
import testhelp.mockController
import kotlin.test.Test


class MoveForwardTest {


    @Test
    fun moveAndRotateTest() = controller {
        val basePosition = observe().position
        blockingMoveForwardByDistance(10f).let {
            assertFloatEquals(10f, basePosition.distanceTo(it.position))
        }

    }
}