package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class MultiAgentTest : MockOrRealGameTest() {


    @Test
    fun createAndSetJetpack() = testContext {
        val observation = observer.observe()

        val newCharacterNames = listOf("se1", "se2")

        newCharacterNames.forEach { id ->
            admin.character.create(
                id,
                observation.position,
                observation.orientationForward,
                observation.orientationUp
            )
        }

        observer.observeCharacters().forEach { characterObservation ->
            assertFalse(characterObservation.jetpackRunning)
            admin.character.switch(characterObservation.id)
            character.turnOnJetpack()
            assertTrue(observer.observe().jetpackRunning)
        }

    }

}
