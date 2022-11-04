package spaceEngineers.transport

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.*
import java.io.Closeable
import java.lang.reflect.Modifier
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds


class SocketReaderWriter(
    val host: String = DEFAULT_HOSTNAME,
    val port: UShort = DEFAULT_PORT,
    val maxWaitTime: Duration = DEFAULT_MAX_WAIT_TIME,
    val socketConnectionTimeout: Duration = DEFAULT_SOCKET_CONNECTION_TIMEOUT,
    val socketDataTimeout: Duration = DEFAULT_SOCKET_DATA_TIMEOUT,
) : StringLineReaderWriter {

    lateinit var socket: Socket
    lateinit var reader: BufferedReader
    lateinit var writer: PrintWriter

    val address: String
        get() = "$host:$port"

    init {
        connect()
    }

    private fun connect(
    ) {
        val startTime = System.nanoTime()
        var connected = false
        var lastException: IOException? = null
        while (!connected && millisElapsed(startTime) < maxWaitTime) {
            try {
                socket = Socket()
                socket.soTimeout = socketDataTimeout.inWholeMilliseconds.toInt()
                socket.connect(
                    InetSocketAddress(host, port.toInt()),
                    socketConnectionTimeout.inWholeMilliseconds.toInt()
                )
                reader = BufferedReader(InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))
                writer = PrintWriter(OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true)
                connected = true
            } catch (ignored: IOException) {
                lastException = ignored
            }
        }
        lastException?.let {
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
        const val DEFAULT_PORT: UShort = 3333u
        val DEFAULT_MAX_WAIT_TIME = 120.seconds
        val DEFAULT_SOCKET_CONNECTION_TIMEOUT = 4.seconds
        val DEFAULT_SOCKET_DATA_TIMEOUT = 1200.seconds

        val SPACE_ENG_GSON: Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
            .create()

        private fun millisElapsed(startTimeNano: Long): Duration {
            return (System.nanoTime() - startTimeNano).nanoseconds
        }

        fun closeSafely(closeable: AutoCloseable?) {
            try {
                closeable?.close()
            } catch (e: IOException) {
                //at this point we don't care, just cleanup
            }
        }

        fun closeSafely(closeable: Closeable?) {
            try {
                closeable?.close()
            } catch (e: IOException) {
                //at this point we don't care, just cleanup
            }
        }

        //ensure this class can be created from Java
        @JvmOverloads
        fun createUsingLongDurations(
            host: String = DEFAULT_HOSTNAME,
            port: Int = DEFAULT_PORT.toInt(),
            maxWaitTimeMs: Int = DEFAULT_MAX_WAIT_TIME.inWholeMilliseconds.toInt(),
            socketConnectionTimeoutMs: Int = DEFAULT_SOCKET_CONNECTION_TIMEOUT.inWholeMilliseconds.toInt(),
            socketDataTimeoutMs: Int = DEFAULT_SOCKET_DATA_TIMEOUT.inWholeMilliseconds.toInt(),
        ) = SocketReaderWriter(
            host = host,
            port = port.toUShort(),
            maxWaitTime = maxWaitTimeMs.milliseconds,
            socketConnectionTimeout = socketConnectionTimeoutMs.milliseconds,
            socketDataTimeout = socketDataTimeoutMs.milliseconds,
        )
    }

}
