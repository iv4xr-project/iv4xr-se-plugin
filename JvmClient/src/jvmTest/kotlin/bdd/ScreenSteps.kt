package bdd

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.TerminalBlock
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.extensions.allBlocks
import java.lang.Thread.sleep
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ScreenSteps : AbstractMultiplayerSteps() {



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

    @Then("Gameplay screen shows {string} beacon.")
    fun gameplay_screen_shows_beacon(oreName: String) = mainClient {
        assertTrue(screens.gamePlay.data().oreMarkers.any { it.text == oreName })
    }

    @Then("Gameplay screen shows beacons for:")
    fun gameplay_screen_shows_beacons_for(map: List<Map<String, String>>) = mainClient {
        map.forEach {
            val oreName = it.getValue("ore")
            assertTrue(screens.gamePlay.data().oreMarkers.any { it.text == oreName })
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

    @Given("Character inventory contains ingot {string}.")
    fun character_inventory_contains(type: String) = mainClient {
        if (screens.focusedScreen() != "TerminalScreen") {
            character.showInventory()
        }
        val definition = DefinitionId.create("Ingot", type)
        assertTrue(observer.observe().inventory.items.any { it.id == definition })
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

    @Then("Character inventory does not contain ingot {string} anymore.")
    fun character_inventory_does_not_contain_anymore(type: String) = observers {
        val definition = DefinitionId.create("Ingot", type)
        assertTrue(observer.observe().inventory.items.none { it.id == definition })
    }

    @Then("There is floating ingot {string} in space around.")
    fun there_is_floating_in_space_around(type: String) = observers {
        val definition = DefinitionId.create("Ingot", type)
        assertTrue(observer.observeFloatingObjects().any { it.itemDefinition.definitionId == definition })
    }


    @Given("Character inventory contains ore {string}.")
    fun character_inventory_contains_ore(type: String) = mainClient {
        if (screens.focusedScreen() != "TerminalScreen") {
            character.showInventory()
        }
        val definition = DefinitionId.create("Ore", type)
        assertTrue(observer.observe().inventory.items.any { it.id == definition })
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

    @Then("Character inventory does not contain ore {string} anymore.")
    fun character_inventory_does_not_contain_ore_anymore(type: String) = observers {
        val definition = DefinitionId.create("Ore", type)
        assertTrue(observer.observe().inventory.items.none { it.id == definition })
    }

    @Then("There is floating ore {string} in space around.")
    fun there_is_floating_ore_in_space_around(type: String) = observers {
        val definition = DefinitionId.create("Ore", type)
        assertTrue(observer.observeFloatingObjects().any { it.itemDefinition.definitionId == definition })
    }

    @Given("Character inventory contains no components.")
    fun character_inventory_contains_no_components() = observers {
        assertTrue(observer.observe().inventory.items.none { it.id.id == "MyObjectBuilder_Component" })
    }

    @Given("Character stands in front of block {string}.")
    fun character_stands_in_front_of_block(type: String) = observers {
        assertEquals(observer.observe().targetBlock?.definitionId?.type, type)
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

    @Then("Character inventory contains components:")
    fun character_inventory_contains_components(map: List<Map<String, String>>) = observers {
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

    @When("Character equips {string}.")
    fun character_equips(tool: String) = mainClient {
        val toolbarLocation = items.toolbar().findLocation(tool) ?: error("Item $tool not found in the toolbar!")
        items.equip(toolbarLocation)
        pause()
    }

    @Given("Test waits for {int} seconds.")
    fun test_waits_for_seconds(seconds: Int) {
        sleep(seconds * 1000L)
    }

}
