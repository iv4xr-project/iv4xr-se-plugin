package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CharacterObservationTest : MockOrRealGameTest(inMockResourcesDirectory("CharacterObservationTest.txt")) {

    @Test
    fun characterFlagStats() = testContext {
        val characterObservation = observer.observe()
        assertTrue(characterObservation.helmetEnabled)
        assertFalse(characterObservation.jetpackRunning)
    }


    @Test
    fun bootState() = testContext {
        assertEquals(0, observer.observe().bootsState)
    }

    @Test
    fun characterBottomLeftStatsCreativeMode() = testContext {
        val characterObservation = observer.observe()
        assertEquals(1f, characterObservation.health)
        assertEquals(1f, characterObservation.oxygen)
        assertEquals(1f, characterObservation.hydrogen)
        assertEquals(1f, characterObservation.suitEnergy)
    }

    @Test
    fun inventory() = testContext {
        val inventory = observer.observe().inventory
        assertEquals(0f, inventory.cargoPercentage)
        assertEquals(94.8f, inventory.currentMass)
        assertEquals(0.3138f, inventory.currentVolume)
        assertEquals(9.223372E12f, inventory.maxMass)
        assertEquals(9.223372E12f, inventory.maxVolume)
        assertEquals(7, inventory.items.size)
    }

}
