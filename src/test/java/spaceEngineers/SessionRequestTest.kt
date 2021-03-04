package spaceEngineers

import org.junit.Ignore
import org.junit.jupiter.api.Assertions.assertTrue
import spaceEngineers.SeRequest
import spaceEngineers.commands.SeSessionCommand
import testhelp.environment

@Ignore("Not ready for unit testing")
class SessionRequestTest {
    // Ignore annotation somehow not working in IDEA
    //@Test
    fun sessionLoadTest() = environment {
        // TODO(PP): support relative path
        val result = getSeResponse(SeRequest.session(SeSessionCommand.load(
                "C:\\Users\\<user>\\AppData\\Roaming\\SpaceEngineers\\Saves\\76561198248453892\\Alien Planet 2020-09-18 12-41")))
        assertTrue(result)
    }
}