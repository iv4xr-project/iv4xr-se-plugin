package spaceEngineers.controller.proxy

import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import spaceEngineers.controller.json
import spaceEngineers.transport.Closeable
import spaceEngineers.transport.StringLineReaderWriter
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcRequest
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcResponse
import spaceEngineers.transport.jsonrpc.resultOrThrow
import kotlin.reflect.KType

class JsonRpcCaller(
    val stringLineReaderWriter: StringLineReaderWriter,
    val callQueue: MutableList<RequestWithReturnType> = mutableListOf(),
) : BatchCallable, Closeable by stringLineReaderWriter {

    override fun <T> execute(block: () -> T): T {
        callDirectly = false
        return block().apply {
            execute()
            callDirectly = true
        }
    }

    private var callDirectly: Boolean = true

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
                spaceEngineers.controller.proxy.RequestWithReturnType(request = request, returnType = returnType)
            )
            null
        }
    }

    fun callDirectly(
        request: KotlinJsonRpcRequest,
        returnType: KType,
    ): Any? {
        return callRpc(
            json.encodeToString(request),
            returnType,
        )
    }

    private fun execute(): List<Result<*>> {
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
        // TODO: by the specs, the order of results is not guaranteed and should be paired by id
        return try {
            json.decodeFromString<List<KotlinJsonRpcResponse<JsonElement?>>>(responseJson).map {
                it.asResult()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun <O : Any?> callRpc(
        encodedRequest: String,
        returnType: KType
    ): O? {
        val responseJson = stringLineReaderWriter.sendAndReceiveLine(encodedRequest)
        return decodeAndUnwrap(responseJson, returnType)
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

}
