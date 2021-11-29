package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Ignore
class MultiAgentTest : MockOrRealGameTest(forceRealGame = true, loadScenario = true) {


    val DEFAULT_ID = "se0"

    @Test
    fun createAndSetJetpack() = testContext {
        admin.character.switch(DEFAULT_ID)
        val observation = observer.observe()

        val newIds = listOf("se1", "se2")
        val ids = listOf(DEFAULT_ID) + newIds

        newIds.forEach { id ->
            admin.character.create(
                id,
                observation.position,
                observation.orientationForward,
                observation.orientationUp
            )
        }

        ids.forEach { id->
            admin.character.switch(id)
            assertFalse(observer.observe().jetpackRunning)
            character.turnOnJetpack()
            assertTrue(observer.observe().jetpackRunning)
        }
    }

}
