package bdd.setup

import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import bdd.connection.GameProcess
import spaceEngineers.controller.processHomeDir
import spaceEngineers.controller.toFile
import spaceEngineers.controller.unixToWindowsPath
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import kotlin.concurrent.thread
import kotlin.test.assertTrue


class DedicatedServerManager(
    val processConfig: GameProcess,
    val scenarioDir: String,
) : AutoCloseable {

    var process: Process? = null
        private set

    suspend fun start(scenarioId: String) {
        val scenarioPath = File(scenarioDir.processHomeDir(), scenarioId).absolutePath.unixToWindowsPath()
        val wdFile = File(processConfig.executablePath.processHomeDir())
        assertTrue(wdFile.exists(), wdFile.absolutePath)
        val wd = wdFile.absolutePath
        val executable = File("${wd}/SpaceEngineersDedicated.exe")
        assertTrue(executable.exists(), executable.absolutePath)
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
        delay(500)
        withTimeout(120_000) {
            while (!gameStarted) {
                if (process == null || process?.isAlive == false) {
                    throw IllegalStateException("Server process already finished")
                }
                yield()
            }
        }
        delay(500)
    }

    override fun close() {
        process?.destroyForcibly()
        killDedicatedServerWindows()
    }

    fun isPortUsed(): Boolean {
        return try {
            Socket().use { s ->
                s.reuseAddress = true
                val sa: SocketAddress = InetSocketAddress(processConfig.address, processConfig.pluginPort.toInt())
                s.connect(sa, 1000)
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    fun isRunning(): Boolean {
        //TODO: or check actual process tree or opened port
        return process?.isAlive == true
    }

    companion object {
        fun killDedicatedServerWindows() {
            val process =
                ProcessBuilder("""C:\windows\system32/taskkill""", "/IM", "SpaceEngineersDedicated.exe", "/F")
                    .redirectErrorStream(true)
                    .start()
            process.waitFor()
        }


    }
}
