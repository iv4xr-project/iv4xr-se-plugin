package bdd

import io.cucumber.java.en.Given
import io.cucumber.junit.Cucumber
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@RunWith(Cucumber::class)
class UtilSteps : AbstractMultiplayerSteps() {

    @Given("Test waits for {float} seconds.")
    fun test_waits_for_seconds(seconds: Float) {
        sleep((seconds * 1000f).toLong())
    }

}
