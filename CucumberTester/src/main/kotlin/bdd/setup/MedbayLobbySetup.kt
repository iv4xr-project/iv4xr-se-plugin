package bdd.setup

import bdd.connection.ConnectionManager
import bdd.connection.ConnectionSetup
import bdd.connection.ProcessWithConnection


class MedbayLobbySetup(
    connectionSetup: ConnectionSetup,
    cm: ConnectionManager,
    realConnectionManagerUser: RealConnectionManagerUser = RealConnectionManagerUser(cm)
) : MedbaySetup(connectionSetup = connectionSetup, cm = cm, realConnectionManagerUser = realConnectionManagerUser) {

    override suspend fun ProcessWithConnection.setupServer(scenarioId: String) {
        createLobbyGame(scenarioId)
    }

    override suspend fun ProcessWithConnection.setupClient() {
        connectToFirstFriendlyGame()
    }

}
