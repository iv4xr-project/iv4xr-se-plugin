package environments

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import spaceEngineers.SeRequest
import spaceEngineers.controller.JsonRpcRequest
import spaceEngineers.controller.JsonRpcResponse
import java.io.*
import java.lang.reflect.Modifier
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets


inline fun <reified I, reified O> SocketReaderWriter.callRpc(request: JsonRpcRequest<I>): JsonRpcResponse<O> {
    val responseJson = sendAndReceiveLine(gson.toJson(request))
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

class SocketReaderWriter @JvmOverloads constructor(
    host: String = DEFAULT_HOSTNAME,
    port: Int = DEFAULT_PORT,
    maxWaitTimeMs: Int = 20000,
    socketConnectionTimeoutMs: Int = 4000,
    socketDataTimeoutMs: Int = 4000,
    val gson: Gson = SPACE_ENG_GSON
) : AutoCloseable {

    lateinit var socket: Socket
    lateinit var reader: BufferedReader
    lateinit var writer: PrintWriter

    init {
        val startTime = System.nanoTime()
        var connected = false
        var exception: IOException? = null
        while (!connected && millisElapsed(startTime) < maxWaitTimeMs) {
            try {
                socket = Socket()
                socket.soTimeout = socketDataTimeoutMs
                socket.connect(InetSocketAddress(host, port), socketConnectionTimeoutMs)
                reader = BufferedReader(InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))
                writer = PrintWriter(OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)
                connected = true
            } catch (ignored: IOException) {
                exception = ignored
            }
        }
        exception?.let {
            if (!connected) {
                throw it
            }
        }
    }

    fun <T> processRequest(request: SeRequest<T>): T {
        val responseJson = sendAndReceiveLine(gson.toJson(request))
        return gson.fromJson(responseJson, request.responseType)
    }

    fun sendAndReceiveLine(line: String): String {
        writer.println(line)
        return reader.readLine()
    }

    override fun close() {
        closeSafely(reader)
        closeSafely(writer)
        closeSafely(socket)
    }

    companion object {
        const val DEFAULT_HOSTNAME = "localhost"

        const val DEFAULT_PORT = 9678

        val SPACE_ENG_GSON: Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
            .create()

        private fun millisElapsed(startTimeNano: Long): Float {
            return (System.nanoTime() - startTimeNano) / 1000000f
        }

        fun closeSafely(closeable: Closeable?) {
            try {
                closeable?.close()
            } catch (e: IOException) {
                //at this point we don't care, just cleanup
            }
        }
    }

}