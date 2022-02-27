package spaceEngineers.game.mockable

import testhelp.MockOrRealGameTest
import kotlin.test.Test


//@Ignore
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
        val li = screens.terminal.data().inventory.leftInventories.first()
        val item = li.items.first()
        screens.terminal.inventory.transferInventoryItem(0, 0, item.itemId)
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

}
