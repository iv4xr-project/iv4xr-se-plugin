package spaceEngineers.controller.connection

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

    val offlineSinglePlayer: Boolean by lazy {
        connections.size == 1 && admin.type == AppType.GAME
    }


    companion object {
        val OFFLINE_STEAM = ConnectionSetup(GameProcess.OFFLINE_STEAM)
        val OFFLINE_DEV = ConnectionSetup(GameProcess.OFFLINE_STEAM)
        val SINGLE_COMPUTER_DEDICATED_DEV =
            ConnectionSetup(GameProcess.LOCAL_CLIENT_DEV, GameProcess.LOCAL_DEDICATED_SERVER_DEV)
        val SINGLE_COMPUTER_DEDICATED_STEAM =
            ConnectionSetup(GameProcess.LOCAL_CLIENT_STEAM, GameProcess.LOCAL_DEDICATED_SERVER_STEAM)

        val DOUBLE_PC_DEDICATED_DEV = ConnectionSetup(
            GameProcess.REMOTE_CLIENT_DEV,
            GameProcess.LOCAL_CLIENT_DEV,
            GameProcess.LOCAL_DEDICATED_SERVER_DEV
        )
        val SINGLE_REMOTE_DEDICATED_DEV =
            ConnectionSetup(GameProcess.REMOTE_MAIN_CLIENT_DEV, GameProcess.LOCAL_DEDICATED_SERVER_DEV)

    }
}
