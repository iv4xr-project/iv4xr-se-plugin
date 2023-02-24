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
) : RuntimeException(message), JsonRpcError<JsonElement> {

    fun isInternalError(): Boolean {
        return code == INTERNAL_ERROR
    }

    companion object {
        const val PARSE_ERROR = -32700
        const val INVALID_REQUEST = -32600
        const val METHOD_NOT_FOUND = -32601
        const val INVALID_PARAMS = -32602
        const val INTERNAL_ERROR = -32603
        val SERVER_ERROR = -32000 downTo -32099
    }
}
