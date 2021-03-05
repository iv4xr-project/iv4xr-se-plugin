package environments

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import spaceEngineers.SeRequest
import java.io.*
import java.lang.reflect.Modifier
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets


fun <T> SocketReaderWriter.processRequest(request: SeRequest<T>): T {
    val responseJson = sendAndReceiveLine(gson.toJson(request))
    return gson.fromJson(responseJson, request.responseType)
}

fun SocketReaderWriter.sendAndReceiveLine(line: String): String {
    //TODO: configure gson to ignore companion object
    writer.println(line.replace(",\"Companion\":{}", ""))
    return reader.readLine()
}

class SocketReaderWriter @JvmOverloads constructor(
    host: String, port: Int, maxWaitTimeMs: Int = 20000,
    socketConnectionTimeoutMs: Int = 4000,
    socketDataTimeoutMs: Int = 4000,
    val gson: Gson = DEFAULT_GSON
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
                writer = PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)
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

    override fun close() {
        closeSafely(reader)
        closeSafely(writer)
        closeSafely(socket)
    }


    companion object {
        private val DEFAULT_GSON = GsonBuilder()
            .serializeNulls()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
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