package testhelp

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import spaceEngineers.controller.*
import spaceEngineers.transport.closeIfCloseable
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcError

const val TEST_AGENT = SpaceEngineers.DEFAULT_AGENT_ID

val RESOURCES_DIR = "src/jvmTest/resources/"
val MOCK_RESOURCES_DIR = "${RESOURCES_DIR}mock/"
val TEST_MOCK_RESPONSE_LINE = """
{"Id":"Mock","Position":{"X":4.0,"Y":2.0,"Z":0.0},"OrientationForward":{"X":0.0,"Y":0.0,"Z":0.0},"OrientationUp":{"X":0.0,"Y":0.0,"Z":0.0},"Velocity":{"X":0.0,"Y":0.0,"Z":0.0},"Extent":{"X":0.0,"Y":0.0,"Z":0.0},"Entities":[{"Id":"Ente","Position":{"X":3.0,"Y":2.0,"Z":1.0}}],"Grids":[{"Blocks":[{"MaxIntegrity":10.0,"BuildIntegrity":1.0,"Integrity":5.0,"BlockType":"MockBlock","MinPosition":{"X":0.0,"Y":0.0,"Z":0.0},"MaxPosition":{"X":0.0,"Y":0.0,"Z":0.0},"Size":{"X":0.0,"Y":0.0,"Z":0.0},"OrientationForward":{"X":0.0,"Y":0.0,"Z":0.0},"OrientationUp":{"X":0.0,"Y":0.0,"Z":0.0},"Id":"blk","Position":{"X":5.0,"Y":5.0,"Z":5.0}}],"Id":null,"Position":{"X":5.0,"Y":5.0,"Z":5.0}}]}
""".trim()

val SIMPLE_PLACE_GRIND_TORCH = "simple-place-grind-torch"


fun JsonObject.dataStringAttribute(name: String): String? {
    if (containsKey(name)) {
        val value = this[name] as JsonPrimitive
        return value.content
    }
    return null
}

val KotlinJsonRpcError.remoteException: RemoteException?
    get() = RemoteException.fromJsonObject(data)


data class RemoteException(
    val className: String? = null,
    val message: String? = null,
    val stacktrace: String? = null,
) {
    companion object {
        fun fromJsonObject(data: JsonElement?): RemoteException? {
            if (data == null || data !is JsonObject) {
                return null
            }
            return RemoteException(
                className = data.dataStringAttribute("ClassName"),
                message = data.dataStringAttribute("Message"),
                stacktrace = data.dataStringAttribute("StackTraceString"),
            )
        }
    }
}

fun spaceEngineersSimplePlaceGrindTorchSuspend(
    scenarioId: String = SIMPLE_PLACE_GRIND_TORCH,
    agentId: String = TEST_AGENT,
    spaceEngineers: SpaceEngineers = JVMSpaceEngineersBuilder.default().localhost(agentId),
    block: suspend SpaceEngineers.() -> Unit
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
        spaceEngineers = JVMSpaceEngineersBuilder.default().localhost(agentId)
    ),
    block: suspend SpaceEngineers.() -> Unit
) {
    try {
        runBlocking {
            block(spaceEngineers)
        }
    } catch (e: KotlinJsonRpcError) {
        e.remoteException?.stacktrace?.let(::println)
        throw e
    } finally {
        spaceEngineers.closeIfCloseable()
    }
}

fun spaceEngineers(
    agentId: String = TEST_AGENT,
    spaceEngineers: SpaceEngineers = ContextControllerWrapper(
        spaceEngineers = JVMSpaceEngineersBuilder.default().localhost(agentId)
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
