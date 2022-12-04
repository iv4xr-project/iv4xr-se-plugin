package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SwitchHelmetTest : MockOrRealGameTest() {

    @Test
    fun switchHelmet() = testContext {
        assertTrue(observer.observe().helmetEnabled)
        character.switchHelmet()
        assertFalse(observer.observe().helmetEnabled)
        character.switchHelmet()
        assertTrue(observer.observe().helmetEnabled)
    }
}
