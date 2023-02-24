package spaceEngineers.controller.proxy

import spaceEngineers.controller.SpaceEngineers
import spaceEngineers.controller.TypedParameter
import spaceEngineers.transport.Closeable
import spaceEngineers.transport.StringLineReaderWriter
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcRequest
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcResponse
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.random.Random
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KTypeProjection
import kotlin.reflect.KVariance
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.valueParameters

class SpaceEngineersBatchJavaProxy(
    val agentId: String,
    val implementedInterface: KClass<*>,
    val prefixName: String = implementedInterface.simpleName!!,
    val memberFunctions: List<KFunction<*>> = implementedInterface.memberFunctions.toList(),
    val subInterfacesByName: MutableMap<String, Any>,
    val rpcCaller: JsonRpcCaller,
) : InvocationHandler, Closeable by rpcCaller {

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
            close()
            return null
        }

        val kotlinMethodDefinition = memberFunctions.firstOrNull { it.name == method.name }
            ?: error("Original kotlin method ${method.name} not found, choices are: ${memberFunctions.map { it.name }}")

        val kotlinReturnType = kotlinMethodDefinition.returnType
        val returnTypeProjection = KTypeProjection(KVariance.INVARIANT, kotlinReturnType)
        val responseReturnType = KotlinJsonRpcResponse::class.createType(arguments = listOf(returnTypeProjection))
        val request = createRequest(
            methodName = "$dottedPrefix${
            method.name.replaceFirstChar
            { it.uppercase() }
            }",
            parameters = method.parameters.mapIndexed
            { index, parameter ->
                val javaParameter = method.parameters[index]
                val type = parameter.type
                val kclass: KClass<Any> = type.kotlin as KClass<Any>
                val ktype = kotlinMethodDefinition.valueParameters[index].type
                TypedParameter(javaParameter.name, args!![index], kclass, ktype)
            },
        )
        return rpcCaller.call(
            request = request,
            returnType = responseReturnType
        )
    }

    private fun createRequest(
        parameters: List<TypedParameter<*>>,
        methodName: String,
    ): KotlinJsonRpcRequest {
        return KotlinJsonRpcRequest(
            id = nextRequestId(),
            method = methodName,
            params = parameters.associate { it.toJsonElementPair() }
        )
    }

    private fun nextRequestId(): Long {
        return Random.nextLong()
    }

    private fun <T : Any> createSubProxy(kls: KClass<T>, prefixName: String): T {
        return createSubProxy(
            agentId = agentId,
            implementedInterface = kls,
            prefixName = prefixName,
            subInterfacesByName = subInterfacesByName,
            batchController = rpcCaller,
        )
    }

    private fun Method.isGetter(): Boolean {
        return name.startsWith("get")
    }

    companion object {

        fun createSeSubProxy(
            agentId: String,
            jsonRpcCaller: JsonRpcCaller,
            implementedInterface: KClass<SpaceEngineers>,
            prefixName: String,
            subInterfacesByName: MutableMap<String, Any>,
        ): BatchProcessableSpaceEngineers {
            val cls = implementedInterface.javaObjectType
            val proxyHandler = SpaceEngineersBatchJavaProxy(
                agentId,
                implementedInterface = implementedInterface,
                prefixName = prefixName,
                subInterfacesByName = subInterfacesByName,
                rpcCaller = jsonRpcCaller,
            )

            return BatchProcessableSpaceEngineers(
                cls.cast(
                    Proxy.newProxyInstance(
                        cls.classLoader,
                        arrayOf<Class<*>>(cls, AutoCloseable::class.java),
                        proxyHandler,
                    )
                ),
                rpcCaller = jsonRpcCaller,
            )
        }

        private fun <T : Any> createSubProxy(
            agentId: String,
            implementedInterface: KClass<T>,
            prefixName: String,
            subInterfacesByName: MutableMap<String, Any>,
            batchController: JsonRpcCaller,
        ): T {
            val cls = implementedInterface.javaObjectType
            val proxyHandler = SpaceEngineersBatchJavaProxy(
                agentId,
                implementedInterface = implementedInterface,
                prefixName = prefixName,
                subInterfacesByName = subInterfacesByName,
                rpcCaller = batchController,
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
        ): BatchProcessableSpaceEngineers {
            return createSeSubProxy(
                agentId = agentId,
                jsonRpcCaller = JsonRpcCaller(stringLineReaderWriter = stringLineReaderWriter),
                implementedInterface = SpaceEngineers::class,
                prefixName = "",
                subInterfacesByName = mutableMapOf(),
            )
        }
    }
}
