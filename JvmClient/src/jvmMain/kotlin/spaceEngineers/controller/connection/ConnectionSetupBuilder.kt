package spaceEngineers.controller.connection

class ConnectionSetupBuilder(
    val localSourcesDirectory: String? = null,
    val gameVersion: GameVersion = GameVersion.DEV,
) {

    init {
        checkLocalSources(localSourcesDirectory, gameVersion)
    }

    var server: GameProcess? = null
    var mainClient: GameProcess? = null
    val clients = mutableListOf<GameProcess>()

    fun setServer(type: AppType, address: String): ConnectionSetupBuilder {
        check(server == null) { "Server already set!" }
        GameProcess(
            address = address,
            pluginPort = if (type == AppType.DEDICATED) {
                DEDICATED_SERVER_PORT
            } else {
                CLIENT_PORT
            },
            configPath = configPath(type),
            executablePath = executablePath(
                localSourcesDirectory = localSourcesDirectory,
                type = type,
                gameVersion = gameVersion
            ),
            roles = serverRoles(type),
            type = type,
            gameVersion = gameVersion,
        ).also {
            server = it
        }
        return this
    }

    fun addMainClient(address: String): ConnectionSetupBuilder {
        check(mainClient == null) { "mainClient already set!" }
        mainClient = createClient(address = address, main = true)
        return this
    }

    fun addClient(address: String): ConnectionSetupBuilder {
        clients.add(
            createClient(address = address, main = false)
        )
        return this
    }

    fun build(): ConnectionSetup {
        if (server?.type == AppType.DEDICATED && mainClient == null) {
            error("server type is ${server?.type}, but no main client")
        }

        return ConnectionSetup(
            connections = listOf(server ?: error("no server set"), mainClient).filterNotNull() + clients
        )
    }

    private fun createClient(address: String, main: Boolean = false, port: UShort = CLIENT_PORT): GameProcess {
        return GameProcess(
            address = address,
            pluginPort = port,
            configPath = configPath(),
            executablePath = executablePath(
                localSourcesDirectory = localSourcesDirectory,
                gameVersion = gameVersion
            ),
            roles = clientRoles(main),
            type = AppType.GAME,
            gameVersion = gameVersion,
        )

    }


    companion object {
        val DEFAULT_STEAM_EXECUTABLE_PATH = "C:/Program Files (x86)/Steam/steamapps/common/SpaceEngineers/Bin64"
        val DEFAULT_DEDICATED_STEAM_EXECUTABLE_PATH =
            "C:/Program Files (x86)/Steam/steamapps/common/SpaceEngineersDedicatedServer/DedicatedServer64"
        val DEFAULT_CONFIG_PATH = "~/AppData/Roaming/SpaceEngineers"
        val DEFAULT_DEDICATED_CONFIG_PATH = "~/AppData/Roaming/SpaceEngineersDedicated"
        val LOCAL_SE_PATH = "se/Sources/SpaceEngineers/bin/x64/Debug/Bin64"
        val LOCAL_SE_DEDICATED_PATH = "se/Sources/SpaceEngineers.Dedicated/bin/x64/Debug/Bin64"
        val LOCAL_SOURCES_DIR_KAREL = "~/git/"
        val LOCAL_SOURCES_DIR_PREMEK = "~/Documents/src/ivxr/se-plugin/"
        val CLIENT_PORT: UShort = 3333u
        val DEDICATED_SERVER_PORT: UShort = 3345u
        val LOCALHOST = "127.0.0.1"
        val REMOTE_DESKTOP = "192.168.121.253"

        fun executablePath(
            localSourcesDirectory: String?,
            type: AppType = AppType.GAME,
            gameVersion: GameVersion
        ): String {
            checkLocalSources(localSourcesDirectory, gameVersion)
            return if (type == AppType.DEDICATED) {
                if (gameVersion == GameVersion.STEAM) {
                    DEFAULT_DEDICATED_STEAM_EXECUTABLE_PATH
                } else {
                    localSourcesDirectory + LOCAL_SE_DEDICATED_PATH
                }
            } else {
                if (gameVersion == GameVersion.STEAM) {
                    DEFAULT_STEAM_EXECUTABLE_PATH
                } else {
                    localSourcesDirectory + LOCAL_SE_PATH
                }
            }
        }

        private fun checkLocalSources(
            localSourcesDirectory: String?,
            gameVersion: GameVersion
        ) {
            if (localSourcesDirectory == null && gameVersion == GameVersion.DEV) {
                error("dev version, but no local sources directory")
            }
        }

        fun clientRoles(main: Boolean): List<Role> {
            return if (main) {
                listOf(Role.MAIN_CLIENT, Role.OBSERVER)
            } else {
                listOf(Role.OBSERVER)
            }
        }

        fun serverRoles(appType: AppType): List<Role> {
            return if (appType == AppType.DEDICATED) {
                listOf(Role.ADMIN, Role.OBSERVER)
            } else {
                listOf(Role.ADMIN, Role.OBSERVER, Role.MAIN_CLIENT)
            }
        }

        fun configPath(appType: AppType = AppType.GAME): String {
            return if (appType == AppType.DEDICATED) {
                DEFAULT_DEDICATED_CONFIG_PATH
            } else {
                DEFAULT_CONFIG_PATH
            }
        }
    }
}
