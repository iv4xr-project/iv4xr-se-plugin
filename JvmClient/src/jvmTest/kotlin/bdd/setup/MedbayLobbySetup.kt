package bdd.setup

import kotlinx.coroutines.delay
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
import testhelp.hideUndeclaredThrowableException
import kotlin.test.assertNotNull


enum class CameraConfig(val text: String) {
    FIRST_PERSON("1st person"), THIRD_PERSON("3rd person");

    companion object {
        fun fromText(text: String): CameraConfig {
            return values().firstOrNull { it.text == text } ?: error("Don't know camera $text")
        }
    }
}

class MedbayLobbySetup(
    val connectionSetup: ConnectionSetup,
    val cm: ConnectionManager,
    val realConnectionManagerUser: RealConnectionManagerUser = RealConnectionManagerUser(cm)
) : AbstractTestSetup(), ConnectionManagerUser by realConnectionManagerUser {


    override fun beforeAll() {

    }

    override fun beforeScenario() {

    }

    override fun afterScenario() {
    }

    override fun afterAll() {
        //exitToMainMenu()
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
        mainClient {
            if (screens.focusedScreen() == "MainMenu") {
                createLobbyGame(scenarioId)
                connectToFirstFriendlyGame()
            }
        }
        ensureEveryoneIsSameSession()
        prepareCharacter()
    }


}
