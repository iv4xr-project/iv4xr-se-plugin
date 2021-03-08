package spaceEngineers.controller

import com.google.gson.annotations.SerializedName
import environments.SocketReaderWriter
import spaceEngineers.commands.InteractionArgs
import spaceEngineers.commands.MoveTowardsArgs
import spaceEngineers.commands.MovementArgs
import spaceEngineers.commands.ObservationArgs
import spaceEngineers.model.SeObservation
import spaceEngineers.transport.GsonReaderWriter
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
    override val message: String
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

class JsonRpcCharacterController(val agentId: String, val gsonReaderWriter: GsonReaderWriter) :
    CharacterController, AutoCloseable {

    private inline fun <reified I, reified O> processSingleParameterMethod(method: KFunction1<I, O>, parameter: I): O {
        val request = JsonRpcRequest<I>(
            id = System.currentTimeMillis(),
            method = method.name,
            params = mapOf(method.parameters.first().name!! to parameter)
        )
        return gsonReaderWriter.callRpc<I, O>(request).result!!
    }

    override fun moveAndRotate(movementArgs: MovementArgs): SeObservation {
        return processSingleParameterMethod(::moveAndRotate, movementArgs)
    }

    override fun moveTowards(moveTowardsArgs: MoveTowardsArgs): SeObservation {
        return processSingleParameterMethod(::moveTowards, moveTowardsArgs)
    }

    override fun observe(observationArgs: ObservationArgs): SeObservation {
        return processSingleParameterMethod(::observe, observationArgs)
    }

    override fun interact(interactionArgs: InteractionArgs): SeObservation {
        return processSingleParameterMethod(::interact, interactionArgs)
    }

    companion object {
        fun localhost(agentId: String): JsonRpcCharacterController {
            return JsonRpcCharacterController(
                agentId = agentId,
                gsonReaderWriter = GsonReaderWriter(stringLineReaderWriter = SocketReaderWriter())
            )
        }
    }

    override fun close() {
        gsonReaderWriter.close()
    }
}