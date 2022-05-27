package bdd

import bdd.ScenarioSetupSteps.Companion.process
import bdd.repetitiveassert.RepetitiveAssertConfig
import bdd.repetitiveassert.RepetitiveAssertTestCase
import bdd.repetitiveassert.SimpleRepetitiveAssertTestCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import spaceEngineers.controller.*
import spaceEngineers.controller.connection.AppType
import spaceEngineers.controller.connection.ConnectionManager
import testhelp.hideUndeclaredThrowableException
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import kotlin.concurrent.thread
import kotlin.test.assertTrue

abstract class AbstractMultiplayerSteps(
    config: RepetitiveAssertConfig = RepetitiveAssertConfig(),
    simpleRepetitiveAssertTestCase: SimpleRepetitiveAssertTestCase = SimpleRepetitiveAssertTestCase(config)
) : AutoCloseable, RepetitiveAssertTestCase by simpleRepetitiveAssertTestCase {

    val cm: ConnectionManager
        get() = CM

    override fun close() {
        cm.close()
    }

    suspend fun smallPause() {
        delay(500)
    }

    suspend fun pause() {
        delay(5_000)
    }

    suspend fun bigPause() {
        delay(15_000)
    }

    fun observers(block: suspend ContextControllerWrapper.() -> Unit) = hideUndeclaredThrowableException {
        cm.observers(block)
    }

    fun <T> mainClient(block: suspend ContextControllerWrapper.() -> T): T = hideUndeclaredThrowableException {
        cm.mainClient(block)
    }

    fun clients(block: suspend ContextControllerWrapper.() -> Unit) = hideUndeclaredThrowableException {
        cm.clients(block)
    }

    fun games(block: suspend ContextControllerWrapper.() -> Unit) = hideUndeclaredThrowableException {
        cm.games(block)
    }

    fun admin(block: suspend ContextControllerWrapper.() -> Unit) = hideUndeclaredThrowableException {
        cm.admin(block)
    }

    fun exitToMainMenu(onException: (Throwable) -> Unit = {println(it)}) {
        clients {
            try {
                session.exitToMainMenu()
            } catch (e: Throwable) {
                onException(e)
            }
        }
        if (cm.admin.gameProcess.type == AppType.GAME) {
            admin {
                try {
                    session.exitToMainMenu()
                } catch (e: Throwable) {
                    onException(e)
                }
            }
        }
    }

    fun loadScenario(scenarioId: String) = hideUndeclaredThrowableException {
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
        observers {
            observer.observeNewBlocks()
        }
    }


    private fun createLobbyGame(scenarioId: String) = admin {
        screens.mainMenu.loadGame()
        pause()
        val data = screens.loadGame.data()
        val index = data.files.indexOfFirst { it.fullName.contains(scenarioId) }
        check(index > -1) {
            "Scenario $scenarioId not found in the list, found: ${data.files.map { it.name }}"
        }
        screens.loadGame.doubleClickWorld(index)
        bigPause()
        screens.waitUntilTheGameLoaded()
    }

    private fun connectToFirstFriendlyGame() {
        clients {
            screens.mainMenu.joinGame()
            pause()
            screens.joinGame.selectTab(5)
            pause()
            screens.joinGame.selectGame(0)

            pause()
            screens.joinGame.joinWorld()
            smallPause()
            screens.waitUntilTheGameLoaded()
        }
        runBlocking {
            pause()
        }
        ensureCharacterExists()
    }

    private fun connectClientsDirectly() {
        clients {
            //TODO: if not in main menu, exit to it rather than failing
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
        }
        ensureCharacterExists()
    }

    fun ensureCharacterExists() = clients {
        if (screens.focusedScreen() == "Medicals") {
            try {
                screens.medicals.chooseFaction(0)
            } catch (e: Exception) {

            }
            pause()
            screens.medicals.respawn(0)
            pause()
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
        val args = "-session:${scenarioPath} -plugin Ivxr.SePlugin.dll -console -start"
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

    companion object {
        lateinit var CM: ConnectionManager
    }
}
