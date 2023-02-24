package spaceEngineers.game.mockable

import org.junit.jupiter.api.Test
import testhelp.MockOrRealGameTest
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class CharacterStatsTest : MockOrRealGameTest() {

    @Test
    fun initialHealth() = testContext {
        delay(10.seconds)
        assertEquals(1f, observer.observe().health, absoluteTolerance = 0.001f)
        assertEquals(1f, screens.gamePlay.data().hud.statsWrapper.health, absoluteTolerance = 0.001f)
    }

    @Test
    fun healthToHalf() = testContext {
        delay(10.seconds)
        admin.character.updateHealth(0.5f)
        assertEquals(0.5f, observer.observe().health, absoluteTolerance = 0.001f)
        assertEquals(0.5f, screens.gamePlay.data().hud.statsWrapper.health, absoluteTolerance = 0.001f)
    }

    @Test
    fun initialEnergy() = testContext {
        delay(10.seconds)
        assertEquals(1f, observer.observe().energy, absoluteTolerance = 0.001f)
        assertEquals(1f, screens.gamePlay.data().hud.statsWrapper.energy, absoluteTolerance = 0.001f)
    }

    @Test
    fun energyToHalf() = testContext {
        delay(10.seconds)
        admin.character.updateEnergy(0.5f)
        assertEquals(0.5f, observer.observe().energy, absoluteTolerance = 0.001f)
        assertEquals(0.5f, screens.gamePlay.data().hud.statsWrapper.energy, absoluteTolerance = 0.001f)
    }

    @Test
    fun initialOxygen() = testContext {
        delay(10.seconds)
        assertEquals(1f, observer.observe().oxygen, absoluteTolerance = 0.001f)
        assertEquals(1f, screens.gamePlay.data().hud.statsWrapper.oxygen, absoluteTolerance = 0.001f)
    }

    @Test
    fun oxygenToHalf() = testContext {
        delay(10.seconds)
        admin.character.updateOxygen(0.5f)
        assertEquals(0.5f, observer.observe().oxygen, absoluteTolerance = 0.001f)
        assertEquals(0.5f, screens.gamePlay.data().hud.statsWrapper.oxygen, absoluteTolerance = 0.001f)
    }

    @Test
    fun initialHydrogen() = testContext {
        delay(10.seconds)
        assertEquals(1f, observer.observe().hydrogen, absoluteTolerance = 0.001f)
        assertEquals(1f, screens.gamePlay.data().hud.statsWrapper.hydrogen, absoluteTolerance = 0.001f)
    }

    @Test
    fun hydrogenToHalf() = testContext {
        delay(10.seconds)
        admin.character.updateHydrogen(0.5f)
        assertEquals(0.5f, observer.observe().hydrogen, absoluteTolerance = 0.001f)
        assertEquals(0.5f, screens.gamePlay.data().hud.statsWrapper.hydrogen, absoluteTolerance = 0.001f)
    }
}
