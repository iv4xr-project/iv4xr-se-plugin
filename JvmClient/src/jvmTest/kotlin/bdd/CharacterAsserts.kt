package bdd

import bdd.repetitiveassert.repeatUntilSuccess
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import kotlinx.coroutines.delay
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
import spaceEngineers.model.extensions.allBlocks
import testhelp.*
import java.lang.Thread.sleep
import kotlin.test.*

class CharacterAsserts : AbstractMultiplayerSteps() {


    @Then("Character is standing.")
    fun character_is_standing() = observers {
        assertTrue(observer.observe().movement.isStanding)
    }

    @Then("Character is crouching.")
    fun character_is_crouching() = observers {
        assertTrue(observer.observe().movement.isCrouching)
    }

    @Then("Character boots are green.")
    fun character_boots_are_green() = observers {
        repeatUntilSuccess {
            observer.observe().bootsState.let { bootState ->
                assertTrue(bootState.isGreen(), message = "BootState is $bootState")
            }
        }
    }

    @Then("Character boots are yellow.")
    fun character_boots_are_yellow() = observers {
        repeatUntilSuccess {
            observer.observe().bootsState.let { bootState ->
                assertTrue(bootState.isYellow(), message = "BootState is $bootState")
            }
        }
    }

    @Then("Character boots are white.")
    fun character_boots_are_white() = observers {
        repeatUntilSuccess {
            observer.observe().bootsState.let { bootState ->
                assertTrue(bootState.isWhite(), message = "BootState is $bootState")
            }
        }
    }

    @Then("Character speed is {int} m\\/s.")
    fun character_speed_is_100m_s(speed: Int) = observers {
        assertEquals(speed.toFloat(), observer.observe().velocity.length(), DEFAULT_SPEED_TOLERANCE)
    }

    @Then("jetpack is off.")
    fun jetpack_is_off() {
        mainClient {
            repeatUntilSuccess {
                assertFalse(screens.gamePlay.data().hud.statsWrapper.jetpackOn)
            }
        }
        observers {
            repeatUntilSuccess {
                assertFalse(observer.observe().jetpackRunning)
            }
        }
    }

    @Then("jetpack is on.")
    fun jetpack_is_on() {
        mainClient {
            repeatUntilSuccess {
                assertTrue(screens.gamePlay.data().hud.statsWrapper.jetpackOn)
            }
        }
        observers {
            repeatUntilSuccess {
                assertTrue(observer.observe().jetpackRunning)
            }
        }
    }

    @Then("Character forward orientation is \\({double}, {double}, {double}).")
    fun character_is_facing(x: Double, y: Double, z: Double) = observers {
        val position = Vec3F(x, y, z)
        assertVecEquals(
            position,
            observer.observe().orientationForward,
            absoluteTolerance = DEFAULT_ORIENTATION_TOLERANCE
        )
    }

    @Then("Character speed is {int} m\\/s after {long} milliseconds.")
    fun character_speed_is_m_s_after_milliseconds(speed: Int, delayMs: Long) {
        sleep(delayMs)
        mainClient {
            assertEquals(speed.toFloat(), screens.gamePlay.data().hud.statsWrapper.speed, DEFAULT_SPEED_TOLERANCE)
        }
        observers {
            assertEquals(speed.toFloat(), observer.observe().velocity.length(), DEFAULT_SPEED_TOLERANCE)
        }
    }


    @Then("Character inventory does not contain ingot {string} anymore.")
    fun character_inventory_does_not_contain_anymore(type: String) = observers {
        val definition = DefinitionId.ingot(type)
        repeatUntilSuccess {
            assertTrue(observer.observe().inventory.items.none { it.id == definition })
        }
    }

    @Then("Character inventory does not contain ore {string} anymore.")
    fun character_inventory_does_not_contain_ore_anymore(type: String) = observers {
        val definition = DefinitionId.ore(type)
        repeatUntilSuccess {
            assertTrue(observer.observe().inventory.items.none { it.id == definition })
        }
    }

    @Given("Character inventory contains no components.")
    fun character_inventory_contains_no_components() = observers {
        assertEquals(0, observer.observe().inventory.items.count { it.id.id == "MyObjectBuilder_Component" })
    }

    @Given("Character stands in front of block {string}.")
    fun character_stands_in_front_of_block(type: String) = observers {
        assertEquals(type, observer.observe().targetBlock?.definitionId?.type)
    }

    @Then("Character inventory contains components:")
    fun character_inventory_contains_components(map: List<Map<String, String>>) = observers {
        repeatUntilSuccess(initialDelayMs = 0, repeats = 2) {
            val items = observer.observe().inventory.items
            map.forEach { row ->
                val definitionId = DefinitionId.create("Component", row["component"]!!)
                val item = items.find { it.id == definitionId }
                assertNotNull(
                    item
                ) {
                    "Item not found: $definitionId"
                }
                assertEquals(
                    item.amount,
                    row["amount"]?.toInt(),
                    "Amount unexpected for $definitionId: ${item.amount} vs ${row["amount"]?.toInt()}"
                )
            }
        }
    }

    @And("Character is at \\({double}, {double}, {double}).")
    fun i_see_character_at_x_y_z(x: Double, y: Double, z: Double) = observers {
        val position = Vec3F(x, y, z)
        assertVecEquals(position, observer.observe().position, absoluteTolerance = DEFAULT_POSITION_TOLERANCE)
    }

    @Then("I can see {int} new block\\(s) with data:")
    fun i_can_see_new_block_with_data(blockCount: Int, data: List<Map<String, String>>) = observers {
        val observation = observer.observeNewBlocks()
        val allBlocks = observation.allBlocks
        assertEquals(
            blockCount,
            allBlocks.size,
            "Expected to see $blockCount blocks, not ${allBlocks.size} ${
                allBlocks.map { it.definitionId.type }.toSet()
            }."
        )
        assertEquals(allBlocks.size, data.size)
        data.forEachIndexed { index, row ->
            val block = allBlocks[index]
            context.lastNewBlock = block
            row["blockType"]?.let {
                assertEquals(it, block.definitionId.type)
            }
            row["integrity"]?.let {
                assertEquals(it.toFloat(), block.integrity)
            }
            row["maxIntegrity"]?.let {
                assertEquals(it.toFloat(), block.maxIntegrity)
            }
            row["buildIntegrity"]?.let {
                assertEquals(it.toFloat(), block.buildIntegrity)
            }
        }
    }

    @Given("character has full hydrogen tank.")
    fun character_has_full_hydrogen_tank() = observers {
        assertEquals(1f, observer.observe().hydrogen, 0.00001f)
    }

    @Given("character has almost full hydrogen tank.")
    fun character_has_almost_full_hydrogen_tank() = observers {
        assertEquals(1f, observer.observe().hydrogen, 0.04f)
    }

    @Given("character has about {int} hydrogen.")
    fun character_has_about_hydrogen(hydrogenPercentage: Int) = observers {
        assertEquals(hydrogenPercentage.toFloat(), observer.observe().hydrogen * 100f, 1f)
    }

    @Then("character doesn't have full hydrogen tank anymore after {int} milliseconds.")
    fun character_doesn_t_have_full_hydrogen_tank_anymore_after_milliseconds(delayMs: Int) = observers {
        delay(delayMs.toLong())
        assertNotEquals(observer.observe().hydrogen, 1f, 0.001f)
    }

    @Then("character has less than {int} hydrogen after {int} milliseconds.")
    fun character_has_less_than_hydrogen_after_milliseconds(hydrogenPercentage: Int, delayMs: Int) = observers {
        delay(delayMs.toLong())
        observer.observe().hydrogen.let { hydrogen ->
            assertLessThan(hydrogen, hydrogenPercentage / 100f, "Hydrogen level is ${hydrogen * 100}")

        }
    }

    @Then("Character has empty hydrogen tank after {int} milliseconds.")
    fun character_has_empty_hydrogen_tank_after_milliseconds(delayMs: Int) = observers {
        delay(delayMs.toLong())
        assertEquals(0f, observer.observe().hydrogen)
    }

    @Then("Character begins to fall towards the ground.")
    fun character_begins_to_fall_towards_the_ground() {
        val gravity = mainClient {
            //gravity is not synchronized, we need to take it from the main client
            with(observer.observe()) {
                assertSameDirection(velocity, gravity)
                gravity
            }
        }
        observers {
            with(observer.observe()) {
                assertSameDirection(velocity, gravity)
                assertGreaterThan(velocity.length(), 0f)
            }
        }
    }

    @Then("Character velocity direction is up.")
    fun character_velocity_direction_is_up() = observers {

    }

    @Then("Character is jumping.")
    fun character_is_jumping() {
        // jumping is actually for a very short time, the rest is falling (even if it's still going up)
        mainClient {
            repeatUntilSuccess {
                with(observer.observe()) {
                    assertSameDirection(velocity, orientationUp)
                    assertTrue(movement.isFalling, movement.toString())
                }
            }
        }
        observers {
            repeatUntilSuccess {
                with(observer.observe()) {
                    //assertSameDirection(velocity, orientationUp)
                    assertTrue(movement.isFalling, movement.toString())
                }
            }
        }
    }

    @Then("dampeners are off.")
    fun dampeners_are_off() {
        mainClient {
            assertFalse(screens.gamePlay.data().hud.statsWrapper.dampenersOn)
            assertFalse(observer.observe().dampenersOn)
        }
        observers {
            //assertFalse(observer.observe().dampenersOn)
        }
    }

    @Then("dampeners are on.")
    fun dampeners_are_on() {
        mainClient {
            assertTrue(screens.gamePlay.data().hud.statsWrapper.dampenersOn)
        }
        observers {
            assertTrue(observer.observe().dampenersOn)
        }
    }

    @Then("Character dampeners are on.")
    fun character_dampeners_are_on() = mainClient {
        repeatUntilSuccess { assertTrue(observer.observe().dampenersOn) }
    }

    @Then("Character dampeners are off.")
    fun character_dampeners_are_off() = mainClient {
        //TODO: maybe they are not synchronized
        repeatUntilSuccess { assertFalse(observer.observe().dampenersOn) }
    }

    @Then("UI dampeners are on.")
    fun ui_dampeners_are_on() = mainClient {
        assertTrue(screens.gamePlay.data().hud.statsWrapper.dampenersOn)
    }

    @Then("UI dampeners are off.")
    fun ui_dampeners_are_off() = mainClient {
        assertFalse(screens.gamePlay.data().hud.statsWrapper.dampenersOn)
    }

    @Then("Dampeners are switched to relative mode.")
    fun dampeners_are_switched_to_relative_mode() = mainClient {
        assertNotNull(observer.observe().relativeDampeningEntity)
        assertTrue(screens.gamePlay.data().hud.statsWrapper.relativeDampenersOn)
    }
}
