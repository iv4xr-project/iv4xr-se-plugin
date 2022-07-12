package bdd.setup

import kotlinx.coroutines.delay
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
import spaceEngineers.util.whileWithTimeout
import testhelp.hideUndeclaredThrowableException


class MedbayDSSetup(
    val connectionSetup: ConnectionSetup,
    val cm: ConnectionManager,
    val realConnectionManagerUser: RealConnectionManagerUser = RealConnectionManagerUser(cm)
) : AbstractTestSetup(), ConnectionManagerUser by realConnectionManagerUser {
    val dedicatedServerManager = DedicatedServerManager(processConfig = connectionSetup.admin)

    override fun beforeAll() {

    }

    override fun beforeScenario() {

    }

    override fun afterScenario() {
    }

    override fun afterAll() {
        exitToMainMenu()
        dedicatedServerManager.close()
        connectionManager.close()
    }

    override val connectionManager: ConnectionManager
        get() = cm

    private val respawner by lazy {
        Respawner(connectionManager = connectionManager)
    }

    override suspend fun scenarioConfig(dataTable: List<Map<String, String>>) {
        val data = dataTable.first()
        val scenarioId = data.getValue("scenario")
        onScenario(scenarioId)
        val mainMedbay = data.getValue("main_client_medbay")
        val observerMedbay = data.getValue("observer_medbay")
        val faction = data["faction"] ?: "Astronaut Movement"
        respawner.respawn(mainMedbay, observerMedbay, faction = faction)
        handleScenarioParameters(data)
    }

    private fun onScenario(scenarioId: String) = hideUndeclaredThrowableException {
        if (!dedicatedServerManager.isRunning()) {
            dedicatedServerManager.start(scenarioId)
        }
        mainClient {
            val focusedScreen = screens.focusedScreen()
            if (focusedScreen == "MainMenu") {
                connectClientsDirectly()
            } else if (focusedScreen == "Loading") {
                whileWithTimeout(20_000L) { screens.focusedScreen() != "Loading" }
                pause()
            } else {

            }
        }
        ensureEveryoneIsSameSession()
        prepareCharacter()
    }
}
