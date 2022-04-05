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

    var process: Process? = null

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
            clients {
                try {
                    session.exitToMainMenu()
                } catch (e: Throwable) {
                    println(e.message)
                }
            }
            if (cm.admin.gameProcess.type == AppType.GAME) {
                admin {
                    try {
                        session.exitToMainMenu()
                    } catch (e: Throwable) {
                        println(e.message)
                    }

                }
            }
            runBlocking {
                smallPause()
            }
        }
        CM.close()
        process?.destroyForcibly()

    }

    @Given("Scenario used is {string}.")
    fun scenario_used_is(scenarioId: String) = runBlocking {
        if (cm.connectionSetup.offlineSinglePlayer) {
            loadScenarioSinglePlayer(scenarioId)
            //createLobbyGame(scenarioId)
        } else if (cm.connectionSetup.admin.type == AppType.GAME) {
            createLobbyGame(scenarioId)
            connectToFirstFriendlyGame()
        } else if (cm.connectionSetup.admin.type == AppType.DEDICATED) {
            startDedicatedWithSessionAsync(scenarioId)
            connectClientsDirectly()
        } else {
            error("Unknown setup")
        }
    }

    private fun createLobbyGame(scenarioId: String) = mainClient {
        screens.mainMenu.loadGame()
        pause()
        val data = screens.loadGame.data()
        val index = data.files.indexOfFirst { it.fullName.contains(scenarioId) }
        screens.loadGame.doubleClickWorld(index)
        bigPause()
        screens.waitUntilTheGameLoaded()
    }

    private fun connectToFirstFriendlyGame() {
        clients {
            screens.mainMenu.joinGame()
            smallPause()
            screens.joinGame.selectTab(5)
            smallPause()
            screens.joinGame.selectGame(0)

            smallPause()
            screens.joinGame.joinWorld()
            smallPause()
            screens.waitUntilTheGameLoaded()
        }
        runBlocking {
            pause()
        }
    }

    private fun connectClientsDirectly() {
        clients {
            val process = cm.admin.gameProcess
            screens.mainMenu.joinGame()
            smallPause()
            screens.joinGame.directConnect()
            smallPause()
            screens.serverConnect.enterAddress("${process.address}:27016")
            smallPause()
            screens.serverConnect.connect()
            screens.waitUntilTheGameLoaded()
        }
        runBlocking {
            bigPause()
            bigPause()
        }
    }

    fun loadScenarioSinglePlayer(scenarioId: String) = mainClient {
        session.loadFromTestResources(scenarioId)
        screens.waitUntilTheGameLoaded()
        smallPause()
    }


    suspend fun startDedicatedWithSessionAsync(scenarioId: String) {
        val scenarioDir = "src/jvmTest/resources/game-saves/".processHomeDir()
        val scenarioPath = File(scenarioDir, scenarioId).absolutePath.unixToWindowsPath()
        val wdFile = File(cm.connectionSetup.admin.executablePath.processHomeDir())
        assertTrue(wdFile.exists())
        val wd = wdFile.absolutePath
        val executable = File("${wd}/SpaceEngineersDedicated.exe")
        assertTrue(executable.exists())
        val cmd = executable.absolutePath
        val args = "-session:${scenarioPath} -console -start"
        val fullArgs = (listOf(cmd) + args.split(" ")).toTypedArray()
        var gameStarted = false
        thread(start = true) {
            process = ProcessBuilder(* fullArgs)
                .directory(wd.toFile())
                .redirectErrorStream(true)
                .start()
            process?.apply {
                val reader = BufferedReader(InputStreamReader(this.inputStream, "UTF-8"))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    println(line)
                    if (line?.contains("Game ready...") == true) {
                        gameStarted = true
                    }
                }
                println("EOF")
            }
        }
        smallPause()
        withTimeout(1120_000) {
            while (!gameStarted) {
                if (process == null || process?.isAlive == false) {
                    throw IllegalStateException("Server process already finished")
                }
                yield()
            }
        }
        smallPause()
    }

}
