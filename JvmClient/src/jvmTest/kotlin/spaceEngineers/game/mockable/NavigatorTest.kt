package spaceEngineers.game.mockable

import spaceEngineers.controller.extensions.navigationGraph
import spaceEngineers.navigation.CharacterNavigation
import testhelp.MockOrRealGameTest
import testhelp.assertGreaterThan
import testhelp.assertLessThan
import kotlin.test.Test


class NavigatorTest : MockOrRealGameTest(
    //forceRealGame = true,
    loadScenario = false,  // TODO: The mock file was recorder with loadScenario = false (the scenario lacks gravity, he can't walk)
) {
    @Test
    fun moveInLine() = testContext {
        val navGraph = observer.navigationGraph()
        assertGreaterThan(navGraph.nodes.size, 30)

        val targetLocation = navGraph.nodes[25].position
        val navigator = CharacterNavigation(this)

        navigator.moveInLine(targetLocation)
        assertLessThan(lower = observer.observe().position.distanceTo(targetLocation), higher = 0.6f)
    }
}
