package spaceEngineers.transport

import spaceEngineers.transport.SocketReaderWriter.Companion.DEFAULT_HOSTNAME
import spaceEngineers.transport.SocketReaderWriter.Companion.DEFAULT_MAX_WAIT_TIME
import spaceEngineers.transport.SocketReaderWriter.Companion.DEFAULT_PORT
import spaceEngineers.transport.SocketReaderWriter.Companion.DEFAULT_SOCKET_CONNECTION_TIMEOUT
import spaceEngineers.transport.SocketReaderWriter.Companion.DEFAULT_SOCKET_DATA_TIMEOUT
import java.io.IOException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class ReconnectingSocketReaderWriter(
    val host: String = DEFAULT_HOSTNAME,
    val port: UShort = DEFAULT_PORT,
    val maxWaitTime: Duration = DEFAULT_MAX_WAIT_TIME,
    val socketConnectionTimeout: Duration = DEFAULT_SOCKET_CONNECTION_TIMEOUT,
    val socketDataTimeout: Duration = DEFAULT_SOCKET_DATA_TIMEOUT,
    val maxRetries: Int = DEFAULT_MAX_RETRIES,
) : StringLineReaderWriter {

    var socketReaderWriter: SocketReaderWriter = connect()

    private fun connect(): SocketReaderWriter {
        return SocketReaderWriter(host, port, maxWaitTime, socketConnectionTimeout, socketDataTimeout)
    }

    private fun reconnect() {
        close()
        socketReaderWriter = connect()
    }

    override fun sendAndReceiveLine(line: String): String {
        var exception: IOException? = null
        repeat(1 + maxRetries) {
            try {
                return socketReaderWriter.sendAndReceiveLine(line)
            } catch (e: IOException) {
                exception = e
                reconnect()
                socketReaderWriter.sendAndReceiveLine(line)
            }
        }
        throw exception!!
    }

    override fun close() {
        socketReaderWriter.close()
    }

    companion object {
        const val DEFAULT_MAX_RETRIES = 3

        @JvmOverloads
        fun createUsingLongDurations(
            host: String = DEFAULT_HOSTNAME,
            port: Int = DEFAULT_PORT.toInt(),
            maxWaitTimeMs: Int = DEFAULT_MAX_WAIT_TIME.inWholeMilliseconds.toInt(),
            socketConnectionTimeoutMs: Int = DEFAULT_SOCKET_CONNECTION_TIMEOUT.inWholeMilliseconds.toInt(),
            socketDataTimeoutMs: Int = DEFAULT_SOCKET_DATA_TIMEOUT.inWholeMilliseconds.toInt(),
            maxRetries: Int = DEFAULT_MAX_RETRIES,
        ) = ReconnectingSocketReaderWriter(
            host = host,
            port = port.toUShort(),
            maxWaitTime = maxWaitTimeMs.milliseconds,
            socketConnectionTimeout = socketConnectionTimeoutMs.milliseconds,
            socketDataTimeout = socketDataTimeoutMs.milliseconds,
            maxRetries = maxRetries,
        )
    }
}
