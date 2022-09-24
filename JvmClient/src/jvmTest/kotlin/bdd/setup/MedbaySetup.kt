package bdd.setup

import io.cucumber.java.Scenario
import kotlinx.coroutines.runBlocking
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
import spaceEngineers.controller.connection.ExitMode
import spaceEngineers.controller.connection.ProcessWithConnection
import testhelp.hideUndeclaredThrowableException

abstract class MedbaySetup(
    val connectionSetup: ConnectionSetup,
    val cm: ConnectionManager,
    val realConnectionManagerUser: RealConnectionManagerUser = RealConnectionManagerUser(cm)
) : AbstractTestSetup(), ConnectionManagerUser by realConnectionManagerUser {

    override fun beforeAll() {

    }

    override fun beforeScenario(scenario: Scenario) {

    }

    override fun afterScenario(scenario: Scenario) = runBlocking {

    }

    override fun afterAll() {
        if (connectionManager.config.exitMode == ExitMode.AFTER_LAST_SCENARIO) {
            Thread.sleep(500)
            exitToMainMenu()
        }
        connectionManager.close()
    }

    override val connectionManager: ConnectionManager
        get() = cm

    protected val respawner by lazy {
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

    abstract suspend fun ProcessWithConnection.setupServer(scenarioId: String)

    abstract suspend fun ProcessWithConnection.setupClient()

    open suspend fun onBeforeScenario(scenarioId: String) {

    }

    private fun onScenario(scenarioId: String) = hideUndeclaredThrowableException {
        onBeforeScenario(scenarioId)
        mainClient {
            if (session.info()?.ready != true) {
                setupServer(scenarioId)
            }
        }
        nonMainClientGameObservers {
            if (session.info()?.ready != true) {
                setupClient()
            }
        }
        games {
            smallPause()
            screens.waitUntilTheGameLoaded()
        }
    }
}
