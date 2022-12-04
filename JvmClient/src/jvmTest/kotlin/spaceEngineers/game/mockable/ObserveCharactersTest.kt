package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import testhelp.assertCharacterObservationEquals
import kotlin.test.Test
import kotlin.test.assertEquals

class ObserveCharactersTest : MockOrRealGameTest() {

    @Test
    fun singleCharacter() = testContext {
        val characterObservations = observer.observeCharacters()
        assertEquals(1, characterObservations.size)
        val characterObservation = observer.observe()
        assertCharacterObservationEquals(characterObservation, characterObservations.first())
    }

    @Test
    fun doubleCharacter() = testContext {
        val observation = observer.observe()

        admin.character.create(
            "se1",
            observation.position,
            observation.orientationForward,
            observation.orientationUp
        )

        val characterObservations = observer.observeCharacters()
        assertEquals(2, characterObservations.size)
    }
}
