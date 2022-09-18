package spaceEngineers.game.mockable

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.extensions.typedFocusedScreen
import spaceEngineers.model.ScreenName
import spaceEngineers.model.ScreenName.Companion.JoinGame
import spaceEngineers.model.ScreenName.Companion.LoadGame
import spaceEngineers.model.ScreenName.Companion.MainMenu
import spaceEngineers.model.ScreenName.Companion.NewGame
import spaceEngineers.model.ScreenName.Companion.ServerConnect
import spaceEngineers.navigation.ScreenNavigation
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ScreenNavigationTest : MockOrRealGameTest(loadScenario = false) {

    @Test
    fun testFromMenuToServerConnectAndBack() = testContext {
        navigateToScreenAndBack(MainMenu, ServerConnect)
    }

    @Test
    fun testLoadAndBack() = testContext {
        navigateToScreenAndBack(MainMenu, LoadGame)
    }

    @Test
    fun testJoinAndBack() = testContext {
        navigateToScreenAndBack(MainMenu, JoinGame)
    }

    @Test
    fun testNewAndBack() = testContext {
        navigateToScreenAndBack(MainMenu, NewGame)
    }

    private suspend fun SpaceEngineers.navigateToScreenAndBack(from: ScreenName, to: ScreenName) {
        assertEquals(from, screens.typedFocusedScreen())
        val navigation = ScreenNavigation(spaceEngineers = this)

        navigation.navigateTo(to)
        assertEquals(to, screens.typedFocusedScreen())

        navigation.navigateTo(from)
        assertEquals(from, screens.typedFocusedScreen())
    }
}
