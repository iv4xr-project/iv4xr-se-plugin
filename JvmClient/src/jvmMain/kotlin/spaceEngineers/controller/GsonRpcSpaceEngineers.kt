package spaceEngineers.controller

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import spaceEngineers.transport.JsonRpcRequest
import spaceEngineers.transport.SocketReaderWriter
import spaceEngineers.transport.StringLineReaderWriter
import java.lang.reflect.Type
import kotlin.reflect.KType
import kotlin.reflect.KTypeProjection


data class GsonRpcRequest(
    @SerializedName("id")
    val id: Long,
    @SerializedName("jsonrpc")
    val jsonrpc: String = "2.0",
    @SerializedName("method")
    val method: String,
    @SerializedName("params")
    val params: Map<String, Any?> = emptyMap()
)

data class GsonRpcError(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    override val message: String,
    @SerializedName("data")
    val data: Any? = null
) : Exception(message)

data class GsonRpcResponse<T>(
    @SerializedName("id")
    val id: Long,
    @SerializedName("jsonrpc")
    val jsonrpc: String = "2.0",
    @SerializedName("result")
    val result: T?,
    @SerializedName("error")
    val error: GsonRpcError? = null
)

class GsonRpcSpaceEngineers(
    agentId: String,
    stringLineReaderWriter: StringLineReaderWriter,
    val gson: Gson = SocketReaderWriter.SPACE_ENG_GSON,
    characterPrefix: String = "Character.",
    itemsPrefix: String = "Items.",
    observerPrefix: String = "Observer.",
    sessionPrefix: String = "Session.",
    definitionsPrefix: String = "Definitions.",
    blocksPrefix: String = "Blocks.",
    adminPrefix: String = "Admin.",
) : JsonRpcSpaceEngineers(
    agentId = agentId,
    stringLineReaderWriter = stringLineReaderWriter,
    characterPrefix = characterPrefix,
    itemsPrefix = itemsPrefix,
    observerPrefix = observerPrefix,
    sessionPrefix = sessionPrefix,
    definitionsPrefix = definitionsPrefix,
    blocksPrefix = blocksPrefix,
    adminPrefix = adminPrefix,
) {
    override fun <O : Any> callRpc(
        rw: StringLineReaderWriter,
        request: JsonRpcRequest,
        parameters: List<TypedParameter<*>>,
        ktype: KType
    ): O {
        val gsonRequest = GsonRpcRequest(
            id = request.id,
            method = request.method,
            params = parameters.associate { it.name to it.value }
        )
        val responseJson = stringLineReaderWriter.sendAndReceiveLine(gson.toJson(gsonRequest))
        val response = gson.fromJson(responseJson, GsonRpcResponse::class.java)
        response.error?.let {
            throw it
        }
        response.result?.let {
            val subtype = ktype.arguments.first() as KTypeProjection
            if (subtype.type?.arguments?.isNotEmpty() == true) {
                val baseClass = subtype.type!!.toString().substringBefore("<")
                val subTypeClass = Class.forName(subtype.type!!.arguments.first().toString())
                if (baseClass == "kotlin.collections.List") {
                    val type: Type = TypeToken
                        .getParameterized(List::class.java, TypeToken.get(subTypeClass).type)
                        .type
                    return gson.fromJson(gson.toJson(it), type)
                } else {
                    error("cannot deserialize ${subtype.type.toString()}")
                }

            } else {
                val cls = Class.forName(subtype.toString()) as Class<O>
                return gson.fromJson(gson.toJson(it), cls)
            }
        }
        return Unit as O
    }
}
