package spaceEngineers.transport.jsonrpc

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class KotlinJsonRpcError(
    @SerialName("code")
    override val code: Int,
    @SerialName("message")
    override val message: String,
    @SerialName("data")
    @Contextual
    override val data: JsonElement? = null
) : RuntimeException(message), JsonRpcError<JsonElement>
