package bdd.setup

import io.cucumber.java.Scenario
import kotlinx.coroutines.runBlocking
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
import spaceEngineers.controller.connection.ExitMode
import spaceEngineers.controller.extensions.typedFocusedScreen
import spaceEngineers.model.ScreenName
import spaceEngineers.model.ScreenName.Companion.CubeBuilder
import spaceEngineers.model.ScreenName.Companion.GamePlay
import spaceEngineers.model.ScreenName.Companion.Loading
import spaceEngineers.model.ScreenName.Companion.MainMenu
import spaceEngineers.model.ScreenName.Companion.Medicals
import spaceEngineers.model.ScreenName.Companion.Progress
import spaceEngineers.model.ScreenName.Companion.Terminal
import spaceEngineers.util.whileWithTimeout
import testhelp.hideUndeclaredThrowableException
import java.lang.Thread.sleep


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

    //val reporter = Reporter(connectionManager = cm)

    override fun beforeAll() {

    }

    override fun beforeScenario(scenario: Scenario) {

    }

    override fun afterScenario(scenario: Scenario) = runBlocking {

    }

    override fun afterAll() {
        if (connectionManager.config.exitMode == ExitMode.AFTER_LAST_SCENARIO) {
            sleep(500)
            exitToMainMenu()
        }
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
            screens.focusedScreen()
            when (val focusedScreen = screens.typedFocusedScreen()) {
                MainMenu -> {
                    createLobbyGame(scenarioId)
                    connectToFirstFriendlyGame()
                }
                Loading -> {
                    whileWithTimeout(20_000L) { screens.typedFocusedScreen() != Loading }
                    pause()
                }
                Progress -> {
                    whileWithTimeout(20_000L) { screens.typedFocusedScreen() != Progress }
                    pause()
                }
                GamePlay, Terminal, Medicals -> {

                }
                ScreenName.MessageBox -> {
                    val message = screens.messageBox.data()
                    when (message.caption) {
                        else -> error("Don't know what to do with MessageBox with caption ${message.caption}, text ${message.text} and type ${message.buttonType}")
                    }
                }
                CubeBuilder -> {
                    screens.toolbarConfig.close()
                }
                else -> {
                    error("Don't know what to do with screen $focusedScreen")
                }
            }
        }
    }
}
