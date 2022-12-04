package spaceEngineers.game.mockable

import spaceEngineers.model.Vec3F
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TeleportTest : MockOrRealGameTest() {

    @Test
    fun teleportPosition() = testContext {
        val distance = 50f
        val observation = observer.observe()
        admin.character.teleport(observation.position.let { it.copy(x = it.x + distance) })
        assertEquals(observation.position.x + distance, observer.observe().position.x)
    }

    @Test
    fun rotate() = testContext {
        val x = Vec3F(1, 0, 0)
        var observation = observer.observe()
        assertNotEquals(x, observation.orientationForward)
        admin.character.teleport(observation.position, orientationForward = x, orientationUp = Vec3F(0, 1, 0))
        observation = observer.observe()
        assertEquals(x, observation.orientationForward)
    }
}
