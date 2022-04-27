package spaceEngineers.controller

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.serializer
import spaceEngineers.transport.StringLineReaderWriter
import spaceEngineers.transport.closeIfCloseable
import spaceEngineers.transport.jsonrpc.JsonRpcResponse
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcRequest
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcResponse
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.random.Random
import kotlin.reflect.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberFunctions


class SpaceEngineersJavaProxy(
    val agentId: String,
    val stringLineReaderWriter: StringLineReaderWriter,
    val implementedInterface: KClass<*>,
    val prefixName: String = implementedInterface.simpleName!!,
    val memberFunctions: List<KFunction<*>> = implementedInterface.memberFunctions.toList(),
    val subInterfacesByName: MutableMap<String, Any>,
) : InvocationHandler {

    val dottedPrefix = if (prefixName.isEmpty()) {
        ""
    } else {
        "$prefixName."
    }

    override fun invoke(proxy: Any, method: Method, args: Array<out Any?>?): Any? {
        if (method.isGetter()) {
            val name = method.name.substring(3)
            val lowercaseName = name.replaceFirstChar { it.lowercase() }
            val memberDefinition = implementedInterface.members.firstOrNull { it.name == lowercaseName }
                ?: error("Member $name not found, choices are: ${implementedInterface.members.map { it.name }}")
            return subInterfacesByName.getOrPut(dottedPrefix + name) {
                createSubProxy(memberDefinition.returnType.classifier as KClass<*>, dottedPrefix + name)
            }
        } else if (method.name == "close" && implementedInterface == SpaceEngineers::class) {
            stringLineReaderWriter.closeIfCloseable()
            return null
        }

        val kotlinMethodDefinition = memberFunctions.firstOrNull { it.name == method.name }
            ?: error("Original kotlin method ${method.name} not found, choices are: ${memberFunctions.map { it.name }}")

        val kotlinReturnType = kotlinMethodDefinition.returnType
        val returnTypeProjection = KTypeProjection(KVariance.INVARIANT, kotlinReturnType)
        val responseReturnType = KotlinJsonRpcResponse::class.createType(arguments = listOf(returnTypeProjection))

        return processParameters(
            methodName = "$dottedPrefix${
                method.name.replaceFirstChar
                { it.uppercase() }
            }",
            parameters = method.parameters.mapIndexed
            { index, parameter ->
                val javaParameter = method.parameters[index]
                val type = parameter.type
                val ktype: KClass<Any> = type.kotlin as KClass<Any>
                TypedParameter(javaParameter.name, args!![index], ktype)
            },
            returnType = responseReturnType
        )
    }

    private fun <T : Any> createSubProxy(kls: KClass<T>, prefixName: String): T {
        return createSubProxy(
            agentId = agentId,
            stringLineReaderWriter = stringLineReaderWriter,
            implementedInterface = kls,
            prefixName = prefixName,
            subInterfacesByName = subInterfacesByName,
        )
    }

    private fun processParameters(
        parameters: List<TypedParameter<*>>,
        methodName: String,
        returnType: KType
    ): Any {
        return callRpc(
            encodeRequest(parameters, methodName),
            returnType,
        )
    }

    private fun nextRequestId(): Long {
        return Random.nextLong()
    }

    private fun encodeRequest(
        parameters: List<TypedParameter<*>>,
        methodName: String
    ): String {
        return json.encodeToString(KotlinJsonRpcRequest(
            id = nextRequestId(),
            method = methodName,
            params = parameters.associate { it.toJsonElementPair() }
        ))
    }

    fun <O : Any> callRpc(
        encodedRequest: String,
        returnType: KType
    ): O {
        val responseJson = stringLineReaderWriter.sendAndReceiveLine(encodedRequest)
        return stringLineReaderWriter.wrapExceptionWithStringLineReaderInfo {
            decodeAndUnwrap(responseJson, returnType)
        }
    }

    private fun <O : Any> decodeAndUnwrap(responseJson: String, ktype: KType): O {
        val response = decodeResponse<O>(responseJson, ktype)
        return unwrap(response)
    }

    private fun <O : Any> decodeResponse(responseJson: String, ktype: KType): JsonRpcResponse<O> {
        return json.decodeFromString<KotlinJsonRpcResponse<O>>(
            serializer(ktype) as KSerializer<KotlinJsonRpcResponse<O>>,
            responseJson
        )
    }

    private fun <O : Any> unwrap(response: JsonRpcResponse<O>): O {
        response.error?.let {
            throw it as Exception
        }
        return response.result ?: Unit as O
    }

    private fun Method.isGetter(): Boolean {
        return name.startsWith("get")
    }

    companion object {

        fun <T : Any> createSubProxy(
            agentId: String,
            stringLineReaderWriter: StringLineReaderWriter,
            implementedInterface: KClass<T>,
            prefixName: String,
            subInterfacesByName: MutableMap<String, Any>,
        ): T {
            val cls = implementedInterface.javaObjectType
            val proxyHandler = SpaceEngineersJavaProxy(
                agentId,
                stringLineReaderWriter,
                implementedInterface = implementedInterface,
                prefixName = prefixName,
                subInterfacesByName = subInterfacesByName,
            )

            return cls.cast(
                Proxy.newProxyInstance(
                    cls.classLoader,
                    arrayOf<Class<*>>(cls, AutoCloseable::class.java),
                    proxyHandler,
                )
            )
        }

        fun fromStringLineReaderWriter(
            agentId: String,
            stringLineReaderWriter: StringLineReaderWriter
        ): SpaceEngineers {
            return createSubProxy(
                agentId = agentId,
                stringLineReaderWriter = stringLineReaderWriter,
                implementedInterface = SpaceEngineers::class,
                prefixName = "",
                subInterfacesByName = mutableMapOf(),
            )
        }
    }
}

