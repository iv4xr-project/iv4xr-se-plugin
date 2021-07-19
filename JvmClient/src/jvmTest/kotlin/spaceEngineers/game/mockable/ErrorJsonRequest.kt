package spaceEngineers.game.mockable

import kotlinx.serialization.json.JsonObject
import spaceEngineers.controller.JsonRpcSpaceEngineers
import spaceEngineers.transport.jsonrpc.JsonRpcError
import spaceEngineers.transport.jsonrpc.KotlinJsonRpcResponse
import testhelp.MockOrRealGameTest
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue


class ErrorJsonRequest : MockOrRealGameTest() {

    @Suppress("TestFunctionName")
    fun ThisMethodDoesNotExist() {}

    @Test
    fun callingIncorrectParameters() = testContext {
        this as JsonRpcSpaceEngineers
        assertFailsWith<Exception> {
            this.processSingleParameterMethod(
                method = ::ThisMethodDoesNotExist,
                methodName = "Character.TurnOnJetpack",
                parameterName = "firstParam",
                parameter = "123",
                parameterType = String::class,
            )
        }.also {
            assertTrue(it is JsonRpcError<*>)
            assertEquals("Invalid params", it.message)
            assertEquals(-32602, it.code)
        }
    }

    @Test
    fun callingNonExistentMethod() = testContext {
        this as JsonRpcSpaceEngineers
        assertFailsWith<Exception> {
            this.processSingleParameterMethod(
                method = ::ThisMethodDoesNotExist,
                methodName = "ThisMethodDoesNotExist",
                parameterName = "firstParam",
                parameter = "123",
                parameterType = String::class,
            )
        }.also {
            assertTrue(it is JsonRpcError<*>)
            assertEquals("Method not found", it.message)
            assertEquals(-32601, it.code)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun sendingNotJson() = testContext {
        this as JsonRpcSpaceEngineers

        assertFailsWith<Exception> {
            this.callRpc<JsonObject>(
                stringLineReaderWriter,
                encodedRequest = "not-json",
                ktype = typeOf<KotlinJsonRpcResponse<JsonObject>>()
            )
        }.also {
            assertTrue(it is JsonRpcError<*>)
            assertEquals("Parse error", it.message)
            assertEquals(-32700, it.code)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun sendingInvalidJson() = testContext {
        this as JsonRpcSpaceEngineers
        assertFailsWith<Exception> {
            this.callRpc<JsonObject>(
                stringLineReaderWriter,
                encodedRequest = "{'invalid-json-rpc':'json'}",
                ktype = typeOf<KotlinJsonRpcResponse<JsonObject>>()
            )
        }.also {
            assertTrue(it is JsonRpcError<*>)
            assertEquals("Invalid Request", it.message)
            assertEquals(-32600, it.code)
        }
    }
}
