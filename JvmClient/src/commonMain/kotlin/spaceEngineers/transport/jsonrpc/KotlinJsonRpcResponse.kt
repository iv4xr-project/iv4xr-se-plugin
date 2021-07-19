package spaceEngineers.transport.jsonrpc

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KotlinJsonRpcResponse<T : Any>(
    @SerialName("id")
    override val id: Long? = null,
    @SerialName("jsonrpc")
    override val jsonrpc: String = "2.0",
    @SerialName("result")
    override val result: T? = null,
    @SerialName("error")
    override val error: KotlinJsonRpcError? = null
): JsonRpcResponse<T>
