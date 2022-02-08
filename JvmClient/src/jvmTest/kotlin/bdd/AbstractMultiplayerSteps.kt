package bdd

import spaceEngineers.controller.SpaceEngineers

abstract class AbstractMultiplayerSteps {
    val connections: MutableMap<String, SpaceEngineers> = mutableMapOf()

    val characterIdByName: MutableMap<String, String> = mutableMapOf()

    var serverAddress = "localhost:27016"

    val server: SpaceEngineers
        get() = connections[MultiplayerSteps.SERVER] ?: error("No server connection")


    fun connection(name: String, block: SpaceEngineers.() -> Unit) {
        block(connection(name))
    }

    fun connection(name: String): SpaceEngineers {
        return connections[name] ?: error("No connection of name $name")
    }

    fun getCharacterIdByName(name: String): String {
        return characterIdByName.getOrPut(name) {
            connection(name).observer.observe().id
        }
    }


}
