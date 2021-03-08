package spaceEngineers.transport

import com.google.gson.Gson
import environments.SocketReaderWriter
import environments.closeIfCloseable
import spaceEngineers.SeRequest
import spaceEngineers.controller.JsonRpcRequest
import spaceEngineers.controller.JsonRpcResponse

class GsonReaderWriter(
    val gson: Gson = SocketReaderWriter.SPACE_ENG_GSON,
    val stringLineReaderWriter: StringLineReaderWriter = SocketReaderWriter()
) : AutoCloseable {


    inline fun <reified I, reified O> callRpc(request: JsonRpcRequest<I>): JsonRpcResponse<O> {
        val responseJson = stringLineReaderWriter.sendAndReceiveLine(gson.toJson(request))
        val responseJson_ = "{\"jsonrpc\":\"2.0\",\"id\":4,\"result\":" +
                "{\"AgentID\":\"Mock\",\"Position\":{\"X\":4.0,\"Y\":2.0,\"Z\":0.0},\"OrientationForward\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"OrientationUp\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"Velocity\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"Extent\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"Entities\":[{\"Id\":\"Ente\",\"Position\":{\"X\":3.0,\"Y\":2.0,\"Z\":1.0}}],\"Grids\":[{\"Blocks\":[{\"MaxIntegrity\":10.0,\"BuildIntegrity\":1.0,\"Integrity\":5.0,\"BlockType\":\"MockBlock\",\"MinPosition\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"MaxPosition\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"Size\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"OrientationForward\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"OrientationUp\":{\"X\":0.0,\"Y\":0.0,\"Z\":0.0},\"Id\":\"blk\",\"Position\":{\"X\":5.0,\"Y\":5.0,\"Z\":5.0}}],\"Id\":null,\"Position\":{\"X\":5.0,\"Y\":5.0,\"Z\":5.0}}]}" +
                "}"
        val response = gson.fromJson(responseJson, JsonRpcResponse::class.java)
        response.error?.let {
            throw it
        }
        response.result?.let {
            return JsonRpcResponse<O>(
                id = response.id,
                error = response.error,
                jsonrpc = response.jsonrpc,
                result = gson.fromJson(it.toString(), O::class.java)
            )
        }
        error("no result and no error received from json")
    }

    fun <T> processRequest(request: SeRequest<T>): T {
        val responseJson = stringLineReaderWriter.sendAndReceiveLine(gson.toJson(request))
        return gson.fromJson(responseJson, request.responseType)
    }

    override fun close() {
        stringLineReaderWriter.closeIfCloseable()
    }

}