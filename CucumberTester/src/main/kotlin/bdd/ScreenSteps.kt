package bdd

import bdd.repetitiveassert.repeatUntilSuccess
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.extensions.typedFocusedScreen
import spaceEngineers.model.*
import spaceEngineers.model.ScreenName.Companion.Terminal
import spaceEngineers.model.extensions.allBlocks
import java.lang.Thread.sleep
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ScreenSteps(connectionManager: ConnectionManager) : AbstractMultiplayerSteps(connectionManager) {

    val filesToCopy = listOf(
        "AustinHarris.JsonRpc.dll",
        "ImpromptuInterface.dll",
        "Ivxr.PlugIndependentLib.dll",
        "Ivxr.SePlugin.dll",
        "Ivxr.SpaceEngineers.dll",
        "Newtonsoft.Json.dll",
    )


    @Given("Character opens terminal of the nearest grid.")
    fun character_opens_terminal_of_the_nearest_grid() = mainClient {
        val firstTerminalBlock = observer.observeBlocks().allBlocks.first { it is TerminalBlock }
        admin.updateDefaultInteractDistance(25f)
        admin.character.showTerminal(firstTerminalBlock.id)
        screens.terminal.selectTab(2)
    }

    @Given("Production queue is empty.")
    fun assembler_production_queue_is_empty() = mainClient {
        assertTrue(screens.terminal.production.data().productionQueue.isEmpty())
    }

    @When("Character adds {string} to production queue.")
    fun character_adds_to_production_queue(blockDisplayName: String) = mainClient {
        with(screens.terminal.production) {
            smallPause()
            selectBlueprint(0)
            smallPause()
            enterBlueprintSearchBox(blockDisplayName)
            smallPause()
            val data = data()
            smallPause()
            assertEquals(2, data.blueprints.size)
            val index = data.blueprints.indexOfFirst { it.displayName == blockDisplayName }
            smallPause()
            addToProductionQueue(index)
            smallPause()
        }
    }

    @Then("Production queue is not empty.")
    fun production_queue_is_not_empty() = mainClient {
        assertTrue(screens.terminal.production.data().productionQueue.isNotEmpty())
    }


    @When("Character equips slot {int} on page {int}. # Drill")
    fun character_equips_slot_on_page_drill(slot: Int, page: Int) = mainClient {
        items.equip(ToolbarLocation(slot = slot - 1, page = page - 1))
        pause()
    }

    @Then("Gameplay screen shows beacons for:")
    fun gameplay_screen_shows_beacons_for(map: List<Map<String, String>>) = mainClient {
        map.forEach {
            val oreName = it.getValue("ore")
            repeatUntilSuccess(repeats = 15) {
                assertTrue(
                    screens.gamePlay.data().oreMarkers.any { it.text == oreName },
                    "Doesn't show beacon for $oreName"
                )
            }
        }
    }

    @Then("Gameplay screen shows no ore beacons.")
    fun gameplay_screen_shows_no_beacons() = mainClient {
        repeatUntilSuccess {
            assertEquals(0, screens.gamePlay.data().oreMarkers.size)
        }
    }

    @Then("Production queue contains exactly:")
    fun production_queue_contains_exactly(map: List<Map<String, String>>) = mainClient {
        val productionQueue = screens.terminal.production.data().productionQueue
        map.forEachIndexed { index, row ->
            val pqi = productionQueue[index]
            assertEquals(pqi.blueprint.displayName, row["name"])
            assertEquals(pqi.amount, row["count"]?.toInt())
        }
    }

    @When("Character selects inventory ingot {string} and drops.")
    fun character_selects_inventory_item_and_drops(type: String) = mainClient {
        with(screens.terminal.inventory) {
            val definition = DefinitionId.create("Ingot", type)
            assertEquals(1, data().leftInventories.size)
            val index = data().leftInventories.first().items.indexOfFirst { it.id == definition }
            if (index < 0) {
                throw IllegalArgumentException("Item $definition not found in the inventory.")
            }
            left.selectItem(index)
            smallPause()
            dropSelected()
        }
    }

    @When("Character selects inventory ore {string} and drops.")
    fun character_selects_inventory_ore_and_drops(type: String) = mainClient {
        with(screens.terminal.inventory) {
            val definition = DefinitionId.create("Ore", type)
            assertEquals(1, data().leftInventories.size)
            val index = data().leftInventories.first().items.indexOfFirst { it.id == definition }
            if (index < 0) {
                throw IllegalArgumentException("Item $definition not found in the inventory.")
            }
            left.selectItem(index)
            dropSelected()
        }
    }

    @When("Character grinds down the block in front completely.")
    fun character_grinds_down_the_block_completely() = mainClient {
        val toolbar = items.toolbar()
        val grinderLocation = toolbar.findLocation("AngleGrinderItem") ?: error("No grinder found")
        items.equip(grinderLocation)
        smallPause()

        val block = observer.observe().targetBlock ?: error("No target block")
        assertNotNull(block)
        character.beginUsingTool()

        withTimeout(10000) {
            while (observer.observe().targetBlock != null) {
                yield()
            }
        }
        character.endUsingTool()
    }


    @When("Character torches up the block in front completely.")
    fun character_torches_up_the_block_completely() = mainClient {
        val toolbar = items.toolbar()
        val grinderLocation = toolbar.findLocation("WelderItem") ?: error("No welder found")
        items.equip(grinderLocation)
        smallPause()

        val block = observer.observe().targetBlock ?: error("No target block")
        assertNotNull(block)
        character.beginUsingTool()

        withTimeout(20000) {
            while (observer.observe().targetBlock?.let { it.integrity < it.maxIntegrity } == true) {
                yield()
            }
        }
        character.endUsingTool()
    }

    @When("Character builds block {string}.")
    fun character_builds_block(blockType: String) = mainClient {
        val toolbarLocation = ToolbarLocation(8, 0)
        items.setToolbarItem(DefinitionId.parse(blockType), toolbarLocation)
        smallPause()
        items.equip(toolbarLocation)
        smallPause()
        blocks.place()
        smallPause()
        items.equip(ToolbarLocation(9, 0))
    }

    @Given("Character opens terminal.")
    fun character_opens_terminal() = mainClient {
        character.showTerminal()
    }

    @Given("Character opens terminal in production tab.")
    fun character_opens_terminal_in_production_tab() = mainClient {
        val assemblerId = DefinitionId.create("Assembler", "LargeAssembler")
        val block = observer.observeBlocks().allBlocks.first { it is TerminalBlock && it.definitionId == assemblerId }
        admin.character.showTerminal(block.id)
        screens.terminal.selectTab(2)
    }

}
