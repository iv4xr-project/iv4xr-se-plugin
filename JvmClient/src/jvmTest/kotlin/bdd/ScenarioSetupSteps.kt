package bdd

import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import kotlinx.serialization.json.Json
import spaceEngineers.controller.connection.AppType
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
import spaceEngineers.controller.loadFromTestResources
import spaceEngineers.controller.processHomeDir
import spaceEngineers.controller.toFile
import spaceEngineers.controller.unixToWindowsPath
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.concurrent.thread
import kotlin.test.assertTrue

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
    fun scenario_used_is(scenarioId: String) = runBlocking {
        loadScenario(scenarioId)
    }

    companion object {
        var process: Process? = null
    }

}
