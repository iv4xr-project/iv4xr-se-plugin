package bdd

import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.junit.Cucumber
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import spaceEngineers.controller.connection.ConnectionManager
import java.lang.Thread.sleep

@RunWith(Cucumber::class)
class UtilSteps(connectionManager: ConnectionManager) : AbstractMultiplayerSteps(connectionManager) {

    @Given("Test waits for {float} seconds.")
    fun test_waits_for_seconds(seconds: Float) = runBlocking {
        delay((seconds * 1000f).toLong())
    }

    @And("Test disconnects all clients.")
    fun debug_disconect_clients() {
        all {
            close()
        }
    }

    @And("Test waits indefinitely.")
    fun debug_wait() {
        println("Closed and waiting indefinitely.")
        while (true) {
            sleep(500)
        }
    }
}
