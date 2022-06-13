package spaceEngineers.controller

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import spaceEngineers.model.*
import spaceEngineers.transport.StringLineReaderWriter
import spaceEngineers.transport.jsonrpc.JsonRpcResponse
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcRequest
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcResponse
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf


val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    allowSpecialFloatingPointValues = true
    serializersModule = SerializersModule {
        polymorphic(Block::class) {
            default { BlockSerializer }
        }
        polymorphic(BlockDefinition::class) {
            default { BlockDefinitionSerializer }
        }
        polymorphic(DefinitionBase::class) {
            default { DataDefinitionBase.serializer() }
        }
        polymorphic(PhysicalItemDefinition::class) {
            default { DataPhysicalItemDefinition.serializer() }
        }
        polymorphic(BlockOrGroupItem::class) {
            default { BlockOrGroupItemSerializer }
        }
    }
}

abstract class RpcSerializer(
    val stringLineReaderWriter: StringLineReaderWriter,
) {

    inline fun <reified I : Any, reified O : Any> processSingleParameterMethod(
        parameter: I,
        parameterName: String,
        parameterType: KClass<I>,
        methodName: String
    ): O {
        return processParameters<O>(
            parameters = listOf<TypedParameter<*>>(
                TypedParameter<I>(
                    name = parameterName,
                    value = parameter,
                    type = parameterType,
                    ktype = typeOf<I>(),
                )
            ),
            methodName = methodName
        )
    }

    inline fun <reified O : Any> processParameters(
        parameters: List<TypedParameter<*>>,
        methodName: String,
    ): O {
        return callRpc<O>(
            stringLineReaderWriter,
            encodeRequest(parameters, methodName),
            typeOf<KotlinJsonRpcResponse<O>>()
        )
    }

    open fun nextRequestId(): Long {
        return Random.nextLong()
    }

    open fun encodeRequest(
        parameters: List<TypedParameter<*>>,
        methodName: String
    ): String {
        return json.encodeToString(KotlinJsonRpcRequest(
            id = nextRequestId(),
            method = methodName,
            params = parameters.associate { it.toJsonElementPair() }
        ))
    }

    open fun <O : Any> decodeResponse(responseJson: String, ktype: KType): JsonRpcResponse<O> {
        return json.decodeFromString<KotlinJsonRpcResponse<O>>(
            serializer(ktype) as KSerializer<KotlinJsonRpcResponse<O>>,
            responseJson
        )
    }

    open fun <O : Any> decodeAndUnwrap(responseJson: String, ktype: KType): O {
        val response = decodeResponse<O>(responseJson, ktype)
        return unwrap(response)
    }

    private fun <O : Any> unwrap(response: JsonRpcResponse<O>): O {
        response.error?.let {
            throw it as Exception
        }
        return response.result ?: Unit as O
    }

    fun <O : Any> callRpc(rw: StringLineReaderWriter, encodedRequest: String, ktype: KType): O {
        val responseJson = rw.sendAndReceiveLine(encodedRequest)
        return decodeAndUnwrap<O>(responseJson, ktype)
    }


    protected inline fun <reified O : Any> processNoParameterMethod(
        methodName: String,
    ): O {
        return processParameters<O>(
            parameters = emptyList(),
            methodName = methodName
        )
    }
}
