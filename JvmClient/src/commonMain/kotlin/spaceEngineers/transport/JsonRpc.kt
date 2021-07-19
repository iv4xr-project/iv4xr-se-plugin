package spaceEngineers.transport

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class JsonRpcRequest(
    @SerialName("id")
    val id: Long,
    @SerialName("jsonrpc")
    val jsonrpc: String = "2.0",
    @SerialName("method")
    val method: String,
    @SerialName("params")
    val params: Map<String, JsonElement?>
)

@Serializable
data class JsonRpcError(
    @SerialName("code")
    val code: Int,
    @SerialName("message")
    override val message: String,
    @SerialName("data")
    @Contextual
    val data: Any? = null
) : Exception(message)

@Serializable
data class JsonRpcResponse<T: Any>(
    @SerialName("id")
    val id: Long,
    @SerialName("jsonrpc")
    val jsonrpc: String = "2.0",
    @SerialName("result")
    val result: T?,
    @SerialName("error")
    val error: JsonRpcError? = null
)
