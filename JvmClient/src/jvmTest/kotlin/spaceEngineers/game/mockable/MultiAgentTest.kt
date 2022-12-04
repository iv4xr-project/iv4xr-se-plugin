package spaceEngineers.game.mockable

import spaceEngineers.transport.jsonrpc.KotlinJsonRpcError
import spaceEngineers.transport.jsonrpc.remoteException
import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

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
            character.turnOffJetpack()
        }

        observer.observeCharacters().forEach { characterObservation ->
            assertFalse(characterObservation.jetpackRunning)
            admin.character.switch(characterObservation.id)
            character.turnOnJetpack()
            assertTrue(observer.observe().jetpackRunning)
        }
    }

    @Test
    fun createAndRemoveCharacter() = testContext {
        assertEquals(1, observer.observeCharacters().size)
        val observation = observer.observe()
        val id = "se1"
        val createdCharacterObservation = admin.character.create(
            id,
            observation.position,
            observation.orientationForward,
            observation.orientationUp
        )
        assertEquals(2, observer.observeCharacters().size)
        admin.character.remove(createdCharacterObservation.id)
        assertEquals(1, observer.observeCharacters().size)
    }

    @Test
    fun removeIncorrectId() = testContext {
        assertFailsWith<KotlinJsonRpcError> {
            admin.character.remove("321")
        }.let { jsonRpcError ->
            val remoteException = jsonRpcError.remoteException ?: fail("Remote exception is supposed to be set")
            assertEquals("System.InvalidOperationException", remoteException.className)
            assertEquals("Character (321) not found!", remoteException.message)
        }
    }

    @Test
    fun switchIncorrectId() = testContext {
        assertFailsWith<KotlinJsonRpcError> {
            admin.character.switch("3211")
        }.let { jsonRpcError ->
            val remoteException = jsonRpcError.remoteException ?: fail("Remote exception is supposed to be set")
            assertEquals("System.InvalidOperationException", remoteException.className)
            assertEquals("Character (3211) not found!", remoteException.message)
        }
    }

    @Test
    fun removeMainCharacter() = testContext {
        val characterObservations = observer.observeCharacters()
        assertEquals(1, characterObservations.size)
        val characterObservation = characterObservations.first()
        assertFailsWith<KotlinJsonRpcError> {
            admin.character.remove(characterObservation.id)
        }.let { jsonRpcError ->
            val remoteException = jsonRpcError.remoteException ?: fail("Remote exception is supposed to be set")
            assertEquals("System.InvalidOperationException", remoteException.className)
            assertEquals("Cannot remove the main character!", remoteException.message)
        }
    }
}
