package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class JetpackRunningTest : MockOrRealGameTest() {


    @Test
    fun testJetpackNotRunning() = testContext {
        assertFalse(observer.observe().jetpackRunning)
        delay(100)
        character.turnOnJetpack()
        delay(100)
        assertTrue(observer.observe().jetpackRunning)
        delay(100)
        character.turnOffJetpack()
        assertFalse(observer.observe().jetpackRunning)
    }
}
