package testhelp

import kotlinx.coroutines.runBlocking
import spaceEngineers.transport.closeIfCloseable
import spaceEngineers.controller.*
import java.io.File

const val TEST_AGENT = "you"

val RESOURCES_DIR = "src/jvmTest/resources/"
val MOCK_RESOURCES_DIR = "${RESOURCES_DIR}mock/"
val TEST_MOCK_RESPONSE_LINE = """
{"Id":"Mock","Position":{"X":4.0,"Y":2.0,"Z":0.0},"OrientationForward":{"X":0.0,"Y":0.0,"Z":0.0},"OrientationUp":{"X":0.0,"Y":0.0,"Z":0.0},"Velocity":{"X":0.0,"Y":0.0,"Z":0.0},"Extent":{"X":0.0,"Y":0.0,"Z":0.0},"Entities":[{"Id":"Ente","Position":{"X":3.0,"Y":2.0,"Z":1.0}}],"Grids":[{"Blocks":[{"MaxIntegrity":10.0,"BuildIntegrity":1.0,"Integrity":5.0,"BlockType":"MockBlock","MinPosition":{"X":0.0,"Y":0.0,"Z":0.0},"MaxPosition":{"X":0.0,"Y":0.0,"Z":0.0},"Size":{"X":0.0,"Y":0.0,"Z":0.0},"OrientationForward":{"X":0.0,"Y":0.0,"Z":0.0},"OrientationUp":{"X":0.0,"Y":0.0,"Z":0.0},"Id":"blk","Position":{"X":5.0,"Y":5.0,"Z":5.0}}],"Id":null,"Position":{"X":5.0,"Y":5.0,"Z":5.0}}]}
""".trim()

val SIMPLE_PLACE_GRIND_TORCH = "simple-place-grind-torch"


fun spaceEngineersSimplePlaceGrindTorchSuspend(
    scenarioId: String = SIMPLE_PLACE_GRIND_TORCH,
    agentId: String = TEST_AGENT,
    spaceEngineers: JsonRpcSpaceEngineers = JsonRpcSpaceEngineersBuilder.localhost(agentId),
    block: suspend JsonRpcSpaceEngineers.() -> Unit
) {
    try {
        spaceEngineers.session.loadFromTestResources(scenarioId)
        runBlocking {
            block(spaceEngineers)
        }
    } finally {
        spaceEngineers.closeIfCloseable()
    }
}

fun spaceEngineersSuspend(
    agentId: String = TEST_AGENT,
    spaceEngineers: SpaceEngineers = ContextControllerWrapper(
        spaceEngineers = JsonRpcSpaceEngineersBuilder.localhost(agentId)
    ),
    block: suspend SpaceEngineers.() -> Unit
) {
    try {
        runBlocking {
            block(spaceEngineers)
        }
    } finally {
        spaceEngineers.closeIfCloseable()
    }
}

fun spaceEngineers(
    agentId: String = TEST_AGENT,
    spaceEngineers: SpaceEngineers = ContextControllerWrapper(
        spaceEngineers = JsonRpcSpaceEngineersBuilder.localhost(agentId)
    ),
    block: SpaceEngineers.() -> Unit
) {
    try {
        runBlocking {
            block(spaceEngineers)
        }
    } finally {
        spaceEngineers.closeIfCloseable()
    }
}
