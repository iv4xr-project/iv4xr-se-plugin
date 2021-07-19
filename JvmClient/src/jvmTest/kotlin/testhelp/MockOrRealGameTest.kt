package testhelp

import spaceEngineers.controller.JsonRpcSpaceEngineers
import spaceEngineers.controller.JsonRpcSpaceEngineersBuilder
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.loadFromTestResources
import spaceEngineers.transport.GsonResponseAppendToFileReaderWriter
import spaceEngineers.transport.SocketReaderWriter
import spaceEngineers.transport.StringLineReaderWriter
import java.io.File


abstract class MockOrRealGameTest(
    val mockFile: File? = null,
    private val agentId: String = TEST_AGENT,
    private val forceRealGame: Boolean = false,
    private val forceWrite: Boolean = false,
    private val scenarioId: String = SIMPLE_PLACE_GRIND_TORCH,
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
        block: suspend SpaceEngineers.() -> Unit
    ) {
        val spaceEngineers = getSpaceEngineers(forceRealGame, file)
        useRealGame = useRealGame(forceRealGame, file)
        spaceEngineers.session.loadFromTestResources(scenarioId)
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

    private fun getSpaceEngineers(forceRealGame: Boolean, file: File): JsonRpcSpaceEngineers {
        return if (useRealGame(forceRealGame, file)) {
            JsonRpcSpaceEngineersBuilder.fromStringLineReaderWriter(agentId, readerWriter(file))
        } else {
            JsonRpcSpaceEngineersBuilder.mock(agentId, file)
        }
    }

    private fun getTestMethodName(index: Int = 4): String {
        val stacktrace = Thread.currentThread().stackTrace
        return stacktrace[index].methodName
    }

    private fun readerWriter(
        file: File, rw: SocketReaderWriter = SocketReaderWriter(
            port = 3333
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
