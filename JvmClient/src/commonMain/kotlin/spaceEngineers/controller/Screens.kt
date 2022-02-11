package spaceEngineers.controller

import spaceEngineers.model.Faction
import spaceEngineers.model.MedicalRoom
import spaceEngineers.model.TerminalScreenData

interface Screens {
    fun focusedScreen(): String
    fun waitUntilTheGameLoaded()
    val medicals: Medicals
    val terminal: Terminal
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
    fun addToProductionQueue(index: Int)
    fun removeFromProductionQueue(index: Int)
    fun toggleProductionRepeatMode()
    fun toggleProductionCooperativeMode()
}
