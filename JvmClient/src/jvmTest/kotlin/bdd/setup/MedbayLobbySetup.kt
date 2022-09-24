package bdd.setup

import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
import spaceEngineers.controller.connection.ProcessWithConnection


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
