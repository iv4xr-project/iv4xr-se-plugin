package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterObservationTest : MockOrRealGameTest() {

    @Test
    fun characterStatsCreativeMode() = testContext {
        val characterObservation = observer.observe()
        assertEquals(1f, characterObservation.health)
        assertEquals(1f, characterObservation.oxygen)
        assertEquals(1f, characterObservation.hydrogen)
        assertEquals(1f, characterObservation.suitEnergy)
    }

}
