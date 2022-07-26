package bdd

import io.cucumber.java.en.Given
import io.cucumber.junit.Cucumber
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(Cucumber::class)
class UtilSteps : AbstractMultiplayerSteps() {

    @Given("Test waits for {float} seconds.")
    fun test_waits_for_seconds(seconds: Float) = runBlocking {
        delay((seconds * 1000f).toLong())
    }

}
