package spaceEngineers.game.mockable

import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.normalizeAsWalk
import testhelp.MockOrRealGameTest
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
        assertEquals(2.9368255f, (position2 - position).length())
    }

    @Test
    fun moveByTicksJetpack() = testContext {
        delay(10000)
        val position = observer.observe().position
        character.turnOnJetpack()
        character.moveAndRotate(
            movement = Vec3F.FORWARD.normalizeAsWalk(),
            ticks = 60,
        )
        delay(6000)
        val position2 = observer.observe().position
        assertEquals(3.8190913f, (position2 - position).length())
    }

}
