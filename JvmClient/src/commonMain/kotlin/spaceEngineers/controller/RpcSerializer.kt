package spaceEngineers.controller

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import spaceEngineers.transport.JsonRpcRequest
import spaceEngineers.transport.JsonRpcResponse
import spaceEngineers.transport.StringLineReaderWriter
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.typeOf


val json = Json {
    encodeDefaults = true
}

data class TypedParameter<T : Any>(
    val name: String,
    val value: T?,
    val type: KClass<T>,
) {
    @OptIn(InternalSerializationApi::class)
    fun toJsonElementPair(): Pair<String, JsonElement?> {
        if (value == null) {
            return name to null
        }
        return name to json.encodeToJsonElement<T>(type.serializer(), value)
    }
}

abstract class RpcSerializer(
    val stringLineReaderWriter: StringLineReaderWriter,
) {

    protected inline fun <reified I : Any, reified O : Any> processSingleParameterMethod(
        method: KFunction<O>,
        parameter: I,
        parameterName: String,
        parameterType: KClass<I>,
        methodName: String = method.name
    ): O {
        return processParameters<O>(
            method = method,
            parameters = listOf<TypedParameter<*>>(
                TypedParameter<I>(
                    name = parameterName,
                    value = parameter,
                    type = parameterType,
                )
            ),
            methodName = methodName
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    protected inline fun <reified O : Any> processParameters(
        method: KFunction<O?>,
        parameters: List<TypedParameter<*>>,
        methodName: String = method.name,
    ): O {
        val request = JsonRpcRequest(
            id = Random.nextLong(),
            method = methodName,
            params = parameters.associate { it.toJsonElementPair() }
        )
        return callRpc<O>(stringLineReaderWriter, request, parameters, typeOf<JsonRpcResponse<O>>())
    }

    @OptIn(ExperimentalStdlibApi::class)
    open fun <O : Any> callRpc(
        rw: StringLineReaderWriter,
        request: JsonRpcRequest,
        parameters: List<TypedParameter<*>>,
        ktype: KType
    ): O {
        return callRpc_<O>(rw, request, ktype).result ?: Unit as O

    }

    fun <O : Any> callRpc_(rw: StringLineReaderWriter, request: JsonRpcRequest, ktype: KType): JsonRpcResponse<O> {
        val responseJson = rw.sendAndReceiveLine(json.encodeToString(request))
        val response = json.decodeFromString<JsonRpcResponse<O>>(
            serializer(ktype) as KSerializer<JsonRpcResponse<O>>,
            responseJson
        )
        response.error?.let {
            throw it
        }
        return response
    }


    protected inline fun <reified O : Any> processNoParameterMethod(
        method: KFunction<O?>,
        methodName: String = method.name
    ): O {
        return processParameters<O>(
            method = method,
            parameters = emptyList(),
            methodName = methodName
        )
    }
}
