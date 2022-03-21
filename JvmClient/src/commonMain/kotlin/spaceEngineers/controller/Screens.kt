package spaceEngineers.controller

import spaceEngineers.model.*

interface Screens {
    fun focusedScreen(): String
    fun waitUntilTheGameLoaded()
    val medicals: Medicals
    val terminal: Terminal
    val mainMenu: MainMenu
    val messageBox: MessageBox
    val joinGame: JoinGame
    val serverConnect: ServerConnect
    val loadGame: LoadGame
    val gamePlay: GamePlay
}

interface GamePlay {
    fun data(): GamePlayData
    fun showMainMenu()
}

interface ServerConnect {
    fun data(): ServerConnectData
    fun connect()
    fun enterAddress(address: String)
    fun toggleAddServerToFavorites()
}

interface JoinGame {
    fun directConnect()
}

interface MessageBox {
    fun data(): MessageBoxData
    fun pressYes()
    fun pressNo()
}

interface MainMenu {
    fun `continue`()
    fun newGame()
    fun loadGame()
    fun joinGame()
    fun options()
    fun character()
    fun exitToWindows()
    fun exitToMainMenu()
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
    fun close()

    val production: ProductionTab
    val inventory: InventoryTab
}

interface InventoryTab {
    fun data(): TerminalInventoryData
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
    fun data(): TerminalProductionData
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

interface LoadGame {
    fun data(): LoadGameData
    fun filter(text: String)
    fun doubleClickWorld(index: Int)
    fun load()
    fun edit()
    fun delete()
    fun save()
    fun publish()
}
