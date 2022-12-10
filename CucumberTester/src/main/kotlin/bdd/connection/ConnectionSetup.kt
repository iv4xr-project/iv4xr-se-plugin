package bdd.connection

import kotlinx.serialization.Serializable
import spaceEngineers.controller.json
import java.io.File

@Serializable
data class ConnectionSetup(
    val connections: List<GameProcess>,
) {

    constructor(vararg connections: GameProcess) : this(connections = connections.toList())

    init {
        check(connections.filter { it.roles.contains(Role.MAIN_CLIENT) }.size == 1)
        check(connections.any { it.roles.contains(Role.OBSERVER) })
        check(connections.filter { it.roles.contains(Role.ADMIN) }.size == 1)
        check(connections.filter { it.type == AppType.DEDICATED }.size <= 1)
    }

    val mainClient = connections.first { it.roles.contains(Role.MAIN_CLIENT) }
    val admin = connections.first { it.roles.contains(Role.ADMIN) }
    val observers = connections.filter { it.roles.contains(Role.OBSERVER) }
    val clients = connections.filter { !it.roles.contains(Role.ADMIN) }
    val games = connections.filter { it.type == AppType.GAME }
    val nonMainClientGameObservers = connections.filter {
        it.type == AppType.GAME && !it.roles.contains(Role.MAIN_CLIENT) && it.roles.contains(Role.OBSERVER)
    }

    val offlineSinglePlayer: Boolean by lazy {
        connections.size == 1 && admin.type == AppType.GAME
    }

    val ds: Boolean by lazy {
        admin.type == AppType.DEDICATED
    }

    val lobby: Boolean by lazy {
        !ds
    }

    companion object {

        val CONNECTION_SETUP_DIR = "src/main/resources/connection-setup/"

        fun loadConfigFromFile(file: File): ConnectionSetup {
            return json.decodeFromString(serializer(), file.readText())
        }

        fun loadConfigFromFile(
            name: String = "config.json",
            directory: String = CONNECTION_SETUP_DIR
        ): ConnectionSetup {
            return loadConfigFromFile(File(directory, name))
        }
    }
}
