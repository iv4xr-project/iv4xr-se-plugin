package spaceEngineers.transport

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import spaceEngineers.controller.ContextControllerWrapper
import spaceEngineers.controller.SpaceEngineers
import java.io.*
import java.lang.reflect.Modifier
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets


class SocketReaderWriter @JvmOverloads constructor(
    val host: String = DEFAULT_HOSTNAME,
    val port: Int = DEFAULT_PORT,
    val maxWaitTimeMs: Int = 120_000,
    val socketConnectionTimeoutMs: Int = 4_000,
    val socketDataTimeoutMs: Int = 120_000,
    connectOnCreate: Boolean = true,
) : AutoCloseable, StringLineReaderWriter {

    lateinit var socket: Socket
    lateinit var reader: BufferedReader
    lateinit var writer: PrintWriter

    val address: String = "$host:$port"

    init {
        if (connectOnCreate) {
            connect()
        }
    }

    fun connect(
    ) {
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
                close()
                throw it
            }
        }
    }

    override fun sendAndReceiveLine(line: String): String {
        writer.println(line)
        return reader.readLine()
    }

    override fun close() {
        if (this::reader.isInitialized) {
            closeSafely(reader)
        }
        if (this::writer.isInitialized) {
            closeSafely(writer)
        }
        if (this::socket.isInitialized) {
            closeSafely(socket)
        }
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
