package bdd

import io.cucumber.datatable.DataTable
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
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
        if (CMInitialized() && CM.initiated) {
            return
        }
        CM = ConnectionManager(loadConfigFromFile("OFFLINE_STEAM.json"))
    }

    @After
    fun cleanup() {
        return
        if (cm.initiated) {
            exitToMainMenu()
            runBlocking {
                delay(2_500)
            }
        }
        process?.destroyForcibly()
        CM.close()
        killDedicatedServerWindows()
    }


    fun killDedicatedServerWindows() {
        var process =
            ProcessBuilder(* arrayOf("""C:\windows\system32/taskkill""", "/IM", "SpaceEngineersDedicated.exe", "/F"))
                .redirectErrorStream(true)
                .start()
        process.waitFor()
    }

    @Given("Scenario used is {string}.")
    fun scenario_used_is(scenarioId: String) = hideUndeclaredThrowableException {
        loadScenario(scenarioId)
        ensureCharacterExists()
        observers {
            observer.observeNewBlocks()
        }
    }

    fun waitUntilMedicalScreen() {
        mainClient {
            withTimeout(30_000) {
                while (screens.focusedScreen() != "Medicals") {
                    delay(1500)
                }
            }
        }
    }

    fun loadScenarioOrDie(scenarioId: String) = hideUndeclaredThrowableException {
        val focusedScreen = mainClient { screens.focusedScreen() }
        if (focusedScreen == "GamePlay") {
            //TODO: ensure we are in the correct scenario
            admin {
                admin.character.die()
                delay(100)
                screens.messageBox.pressYes()
            }
            waitUntilMedicalScreen()
        } else if (focusedScreen == "Medicals") {

        } else {
            loadScenario(scenarioId)
        }
    }

    private fun respawn(mainMedbay: String, observerMedbay: String) {
        clients {
            with(screens.medicals) {
                val index = medicalRooms().indexOfFirst { it.name == observerMedbay }
                check(index != -1)
                respawn(index)
            }
        }
        mainClient {
            with(screens.medicals) {
                val index = medicalRooms().indexOfFirst { it.name == mainMedbay }
                check(index != -1)
                //TODO: throw for -1 on server
                respawn(index)
            }
        }
    }

    @Given("Scenario config:")
    fun scenario_config(dataTable: DataTable) = hideUndeclaredThrowableException {
        val data = dataTable.asMaps().first()
        loadScenarioOrDie(data.getValue("scenario"))
        val mainMedbay = data.getValue("main_client_medbay")
        val observerMedbay = data.getValue("observer_medbay")
        respawn(mainMedbay, observerMedbay)
        pause()
    }


    companion object {
        var process: Process? = null
    }

}
