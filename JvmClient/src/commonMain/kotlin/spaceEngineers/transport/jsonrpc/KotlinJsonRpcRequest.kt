package spaceEngineers.transport.jsonrpc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class KotlinJsonRpcRequest(
    @SerialName("id")
    val id: Long,
    @SerialName("jsonrpc")
    val jsonrpc: String = "2.0",
    @SerialName("method")
    val method: String,
    @SerialName("params")
    val params: Map<String, JsonElement?>
)
