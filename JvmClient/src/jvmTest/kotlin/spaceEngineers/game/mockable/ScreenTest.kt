package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Ignore
import kotlin.test.Test


@Ignore
class ScreenTest : MockOrRealGameTest(forceRealGame = true, loadScenario = false) {



    @Test
    fun medicalRooms() = testContext {
        screens.medicals.medicalRooms().apply(::println)
    }

    @Test
    fun respawn() = testContext {
        screens.medicals.respawn(1)
    }

    @Test
    fun focusedScreen() = testContext {
        println(screens.focusedScreen())
    }

    @Test
    fun chooseFaction() = testContext {
        screens.medicals.chooseFaction(0)
    }

    @Test
    fun factions() = testContext {
        screens.medicals.factions().apply(::println)
    }

    @Test
    fun waitUntilTheGameLoaded() = testContext {
        screens.waitUntilTheGameLoaded()
    }

}
