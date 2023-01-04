package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SwitchHelmetTest : MockOrRealGameTest() {

    @Test
    fun switchHelmet() = testContext {
        assertTrue(observer.observe().helmetEnabled)
        character.setHelmet(false)
        assertFalse(observer.observe().helmetEnabled)
        character.setHelmet(true)
        assertTrue(observer.observe().helmetEnabled)
    }
}
