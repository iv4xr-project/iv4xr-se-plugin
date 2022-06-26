package bdd.setup

import kotlinx.coroutines.delay
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
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
        //exitToMainMenu()
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
        data["delay_after_spawn"]?.toFloatOrNull()?.let { delaySeconds ->
            delay((delaySeconds.toDouble() * 1000.0).toLong())
        }
    }

    private fun onScenario(scenarioId: String) = hideUndeclaredThrowableException {
        if (!dedicatedServerManager.isRunning()) {
            dedicatedServerManager.start(scenarioId)
        }
        mainClient {
            val focusedScreen = screens.focusedScreen()
            if (focusedScreen == "MainMenu") {
                connectClientsDirectly()
                ensureEveryoneIsDS()
            } else if (focusedScreen == "Loading") {
                error("Loading!")
            } else {

            }
        }
        prepareCharacter()
    }

    private fun ensureEveryoneIsDS() = observers {
        if (!admin.debugInfo().apply(::println).isDedicated) {
            "Observer not dedicated when supposed to be DS!"
        }
    }


}
