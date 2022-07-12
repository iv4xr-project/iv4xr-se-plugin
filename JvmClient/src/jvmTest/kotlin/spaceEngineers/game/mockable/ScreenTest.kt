package spaceEngineers.game.mockable

import spaceEngineers.controller.extensions.blockDefinitionByType
import spaceEngineers.model.DefinitionId
import spaceEngineers.model.Vec3F
import kotlin.test.Ignore
import testhelp.MockOrRealGameTest
import java.lang.Thread.sleep
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@Ignore
class ScreenTest : MockOrRealGameTest(forceRealGame = true, loadScenario = false, scenarioId = "automation-se") {


    @Test
    fun medicalRooms() = testContext {
        screens.medicals.data().medicalRooms.apply(::println)
    }

    @Test
    fun respawn() = testContext {
        screens.medicals.selectRespawn(1)
        screens.medicals.respawn()
    }

    @Test
    fun observe() = testContext {
        observer.observe()
    }

    @Test
    fun focusedScreen() = testContext {
        println(screens.focusedScreen())
    }

    @Test
    fun showMainMenu() = testContext {
        screens.gamePlay.showMainMenu()
        sleep(300)
        screens.mainMenu.exitToMainMenu()
        sleep(1300)
        screens.messageBox.pressNo()
    }
    @Test
    fun exitToMainMenu() = testContext {
        session.exitToMainMenu()
    }

    @Test
    fun chooseFaction() = testContext {
        screens.medicals.selectFaction(0)
    }

    @Test
    fun factions() = testContext {
        screens.medicals.data().factions.apply(::println)
    }

    @Test
    fun waitUntilTheGameLoaded() = testContext {
        screens.waitUntilTheGameLoaded()
    }

    @Test
    fun transferItem() = testContext {
        val firstLeftInventory = screens.terminal.inventory.data().leftInventories.first()
        val firstItem = firstLeftInventory.items.first()
        screens.terminal.inventory.transferInventoryItem(0, 0, firstItem.itemId)
    }

    @Test
    fun switchLeftInventoryToCharacter() = testContext {
        screens.terminal.inventory.left.swapToCharacterOrItem()
    }

    @Test
    fun switchLeftInventoryToGrid() = testContext {
        screens.terminal.inventory.left.swapToGrid()
    }


    @Test
    fun switchRightInventoryToGrid() = testContext {
        screens.terminal.inventory.right.swapToGrid()
    }

    @Test
    fun filterLeftSide() = testContext {
        screens.terminal.inventory.left.filter("Gener")
    }

    @Test
    fun filterLeftEnergy() = testContext {
        screens.terminal.inventory.left.filterEnergy()
    }

    @Test
    fun filterLeftAll() = testContext {
        screens.terminal.inventory.left.filterAll()
    }

    @Test
    fun deposit() = testContext {
        screens.terminal.inventory.deposit()
    }

    @Test
    fun withdraw() = testContext {
        screens.terminal.inventory.withdraw()
    }

    @Test
    fun leftToggleHideEmpty() = testContext {
        screens.terminal.inventory.left.toggleHideEmpty()
    }

    @Test
    fun showTerminal() = testContext {
        character.showTerminal()
    }

    @Test
    fun showInventory() = testContext {
        character.showInventory()
    }

    @Test
    fun selectItemAndClick() = testContext {
        screens.terminal.inventory.left.selectItem(0)
        screens.terminal.inventory.dropSelected()
        //screens.terminal.inventory.left.doubleClickSelectedItem()
    }

    @Test
    fun mainMenuContinue() = testContext {
        screens.mainMenu.`continue`()
    }

    @Test
    fun exitToWindows() = testContext {
        screens.mainMenu.exitToWindows()
        println(screens.messageBox.data())
        screens.messageBox.pressNo()
    }

    @Test
    fun messageBoxData() = testContext {
        println(screens.messageBox.data())
    }

    @Test
    fun character() = testContext {
        screens.mainMenu.character()
    }

    @Test
    fun directConnect() = testContext {
        screens.serverConnect.connect()
    }

    @Test
    fun enterAddress() = testContext {
        screens.mainMenu.joinGame()
        delay(100)
        screens.joinGame.directConnect()
        screens.serverConnect.enterAddress("1.2.3.4:20016")
    }

    @Test
    fun enterAddress2() = testContext {
        screens.serverConnect.enterAddress("1.2.3.4:20016")
        screens.serverConnect.toggleAddServerToFavorites()
        println(screens.serverConnect.data())
    }

    @Test
    fun closeTerminal() = testContext {
        screens.terminal.close()
    }

    @Test
    fun selectLoad() = testContext {
        //screens.mainMenu.loadGame()
        //delay(5000)
        screens.loadGame.doubleClickWorld(0)
    }

    @Test
    fun loadData() = testContext {
        println(screens.loadGame.data())
    }

    @Test
    fun production() = testContext {
        val blockDisplayName = "Cockpit"
        val definition = DefinitionId.create("Cockpit", "LargeBlockCockpit")
        val blockDefinitions = definitions.blockDefinitions().filter { it.definitionId == definition }
        with(screens.terminal.production) {
            selectBlueprint(0)
            enterBlueprintSearchBox(blockDisplayName)
            val data = data()
            assertEquals(2, data.blueprints.size)
            assertTrue(data.productionQueue.isEmpty())
            val index = data.blueprints.indexOfFirst { it.displayName == blockDisplayName }
            addToProductionQueue(index)
            val productionQueue = data().productionQueue
            productionQueue.forEach { it ->
                val blueprint = it.blueprint
                println(blueprint.displayName + " (${it.amount})" + blueprint.prerequisites.map { "${it.amount}: ${it.id}" } + " -> " + blueprint.results.map { "${it.amount}: ${it.id}" })
                println()
            }
            //println(productionQueue.flatMap { it.blueprint.results }.map { "${it.amount}: ${it.id}" })
            blockDefinitions.forEach { blockDefinition ->
                println(blockDefinition.components.map { "${it.count}: ${it.definition.definitionId} - ${it.deconstructItem.definitionId}" })
                assertEquals(productionQueue.size, blockDefinition.components.size)
            }


        }
    }

    @Test
    fun productionData() = testContext {
        screens.terminal.production.data().productionQueue.forEach(::println)
    }

    @Test
    fun gamePlayData() = testContext {
        println(screens.gamePlay.data())
    }

    @Test
    fun drillDefinitions() = testContext {
        println(definitions.allDefinitions().map { it.definitionId }.filter { it.toString().contains("Drill") })
    }
}
