package spaceEngineers.controller.connection

data class GameProcess(
    val address: String,
    val pluginPort: UShort,
    val type: AppType,
    val roles: List<Role>,
    val executablePath: String,
    val configPath: String,
) {
    fun createId(): String {
        return "$type($roles)@$address:$pluginPort"
    }

    companion object {
        val SERVER_IP = "10.0.0.33"
        val CLIENT_IP = "10.0.0.34"
        val OFFLINE_STEAM = GameProcess(
            address = "127.0.0.1",
            pluginPort = 3333u,
            type = AppType.GAME,
            roles = Role.values().toList(),
            executablePath = "C:/Program Files (x86)/Steam/steamapps/common/SpaceEngineers/Bin64",
            configPath = "~/AppData/Roaming/SpaceEngineers",
        )
        val OFFLINE_DEV = GameProcess(
            address = "127.0.0.1",
            pluginPort = 3333u,
            type = AppType.GAME,
            roles = Role.values().toList(),
            executablePath = "~/git/se/Sources/SpaceEngineers/bin/x64/Debug/Bin64",
            configPath = "~/AppData/Roaming/SpaceEngineers",
        )
        val LOCAL_CLIENT_DEV = OFFLINE_DEV.copy(
            roles = listOf(Role.MAIN_CLIENT, Role.OBSERVER)
        )
        val REMOTE_CLIENT_DEV = OFFLINE_DEV.copy(
            address = CLIENT_IP,
            roles = listOf(Role.OBSERVER)
        )
        val REMOTE_MAIN_CLIENT_DEV = OFFLINE_DEV.copy(
            address = CLIENT_IP,
            roles = listOf(Role.MAIN_CLIENT, Role.OBSERVER)
        )
        val LOCAL_CLIENT_STEAM = OFFLINE_STEAM.copy(
            roles = listOf(Role.MAIN_CLIENT, Role.OBSERVER)
        )
        val LOCAL_DEDICATED_SERVER_DEV = GameProcess(
            address = SERVER_IP,
            pluginPort = 3345u,
            type = AppType.DEDICATED,
            roles = listOf(Role.ADMIN, Role.OBSERVER),
            executablePath = "~/git/se/Sources/SpaceEngineers.Dedicated/bin/x64/Debug/Bin64",
            configPath = "~/AppData/Roaming/SpaceEngineersDedicated",
        )
        val LOCAL_DEDICATED_SERVER_STEAM = GameProcess(
            address = SERVER_IP,
            pluginPort = 3345u,
            type = AppType.DEDICATED,
            roles = listOf(Role.ADMIN, Role.OBSERVER),
            executablePath = "C:/Program Files (x86)/Steam/steamapps/common/SpaceEngineersDedicatedServer/DedicatedServer64",
            configPath = "~/AppData/Roaming/SpaceEngineersDedicated",
        )
    }
}
