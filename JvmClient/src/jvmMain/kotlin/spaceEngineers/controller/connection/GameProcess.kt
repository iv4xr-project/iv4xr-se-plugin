package spaceEngineers.controller.connection

import kotlinx.serialization.Serializable
import spaceEngineers.controller.connection.ConnectionSetupBuilder.Companion.DEDICATED_SERVER_PORT

@Serializable
data class GameProcess(
    val address: String,
    val pluginPort: UShort,
    val type: AppType,
    val roles: List<Role>,
    val executablePath: String,
    val configPath: String,
    val gameVersion: GameVersion,
) {
    fun createId(): String {
        return "$type($roles)@$address:$pluginPort"
    }

    fun isMainClient(): Boolean {
        return Role.MAIN_CLIENT in roles
    }

}
