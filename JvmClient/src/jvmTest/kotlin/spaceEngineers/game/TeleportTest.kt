package spaceEngineers.game

import spaceEngineers.model.Vec3
import testhelp.spaceEngineersSimplePlaceGrindTorch
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


class TeleportTest {


    @Test
    fun teleportPosition() = spaceEngineersSimplePlaceGrindTorch {
        val distance = 50f
        val observation = observer.observe()
        admin.character.teleport(observation.position.let { it.copy(x = it.x + distance) })
        assertEquals(observation.position.x + distance, observer.observe().position.x)
    }

    @Test
    fun rotate() = spaceEngineersSimplePlaceGrindTorch {
        val x = Vec3(1, 0, 0)
        var observation = observer.observe()
        assertNotEquals(x, observation.orientationForward)
        admin.character.teleport(observation.position, orientationForward = x, orientationUp = Vec3(0, 1, 0))
        observation = observer.observe()
        assertEquals(x, observation.orientationForward)
    }
}
