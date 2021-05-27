package spaceEngineers.game

import spaceEngineers.controller.loadFromTestResources
import testhelp.spaceEngineers
import java.lang.Thread.sleep
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class JetpackRunningTest {


    @Test
    fun testJetpackNotRunning() = spaceEngineers {
        val scenarioId = "simple-place-grind-torch-with-tools"
        session.loadFromTestResources(scenarioId)
        assertFalse(observer.observe().jetpackRunning)
        character.turnOnJetpack()
        assertTrue(observer.observe().jetpackRunning)
        sleep(5000)
        character.turnOffJetpack()
        assertFalse(observer.observe().jetpackRunning)
    }
}
