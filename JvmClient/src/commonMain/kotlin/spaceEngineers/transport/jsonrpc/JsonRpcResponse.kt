package spaceEngineers.transport.jsonrpc

interface JsonRpcResponse<T : Any?> {
    val id: Long?
    val jsonrpc: String
    val result: T?
    val error: JsonRpcError<*>?
}

fun <T : Any?> JsonRpcResponse<T>.resultOrThrow(): T? {
    error?.let {
        throw it as Throwable
    }
    return result
}
