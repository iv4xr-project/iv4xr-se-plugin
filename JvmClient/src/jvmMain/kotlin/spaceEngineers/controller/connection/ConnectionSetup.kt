package spaceEngineers.controller.connection

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import spaceEngineers.controller.connection.ConnectionSetupBuilder.Companion.LOCALHOST
import spaceEngineers.controller.connection.ConnectionSetupBuilder.Companion.LOCAL_SOURCES_DIR_KAREL
import spaceEngineers.controller.connection.ConnectionSetupBuilder.Companion.REMOTE_DESKTOP
import java.io.File


fun main(args: Array<String>) {
    val CONNECTION_SETUP_DIR = File("src/jvmTest/resources/connection-setup/")
    val json = Json {
        prettyPrint = true
    }
    CONNECTION_SETUP_DIR.mkdirs()
    ConnectionSetup.byName.forEach {
        File(CONNECTION_SETUP_DIR, it.key).writeText(json.encodeToString(it.value))
    }
}

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
        val OFFLINE_STEAM = ConnectionSetupBuilder(gameVersion = GameVersion.STEAM).setServer(
            AppType.GAME, address = LOCALHOST,
        ).build()

        val OFFLINE_DEV_KAREL = ConnectionSetupBuilder(
            gameVersion = GameVersion.DEV,
            localSourcesDirectory = LOCAL_SOURCES_DIR_KAREL
        ).setServer(
            AppType.GAME, address = LOCALHOST,
        ).build()

        val SINGLE_COMPUTER_DEDICATED_DEV_KAREL = ConnectionSetupBuilder(
            gameVersion = GameVersion.DEV,
            localSourcesDirectory = LOCAL_SOURCES_DIR_KAREL
        ).setServer(
            AppType.DEDICATED, address = LOCALHOST,
        ).addMainClient(LOCALHOST).build()

        val SINGLE_COMPUTER_DEDICATED_STEAM = ConnectionSetupBuilder(gameVersion = GameVersion.STEAM).setServer(
            AppType.DEDICATED, address = LOCALHOST,
        ).addMainClient(LOCALHOST).build()

        val DOUBLE_PC_DEDICATED_DEV_KAREL =
            ConnectionSetupBuilder(gameVersion = GameVersion.DEV, localSourcesDirectory = LOCAL_SOURCES_DIR_KAREL)
                .setServer(AppType.DEDICATED, address = LOCALHOST)
                .addMainClient(LOCALHOST).addClient("10.0.0.33").build()

        val DOUBLE_PC_LOBBY_STEAM = ConnectionSetupBuilder(gameVersion = GameVersion.STEAM)
            .setServer(AppType.GAME, address = LOCALHOST)
            .addClient("10.0.0.33").build()

        val DOUBLE_PC_LOBBY_STEAM_REMOTE_DESKTOP = ConnectionSetupBuilder(gameVersion = GameVersion.STEAM)
            .setServer(AppType.GAME, address = REMOTE_DESKTOP)
            .addClient(LOCALHOST).build()

        val DOUBLE_PC_LOBBY_DEV_KAREL =
            ConnectionSetupBuilder(gameVersion = GameVersion.DEV, localSourcesDirectory = LOCAL_SOURCES_DIR_KAREL)
                .setServer(AppType.GAME, address = LOCALHOST)
                .addClient("10.11.12.10").build()

        val byName = mapOf(
            "OFFLINE_STEAM.json" to OFFLINE_STEAM,
            "OFFLINE_DEV_KAREL.json" to OFFLINE_DEV_KAREL,
            "SINGLE_COMPUTER_DEDICATED_STEAM" to SINGLE_COMPUTER_DEDICATED_STEAM,
            "SINGLE_COMPUTER_DEDICATED_DEV_KAREL" to SINGLE_COMPUTER_DEDICATED_DEV_KAREL,
            "DOUBLE_PC_LOBBY_STEAM" to DOUBLE_PC_LOBBY_STEAM,
        )


        private val json: Json = Json {
        }

        val CONNECTION_SETUP_DIR = "src/jvmTest/resources/connection-setup/"

        fun loadConfigFromFile(file: File): ConnectionSetup {
            return json.decodeFromString(ConnectionSetup.serializer(), file.readText())
        }

        fun loadConfigFromFile(name: String = "config.json"): ConnectionSetup {
            return loadConfigFromFile(File(CONNECTION_SETUP_DIR, name))
        }
    }
}
