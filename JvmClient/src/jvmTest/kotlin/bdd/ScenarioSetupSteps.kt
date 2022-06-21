package bdd

import bdd.repetitiveassert.repeatUntilSuccess
import io.cucumber.datatable.DataTable
import io.cucumber.java.After
import io.cucumber.java.AfterAll
import io.cucumber.java.Before
import io.cucumber.java.BeforeAll
import io.cucumber.java.en.Given
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.SocketReaderWriterException
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.connection.ConnectionManager
import spaceEngineers.controller.connection.ConnectionSetup
import spaceEngineers.controller.extensions.waitForScreen
import spaceEngineers.model.Vec3F
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcError
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

    @BeforeAll
    fun beforeAll() {

    }

    @AfterAll
    fun afterAll() {
        exitToMenuAndCloseEverything()
    }

    @Before
    fun setup() {
        if (CMInitialized() && CM.initiated) {
            return
        }
        //SINGLE_COMPUTER_DEDICATED_DEV_KAREL    OFFLINE_STEAM
        CM = ConnectionManager(loadConfigFromFile("OFFLINE_STEAM.json"))
    }

    @After
    fun cleanup() {
    }

    fun exitToMenuAndCloseEverything() = runBlocking {
        if (cm.initiated) {
            exitToMainMenu()
            delay(2_500)
        }
        process?.destroyForcibly()
        killDedicatedServerWindows()
        CM.close()
    }


    fun killDedicatedServerWindows() {
        var process =
            ProcessBuilder("""C:\windows\system32/taskkill""", "/IM", "SpaceEngineersDedicated.exe", "/F")
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


    fun loadScenarioOrDie(scenarioId: String) = hideUndeclaredThrowableException {
        if (!cm.initiated && cm.connectionSetup.ds) {
            loadScenario(scenarioId)
            return@hideUndeclaredThrowableException
        }
        val focusedScreen = mainClient { screens.focusedScreen() }
        if (focusedScreen == "GamePlay") {
            //TODO: ensure we are in the correct scenario
            games {
                delay(100)
                try {
                    val id = admin.character.mainCharacterId()
                    admin {
                        admin.character.switch(id)
                        admin.character.teleport(Vec3F.ZERO)
                    }

                    delay(100)
                    admin.character.die()
                    delay(500)
                    screens.messageBox.pressYes()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(100)
                waitForMedicalScreen()
            }

        } else if (focusedScreen == "Medicals") {
        } else {
            loadScenario(scenarioId)
        }
    }

    private suspend fun ContextControllerWrapper.waitForMedicalScreen() {
        screens.waitForScreen(timeoutMs = 10_000, screenName = "Medicals")
    }

    suspend fun ContextControllerWrapper.respawn(medbay: String) = with(screens.medicals) {
        repeatUntilSuccess(
            initialDelayMs = 0,
            repeats = 5,
            delayMs = 1_500,
            swallowedExceptionTypes = setOf(SocketReaderWriterException::class, KotlinJsonRpcError::class)
        ) {
            val index = medicalRooms().indexOfFirst { it.name == medbay }
            //TODO: throw for -1 on server
            check(index != -1) { "Spawn point not found" }
            selectRespawn(index)
            delay(500L * index)
            respawn()
        }
    }

    private suspend fun respawn(mainMedbay: String, observerMedbay: String) {
        nonMainClientGameObservers {
            respawn(observerMedbay)
        }
        mainClient {
            respawn(mainMedbay)
            waitForGameplay()
        }
        delay(100)
    }

    suspend fun SpaceEngineers.waitForGameplay() {
        screens.waitForScreen(timeoutMs = 60_000, screenName = "GamePlay")
    }

    @Given("Scenario config:")
    fun scenario_config(dataTable: DataTable) = hideUndeclaredThrowableException {
        val data = dataTable.asMaps().first()
        loadScenarioOrDie(data.getValue("scenario"))
        val mainMedbay = data.getValue("main_client_medbay")
        val observerMedbay = data.getValue("observer_medbay")
        respawn(mainMedbay, observerMedbay)
        data["delay_after_spawn"]?.toFloatOrNull()?.let { delaySeconds ->
            delay((delaySeconds.toDouble() * 1000.0).toLong())
        }

    }


    companion object {
        var process: Process? = null
    }

}
