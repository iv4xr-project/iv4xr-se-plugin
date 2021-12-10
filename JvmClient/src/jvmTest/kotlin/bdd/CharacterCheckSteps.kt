package bdd

import io.cucumber.java.en.Then
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import spaceEngineers.model.CharacterMovement
import spaceEngineers.model.Vec3F
import testhelp.assertVecEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CharacterCheckSteps : AbstractSpaceEngineersSteps() {


    @Then("Character is standing.")
    fun character_is_standing() {
        assertEquals(CharacterMovement.standing, observer.observe().movement.value.toInt())
    }

    @Then("Character boots are green.")
    fun character_boots_are_green() {
        assertTrue(observer.observe().bootsState.isGreen())
    }

    @Then("Character boots are green after {int}ms.")
    fun character_boots_are_green_after_ms(ms: Int) = runBlocking {
        delay(ms.toLong())
        assertTrue(observer.observe().bootsState.isGreen())
    }

    @Then("Character boots are yellow.")
    fun character_boots_are_yellow() {
        assertTrue(observer.observe().bootsState.isYellow())
    }

    @Then("Character boots are yellow after {int}ms.")
    fun character_boots_are_yellow_after_ms(ms: Int) = runBlocking {
        delay(ms.toLong())
        assertTrue(observer.observe().bootsState.isYellow())
    }


    @Then("Character speed is {int} m\\/s.")
    fun character_speed_is_100m_s(speed: Int) {
        assertEquals(speed.toFloat(), observer.observe().velocity.length(), 0.0001f)
    }

    @Then("jetpack is off.")
    fun jetpack_is_off() {
        assertFalse(observer.observe().jetpackRunning)
    }

    @Then("jetpack is on.")
    fun jetpack_is_on() {
        assertTrue(observer.observe().jetpackRunning)
    }

    @Then("Character forward orientation is \\({double}, {double}, {double}).")
    fun character_is_facing(x: Double, y: Double, z: Double) {
        val position = Vec3F(x, y, z)
        context.observationHistory.last().let { observation ->
            assertVecEquals(position, observation.character.orientationForward, diff = 0.1f)
        }
    }
}
