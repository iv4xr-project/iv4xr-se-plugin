package spaceEngineers.transport.jsonrpc

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.io.PrintStream
import java.io.PrintWriter

data class RemoteException(
    val className: String? = null,
    override val message: String? = null,
    val stacktrace: String? = null,
) : Throwable(message) {
    override fun printStackTrace(s: PrintWriter) {
        s.println(stacktrace)

    }

    override fun printStackTrace() {
        println(stacktrace)
    }

    override fun printStackTrace(s: PrintStream?) {
        s?.println(stacktrace)
    }

    companion object {
        fun fromJsonObject(data: JsonElement?): RemoteException? {
            if (data == null || data !is JsonObject) {
                return null
            }
            return RemoteException(
                className = data.dataStringAttribute("ClassName"),
                message = data.dataStringAttribute("Message"),
                stacktrace = data.dataStringAttribute("StackTraceString"),
            )
        }
    }
}


fun JsonObject.dataStringAttribute(name: String): String? {
    if (containsKey(name)) {
        val value = this[name] as JsonPrimitive
        return value.content
    }
    return null
}

val KotlinJsonRpcError.remoteException: RemoteException?
    get() = RemoteException.fromJsonObject(data)


