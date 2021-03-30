package spaceEngineers.controller

import com.google.gson.annotations.SerializedName
import environments.SocketReaderWriter
import spaceEngineers.model.SeObservation
import spaceEngineers.model.ToolbarLocation
import spaceEngineers.model.Vec3
import spaceEngineers.transport.GsonReaderWriter
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction1

data class JsonRpcRequest<T>(
    @SerializedName("id")
    val id: Long,
    @SerializedName("jsonrpc")
    val jsonrpc: Double = 2.0,
    @SerializedName("method")
    val method: String,
    @SerializedName("params")
    val params: Map<String, T> = emptyMap()
)

data class JsonRpcError(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    override val message: String,
    @SerializedName("data")
    val data: String? = null
) : Exception(message)

data class JsonRpcResponse<T>(
    @SerializedName("id")
    val id: Long,
    @SerializedName("jsonrpc")
    val jsonrpc: Double = 2.0,
    @SerializedName("result")
    val result: T?,
    @SerializedName("error")
    val error: JsonRpcError? = null
)

inline fun <reified I, reified O> GsonReaderWriter.callRpc(request: JsonRpcRequest<I>): JsonRpcResponse<O> {
    println(gson.toJson(request))
    val responseJson = stringLineReaderWriter.sendAndReceiveLine(gson.toJson(request))
    val responseJson_ = "{\"jsonrpc\":\"2.0\",\"id\":4,\"result\":" +
            "{\"AgentID\":\"Mock\",\"Position\":{\"X\":4.0,\"Y\":2.0,\"Z\":0.0},\"OrientationForward\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"OrientationUp\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"Velocity\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"Extent\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"Entities\":[{\"Id\":\"Ente\",\"Position\":{\"X\":3.0,\"Y\":2.0,\"Z\":1.0}}],\"Grids\":[{\"Blocks\":[{\"MaxIntegrity\":10.0,\"BuildIntegrity\":1.0,\"Integrity\":5.0,\"BlockType\":\"MockBlock\",\"MinPosition\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"MaxPosition\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"Size\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"OrientationForward\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"OrientationUp\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"Id\":\"blk\",\"Position\":{\"X\":5.0,\"Y\":5.0,\"Z\":5.0}}],\"Id\":null,\"Position\":{\"X\":5.0,\"Y\":5.0,\"Z\":5.0}}]}" +
            "}"
    println(responseJson)
    val response = gson.fromJson(responseJson, JsonRpcResponse::class.java)
    response.error?.let {
        throw it
    }
    response.result?.let {
        return JsonRpcResponse<O>(
            id = response.id,
            error = response.error,
            jsonrpc = response.jsonrpc,
            result = gson.fromJson(gson.toJson(it), O::class.java)
        )
    }
    //error("no result and no error received from json")
    return JsonRpcResponse(
        id = response.id,
        error = response.error,
        jsonrpc = response.jsonrpc,
        result = null as O?
    )

}

class JsonRpcCharacterController(
    val agentId: String,
    val gsonReaderWriter: GsonReaderWriter,
    val characterPrefix: String = "Character.",
    val itemsPrefix: String = "Items.",
    val observerPrefix: String = "Observer.",
    val sessionPrefix: String = "Session."
) :
    SpaceEngineers, AutoCloseable {

    private inline fun <reified I, reified O> processSingleParameterMethod(
        method: KFunction1<I, O>,
        parameter: I,
        methodName: String = method.name
    ): O {
        return processParameters<I, O>(
            params = mapOf(method.parameters.first().name!! to parameter),
            methodName = methodName
        )
    }

    private inline fun <reified I, reified O> processNoParameterMethod(
        method: KFunction<O>,
        methodName: String = method.name
    ): O {
        return processParameters<I, O>(
            params = mapOf(),
            methodName = methodName
        )
    }

    private inline fun <reified I, reified O> processParameters(
        params: Map<String, I>,
        methodName: String
    ): O {
        val request = JsonRpcRequest<I>(
            id = System.currentTimeMillis(),
            method = methodName,
            params = params
        )
        return gsonReaderWriter.callRpc<I, O>(request).result.let {
            if (O::class.java == Unit::class.java) {
                Unit as O
            } else {
                it!!
            }
        }
    }

    override fun close() {
        gsonReaderWriter.close()
    }

    override val session: Session = object : Session {
        override fun loadScenario(scenarioPath: String) {
            processSingleParameterMethod<String, Unit>(::loadScenario, scenarioPath, "${sessionPrefix}LoadScenario")
        }
    }

    override val character: Character = object : Character {
        override fun moveAndRotate(movement: Vec3, rotation3: Vec3, roll: Float): SeObservation {
            return processParameters<Any, SeObservation>(
                params = mapOf("movement" to movement, "rotation3" to rotation3, "roll" to roll),
                methodName = "${characterPrefix}MoveAndRotate"
            )
        }
    }

    override val items: Items = object : Items {
        override fun place() {
            processNoParameterMethod<Unit, Unit>(::place, "${itemsPrefix}Place")
        }

        override fun equip(toolbarLocation: ToolbarLocation) {
            processSingleParameterMethod<ToolbarLocation, Unit>(::equip, toolbarLocation, "${itemsPrefix}Equip")
        }

        override fun startUsingTool() {
            processNoParameterMethod<Unit, Unit>(::startUsingTool, "${itemsPrefix}StartUsingTool")
        }

        override fun endUsingTool() {
            processNoParameterMethod<Unit, Unit>(::endUsingTool, "${itemsPrefix}EndUsingTool")
        }
    }

    override val observer: Observer = object : Observer {
        override fun observe(): SeObservation {
            return processNoParameterMethod<Unit, SeObservation>(::observe, "${observerPrefix}Observe")
        }

        override fun observeBlocks(): SeObservation {
            return processNoParameterMethod<Unit, SeObservation>(::observeBlocks, "${observerPrefix}ObserveBlocks")
        }

        override fun observeNewBlocks(): SeObservation {
            return processNoParameterMethod<Unit, SeObservation>(
                ::observeNewBlocks,
                "${observerPrefix}ObserveNewBlocks"
            )
        }

        override fun observeEntities(): SeObservation {
            return processNoParameterMethod<Unit, SeObservation>(::observeEntities, "${observerPrefix}ObserveEntities")
        }
    }

    companion object {
        fun localhost(agentId: String): JsonRpcCharacterController {
            return JsonRpcCharacterController(
                agentId = agentId,
                gsonReaderWriter = GsonReaderWriter(
                    stringLineReaderWriter = SocketReaderWriter(
                        port = 3333
                    )
                )
            )
        }
    }
}