package spaceEngineers.controller

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.serializer
import spaceEngineers.transport.StringLineReaderWriter
import spaceEngineers.transport.closeIfCloseable
import spaceEngineers.transport.jsonrpc.JsonRpcResponse
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcRequest
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcResponse
import java.lang.reflect.*
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


    val prefix = if (prefixName.isEmpty()) {
        ""
    } else {
        "$prefixName."
    }

    override fun invoke(proxy: Any, method: Method, args: Array<out Any?>?): Any? {
        return try {
            return tryInvoke(proxy, method, args)
        } catch (e: java.lang.Exception) {
            println("rethrow")
            throw e
        }
    }

    fun tryInvoke(proxy: Any, method: Method, args: Array<out Any?>?): Any? {
        if (method.name.startsWith("get")) {
            val name = method.name.substring(3)
            val lowercaseName = name.replaceFirstChar { it.lowercase() }
            val memberDefinition = implementedInterface.members.first { it.name == lowercaseName }
                ?: error("Member ${name} not found, choices are ${implementedInterface.members.map { it.name }}")
            println("creating proxy for $prefix$name")
            return subInterfacesByName.getOrPut(prefix + name) {
                val kls = memberDefinition.returnType.classifier as KClass<*>
                val cls = kls.javaObjectType
                println(memberDefinition.returnType.classifier)
                val px = SpaceEngineersJavaProxy(
                    agentId,
                    stringLineReaderWriter,
                    implementedInterface = kls,
                    prefixName = prefix + name,
                    subInterfacesByName = subInterfacesByName,
                )



                cls.cast(
                    Proxy.newProxyInstance(
                        cls.classLoader,
                        arrayOf<Class<*>>(cls, AutoCloseable::class.java),
                        px,
                    )
                )
            }
        } else if (method.name == "close") {
            println("closing!")
            stringLineReaderWriter.closeIfCloseable()
            return null
        }

        val kotlinMethodDefinition = memberFunctions.find { it.name == method.name }
            ?: error("couldn't find original kotlin method ${method.name}, choices: ${memberFunctions.map { it.name }}")

        val kotlinReturnType = kotlinMethodDefinition.returnType
        val returnTypeProjection = KTypeProjection(KVariance.INVARIANT, kotlinReturnType)
        val responseReturnType = KotlinJsonRpcResponse::class.createType(arguments = listOf(returnTypeProjection))

        return processParameters(
            methodName = "$prefix${
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

    private fun processParameters(
        parameters: List<TypedParameter<*>>,
        methodName: String,
        returnType: KType
    ): Any {
        println("Method $methodName")
        return callRpc(
            stringLineReaderWriter,
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
        rw: StringLineReaderWriter,
        encodedRequest: String,
        returnType: KType
    ): O {
        val responseJson = rw.sendAndReceiveLine(encodedRequest)
        return decodeAndUnwrap<O>(responseJson, returnType)
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

    companion object {

        fun fromStringLineReaderWriter(
            agentId: String,
            stringLineReaderWriter: StringLineReaderWriter
        ): SpaceEngineers {
            return Proxy.newProxyInstance(
                SpaceEngineers::class.java.classLoader, arrayOf(SpaceEngineers::class.java, AutoCloseable::class.java),
                SpaceEngineersJavaProxy(
                    agentId,
                    stringLineReaderWriter,
                    implementedInterface = SpaceEngineers::class,
                    prefixName = "",
                    subInterfacesByName = mutableMapOf(),
                )
            ) as SpaceEngineers
        }
    }
}

