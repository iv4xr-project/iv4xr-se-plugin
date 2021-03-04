package spaceEngineers

import spaceEngineers.environments.SeSocketEnvironment
import nl.uu.cs.aplib.mainConcepts.Environment.EnvOperation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class EngineersCommTest {
    @Test
    fun commScrewingTest() {
        val environment = SeSocketEnvironment("localhost", SeSocketEnvironment.DEFAULT_PORT)

        // These messages are invalid, so we basically test error handling here.
        // (The server cuts of the connection after the first message.)
        val messagesToReplies = mapOf("hello" to "False", "you" to null, "fool" to null)
        for (message in messagesToReplies) {
            val reply = environment.sendCommand(
                    EnvOperation("me", "you", "request", message.key, Any::class.java)
            )
            assertEquals(reply, message.value)
        }
    }
}