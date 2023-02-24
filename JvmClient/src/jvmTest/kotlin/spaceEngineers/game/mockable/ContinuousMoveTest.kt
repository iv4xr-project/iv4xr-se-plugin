package spaceEngineers.game.mockable

import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.normalizeAsWalk
import testhelp.MockOrRealGameTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class ContinuousMoveTest : MockOrRealGameTest() {

    @Test
    fun moveByTicks() = testContext {
        delay(10_000)
        val position = observer.observe().position
        character.moveAndRotate(
            movement = Vec3F.FORWARD.normalizeAsWalk(),
            ticks = 60,
        )
        delay(6000)
        val position2 = observer.observe().position
        assertEquals(2.9368255f, (position2 - position).length(), 0.01f)
    }

    @Ignore
    @Test
    fun moveByTicksAnotherCharacter() = testContext {
        delay(5_000)
        val obs = observer.observe()
        admin.character.create("id", position = obs.position + obs.orientationForward * 2.5f, obs.orientationForward, obs.orientationUp).id
        admin.character.teleport(obs.position + obs.orientationForward * 2.5f)
        val position = observer.observe().position
        character.moveAndRotate(
            movement = Vec3F.FORWARD.normalizeAsWalk(),
            ticks = 60,
        )
        delay(2000)
        val position2 = observer.observe().position
        assertEquals(3.0498333f, (position2 - position).length(), 0.001f)
    }

    @Test
    fun moveByTicksJetpack() = testContext {
        delay(10_000)
        val position = observer.observe().position
        character.turnOnJetpack()
        character.moveAndRotate(
            movement = Vec3F.FORWARD.normalizeAsWalk(),
            ticks = 60,
        )
        delay(6000)
        val position2 = observer.observe().position
        assertEquals(2.3818512f, (position2 - position).length(), absoluteTolerance = 0.01f)
    }
}
