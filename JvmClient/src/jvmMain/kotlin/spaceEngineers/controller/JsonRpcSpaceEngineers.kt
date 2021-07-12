package spaceEngineers.controller

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
import spaceEngineers.transport.SocketReaderWriter
import spaceEngineers.model.*
import spaceEngineers.transport.AlwaysReturnSameLineReaderWriter
import spaceEngineers.transport.GsonReaderWriter
import spaceEngineers.transport.PresetLinesReaderWriter
import java.io.File
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction1

data class JsonRpcRequest<T>(
    @SerializedName("id")
    val id: Long,
    @SerializedName("jsonrpc")
    val jsonrpc: String = "2.0",
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
    val jsonrpc: String = "2.0",
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

class JsonRpcSpaceEngineers(
    val agentId: String,
    val gsonReaderWriter: GsonReaderWriter,
    val characterPrefix: String = "Character.",
    val itemsPrefix: String = "Items.",
    val observerPrefix: String = "Observer.",
    val sessionPrefix: String = "Session.",
    val definitionsPrefix: String = "Definitions.",
    val blocksPrefix: String = "Blocks.",
    val adminPrefix: String = "Admin.",
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
        override fun moveAndRotate(movement: Vec3, rotation3: Vec2, roll: Float): CharacterObservation {
            return processParameters<Any, CharacterObservation>(
                params = mapOf("movement" to movement, "rotation3" to rotation3, "roll" to roll),
                methodName = "${characterPrefix}MoveAndRotate"
            )
        }

        override fun turnOnJetpack(): CharacterObservation {
            return processNoParameterMethod<Unit, CharacterObservation>(
                method = ::turnOnJetpack,
                "${characterPrefix}TurnOnJetpack"
            )
        }

        override fun turnOffJetpack(): CharacterObservation {
            return processNoParameterMethod<Unit, CharacterObservation>(
                method = ::turnOffJetpack,
                "${characterPrefix}TurnOffJetpack"
            )
        }
    }

    override val items: Items = object : Items {
        override fun equip(toolbarLocation: ToolbarLocation) {
            processSingleParameterMethod<ToolbarLocation, Unit>(::equip, toolbarLocation, "${itemsPrefix}Equip")
        }

        override fun beginUsingTool() {
            processNoParameterMethod<Unit, Unit>(::beginUsingTool, "${itemsPrefix}BeginUsingTool")
        }

        override fun endUsingTool() {
            processNoParameterMethod<Unit, Unit>(::endUsingTool, "${itemsPrefix}EndUsingTool")
        }

        override fun setToolbarItem(name: String, toolbarLocation: ToolbarLocation) {
            processParameters<Any, Unit>(
                params = mapOf("name" to name, "toolbarLocation" to toolbarLocation),
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

    override val blocks: Blocks = object : Blocks {
        override fun place() {
            processNoParameterMethod<Unit, Unit>(::place, "${blocksPrefix}Place")
        }
    }
    override val admin: SpaceEngineersAdmin = object : SpaceEngineersAdmin {
        override val blocks: BlocksAdmin = object : BlocksAdmin {
            override fun placeAt(blockType: String, position: Vec3, orientationForward: Vec3, orientationUp: Vec3) {
                processParameters<Any, Unit>(
                    params = mapOf(
                        "blockType" to blockType,
                        "position" to position,
                        "orientationForward" to orientationForward,
                        "orientationUp" to orientationUp,
                    ),
                    methodName = "${adminPrefix}${blocksPrefix}PlaceAt"
                )
            }

            override fun remove(blockId: String) {
                processSingleParameterMethod<String, Unit>(
                    ::remove,
                    methodName = "${adminPrefix}${blocksPrefix}Remove",
                    parameter = blockId
                )
            }

            override fun setIntegrity(blockId: String, integrity: Float) {
                processParameters<Any, Unit>(
                    params = mapOf(
                        "blockId" to blockId,
                        "integrity" to integrity,
                    ),
                    methodName = "${adminPrefix}${blocksPrefix}SetIntegrity"
                )
            }
        }

        override val character: CharacterAdmin = object : CharacterAdmin {
            override fun teleport(position: Vec3, orientationForward: Vec3?, orientationUp: Vec3?): CharacterObservation {
                return processParameters<Any?, CharacterObservation>(
                    params = mapOf(
                        "position" to position,
                        "orientationForward" to orientationForward,
                        "orientationUp" to orientationUp
                    ),
                    methodName = "${adminPrefix}${characterPrefix}Teleport"
                )
            }
        }

    }

    override val observer: Observer = object : Observer {
        override fun observe(): CharacterObservation {
            return processNoParameterMethod<Unit, CharacterObservation>(::observe, "${observerPrefix}Observe")
        }

        override fun observeBlocks(): Observation {
            return processNoParameterMethod<Unit, Observation>(::observeBlocks, "${observerPrefix}ObserveBlocks")
        }

        override fun observeNewBlocks(): Observation {
            return processNoParameterMethod<Unit, Observation>(
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
    override val definitions: Definitions = object : Definitions {
        override fun blockDefinitions(): List<BlockDefinition> {
            return processParameters<Unit, List<LinkedTreeMap<Any, Any>>>(
                params = mapOf(),
                methodName = "${definitionsPrefix}BlockDefinitions"
            ).map {
                gsonReaderWriter.gson.fromJson(gsonReaderWriter.gson.toJson(it), BlockDefinition::class.java)
            }
        }

        override fun allDefinitions(): List<DefinitionBase> {
            return processParameters<Unit, List<LinkedTreeMap<Any, Any>>>(
                params = mapOf(),
                methodName = "${definitionsPrefix}AllDefinitions"
            ).map {
                gsonReaderWriter.gson.fromJson(gsonReaderWriter.gson.toJson(it), DefinitionBase::class.java)
            }
        }
    }


    companion object {
        fun localhost(agentId: String): JsonRpcSpaceEngineers {
            return JsonRpcSpaceEngineers(
                agentId = agentId,
                gsonReaderWriter = GsonReaderWriter(
                    stringLineReaderWriter = SocketReaderWriter(
                        port = 3333
                    )
                )
            )
        }

        fun mock(agentId: String, file: File): JsonRpcSpaceEngineers {
            val text = file.readText().trimEnd()
            val lines = text.lines()
            return if (lines.size > 1) {
                mock(agentId = agentId, lines = lines)
            } else {
                mock(agentId = agentId, lineToReturn = lines.first())
            }
        }

        fun mock(agentId: String, lineToReturn: String): JsonRpcSpaceEngineers {
            return JsonRpcSpaceEngineers(
                agentId = agentId,
                gsonReaderWriter = GsonReaderWriter(
                    stringLineReaderWriter = AlwaysReturnSameLineReaderWriter(response = lineToReturn)
                )
            )
        }

        fun mock(agentId: String, lines: List<String>): JsonRpcSpaceEngineers {
            return JsonRpcSpaceEngineers(
                agentId = agentId,
                gsonReaderWriter = GsonReaderWriter(
                    stringLineReaderWriter = PresetLinesReaderWriter(lines = lines)
                )
            )
        }

    }
}
