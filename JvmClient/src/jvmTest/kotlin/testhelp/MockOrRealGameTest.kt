package testhelp

import spaceEngineers.controller.*
import spaceEngineers.transport.GsonResponseAppendToFileReaderWriter
import spaceEngineers.transport.ReconnectingSocketReaderWriter
import spaceEngineers.transport.SocketReaderWriter.Companion.DEFAULT_PORT
import spaceEngineers.transport.StringLineReaderWriter
import java.io.File
import java.lang.reflect.UndeclaredThrowableException


abstract class MockOrRealGameTest(
    val mockFile: File? = null,
    private val agentId: String = TEST_AGENT,
    private val forceRealGame: Boolean = false,
    private val forceWrite: Boolean = false,
    protected val scenarioId: String = SIMPLE_PLACE_GRIND_TORCH,
    private val loadScenario: Boolean = true,
    private val port: UShort = DEFAULT_PORT,
    private val spaceEngineersBuilder: JsonRpcSpaceEngineersBuilder = SpaceEngineersJavaProxyBuilder()
) {

    var useRealGame: Boolean = forceRealGame

    fun testContext(
        forceRealGame: Boolean = this.forceRealGame,
        scenarioId: String,
        fileName: String,
        block: suspend SpaceEngineers.() -> Unit
    ) {
        testContext(
            forceRealGame = forceRealGame,
            scenarioId = scenarioId,
            file = inMockResourcesDirectory(fileName),
            block = block
        )
    }

    fun testContext(
        forceRealGame: Boolean = this.forceRealGame,
        scenarioId: String = this.scenarioId,
        file: File = mockFile ?: inMockResourcesDirectory("${this::class.simpleName}-${getTestMethodName()}.txt"),
        spaceEngineersBuilder: JsonRpcSpaceEngineersBuilder = this.spaceEngineersBuilder,
        block: suspend ExtendedSpaceEngineers.() -> Unit
    ) {
        val spaceEngineers = getSpaceEngineers(forceRealGame, file, spaceEngineersBuilder)
        useRealGame = useRealGame(forceRealGame, file)
        if (loadScenario) {
            try {
                spaceEngineers.session.loadFromTestResources(scenarioId)
                spaceEngineers.screens.waitUntilTheGameLoaded()
            } catch (e: UndeclaredThrowableException) {
                throw e.cause ?: e
            } catch (e: Throwable) {
                spaceEngineers.close()
                throw e
            }
        }
        spaceEngineersSuspend(agentId = agentId, spaceEngineers = spaceEngineers, block = block)
    }

    private fun useRealGame(forceRealGame: Boolean, file: File): Boolean {
        return forceRealGame || !file.exists()
    }

    protected suspend fun delay(timeMillis: Long) {
        if (useRealGame) {
            kotlinx.coroutines.delay(timeMillis)
        }
    }

    private fun getSpaceEngineers(
        forceRealGame: Boolean,
        file: File,
        builder: JsonRpcSpaceEngineersBuilder
    ): ExtendedSpaceEngineers {
        return if (useRealGame(forceRealGame, file)) {
            builder.fromStringLineReaderWriter(agentId, readerWriter(file))
        } else {
            builder.mock(agentId, file)
        }.extend()
    }

    private fun getTestMethodName(index: Int = 4): String {
        val stacktrace = Thread.currentThread().stackTrace
        return stacktrace[index].methodName
    }

    private fun readerWriter(
        file: File, rw: StringLineReaderWriter = ReconnectingSocketReaderWriter(
            port = port
        )
    ): StringLineReaderWriter {
        if (!file.exists() || forceWrite) {
            return GsonResponseAppendToFileReaderWriter(
                rw = rw,
                file = file
            )
        }
        return rw
    }

    companion object {

        fun inMockResourcesDirectory(fileName: String): File {
            return File("${MOCK_RESOURCES_DIR}${fileName}")
        }
    }

}
