package bdd.setup.process

import bdd.connection.ConnectionManager.Companion.createConnection
import bdd.connection.ConnectionSetup
import bdd.connection.GameProcess
import bdd.connection.ProcessWithConnection
import bdd.setup.process.SECommands.EXECUTABLE_FULL_DEFAULT_PATH
import bdd.setup.process.SECommands.NO_TASKS_RUNNING
import bdd.setup.process.SECommands.isRunningCmd
import bdd.setup.process.SECommands.killCmd
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import spaceEngineers.controller.JvmSpaceEngineersBuilder
import spaceEngineers.controller.extensions.waitForScreen
import spaceEngineers.model.ScreenName
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds



suspend fun <In, Out> Collection<In>.parallelEach(block: suspend (In) -> Out): Collection<Out> =
    coroutineScope {
        this@parallelEach.map {
            async {
                block(it)
            }
        }.awaitAll()
    }

class GameProcessManager(
    val executor: ProcessExecutor,
    val gameProcess: GameProcess,
    val executable: String = "SpaceEngineers.exe",
    val initialWaitForLaunch: Duration = 7.seconds,
    val extraWaitForProcess: Duration = 13.seconds,
) : AutoCloseable {

    val lazyConnection = lazy {
        ProcessWithConnection(
            createConnection(gameProcess, factory = JvmSpaceEngineersBuilder.default()), gameProcess
        )
    }
    val processWithConnection by lazyConnection

    fun println(text: String) {
        gameProcess.println(text)
    }


    override fun close() {
        if (lazyConnection.isInitialized()) {
            processWithConnection.close()
        }
    }

    suspend fun ensureGameIsReadyForTesting() {
        val pid = getPID()
        yield()
        if (pid == null) {
            println("Game not running, starting")
            val result = start(gameProcess.executablePath)
            yield()
            ensureGameLaunched(result)
            println("Waiting for menu screen")
            processWithConnection.screens.waitForScreen(60_123.milliseconds, screenName = ScreenName.MainMenu)
            delay(2.seconds)
        } else {
            println("Game already running, ensuring main menu.")
            processWithConnection.extensions.screen.navigation.navigateTo(ScreenName.MainMenu)
            yield()
        }
        println("Done!")
    }

    private suspend fun ensureGameLaunched(result: String) {
        println("Waiting for initial ${initialWaitForLaunch}.")
        delay(initialWaitForLaunch)
        if (getPID() == null) {
            delay(extraWaitForProcess)
            if (getPID() == null) {
                error("The game process didn't launch in ${initialWaitForLaunch + extraWaitForProcess}, is the executable path correct?\n${gameProcess.executablePath}\nOutput: $result")
            }
        }
    }

    fun start(fullPath: String = EXECUTABLE_FULL_DEFAULT_PATH): String {
        return executor.startGuiApp(listOf(fullPath))
    }

    fun kill() {
        executor.execute(killCmd(executable))
    }

    fun getPID(): Long? {
        val result = executor.execute(isRunningCmd(executable))
        if (result.trim() == NO_TASKS_RUNNING) {
            return null
        }

        return result.lines().firstOrNull {
            it.startsWith("PID:")
        }?.substring(4)?.trim()?.toLongOrNull() ?: error(" couldn't get PID from the result: \n$result")
    }


}
