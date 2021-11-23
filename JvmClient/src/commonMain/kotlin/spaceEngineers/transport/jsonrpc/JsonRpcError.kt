package spaceEngineers.transport.jsonrpc

interface JsonRpcError<T> {
    val code: Int
    val message: String
    val data: T?
}

