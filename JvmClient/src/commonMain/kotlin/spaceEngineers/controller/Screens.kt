package spaceEngineers.controller

import spaceEngineers.model.Faction
import spaceEngineers.model.MedicalRoom
import spaceEngineers.model.MessageBoxData
import spaceEngineers.model.TerminalScreenData

interface Screens {
    fun focusedScreen(): String
    fun waitUntilTheGameLoaded()
    val medicals: Medicals
    val terminal: Terminal
    val mainMenu: MainMenu
    val messageBox: MessageBox
    val joinGame: JoinGame
    val serverConnect: ServerConnect
}

interface ServerConnect {
    fun connect()
    fun enterAddress(address: String)
}

interface JoinGame {
    fun directConnect()
}

interface MessageBox {
    fun pressYes()
    fun pressNo()
    fun data(): MessageBoxData
}

interface MainMenu {
    fun `continue`()
    fun newGame()
    fun loadGame()
    fun joinGame()
    fun options()
    fun character()
    fun exitToWindows()
}

interface Medicals {
    fun medicalRooms(): List<MedicalRoom>
    fun respawn(roomIndex: Int)
    fun factions(): List<Faction>
    fun chooseFaction(factionIndex: Int)

}

interface Terminal {
    fun data(): TerminalScreenData
    fun selectTab(index: Int)

    val production: ProductionTab
    val inventory: InventoryTab
}

interface InventoryTab {
    fun transferInventoryItem(sourceInventoryId: Int, destinationInventoryId: Int, itemId: Int)
    fun withdraw()
    fun deposit()
    fun dropSelected()
    fun fromBuildPlannerToProductionQueue()
    fun selectedToProductionQueue()

    val left: InventorySide
    val right: InventorySide
}

interface ProductionTab {
    fun addToProductionQueue(index: Int)
    fun removeFromProductionQueue(index: Int)
    fun selectBlueprint(index: Int)
    fun enterBlueprintSearchBox(text: String)
    fun toggleProductionRepeatMode()
    fun toggleProductionCooperativeMode()
    fun selectAssembler(index: Int)
}

interface InventorySide {
    fun filter(text: String)
    fun swapToGrid()
    fun swapToCharacterOrItem()
    fun filterAll()
    fun filterEnergy()
    fun filterShip()
    fun filterSystem()
    fun filterStorage()
    fun toggleHideEmpty()
    fun selectItem(index: Int)
    fun clickSelectedItem()
    fun doubleClickSelectedItem()
}
