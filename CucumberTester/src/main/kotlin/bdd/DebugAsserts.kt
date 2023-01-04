package bdd

import bdd.connection.ConnectionManager
import bdd.repetitiveassert.repeatUntilSuccess
import io.cucumber.java.PendingException
import io.cucumber.java.en.Then
import spaceEngineers.model.Sound
import kotlin.test.assertTrue


class DebugAsserts(connectionManager: ConnectionManager) : AbstractMultiplayerSteps(connectionManager) {

    @Then("Sound {string} is being played.")
    fun sound_is_being_played(soundNames: String) = mainClient {
        repeatUntilSuccess {
            checkSoundsAreBeingPlayed(soundNames, debug.sounds().sound)
        }
    }

    @Then("Hud sound {string} is being played.")
    fun hud_sound_is_being_played(soundNames: String) = mainClient {
        repeatUntilSuccess(repeats = 15) {
            checkSoundsAreBeingPlayed(soundNames, debug.sounds().hud)
        }
    }

    fun checkSoundsAreBeingPlayed(soundNames: String, sounds: List<Sound>) {
        soundNames.split(",").map { it.trim() }.forEach { soundName ->
            assertTrue(
                sounds.filter {
                    it.isPlaying
                }.any { it.cueEnum == soundName },
                "Playing sound '$soundName' not found, current playing sounds are: ${
                    sounds.filter { it.isPlaying }.map { it.cueEnum }.toSet()
                }"
            )
        }
    }

    @Then("Sound {string} is not being played.")
    fun sound_is_not_being_played(soundName: String) = mainClient {
        val playingSounds = debug.sounds()
        assertTrue(
            playingSounds.sound.filter {
                it.isPlaying
            }.none { it.cueEnum == soundName },
            "Playing '$soundName' is playing when it shouldn't. Playing: ${
                playingSounds.sound.filter { it.isPlaying }.map { it.cueEnum }.toSet()
            }"
        )
    }

    @Then("Particle effect {string} is being played.")
    fun particle_is_being_played(particleNames: String) = mainClient {
        repeatUntilSuccess {
            val position = observer.observe().position

            val originalParticles = debug.particles().effects
            val particles = originalParticles.filter { it.position.distanceTo(position) < 4f }
            if (particleNames == "-") {
                assertTrue(
                    particles.isEmpty(),
                    "Particles are supposed to be empty, but there's: ${particles.map { it.name }.toSet()}"
                )
                return@repeatUntilSuccess
            }
            if (particleNames.isBlank()) {
                return@repeatUntilSuccess
            }

            particleNames.split(",").map { it.trim() }.forEach { particleName ->
                assertTrue(
                    particles.any { it.name == particleName },
                    "Particle '$particleName' not found, current particles are: ${
                        particles.map { it.name }.toSet()
                    }"
                )
            }
        }
    }

    @Then("Text notification {string} appears on HUD.")
    fun text_notification_appears_on_hud(text: String) {
        mainClient {
            repeatUntilSuccess {
                val notifications = screens.gamePlay.data().hud.notifications
                assertTrue(notifications.any {
                    it.text == text
                }, "No notification with text $text found, only ${notifications.map { it.text }}")
            }
            println(screens.gamePlay.data().hud.notifications)
        }
    }
}
