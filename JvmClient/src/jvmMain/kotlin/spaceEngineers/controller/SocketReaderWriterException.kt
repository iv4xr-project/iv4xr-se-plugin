package spaceEngineers.controller

import spaceEngineers.transport.SocketReaderWriter
import spaceEngineers.transport.StringLineReaderWrapper
import spaceEngineers.transport.StringLineReaderWriter
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcError

fun <O : Any> StringLineReaderWriter.wrapExceptionWithStringLineReaderInfo(
    block: () -> O
): O {
    return try {
        block()
    } catch (e: KotlinJsonRpcError) {
        socketReaderWriter()?.let {
            throw SocketReaderWriterException(e, it.address)
        } ?: throw e
    }
}

fun StringLineReaderWriter.socketReaderWriter(): SocketReaderWriter? {
    if (this is SocketReaderWriter) {
        return this
    }
    if (this is StringLineReaderWrapper) {
        return rw.socketReaderWriter()
    }
    return null
}

class SocketReaderWriterException(
    kotlinJsonRpcError: KotlinJsonRpcError,
    stringLineReaderWriterName: String,
) : RuntimeException(
    "$stringLineReaderWriterName ${kotlinJsonRpcError.message}",
    kotlinJsonRpcError,
)
