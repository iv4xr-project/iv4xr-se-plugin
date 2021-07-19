package spaceEngineers.transport.jsonrpc

interface JsonRpcResponse<T : Any> {
    val id: Long?
    val jsonrpc: String
    val result: T?
    val error: JsonRpcError<*>?
}
