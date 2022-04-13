package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class DampenerTest : MockOrRealGameTest() {


    @Test
    fun testDampenersOff() = testContext {
        assertTrue(observer.observe().dampenersOn)
        delay(100)
        character.turnOffDampeners()
        assertFalse(observer.observe().dampenersOn)
        delay(100)
        character.turnOnDampeners()
        delay(100)
        assertTrue(observer.observe().dampenersOn)
    }
}
