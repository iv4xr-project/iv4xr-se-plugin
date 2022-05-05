package bdd

import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
import testhelp.hideUndeclaredThrowableException
import java.io.File

class ScenarioSetupSteps : AbstractMultiplayerSteps() {

    private val json: Json = Json {
    }

    val CONNECTION_SETUP_DIR = "src/jvmTest/resources/connection-setup/"

    fun loadConfigFromFile(file: File): ConnectionSetup {
        return json.decodeFromString(ConnectionSetup.serializer(), file.readText())
    }

    fun loadConfigFromFile(name: String = "config.json"): ConnectionSetup {
        return loadConfigFromFile(File(CONNECTION_SETUP_DIR, name))
    }

    @Before
    fun setup() {
        CM = ConnectionManager(loadConfigFromFile("OFFLINE_STEAM.json"))
    }

    @After
    fun cleanup() {
        if (cm.initiated) {
            exitToMainMenu()
            runBlocking {
                smallPause()
            }
        }
        process?.destroyForcibly()
        CM.close()
    }

    @Given("Scenario used is {string}.")
    fun scenario_used_is(scenarioId: String) = hideUndeclaredThrowableException {
        loadScenario(scenarioId)
    }

    companion object {
        var process: Process? = null
    }

}
