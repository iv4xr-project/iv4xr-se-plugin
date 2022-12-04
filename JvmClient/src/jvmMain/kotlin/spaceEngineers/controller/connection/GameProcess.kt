package spaceEngineers.controller.connection

import kotlinx.serialization.Serializable

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

    val mainRole: Role
        get() {
            if (Role.MAIN_CLIENT in roles) {
                return Role.MAIN_CLIENT
            }
            if (Role.ADMIN in roles) {
                return Role.ADMIN
            }
            return Role.OBSERVER
        }

    fun isMainClient(): Boolean {
        return Role.MAIN_CLIENT in roles
    }

    fun simpleString(): String {
        return "$address:$pluginPort($mainRole)"
    }
}
