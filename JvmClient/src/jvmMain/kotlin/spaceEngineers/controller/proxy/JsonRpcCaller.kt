package spaceEngineers.controller.proxy

import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer
import spaceEngineers.controller.json
import spaceEngineers.controller.wrapExceptionWithStringLineReaderInfo
import spaceEngineers.transport.StringLineReaderWriter
import spaceEngineers.transport.closeIfCloseable
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcRequest
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcResponse
import spaceEngineers.transport.jsonrpc.resultOrThrow
import kotlin.reflect.KType

class JsonRpcCaller(
    private val stringLineReaderWriter: StringLineReaderWriter,
    val callQueue: MutableList<RequestWithReturnType> = mutableListOf(),
    initialCallDirectly: Boolean = true,
) : AutoCloseable {


    private var _callDirectly: Boolean = initialCallDirectly
    var callDirectly: Boolean
        get() {
            return _callDirectly
        }
        set(value) {
            if (value && !_callDirectly) {
                error("calls in queue, callMultiple first")
            }
            _callDirectly = value
        }

    fun call(
        request: KotlinJsonRpcRequest,
        returnType: KType,
    ): Any? {
        return if (callDirectly) {
            callDirectly(
                request = request,
                returnType = returnType,
            )
        } else {
            callQueue.add(
                RequestWithReturnType(request = request, returnType = returnType)
            )
            null
        }
    }

    private fun callDirectly(
        request: KotlinJsonRpcRequest,
        returnType: KType,
    ): Any? {
        return callRpc(
            json.encodeToString(request),
            returnType,
        )
    }

    fun callMultiple(): List<Result<*>> {
        if (callQueue.isEmpty()) {
            return emptyList()
        }
        if (callQueue.size == 1) {
            val r = callQueue.removeFirst()
            return listOf(decodeResponse<Any?>(json.encodeToString(r.request), r.returnType).asResult())
        }

        val encodedRequest = json.encodeToString(callQueue.map { it.request })
        callQueue.clear()
        val responseJson = stringLineReaderWriter.sendAndReceiveLine(encodedRequest)
        return json.decodeFromString<List<KotlinJsonRpcResponse<JsonObject?>>>(responseJson).map {
            it.asResult()
        }
    }

    private fun <O : Any?> callRpc(
        encodedRequest: String,
        returnType: KType
    ): O? {
        val responseJson = stringLineReaderWriter.sendAndReceiveLine(encodedRequest)
        return stringLineReaderWriter.wrapExceptionWithStringLineReaderInfo {
            decodeAndUnwrap(responseJson, returnType)
        }
    }

    private fun <O : Any?> decodeAndUnwrap(responseJson: String, ktype: KType): O? {
        return decodeResponse<O?>(responseJson, ktype).resultOrThrow()
    }

    private fun <O : Any?> decodeResponse(responseJson: String, ktype: KType): KotlinJsonRpcResponse<O> {
        return json.decodeFromString<KotlinJsonRpcResponse<O>>(
            serializer(ktype) as KSerializer<KotlinJsonRpcResponse<O>>,
            responseJson
        )
    }

    override fun close() {
        stringLineReaderWriter.closeIfCloseable()
    }
}
