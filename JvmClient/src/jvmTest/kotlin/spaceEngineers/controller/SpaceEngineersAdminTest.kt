package spaceEngineers.controller

import testhelp.MockOrRealGameTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SpaceEngineersAdminTest : MockOrRealGameTest(loadScenario = false) {

    @Test
    fun testPing() = testContext {
        val result = admin.ping()
        assertEquals("Pong", result)
    }

    @Test
    fun testEcho() = testContext {
        val result = admin.echo("goodbye")
        assertEquals("goodbye", result)
    }
}
