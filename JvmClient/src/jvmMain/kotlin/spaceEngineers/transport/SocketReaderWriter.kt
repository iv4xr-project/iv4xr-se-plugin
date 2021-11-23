package spaceEngineers.transport

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.JsonRpcSpaceEngineers
import spaceEngineers.controller.SpaceEngineers
import java.io.*
import java.lang.reflect.Modifier
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets


fun Any?.closeIfCloseable() {
    this?.let {
        if (it is AutoCloseable) {
            it.close()
        }
    }
}

fun SpaceEngineers?.closeIfCloseable() {
    this?.let {
        if (it is ContextControllerWrapper) {
            it.spaceEngineers.closeIfCloseable()
        }
        if (it is JsonRpcSpaceEngineers) {
            if (it.stringLineReaderWriter is AutoCloseable) {
                it.stringLineReaderWriter.close()
            }
        }
    }
}

class SocketReaderWriter @JvmOverloads constructor(
    host: String = DEFAULT_HOSTNAME,
    port: Int = DEFAULT_PORT,
    maxWaitTimeMs: Int = 45_000,
    socketConnectionTimeoutMs: Int = 4_000,
    socketDataTimeoutMs: Int = 45_000
) : AutoCloseable, StringLineReaderWriter {

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

    override fun sendAndReceiveLine(line: String): String {
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

        const val DEFAULT_PORT = 3333

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
