package spaceEngineers.controller

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import spaceEngineers.transport.*
import spaceEngineers.transport.jsonrpc.JsonRpcResponse
import spaceEngineers.transport.jsonrpc.JsonRpcError
import java.lang.reflect.Type
import kotlin.reflect.KFunction
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
    override val code: Int,
    @SerializedName("message")
    override val message: String,
    @SerializedName("data")
    override val data: Any? = null
) : Exception(message), JsonRpcError<Any>

data class GsonRpcResponse<T : Any>(
    @SerializedName("id")
    override val id: Long? = null,
    @SerializedName("jsonrpc")
    override val jsonrpc: String = "2.0",
    @SerializedName("result")
    override val result: T?,
    @SerializedName("error")
    override val error: GsonRpcError? = null
) : JsonRpcResponse<T>

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

    override fun encodeRequest(
        parameters: List<TypedParameter<*>>,
        methodName: String
    ): String {
        return gson.toJson(GsonRpcRequest(
            id = nextRequestId(),
            method = methodName,
            params = parameters.associate { it.name to it.value }
        ))
    }

    override fun <O : Any> decodeAndUnwrap(responseJson: String, ktype: KType): O {
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
