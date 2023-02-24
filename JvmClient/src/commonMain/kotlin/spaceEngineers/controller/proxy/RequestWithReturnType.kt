package spaceEngineers.controller.proxy

import spaceEngineers.transport.jsonrpc.KotlinJsonRpcRequest
import kotlin.reflect.KType

data class RequestWithReturnType(
    val request: KotlinJsonRpcRequest,
    val returnType: KType,
)
