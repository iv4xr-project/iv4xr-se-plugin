package spaceEngineers.game.mockable

import kotlin.test.Ignore
import testhelp.MockOrRealGameTest
import kotlin.test.Test


@Ignore
class ScreenTest : MockOrRealGameTest(forceRealGame = true, loadScenario = false) {



    @Test
    fun medicalRooms() = testContext {
        screens.medicals.medicalRooms().apply(::println)
    }

    @Test
    fun respawn() = testContext {
        screens.medicals.respawn(1)
    }

    @Test
    fun focusedScreen() = testContext {
        println(screens.focusedScreen())
    }

    @Test
    fun chooseFaction() = testContext {
        screens.medicals.chooseFaction(0)
    }

    @Test
    fun factions() = testContext {
        screens.medicals.factions().apply(::println)
    }

    @Test
    fun waitUntilTheGameLoaded() = testContext {
        screens.waitUntilTheGameLoaded()
    }

    @Test
    fun terminalData() = testContext {
        println(screens.terminal.data().inventory.leftInventories)
        println(screens.terminal.data().inventory.rightInventories)
    }

    @Test
    fun transferItem() = testContext {
        val firstLeftInventory = screens.terminal.data().inventory.leftInventories.first()
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

}
