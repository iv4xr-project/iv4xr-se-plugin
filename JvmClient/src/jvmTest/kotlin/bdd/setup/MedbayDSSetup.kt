package bdd.setup

import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
import spaceEngineers.controller.connection.ProcessWithConnection


class MedbayDSSetup(
    connectionSetup: ConnectionSetup,
    cm: ConnectionManager,
    realConnectionManagerUser: RealConnectionManagerUser = RealConnectionManagerUser(cm)
) : MedbaySetup(connectionSetup = connectionSetup, cm = cm, realConnectionManagerUser = realConnectionManagerUser) {
    val dedicatedServerManager = DedicatedServerManager(processConfig = connectionSetup.admin)


    override fun afterAll() {
        super.afterAll()
        dedicatedServerManager.close()
    }


    override suspend fun ProcessWithConnection.setupServer(scenarioId: String) {
        connectDirectly(connectionManager.admin.gameProcess.address)
    }

    override suspend fun ProcessWithConnection.setupClient() {
        connectDirectly(connectionManager.admin.gameProcess.address)
    }

    override suspend fun onBeforeScenario(scenarioId: String) {
        if (!dedicatedServerManager.isRunning()) {
            dedicatedServerManager.start(scenarioId)
        }
    }

}
