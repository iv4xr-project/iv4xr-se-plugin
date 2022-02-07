package bdd

import spaceEngineers.controller.MultipleConnectionManager
import spaceEngineers.controller.SpaceEngineers

abstract class AbstractMultiplayerSteps(
    val mcm: MultipleConnectionManager = MultipleConnectionManager()
) {

    fun client(name: String, block: SpaceEngineers.() -> Unit) {
        mcm.connection(name, block)
    }

    fun server(block: SpaceEngineers.() -> Unit) {
        mcm.server(block)
    }

    fun getCharacterIdByName(name: String): String {
        return mcm.getCharacterIdByName(name)
    }

}
