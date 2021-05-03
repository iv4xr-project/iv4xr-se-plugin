package testhelp

import environments.SocketReaderWriter
import spaceEngineers.controller.JsonRpcCharacterController
import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.loadFromTestResources
import spaceEngineers.transport.GsonReaderWriter
import spaceEngineers.transport.GsonResponseToFileReaderWriter
import java.io.File

val preferMocking = true

abstract class MockOrRealGameTest(
    val jsonMockFile: File,
    val useMock: Boolean = preferMocking,
    val scenarioId: String = SIMPLE_PLACE_GRIND_TORCH,
) {

    fun testContext(block: SpaceEngineers.() -> Unit) {
        if (useMock) {
            mockGameContext(block)
        } else {
            realGameContext(block = block)
        }
    }

    open fun realGameContext(block: SpaceEngineers.() -> Unit) {
        spaceEngineersSimplePlaceGrindTorch(scenarioId = scenarioId, block = block)
    }

    open fun mockGameContext(block: SpaceEngineers.() -> Unit) {
        mockFileResponse(
            response = jsonMockFile.readText().trimEnd(),
            block = block
        )
    }

    fun runAndWriteResponse(file: File = jsonMockFile, block: SpaceEngineers.() -> Unit) {
        Companion.runAndWriteResponse(file, scenarioId, block)
    }

    companion object {

        /**
         * Creates scenario joining real game and saves last json response to file.
         */
        fun runAndWriteResponse(
            file: File,
            scenario: String = SIMPLE_PLACE_GRIND_TORCH,
            block: SpaceEngineers.() -> Unit
        ) {
            val spaceEngineers = JsonRpcCharacterController(
                agentId = TEST_AGENT,
                gsonReaderWriter = GsonReaderWriter(
                    stringLineReaderWriter = GsonResponseToFileReaderWriter(
                        rw = SocketReaderWriter(port = 3333),
                        //rw = NamedPipeReaderWriter("StreamJsonRpcSamplePipe"),
                        file = file,
                    )
                )
            )
            spaceEngineers.use {
                it.session.loadFromTestResources(scenario)
                block(it)
            }
        }
    }

}
