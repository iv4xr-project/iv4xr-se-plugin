package spaceEngineers.controller

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
import environments.SocketReaderWriter
import spaceEngineers.model.*
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
    val data: Any? = null
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
    val responseJson = stringLineReaderWriter.sendAndReceiveLine(gson.toJson(request))
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
    val sessionPrefix: String = "Session.",
    val definitionsPrefix: String = "Definitions.",
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

        override fun beginUsingTool() {
            processNoParameterMethod<Unit, Unit>(::beginUsingTool, "${itemsPrefix}StartUsingTool")
        }

        override fun endUsingTool() {
            processNoParameterMethod<Unit, Unit>(::endUsingTool, "${itemsPrefix}EndUsingTool")
        }

        override fun setToolbarItem(name: String, location: ToolbarLocation) {
            processParameters<Any, Unit>(
                params = mapOf("name" to name, "location" to location),
                methodName = "${itemsPrefix}SetToolbarItem"
            )
        }

        override fun getToolbar(): Toolbar {
            return processParameters<Unit, Toolbar>(
                params = mapOf(),
                methodName = "${itemsPrefix}GetToolbar"
            )
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

        override fun takeScreenshot(absolutePath: String) {
            return processSingleParameterMethod<String, Unit>(
                ::takeScreenshot,
                absolutePath,
                "${observerPrefix}TakeScreenshot"
            )
        }
    }
    override val definitions: Definitions = object: Definitions {
        override fun blockDefinitions(): List<SeBlockDefinition> {
            return processParameters<Unit, List<LinkedTreeMap<Any, Any>>>(
                params = mapOf(),
                methodName = "${definitionsPrefix}BlockDefinitions"
            ).map {
                gsonReaderWriter.gson.fromJson(gsonReaderWriter.gson.toJson(it), SeBlockDefinition::class.java)
            }
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